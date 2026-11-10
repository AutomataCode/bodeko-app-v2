#!/usr/bin/env python3
"""Busca secretos sin imprimir sus valores. Requiere Python 3 y Git."""
from __future__ import annotations

import argparse
import fnmatch
import json
import re
import subprocess
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]

# Estas claves sirven únicamente para los simuladores locales, no para Culqi real.
MOCK_VALUES = {'sk_test_mock', 'simulator-local-only'}
PROVIDER_PATTERNS = {
    'clave de proveedor': r'\b(?:AKIA|ASIA)[A-Z0-9]{16}\b|\b(?:sk_(?:live|test)_[A-Za-z0-9_-]{8,}|re_[A-Za-z0-9_]{15,}|gh[pousr]_[A-Za-z0-9]{20,}|github_pat_[A-Za-z0-9_]{20,}|xox[baprs]-[A-Za-z0-9-]{15,})\b',
    'clave privada': r'-----BEGIN (?:[A-Z ]+ )?PRIVATE KEY-----',
    'JWT completo': r'\beyJ[A-Za-z0-9_-]{15,}\.[A-Za-z0-9_-]{15,}\.[A-Za-z0-9_-]{15,}',
    'credencial en URL': r'\b(?:https?|postgres(?:ql)?|mysql|mongodb(?:\+srv)?)://[^\s/:]+:[^\s/@]+@',
    'secreto privado expuesto en Vite': r'\bVITE_[A-Z0-9_]*(?:SECRET|PASSWORD|PRIVATE_KEY|ACCESS_KEY)[A-Z0-9_]*\b',
}
SENSITIVE_ASSIGNMENT = re.compile(
    r'''(?ix)^\s*(?:export\s+)?["']?
    (?P<key>(?:spring\.datasource\.password|jwt\.secret|culqi\.secret-key|mail\.resend\.api-key|(?:[A-Z_]*(?:PASSWORD|SECRET|API_KEY|ACCESS_KEY|PRIVATE_KEY|AUTH_TOKEN))|api[_-]?key|secret[_-]?key|access[_-]?key|password|passwd))
    ["']?\s*[:=]\s*(?P<value>.+?)\s*[,;]?\s*$'''
)
FORBIDDEN_EXTENSIONS = ('.pem', '.key', '.p12', '.pfx', '.jks', '.keystore', '.dump', '.backup', '.bak', '.sql.gz', '.sqlite', '.sqlite3', '.hprof', '.log')


def local_secrets() -> list[str]:
    """Carga secretos locales solo para comparar; no los imprime ni los guarda en informes."""
    values = set()
    for file in ['.env', '.env.dev', '.env.prod', '.env.local', 'frontend/.env.local']:
        source = ROOT / file
        if not source.is_file():
            continue
        for line in source.read_text(encoding='utf-8-sig').splitlines():
            if line.lstrip().startswith('#') or '=' not in line:
                continue
            key, value = line.split('=', 1)
            value = value.strip().strip('"\'')
            if (re.search(r'PASSWORD|SECRET|API_KEY|ACCESS_KEY|AUTH_TOKEN', key)
                    and len(value) >= 4 and value not in MOCK_VALUES
                    and not value.startswith(('<', '${'))):
                values.add(value)
    return sorted(values)


LOCAL_SECRETS = local_secrets()


def git(*args: str) -> bytes:
    """Ejecuta consultas de Git sin cambiar archivos, índice ni historial."""
    return subprocess.check_output(['git', '-C', str(ROOT), *args], stderr=subprocess.DEVNULL)


def private_path(file: str) -> bool:
    """Detecta entornos privados, respaldos y archivos generados que no deben publicarse."""
    name = Path(file).name.lower()
    example = name.endswith('.example')
    return (
        ((name == '.env' or name.startswith('.env.')) and not example)
        or name.endswith(FORBIDDEN_EXTENSIONS)
        or name in {'.pgpass', '.npmrc', 'credentials.json'}
        or fnmatch.fnmatch(name, 'service-account*.json')
        or fnmatch.fnmatch(name, 'service_account*.json')
        or bool(set(Path(file).parts) & {'.qa', 'node_modules', 'target', 'dist', '.codex', '.agents'})
    )


