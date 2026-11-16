-- Cada tienda tiene sus propios clientes: el mismo correo puede registrarse en tiendas distintas.
-- La tabla users queda solo para los propietarios (rol ADMIN).
-- Detener la migración antes de modificar datos si hay clientes sin tienda inferible.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM users u
        WHERE u.role = 'CUSTOMER'
          AND NOT EXISTS (SELECT 1 FROM shopping_carts c WHERE c.user_id = u.id)
          AND NOT EXISTS (SELECT 1 FROM orders o WHERE o.customer_id = u.id)
    ) THEN
        RAISE EXCEPTION 'V9 detenida: hay clientes sin carrito ni órdenes; asígneles una tienda antes de migrar';
    END IF;
END;
$$;

CREATE TABLE customers
(
    id              UUID PRIMARY KEY,
    store_id        UUID         NOT NULL,
    email           VARCHAR(255) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    full_name       VARCHAR(150) NOT NULL,
    phone_number    VARCHAR(20),
    document_type   VARCHAR(20),
    document_number VARCHAR(20),
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_customers_store
        FOREIGN KEY (store_id)
            REFERENCES stores (id)
            ON DELETE CASCADE,

    CONSTRAINT ck_customers_document CHECK (
        (document_type IS NULL AND document_number IS NULL)
        OR (document_type IS NOT NULL AND document_number IS NOT NULL AND (
            (document_type = 'DNI' AND document_number ~ '^[0-9]{8}$')
            OR (document_type = 'RUC' AND document_number ~ '^[0-9]{11}$')
            OR (document_type IN ('CE', 'PASSPORT') AND document_number ~ '^[A-Za-z0-9]{6,20}$')
        ))
    )
);

CREATE UNIQUE INDEX uq_customers_store_email_normalized
    ON customers (store_id, lower(btrim(email)));

-- Los clientes existentes se copian a cada tienda donde tienen carrito u órdenes.
-- Un cliente sin actividad no pertenece a ninguna tienda y deberá registrarse en la que use.
CREATE TEMP TABLE customer_migration ON COMMIT DROP AS
SELECT u.id AS user_id, activity.store_id, gen_random_uuid() AS customer_id
FROM users u
JOIN (SELECT user_id AS uid, store_id FROM shopping_carts
      UNION
      SELECT customer_id, store_id FROM orders) activity ON activity.uid = u.id
WHERE u.role = 'CUSTOMER';

INSERT INTO customers (id, store_id, email, password_hash, full_name, phone_number,
                       document_type, document_number, created_at, is_active)
SELECT m.customer_id, m.store_id, u.email, u.password_hash, u.full_name, u.phone_number,
       u.document_type, u.document_number, u.created_at, u.is_active
FROM customer_migration m
JOIN users u ON u.id = m.user_id;

ALTER TABLE orders DROP CONSTRAINT fk_orders_customer;
ALTER TABLE shopping_carts DROP CONSTRAINT fk_shopping_carts_user;
-- El autor de un cambio de estado o de una petición puede ser propietario o cliente.
ALTER TABLE order_status_history DROP CONSTRAINT fk_order_status_history_user;
ALTER TABLE api_idempotency DROP CONSTRAINT IF EXISTS api_idempotency_actor_id_fkey;

UPDATE order_status_history h
SET changed_by = m.customer_id
FROM orders o, customer_migration m
WHERE h.order_id = o.id AND m.user_id = h.changed_by AND m.store_id = o.store_id;

UPDATE orders o
SET customer_id = m.customer_id
FROM customer_migration m
WHERE o.customer_id = m.user_id AND o.store_id = m.store_id;

UPDATE shopping_carts c
SET user_id = m.customer_id
FROM customer_migration m
WHERE c.user_id = m.user_id AND c.store_id = m.store_id;

-- Las claves de idempotencia son temporales; las de clientes antiguos ya no aplican.
DELETE FROM api_idempotency WHERE actor_id IN (SELECT id FROM users WHERE role = 'CUSTOMER');
DELETE FROM users WHERE role = 'CUSTOMER';

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE RESTRICT;
ALTER TABLE shopping_carts
    ADD CONSTRAINT fk_shopping_carts_customer
        FOREIGN KEY (user_id) REFERENCES customers (id) ON DELETE CASCADE;
