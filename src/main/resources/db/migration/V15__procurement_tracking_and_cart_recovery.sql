ALTER TABLE orders
    ADD COLUMN tracking_carrier varchar(100),
    ADD COLUMN tracking_number varchar(120),
    ADD COLUMN tracking_url varchar(500),
    ADD COLUMN estimated_delivery_at timestamptz;

ALTER TABLE customers ADD COLUMN marketing_consent boolean NOT NULL DEFAULT false;

ALTER TABLE shopping_carts
    ADD COLUMN updated_at timestamptz NOT NULL DEFAULT now(),
    ADD COLUMN recovery_email_sent_at timestamptz;
CREATE INDEX idx_shopping_carts_recovery
    ON shopping_carts(status, updated_at)
    WHERE status = 'OPEN' AND recovery_email_sent_at IS NULL;

CREATE TABLE suppliers (
    id uuid PRIMARY KEY,
    store_id uuid NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    name varchar(120) NOT NULL,
    contact_email varchar(255),
    phone varchar(30),
    notes varchar(500),
    active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT uq_suppliers_store_id_id UNIQUE(store_id, id)
);
CREATE UNIQUE INDEX uq_suppliers_store_name ON suppliers(store_id, lower(trim(name)));
CREATE INDEX idx_suppliers_store ON suppliers(store_id, active, name);

CREATE TABLE purchase_orders (
    id uuid PRIMARY KEY,
    store_id uuid NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    supplier_id uuid NOT NULL,
    order_number varchar(40) NOT NULL UNIQUE,
    status varchar(24) NOT NULL CHECK (status IN ('ORDERED','PARTIALLY_RECEIVED','RECEIVED','CANCELLED')),
    notes varchar(500),
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT fk_purchase_orders_supplier FOREIGN KEY(store_id, supplier_id) REFERENCES suppliers(store_id, id),
    CONSTRAINT uq_purchase_orders_store_id_id UNIQUE(store_id, id)
);
CREATE INDEX idx_purchase_orders_store_date ON purchase_orders(store_id, created_at DESC);

CREATE TABLE purchase_order_items (
    id uuid PRIMARY KEY,
    purchase_order_id uuid NOT NULL REFERENCES purchase_orders(id) ON DELETE CASCADE,
    variant_id uuid NOT NULL REFERENCES product_variants(id),
    product_name varchar(200) NOT NULL,
    sku varchar(80) NOT NULL,
    variant_options varchar(500) NOT NULL DEFAULT '',
    quantity_ordered integer NOT NULL CHECK(quantity_ordered BETWEEN 1 AND 10000),
    quantity_received integer NOT NULL DEFAULT 0 CHECK(quantity_received >= 0),
    quantity_rejected integer NOT NULL DEFAULT 0 CHECK(quantity_rejected >= 0),
    unit_cost numeric(12,2) NOT NULL CHECK(unit_cost >= 0),
    CONSTRAINT uq_purchase_order_variant UNIQUE(purchase_order_id, variant_id),
    CONSTRAINT ck_purchase_order_item_quantities CHECK(quantity_received + quantity_rejected <= quantity_ordered)
);
CREATE INDEX idx_purchase_order_items_order ON purchase_order_items(purchase_order_id);
