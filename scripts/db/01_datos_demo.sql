-- BODEKO: datos sintéticos para desarrollo, posteriores a Flyway V1-V5.
-- Ejecutar primero 00_compatibilidad_historial.sql y después este archivo completo.
-- Esta carga no crea/elimina tablas ni modifica datos existentes.
-- Contraseña de TODAS las cuentas demo: BodekoDemo2026!
-- El hash BCrypt está precalculado; NO requiere la extensión pgcrypto.
-- Los UUID son fijos. Reejecutar conserva cambios hechos desde la aplicación.
BEGIN;

DO $$
BEGIN
    IF to_regclass('public.order_status_history') IS NULL
       OR NOT EXISTS (SELECT 1 FROM information_schema.columns
                      WHERE table_schema = 'public' AND table_name = 'users' AND column_name = 'document_number') THEN
        RAISE EXCEPTION 'Primero inicia el backend para aplicar las migraciones Flyway V1-V5.';
    END IF;
END $$;

-- Detectar una BD antigua antes de insertar cualquier dato de prueba.
DO $$
BEGIN
    IF (SELECT count(*) FROM information_schema.columns
        WHERE table_schema = 'public' AND table_name = 'order_status_history'
          AND column_name IN ('from_status', 'to_status', 'changed_by', 'created_at')) <> 4 THEN
        RAISE EXCEPTION 'La estructura del historial no coincide con el backend. Ejecuta primero scripts/db/00_compatibilidad_historial.sql.';
    END IF;
END $$;

-- Dos propietarios: permiten comprobar que cada uno solo administra sus propias tiendas.
-- Dos clientes: permiten comprobar que una cuenta no puede consultar las órdenes de otra.
INSERT INTO users (id, email, password_hash, full_name, role, phone_number, document_type, document_number)
VALUES
('b0de0000-0000-4000-8000-000000000001', 'admin.demo@bodeko.test', '$2a$10$tlJgtV/apg1y/X5xe7Rju.oEx/hcwRcQSu8OQTtEE4pYA8YP6xy.m', 'Administrador Demo', 'ADMIN', '+51999999001', NULL, NULL),
('b0de0000-0000-4000-8000-000000000002', 'tecnologia.demo@bodeko.test', '$2a$10$tlJgtV/apg1y/X5xe7Rju.oEx/hcwRcQSu8OQTtEE4pYA8YP6xy.m', 'Administrador Tecnología', 'ADMIN', '+51999999002', NULL, NULL),
('b0de0000-0000-4000-8000-000000000003', 'cliente.demo@bodeko.test', '$2a$10$tlJgtV/apg1y/X5xe7Rju.oEx/hcwRcQSu8OQTtEE4pYA8YP6xy.m', 'Cliente Demo', 'CUSTOMER', '+51999999003', 'DNI', '12345678'),
('b0de0000-0000-4000-8000-000000000004', 'cliente2.demo@bodeko.test', '$2a$10$tlJgtV/apg1y/X5xe7Rju.oEx/hcwRcQSu8OQTtEE4pYA8YP6xy.m', 'Segundo Cliente Demo', 'CUSTOMER', '+51999999004', 'DNI', '87654321')
ON CONFLICT (id) DO NOTHING;

INSERT INTO stores (id, owner_id, name, tax_id, legal_name, subdomain, currency, tax_rate)
VALUES
('b0de1000-0000-4000-8000-000000000001', 'b0de0000-0000-4000-8000-000000000001', 'Bodeko Market Demo', '20999999001', 'Comercio Ficticio Demo S.A.C.', 'bodeko-market-demo', 'PEN', 0.18),
('b0de1000-0000-4000-8000-000000000002', 'b0de0000-0000-4000-8000-000000000002', 'Bodeko Tecnología Demo', '20999999002', 'Tecnología Ficticia Demo S.A.C.', 'bodeko-tech-demo', 'PEN', 0.18)
ON CONFLICT (id) DO NOTHING;

INSERT INTO categories (id, store_id, name)
VALUES
('b0de2000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000001', 'Abarrotes'),
('b0de2000-0000-4000-8000-000000000002', 'b0de1000-0000-4000-8000-000000000001', 'Bebidas y café'),
('b0de2000-0000-4000-8000-000000000003', 'b0de1000-0000-4000-8000-000000000001', 'Limpieza'),
('b0de2000-0000-4000-8000-000000000004', 'b0de1000-0000-4000-8000-000000000001', 'Categoría vacía de prueba'),
('b0de2000-0000-4000-8000-000000000005', 'b0de1000-0000-4000-8000-000000000002', 'Audio'),
('b0de2000-0000-4000-8000-000000000006', 'b0de1000-0000-4000-8000-000000000002', 'Periféricos')
ON CONFLICT (id) DO NOTHING;

