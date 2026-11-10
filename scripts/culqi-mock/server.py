"""
Culqi simulado (mock) para desarrollo local de bodeko-core-api.

Imita los endpoints de la API v2 de Culqi que usa el backend:
  POST /v2/charges  -> cobros
  POST /v2/refunds  -> reembolsos

No mueve dinero real ni se conecta a Culqi. Solo usa la libreria estandar de Python.

Uso:
  python server.py                  (puerto 8199, llave sk_test_mock)
  MOCK_PORT=8200 python server.py   (otro puerto)

Tokens (campo sourceId del backend):
  tkn_test_ok        -> cargo aprobado
  tkn_test_declined  -> cargo rechazado (fondos insuficientes)
  cualquier otro     -> token invalido
"""
import json
import os
import threading
import time
import uuid
from datetime import datetime
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

PORT = int(os.environ.get("MOCK_PORT", "8199"))
SECRET_KEY = os.environ.get("MOCK_SECRET_KEY", "sk_test_mock")

OK_TOKEN = "tkn_test_ok"
DECLINED_TOKEN = "tkn_test_declined"

# charge_id -> {"amount": int (centimos), "refunded": int (centimos), "email": str}
CHARGES = {}
LOCK = threading.Lock()


def new_id(prefix):
    return f"{prefix}_test_{uuid.uuid4().hex[:16]}"


def log(message):
    print(f"[{datetime.now():%H:%M:%S}] {message}", flush=True)


def soles(cents):
    return f"S/ {cents / 100:.2f}"


def error(status, error_type, user_message, merchant_message, **extra):
    body = {
        "object": "error",
        "type": error_type,
        "user_message": user_message,
        "merchant_message": merchant_message,
    }
    body.update(extra)
    return status, body


def create_charge(body):
    source_id = body.get("source_id")
    amount = body.get("amount")
    email = body.get("email")
    if not isinstance(amount, int) or amount <= 0:
        return error(400, "invalid_request_error", "El monto es invalido.",
                     "amount debe ser un entero mayor a 0 (en centimos).", param="amount")

    if source_id == OK_TOKEN:
        charge_id = new_id("chr")
        with LOCK:
            CHARGES[charge_id] = {"amount": amount, "refunded": 0, "email": email}
        log(f"CARGO APROBADO   {charge_id}  {soles(amount)}  {email}")
        return 201, {
            "object": "charge",
            "id": charge_id,
            "creation_date": int(time.time() * 1000),
            "amount": amount,
            "currency_code": body.get("currency_code"),
            "email": email,
            "description": body.get("description"),
            "metadata": body.get("metadata") or {},
            "outcome": {
                "type": "venta_exitosa",
                "code": "AUT0000",
                "merchant_message": "La operacion de venta ha sido autorizada exitosamente",
                "user_message": "Su compra ha sido exitosa.",
            },
        }

    if source_id == DECLINED_TOKEN:
        charge_id = new_id("chr")
        log(f"CARGO RECHAZADO  {charge_id}  {soles(amount)}  {email}  (fondos insuficientes)")
        return error(402, "card_error",
                     "Su tarjeta no tiene fondos suficientes. Para realizar la compra, "
                     "verifica tus fondos disponibles con el banco emisor o intenta nuevamente con otra tarjeta.",
                     "La tarjeta no tiene fondos suficientes.",
                     charge_id=charge_id, code="card_declined", decline_code="insufficient_funds")

    log(f"TOKEN INVALIDO   {source_id!r}")
    return error(400, "invalid_request_error",
                 "El token de la tarjeta es invalido o ya fue usado. Intenta nuevamente.",
                 "El source_id enviado no existe o ya fue usado.", param="source_id")


def create_refund(body):
    charge_id = body.get("charge_id")
    amount = body.get("amount")
    if not isinstance(amount, int) or amount <= 0:
        return error(400, "invalid_request_error", "El monto a devolver es invalido.",
                     "amount debe ser un entero mayor a 0 (en centimos).", param="amount")
    with LOCK:
        charge = CHARGES.get(charge_id)
        if charge is None:
            log(f"REEMBOLSO FALLIDO {charge_id}  (el cargo no existe)")
            return error(400, "invalid_request_error", "No se encontro el cargo a devolver.",
                         "El charge_id no existe o no fue aprobado.", param="charge_id")
        available = charge["amount"] - charge["refunded"]
        if amount > available:
            log(f"REEMBOLSO FALLIDO {charge_id}  {soles(amount)} > disponible {soles(available)}")
            return error(400, "invalid_request_error", "El monto a devolver supera lo cobrado.",
                         f"Monto disponible para devolucion: {available} centimos.", param="amount")
        charge["refunded"] += amount

    refund_id = new_id("ref")
    log(f"REEMBOLSO OK     {refund_id}  {soles(amount)}  del cargo {charge_id}  (motivo: {body.get('reason')})")
    return 201, {
        "object": "refund",
        "id": refund_id,
        "charge_id": charge_id,
        "creation_date": int(time.time() * 1000),
        "amount": amount,
        "reason": body.get("reason"),
        "status": "aprobado",
    }


ROUTES = {
    "/v2/charges": create_charge,
    "/v2/refunds": create_refund,
}


class Handler(BaseHTTPRequestHandler):

    def do_POST(self):
        if self.headers.get("Authorization") != f"Bearer {SECRET_KEY}":
            log(f"401 llave invalida en {self.path}")
            return self.reply(*error(401, "authentication_error", "Error de autenticacion.",
                                     "La llave secreta es invalida."))
        handler = ROUTES.get(self.path)
        if handler is None:
            return self.reply(*error(404, "invalid_request_error", "Recurso no encontrado.",
                                     f"La ruta {self.path} no existe en el mock."))
        try:
            length = int(self.headers.get("Content-Length") or 0)
            body = json.loads(self.rfile.read(length) or b"{}")
        except (ValueError, json.JSONDecodeError):
            return self.reply(*error(400, "invalid_request_error", "Solicitud invalida.", "El cuerpo no es JSON valido."))
        self.reply(*handler(body))

    def reply(self, status, body):
        data = json.dumps(body).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)

    def log_message(self, *args):
        pass  # los mensajes propios ya se imprimen con log()


if __name__ == "__main__":
    log(f"Culqi simulado escuchando en http://127.0.0.1:{PORT}/v2  (llave: Bearer {SECRET_KEY})")
    log(f"Tokens: {OK_TOKEN} = aprobado | {DECLINED_TOKEN} = rechazado | otro = invalido")
    try:
        ThreadingHTTPServer(("127.0.0.1", PORT), Handler).serve_forever()
    except KeyboardInterrupt:
        log("Detenido.")
