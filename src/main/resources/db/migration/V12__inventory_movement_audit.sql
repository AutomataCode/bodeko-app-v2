CREATE TABLE inventory_movements (
    id UUID PRIMARY KEY,
    store_id UUID NOT NULL REFERENCES stores(id),
    product_id UUID NOT NULL REFERENCES products(id),
    variant_id UUID NOT NULL REFERENCES product_variants(id),
    product_name VARCHAR(200) NOT NULL,
    variant_sku VARCHAR(80) NOT NULL,
    variant_options VARCHAR(2000) NOT NULL,
    movement_type VARCHAR(32) NOT NULL,
    units INTEGER NOT NULL CHECK (units > 0),
    quantity_change INTEGER NOT NULL,
    quantity_before INTEGER NOT NULL,
    quantity_after INTEGER NOT NULL,
    reference_type VARCHAR(30),
    reference_id UUID,
    reference_number VARCHAR(100),
    reason VARCHAR(500),
    actor_id UUID,
    actor_name VARCHAR(150),
    actor_email VARCHAR(255),
    actor_role VARCHAR(30),
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT ck_inventory_movements_type CHECK (movement_type IN (
        'RESERVATION', 'SALE', 'RESERVATION_RELEASE', 'STOCK_ENTRY',
        'ADJUSTMENT', 'RETURN', 'DAMAGE', 'LOSS'
    )),
    CONSTRAINT ck_inventory_movements_balance CHECK (quantity_after >= 0)
);

CREATE INDEX idx_inventory_movements_store_date
    ON inventory_movements (store_id, created_at DESC);
CREATE INDEX idx_inventory_movements_store_type_date
    ON inventory_movements (store_id, movement_type, created_at DESC);
CREATE INDEX idx_inventory_movements_variant_date
    ON inventory_movements (variant_id, created_at DESC);
CREATE INDEX idx_inventory_movements_reference
    ON inventory_movements (reference_id) WHERE reference_id IS NOT NULL;

-- Conserva un saldo inicial de referencia para el inventario que ya existía.
INSERT INTO inventory_movements (
    id, store_id, product_id, variant_id, product_name, variant_sku, variant_options,
    movement_type, units, quantity_change, quantity_before, quantity_after,
    reference_type, reference_id, reason, actor_name, actor_role, created_at
)
SELECT md5(i.variant_id::text || ':inventory-kardex-v12')::uuid,
       p.store_id, p.id, v.id, p.name, v.sku,
       COALESCE((SELECT string_agg(option.key || ': ' || option.value, ', ' ORDER BY option.key)
                 FROM jsonb_each_text(v.options) AS option), 'Presentación estándar'),
       'ADJUSTMENT', i.quantity, i.quantity, 0, i.quantity,
       'BASELINE', i.variant_id, 'Saldo inicial al habilitar el kardex',
       'Sistema', 'SYSTEM', now()
FROM inventory i
JOIN products p ON p.id = i.product_id
JOIN product_variants v ON v.id = i.variant_id
WHERE i.quantity > 0;
