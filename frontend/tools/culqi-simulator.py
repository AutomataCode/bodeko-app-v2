"""Simulador LOCAL de cargos y devoluciones para el Entregable 2.

No conecta a Culqi, no recibe tarjetas y no realiza cobros. Escucha solo en loopback.
Tokens admitidos: tkn_test_approved y tkn_test_declined. No usar en producción.
Ejecutar: python tools/culqi-simulator.py
Backend: CULQI_API_URL=http://127.0.0.1:8090/v2
         CULQI_SECRET_KEY=simulator-local-only
"""
import json
import uuid
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer


class Handler(BaseHTTPRequestHandler):
    """Emula exclusivamente los campos consumidos por CulqiPaymentGateway.java."""

    def send_json(self, status, payload):
        body = json.dumps(payload).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def do_POST(self):
        if self.headers.get("Authorization") != "Bearer simulator-local-only":
            return self.send_json(401, {"object": "error", "type": "authentication_error", "user_message": "Configuración de simulador inválida."})
        length = int(self.headers.get("Content-Length", "0"))
        if length > 16384:
            return self.send_json(413, {"object": "error"})
        try:
            data = json.loads(self.rfile.read(length))
        except (ValueError, UnicodeDecodeError):
            return self.send_json(400, {"object": "error", "type": "parameter_error"})
        amount = data.get("amount")
        if not isinstance(amount, int) or isinstance(amount, bool) or amount <= 0:
            return self.send_json(400, {"object": "error", "type": "parameter_error", "user_message": "Importe inválido."})
        if self.path == "/v2/charges":
            token = data.get("source_id")
            if token not in ("tkn_test_approved", "tkn_test_declined"):
                return self.send_json(400, {"object": "error", "type": "parameter_error", "user_message": "Token de simulación desconocido."})
            charge_id = "chr_test_" + uuid.uuid4().hex[:16]
            if token == "tkn_test_declined":
                return self.send_json(402, {"object": "error", "type": "card_error", "charge_id": charge_id, "decline_code": "insufficient_funds", "user_message": "Saldo insuficiente en la simulación.", "merchant_message": "Simulated decline"})
            return self.send_json(201, {"object": "charge", "id": charge_id, "outcome": {"user_message": "Pago de prueba aprobado."}})
        if self.path == "/v2/refunds" and str(data.get("charge_id", "")).startswith("chr_test_"):
            return self.send_json(201, {"object": "refund", "id": "ref_test_" + uuid.uuid4().hex[:16], "status": "exitoso"})
        return self.send_json(404, {"object": "error", "type": "not_found"})

    def log_message(self, format_string, *args):
        # No registra datos de la solicitud ni cabeceras de autorización.
        print("Simulador local:", format_string % args)


if __name__ == "__main__":
    print("Simulador sin cobros reales: http://127.0.0.1:8090/v2", flush=True)
    try:
        ThreadingHTTPServer(("127.0.0.1", 8090), Handler).serve_forever()
    except KeyboardInterrupt:
        pass