-- Los datos ficticios no dependen de servicios externos de imágenes.
INSERT INTO products (id, store_id, category_id, name, description, base_price, is_tax_exempt, status)
VALUES
('b0de3000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000002', 'Café peruano 250 g', 'Producto demo con oferta activa y stock disponible.', 25.90, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000002', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Arroz extra 1 kg', 'Producto demo exonerado de IGV y con stock bajo.', 5.00, TRUE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000003', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Leche evaporada', 'Producto demo agotado para comprobar el filtro de stock.', 4.50, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000004', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Quinua 500 g', 'Producto demo exonerado de IGV.', 12.00, TRUE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000005', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000003', 'Detergente 1 kg', 'Producto demo con una oferta ya vencida.', 18.00, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000006', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Chocolate en borrador', 'Solo visible para el administrador hasta su publicación.', 8.50, FALSE, 'DRAFT'),
('b0de3000-0000-4000-8000-000000000007', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Azúcar archivada', 'Producto archivado que no debe aparecer en el catálogo público.', 4.20, FALSE, 'ARCHIVED'),
('b0de3000-0000-4000-8000-000000000008', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Aceite de oliva 250 ml', 'Producto demo con una oferta programada para más adelante.', 22.00, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000009', 'b0de1000-0000-4000-8000-000000000002', 'b0de2000-0000-4000-8000-000000000005', 'Audífonos inalámbricos', 'Producto de la segunda tienda para probar el aislamiento.', 89.00, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000010', 'b0de1000-0000-4000-8000-000000000002', 'b0de2000-0000-4000-8000-000000000006', 'Teclado compacto', 'Producto demo con pocas unidades.', 129.00, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000011', 'b0de1000-0000-4000-8000-000000000002', 'b0de2000-0000-4000-8000-000000000006', 'Mouse inalámbrico', 'Producto demo agotado.', 49.00, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000012', 'b0de1000-0000-4000-8000-000000000002', 'b0de2000-0000-4000-8000-000000000006', 'Cámara web en borrador', 'Producto demo pendiente de publicación.', 159.00, FALSE, 'DRAFT')
ON CONFLICT (id) DO NOTHING;

-- El SQL no pasa por ProductService: por eso crea explícitamente los inventarios.
INSERT INTO inventory (id, product_id, quantity, low_stock_threshold)
SELECT replace(product_id, 'b0de3000', 'b0de4000')::uuid, product_id::uuid, quantity, threshold
FROM (VALUES
('b0de3000-0000-4000-8000-000000000001', 40, 5),
('b0de3000-0000-4000-8000-000000000002', 4, 5),
('b0de3000-0000-4000-8000-000000000003', 0, 5),
('b0de3000-0000-4000-8000-000000000004', 25, 5),
('b0de3000-0000-4000-8000-000000000005', 12, 5),
('b0de3000-0000-4000-8000-000000000006', 0, 5),
('b0de3000-0000-4000-8000-000000000007', 0, 5),
('b0de3000-0000-4000-8000-000000000008', 20, 5),
('b0de3000-0000-4000-8000-000000000009', 8, 3),
('b0de3000-0000-4000-8000-000000000010', 2, 3),
('b0de3000-0000-4000-8000-000000000011', 0, 3),
('b0de3000-0000-4000-8000-000000000012', 0, 3)
) AS stock(product_id, quantity, threshold)
ON CONFLICT (product_id) DO NOTHING;

-- Se insertan solo si el producto aún no tiene ofertas: repetir no crea solapamientos.
INSERT INTO product_offers (store_id, product_id, offer_price, starts_at, ends_at)
SELECT p.store_id, p.id, offers.price, now() + offers.starts_offset, now() + offers.ends_offset
FROM (VALUES
('b0de3000-0000-4000-8000-000000000001'::uuid, 19.90, interval '-1 day', interval '30 days'),
('b0de3000-0000-4000-8000-000000000005'::uuid, 14.00, interval '-10 days', interval '-1 day'),
('b0de3000-0000-4000-8000-000000000008'::uuid, 18.00, interval '7 days', interval '14 days'),
('b0de3000-0000-4000-8000-000000000009'::uuid, 69.00, interval '-1 day', interval '30 days')
) AS offers(product_id, price, starts_offset, ends_offset)
JOIN products p ON p.id = offers.product_id
WHERE NOT EXISTS (SELECT 1 FROM product_offers existing WHERE existing.product_id = p.id);

INSERT INTO shopping_carts (id, user_id, store_id, status)
VALUES ('b0de6000-0000-4000-8000-000000000001', 'b0de0000-0000-4000-8000-000000000003', 'b0de1000-0000-4000-8000-000000000001', 'OPEN')
ON CONFLICT DO NOTHING;

INSERT INTO shopping_cart_items (store_id, cart_id, product_id, quantity)
SELECT c.store_id, c.id, items.product_id, items.quantity
FROM shopping_carts c
CROSS JOIN (VALUES
('b0de3000-0000-4000-8000-000000000001'::uuid, 1),
('b0de3000-0000-4000-8000-000000000002'::uuid, 2)
) AS items(product_id, quantity)
WHERE c.id = 'b0de6000-0000-4000-8000-000000000001' AND c.status = 'OPEN'
ON CONFLICT (cart_id, product_id) DO NOTHING;

-- Historial ficticio ya contabilizado en el stock inicial; no son operaciones reales.
-- Todas las líneas usan un café a S/25.90, IGV S/4.66, total S/30.56.
INSERT INTO orders (id, store_id, customer_id, order_number, invoice_type,
    customer_document_type, customer_document_number, customer_business_name,
    customer_billing_address, subtotal, tax_amount, total, applied_tax_rate, status, created_at)
SELECT fixture.id, 'b0de1000-0000-4000-8000-000000000001', 'b0de0000-0000-4000-8000-000000000003',
    fixture.number, fixture.invoice, CASE WHEN fixture.invoice = 'INVOICE' THEN 'RUC' ELSE 'DNI' END,
    CASE WHEN fixture.invoice = 'INVOICE' THEN '20999999003' ELSE '12345678' END,
    CASE WHEN fixture.invoice = 'INVOICE' THEN 'Cliente Ficticio Demo S.A.C.' ELSE NULL END,
    'Dirección ficticia de prueba 123', 25.90, 4.66, 30.56, 0.18, fixture.status, now() - fixture.age
FROM (VALUES
('b0de7000-0000-4000-8000-000000000001'::uuid, 'BOD-DEMO-PENDIENTE', 'RECEIPT', 'PENDING', interval '2 hours'),
('b0de7000-0000-4000-8000-000000000002'::uuid, 'BOD-DEMO-PAGADA', 'INVOICE', 'PAID', interval '1 day'),
('b0de7000-0000-4000-8000-000000000003'::uuid, 'BOD-DEMO-ENVIADA', 'RECEIPT', 'SHIPPED', interval '2 days'),
('b0de7000-0000-4000-8000-000000000004'::uuid, 'BOD-DEMO-ENTREGADA', 'INVOICE', 'DELIVERED', interval '3 days'),
('b0de7000-0000-4000-8000-000000000005'::uuid, 'BOD-DEMO-CANCELADA', 'RECEIPT', 'CANCELLED', interval '4 days')
) AS fixture(id, number, invoice, status, age)
ON CONFLICT (id) DO NOTHING;

INSERT INTO order_items (store_id, order_id, product_id, product_name, quantity, list_unit_price, charged_unit_price, item_subtotal)
SELECT store_id, id, 'b0de3000-0000-4000-8000-000000000001', 'Café peruano 250 g', 1, 25.90, 25.90, 25.90
FROM orders WHERE id IN (
'b0de7000-0000-4000-8000-000000000001', 'b0de7000-0000-4000-8000-000000000002',
'b0de7000-0000-4000-8000-000000000003', 'b0de7000-0000-4000-8000-000000000004',
'b0de7000-0000-4000-8000-000000000005')
ON CONFLICT (order_id, product_id) DO NOTHING;

INSERT INTO payments (id, order_id, provider, transaction_id, amount, status)
VALUES
('b0de8000-0000-4000-8000-000000000001', 'b0de7000-0000-4000-8000-000000000001', 'CULQI', 'chr_test_seed_declined', 30.56, 'FAILED'),
('b0de8000-0000-4000-8000-000000000002', 'b0de7000-0000-4000-8000-000000000002', 'CULQI', 'chr_test_seed_paid', 30.56, 'APPROVED'),
('b0de8000-0000-4000-8000-000000000003', 'b0de7000-0000-4000-8000-000000000003', 'CULQI', 'chr_test_seed_shipped', 30.56, 'APPROVED'),
('b0de8000-0000-4000-8000-000000000004', 'b0de7000-0000-4000-8000-000000000004', 'CULQI', 'chr_test_seed_delivered', 30.56, 'APPROVED'),
('b0de8000-0000-4000-8000-000000000005', 'b0de7000-0000-4000-8000-000000000005', 'CULQI', 'chr_test_seed_refunded', 30.56, 'REFUNDED')
ON CONFLICT (id) DO NOTHING;

-- Las transiciones de preparación/envío/cancelación que administra el backend.
INSERT INTO order_status_history (id, order_id, from_status, to_status, changed_by, created_at)
VALUES
('b0de9000-0000-4000-8000-000000000001', 'b0de7000-0000-4000-8000-000000000003', 'PAID', 'SHIPPED', 'b0de0000-0000-4000-8000-000000000001', now() - interval '1 day'),
('b0de9000-0000-4000-8000-000000000002', 'b0de7000-0000-4000-8000-000000000004', 'PAID', 'SHIPPED', 'b0de0000-0000-4000-8000-000000000001', now() - interval '2 days'),
('b0de9000-0000-4000-8000-000000000003', 'b0de7000-0000-4000-8000-000000000004', 'SHIPPED', 'DELIVERED', 'b0de0000-0000-4000-8000-000000000001', now() - interval '1 day'),
('b0de9000-0000-4000-8000-000000000004', 'b0de7000-0000-4000-8000-000000000005', 'PAID', 'CANCELLED', 'b0de0000-0000-4000-8000-000000000001', now() - interval '3 days')
ON CONFLICT (id) DO NOTHING;

COMMIT;
