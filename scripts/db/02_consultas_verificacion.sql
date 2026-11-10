-- Consultas de SOLO LECTURA. Ejecutar después del seed y repetir tras usar la app.

-- 1. Usuarios de prueba. No muestra hashes de contraseña.
SELECT full_name, email, role, is_active
FROM users WHERE id::text LIKE 'b0de0000-%' ORDER BY email;

-- 2. Catálogo e inventario de ambas tiendas.
SELECT s.name AS tienda, p.name AS producto, p.status, p.base_price,
       p.is_tax_exempt, i.quantity, i.low_stock_threshold,
       i.quantity <= i.low_stock_threshold AS alerta_stock_bajo
FROM products p JOIN stores s ON s.id = p.store_id
JOIN inventory i ON i.product_id = p.id
WHERE s.id IN ('b0de1000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000002')
ORDER BY s.name, p.name;

-- 3. Ofertas y vigencia (la fecha cambia naturalmente con el paso del tiempo).
SELECT p.name, o.offer_price, o.starts_at, o.ends_at,
       now() >= o.starts_at AND now() < o.ends_at AS vigente
FROM product_offers o JOIN products p ON p.id = o.product_id
WHERE p.id::text LIKE 'b0de3000-%' ORDER BY p.name;

-- 4. Carritos: confirma que permanecen tras cerrar e iniciar sesión.
SELECT u.email, s.name AS tienda, c.status, p.name AS producto, ci.quantity
FROM shopping_carts c JOIN users u ON u.id = c.user_id JOIN stores s ON s.id = c.store_id
LEFT JOIN shopping_cart_items ci ON ci.cart_id = c.id LEFT JOIN products p ON p.id = ci.product_id
WHERE u.id IN ('b0de0000-0000-4000-8000-000000000003', 'b0de0000-0000-4000-8000-000000000004')
ORDER BY c.created_at DESC, p.name;

-- 5. Órdenes y pagos: incluye las órdenes nuevas creadas desde la aplicación.
SELECT o.order_number, o.status AS estado_orden, o.invoice_type,
       o.subtotal, o.tax_amount, o.total, pay.status AS estado_pago, pay.amount
FROM orders o LEFT JOIN payments pay ON pay.order_id = o.id
WHERE o.store_id IN ('b0de1000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000002')
ORDER BY o.created_at DESC, pay.created_at;

-- 6. Debe devolver CERO filas: subtotal e IGV cuadran con el total y las líneas.
SELECT o.order_number, o.subtotal, sum(oi.item_subtotal) AS subtotal_items, o.tax_amount, o.total
FROM orders o JOIN order_items oi ON oi.order_id = o.id
WHERE o.store_id IN ('b0de1000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000002')
GROUP BY o.id
HAVING o.subtotal <> sum(oi.item_subtotal) OR o.total <> o.subtotal + o.tax_amount;

-- 7. Historial de cambios del usuario (administrador o cliente), incluidas tus pruebas nuevas.
SELECT o.order_number, h.from_status, h.to_status,
       COALESCE(u.email, 'No registrado (histórico)') AS usuario, h.created_at
FROM order_status_history h JOIN orders o ON o.id = h.order_id LEFT JOIN users u ON u.id = h.changed_by
WHERE o.store_id IN ('b0de1000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000002')
ORDER BY h.created_at DESC;
