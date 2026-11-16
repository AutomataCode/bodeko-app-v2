-- Separa la administración de la plataforma de los propietarios de tiendas.
ALTER TABLE users DROP CONSTRAINT ck_users_role;
ALTER TABLE users ADD CONSTRAINT ck_users_role CHECK (role IN ('ADMIN', 'SUPER_ADMIN'));

CREATE INDEX idx_users_role_active_created ON users (role, is_active, created_at DESC);
CREATE INDEX idx_customers_store_active_created ON customers (store_id, is_active, created_at DESC);

-- Registra cada suspensión o reactivación sin guardar datos de acceso.
CREATE TABLE platform_account_status_history (
    id UUID PRIMARY KEY,
    actor_id UUID NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    account_type VARCHAR(20) NOT NULL CHECK (account_type IN ('VENDOR', 'CUSTOMER')),
    account_id UUID NOT NULL,
    previous_active BOOLEAN NOT NULL,
    new_active BOOLEAN NOT NULL,
    reason VARCHAR(500) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_platform_account_history_target ON platform_account_status_history (account_type, account_id, created_at DESC);
