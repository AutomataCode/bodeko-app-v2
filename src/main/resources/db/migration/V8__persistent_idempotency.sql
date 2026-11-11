-- No guardar cuerpos recibidos: la huella basta para detectar una clave reutilizada con otros datos.
CREATE TABLE api_idempotency (
 actor_id UUID NOT NULL REFERENCES users(id),
 scope VARCHAR(160) NOT NULL,
 request_key VARCHAR(128) NOT NULL,
 request_hash CHAR(64) NOT NULL,
 state VARCHAR(20) NOT NULL CHECK(state IN ('IN_PROGRESS','COMPLETED','FAILED','UNCERTAIN')),
 external_started BOOLEAN NOT NULL DEFAULT false,
 response_json TEXT,
 http_status INTEGER,
 error_code VARCHAR(80),
 error_message VARCHAR(1000),
 created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
 updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
 PRIMARY KEY(actor_id,scope,request_key)
);
CREATE INDEX idx_idempotency_scope_state ON api_idempotency(scope,state);
