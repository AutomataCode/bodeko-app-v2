<#
.SYNOPSIS
Carga datos ficticios de Bodeko y ejecuta consultas de verificación sobre una BD de desarrollo.
.EXAMPLE
.\scripts\db\ejecutar-demo.ps1 -Database db_bodekos_v2 -DbUser postgres
#>
[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)][string]$Database,
    [Parameter(Mandatory = $true)][string]$DbUser,
    [string]$DbHost = 'localhost',
    [ValidateRange(1, 65535)][int]$Port = 5432,
    [string]$PsqlPath,
    [switch]$SoloVerificar
)

$ErrorActionPreference = 'Stop'
if (-not $PsqlPath) {
    $command = Get-Command psql.exe -ErrorAction SilentlyContinue
    if ($command) { $PsqlPath = $command.Source }
    else {
        $candidates = @('C:\Program Files\PostgreSQL\18\bin\psql.exe', 'C:\Program Files\PostgreSQL\17\bin\psql.exe', 'C:\Program Files\PostgreSQL\16\bin\psql.exe')
        $PsqlPath = $candidates | Where-Object { Test-Path -LiteralPath $_ } | Select-Object -First 1
    }
}
if (-not $PsqlPath -or -not (Test-Path -LiteralPath $PsqlPath)) {
    throw 'No se encontró psql.exe. Indica su ruta con -PsqlPath.'
}

Write-Host "Destino: $DbHost`:$Port / $Database. Usa una base de desarrollo."
# Aviso informativo: las variables de entorno pueden sobrescribir la configuración Java.
$backendConfigPath = Join-Path $PSScriptRoot '..\..\src\main\resources\application.properties'
if (Test-Path -LiteralPath $backendConfigPath) {
    $connectionLine = Get-Content -LiteralPath $backendConfigPath | Where-Object { $_ -match '^spring\.datasource\.url\s*=' } | Select-Object -First 1
    $databaseMatch = [regex]::Match([string]$connectionLine, '^spring\.datasource\.url\s*=\s*jdbc:postgresql://[^/]+/([^?\s]+)')
    if ($databaseMatch.Success -and $databaseMatch.Groups[1].Value -ne $Database) {
        Write-Warning "El backend declara la base '$($databaseMatch.Groups[1].Value)', pero estás usando '$Database'. Los usuarios y tiendas solo aparecerán si ambos apuntan a la misma base. Comprueba también las variables de entorno del backend."
    }
}
# psql solicita la contraseña si hace falta. El script no la recibe ni la guarda.
# PostgreSQL interpreta los SQL como UTF-8 incluso desde Windows PowerShell 5.1.
$previousClientEncoding = $env:PGCLIENTENCODING
$previousConsoleEncoding = [Console]::OutputEncoding
$files = if ($SoloVerificar) { @('02_consultas_verificacion.sql') } else { @('00_compatibilidad_historial.sql', '01_datos_demo.sql', '02_consultas_verificacion.sql') }
try {
    $env:PGCLIENTENCODING = 'UTF8'
    [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding($false)
    foreach ($file in $files) {
        & $PsqlPath -X -h $DbHost -p $Port -U $DbUser -d $Database -v ON_ERROR_STOP=1 -f (Join-Path $PSScriptRoot $file)
        if ($LASTEXITCODE -ne 0) { throw "Falló $file. Revisa el mensaje de PostgreSQL; no continúes con la siguiente carga." }
    }
}
finally {
    if ($null -eq $previousClientEncoding) { Remove-Item Env:PGCLIENTENCODING -ErrorAction SilentlyContinue }
    else { $env:PGCLIENTENCODING = $previousClientEncoding }
    [Console]::OutputEncoding = $previousConsoleEncoding
}
Write-Host 'Scripts completados. Revisa scripts/db/README.md para las cuentas y los escenarios.'