def inspect(file: str, data: bytes, origin: str) -> list[dict]:
    """Devuelve ubicaciones y tipos de hallazgo, nunca los valores detectados."""
    findings = []
    if private_path(file):
        findings.append({'archivo': file, 'linea': 0, 'tipo': 'archivo privado o generado', 'origen': origin})
    if b'\0' in data:
        return findings
    try:
        content = data.decode('utf-8-sig')
    except UnicodeError:
        return findings
    for number, line in enumerate(content.splitlines(), 1):
        stripped = line.strip()
        if stripped.startswith(('#', '//', '--', '*')):
            # Las claves de proveedor también se buscan en comentarios.
            assignment = None
        else:
            assignment = SENSITIVE_ASSIGNMENT.match(line)
        types = set()
        for kind, pattern in PROVIDER_PATTERNS.items():
            for match in re.finditer(pattern, line):
                if match.group() not in MOCK_VALUES:
                    types.add(kind)
        if not types and any(value in line for value in LOCAL_SECRETS):
            types.add('coincide con un secreto del entorno local')
        if assignment:
            key = assignment['key']
            value = assignment['value'].strip().strip('"\'')
            type_definition = value.rstrip(';') in {'string', 'number', 'boolean', 'str'}
            # Los únicos valores fijos admitidos aquí son vacíos, referencias y ejemplos explícitos.
            placeholder = not value or value.startswith(('${', '${{', '<', 'process.env.', 'os.environ', 'System.getenv')) or value in MOCK_VALUES
            fallback = re.fullmatch(r'\$\{[^}:]+:([^}]+)\}', value)
            if fallback and fallback[1] not in MOCK_VALUES:
                placeholder = False
            ci_database = file == '.github/workflows/cy.yml' and key in {'POSTGRES_PASSWORD', 'DB_PASSWORD'} and value == 'testpass'
            if not placeholder and not ci_database and not type_definition:
                types.add('credencial escrita directamente')
        for kind in sorted(types):
            findings.append({'archivo': file, 'linea': number, 'tipo': kind, 'origen': origin})
    return findings


def current_files(staged: bool) -> tuple[list[dict], int]:
    """Revisa el árbol publicable o el índice completo, según la opción elegida."""
    args = ['ls-files', '--cached']
    if not staged:
        args += ['--others', '--exclude-standard']
    files = sorted(set(git(*args, '-z').decode('utf-8').split('\0')) - {''})
    findings = []
    checked = 0
    for file in files:
        if staged:
            try:
                data = git('show', ':' + file)
            except subprocess.CalledProcessError:
                continue
        else:
            source = ROOT / file
            if not source.is_file():
                continue
            data = source.read_bytes()
        findings += inspect(file, data, 'indice' if staged else 'actual')
        checked += 1
    return findings, checked


def history_files() -> tuple[list[dict], int]:
    """Revisa una vez cada archivo histórico alcanzable desde las referencias locales."""
    objects = []
    for line in git('rev-list', '--objects', '--all').decode('utf-8').splitlines():
        parts = line.split(' ', 1)
        if len(parts) == 2:
            objects.append(parts)
    findings = []
    checked = 0
    # El protocolo por lotes evita crear un proceso por cada archivo histórico.
    process = subprocess.Popen(['git', '-C', str(ROOT), 'cat-file', '--batch'], stdin=subprocess.PIPE, stdout=subprocess.PIPE)
    try:
        for oid, file in objects:
            process.stdin.write((oid + '\n').encode())
            process.stdin.flush()
            header = process.stdout.readline().decode().split()
            if len(header) != 3:
                continue
            size = int(header[2])
            data = process.stdout.read(size)
            process.stdout.read(1)
            if header[1] != 'blob':
                continue
            findings += inspect(file, data, 'historial:' + oid[:12])
            checked += 1
    finally:
        process.stdin.close()
        process.stdout.close()
        process.wait()
    return findings, checked


def main() -> int:
    """Termina con código 1 si existe algún hallazgo que requiere revisión."""
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--staged', action='store_true', help='Revisar el índice completo que se incluiría en el commit.')
    parser.add_argument('--history', action='store_true', help='Añadir el historial local alcanzable. No consulta el remoto.')
    options = parser.parse_args()
    findings, checked = current_files(options.staged)
    print(f'Archivos actuales revisados: {checked}.')
    if options.history:
        historical, count = history_files()
        findings += historical
        print(f'Versiones históricas revisadas: {count}.')
    for finding in findings:
        print(json.dumps(finding, ensure_ascii=True))
    print(f'Hallazgos: {len(findings)}. Los valores sensibles no se muestran.')
    return 1 if findings else 0


if __name__ == '__main__':
    raise SystemExit(main())
