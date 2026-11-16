-- Cada producto conserva sus datos comunes; cada variante tendrá SKU, precio y stock propios.
CREATE TABLE product_variants (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    store_id UUID NOT NULL,
    sku VARCHAR(80) NOT NULL,
    options JSONB NOT NULL DEFAULT '{}'::jsonb,
    options_key VARCHAR(2000) NOT NULL,
    base_price NUMERIC(12,2) NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_product_variants_product FOREIGN KEY (product_id, store_id)
        REFERENCES products (id, store_id) ON DELETE CASCADE,
    CONSTRAINT uq_product_variants_id_product UNIQUE (id, product_id),
    CONSTRAINT uq_product_variants_id_product_store UNIQUE (id, product_id, store_id),
    CONSTRAINT ck_product_variants_price CHECK (base_price BETWEEN 0.01 AND 100000.00),
    CONSTRAINT ck_product_variants_options CHECK (jsonb_typeof(options) = 'object')
);

CREATE UNIQUE INDEX uq_product_variants_store_sku ON product_variants (store_id, lower(btrim(sku)));
CREATE UNIQUE INDEX uq_product_variants_product_options ON product_variants (product_id, options_key);
CREATE UNIQUE INDEX uq_product_variants_default ON product_variants (product_id) WHERE is_default;
CREATE INDEX idx_product_variants_product ON product_variants (product_id, is_active);

-- Los productos existentes se convierten en su variante predeterminada y conservan su identificador.
INSERT INTO product_variants (id, product_id, store_id, sku, options, options_key, base_price, is_default, is_active, created_at)
SELECT p.id, p.id, p.store_id, 'LEG-' || upper(substr(replace(p.id::text, '-', ''), 1, 24)),
       '{}'::jsonb, 'DEFAULT', p.base_price, TRUE, TRUE, p.created_at
FROM products p;

ALTER TABLE inventory ADD COLUMN variant_id UUID;
UPDATE inventory SET variant_id = product_id;
ALTER TABLE inventory ALTER COLUMN variant_id SET NOT NULL;
ALTER TABLE inventory DROP CONSTRAINT inventory_product_id_key;
ALTER TABLE inventory ADD CONSTRAINT uq_inventory_variant UNIQUE (variant_id);
ALTER TABLE inventory ADD CONSTRAINT fk_inventory_variant FOREIGN KEY (variant_id, product_id)
    REFERENCES product_variants (id, product_id) ON DELETE CASCADE;

ALTER TABLE shopping_cart_items ADD COLUMN variant_id UUID;
UPDATE shopping_cart_items SET variant_id = product_id;
ALTER TABLE shopping_cart_items ALTER COLUMN variant_id SET NOT NULL;
ALTER TABLE shopping_cart_items DROP CONSTRAINT pk_shopping_cart_items;
ALTER TABLE shopping_cart_items ADD CONSTRAINT pk_shopping_cart_items PRIMARY KEY (cart_id, variant_id);
ALTER TABLE shopping_cart_items ADD CONSTRAINT fk_cart_items_variant FOREIGN KEY (variant_id, product_id, store_id)
    REFERENCES product_variants (id, product_id, store_id) ON DELETE CASCADE;
CREATE INDEX idx_cart_items_variant ON shopping_cart_items (variant_id);

ALTER TABLE order_items ADD COLUMN variant_id UUID;
UPDATE order_items SET variant_id = product_id;
ALTER TABLE order_items ALTER COLUMN variant_id SET NOT NULL;
ALTER TABLE order_items ADD COLUMN variant_sku VARCHAR(80);
ALTER TABLE order_items ADD COLUMN variant_options JSONB;
UPDATE order_items oi
SET variant_sku = pv.sku, variant_options = pv.options
FROM product_variants pv
WHERE pv.id = oi.variant_id;
ALTER TABLE order_items ALTER COLUMN variant_sku SET NOT NULL;
ALTER TABLE order_items ALTER COLUMN variant_options SET NOT NULL;
ALTER TABLE order_items ALTER COLUMN variant_options SET DEFAULT '{}'::jsonb;
ALTER TABLE order_items DROP CONSTRAINT pk_order_items;
ALTER TABLE order_items ADD CONSTRAINT pk_order_items PRIMARY KEY (order_id, variant_id);
ALTER TABLE order_items ADD CONSTRAINT fk_order_items_variant FOREIGN KEY (variant_id, product_id, store_id)
    REFERENCES product_variants (id, product_id, store_id) ON DELETE RESTRICT;
CREATE INDEX idx_order_items_variant ON order_items (variant_id);
