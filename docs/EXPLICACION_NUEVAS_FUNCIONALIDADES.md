# Explicación de las nuevas funcionalidades de Bodeko

## Objetivo

Este documento explica las mejoras incorporadas al proyecto Bodeko para administrar compras, existencias, entregas y recuperación de carritos. Está escrito como material de estudio y exposición. Las órdenes de compra y los comprobantes del sistema son funciones académicas; el módulo de compras no emite facturas fiscales.

## Resumen para explicar al docente

> Se ampliaron cuatro procesos de la tienda. El administrador ahora registra proveedores y órdenes de compra con variantes, recibe los productos total o parcialmente y solo las unidades aceptadas aumentan el inventario. El sistema muestra alertas cuando una variante llega a su umbral de stock. Al enviar una orden, se guarda el seguimiento para que lo vea el cliente. Finalmente, los carritos sin actividad pueden generar un recordatorio de correo, pero únicamente cuando el cliente dio su consentimiento. Las operaciones de inventario quedan vinculadas con la tienda y auditadas.

## Arquitectura utilizada

La aplicación conserva su arquitectura frontend/backend:

- **Frontend:** React, TypeScript y Vite. Las páginas están separadas por funcionalidades; consumen los servicios HTTP y muestran estados de carga, errores en español y confirmaciones con Sonner.
- **Backend:** Java con Spring Boot. Los controladores reciben y validan solicitudes; los servicios aplican reglas de negocio dentro de transacciones; los repositorios consultan PostgreSQL.
- **Base de datos:** PostgreSQL conserva proveedores, órdenes de compra, recepciones, seguimiento de órdenes, consentimiento y actividad de carritos.
- **Seguridad:** las operaciones administrativas validan la tienda y el usuario. Los clientes solo pueden consultar o cambiar sus propias preferencias.

## 1. Proveedores y órdenes de compra

### Propósito

Registrar el abastecimiento de una tienda sin depender de una factura de compra. Cada línea de la orden identifica una variante por su SKU, opciones, cantidad solicitada y costo unitario.

### Flujo

1. El administrador abre **Compras** dentro del panel de una tienda.
2. Registra un proveedor con nombre y datos de contacto.
3. Selecciona el proveedor, producto, variante, cantidad y costo unitario.
4. Bodeko crea una orden con estado `ORDERED`.
5. Cuando llega el pedido, el administrador registra cuántas unidades acepta y cuántas rechaza.
6. Las unidades aceptadas aumentan el stock de esa variante y se guardan en el kardex como `STOCK_ENTRY`, con el usuario responsable y el motivo.
7. Las unidades rechazadas quedan registradas en la línea de compra, pero no entran al inventario disponible.
8. Si la recepción se realiza en varias entregas, la orden muestra cantidades recibidas, rechazadas y pendientes; su estado pasa a `PARTIALLY_RECEIVED` o `RECEIVED`.

### Validaciones

- El proveedor debe estar activo y pertenecer a la tienda indicada.
- Las variantes deben pertenecer a esa tienda y estar habilitadas para la venta.
- No se permite repetir una variante dentro de la misma orden.
- La cantidad solicitada debe ser un entero de 1 a 10 000.
- La suma de cantidades aceptadas y rechazadas no puede superar lo pendiente.
- La recepción bloquea la orden y las filas de inventario durante la operación. Las solicitudes de escritura usan `Idempotency-Key` para evitar duplicados al reintentar.

### Endpoints

- `GET /api/stores/{storeId}/procurement/suppliers`
- `POST /api/stores/{storeId}/procurement/suppliers`
- `GET /api/stores/{storeId}/procurement/orders`
- `POST /api/stores/{storeId}/procurement/orders`
- `POST /api/stores/{storeId}/procurement/orders/{orderId}/receive`

### Tablas

- `suppliers`: proveedores de cada tienda.
- `purchase_orders`: cabecera, proveedor, fecha y estado.
- `purchase_order_items`: variante, SKU congelado, unidades, cantidades recibidas/rechazadas y costo unitario.
- `inventory` e `inventory_movements`: stock actual y auditoría de las unidades aceptadas.

## 2. Alertas y reposición de stock

### Propósito

Mostrar en el dashboard las variantes que llegaron o bajaron de su umbral de existencias. El control es por variante: las unidades de una talla, color o presentación no se mezclan con las de otra.

### Funcionamiento

- El backend consulta productos activos y variantes activas cuya cantidad sea menor o igual a `low_stock_threshold`.
- El dashboard muestra el producto, SKU, opciones, stock actual y umbral.
- La cantidad sugerida es `umbral - stock actual + 1`, es decir, el mínimo para quedar una unidad por encima del umbral. Es una guía inmediata, no un pronóstico de ventas.
- El administrador puede ajustar el umbral desde el editor de inventario y luego crear una orden de compra desde la sección **Compras**.

### Endpoint

- `GET /api/stores/{storeId}/inventory/low-stock`

La alerta es visual en el panel; no envía un correo al administrador.

## 3. Seguimiento de entregas

### Propósito

Permitir que la tienda comparta los datos del envío y que el cliente los consulte en su orden.

### Flujo

1. El administrador cambia la orden a `SHIPPED`.
2. En el detalle de esa orden completa transportista, número de seguimiento, enlace y fecha estimada.
3. Bodeko guarda los datos asociados a la orden.
4. El cliente abre el detalle desde **Mi cuenta > Mis órdenes** y consulta la información o sigue el enlace del transportista.

### Validaciones y endpoint

- Solo se permite guardar seguimiento si la orden está `SHIPPED`.
- Se valida el formato de la URL y la tienda debe pertenecer al administrador.
- `PATCH /api/stores/{storeId}/orders/{orderId}/tracking`

Los estados y datos de seguimiento son administrados manualmente. No se consulta una API de transportistas ni se reciben eventos automáticos de entrega.

### Datos guardados en `orders`

- `tracking_carrier`
- `tracking_number`
- `tracking_url`
- `estimated_delivery_at`

## 4. Recordatorios de carritos abandonados

### Propósito

Dar al cliente la opción de recibir un correo recordándole que dejó productos en el carrito.

### Consentimiento y flujo

1. Al crear su cuenta en una tienda, el cliente puede marcar una casilla opcional, desmarcada por defecto.
2. El carrito registra cuándo tuvo actividad. Agregar, quitar o cambiar unidades reinicia el periodo de inactividad.
3. Una tarea programada busca carritos abiertos con productos, sin actividad por 24 horas y con consentimiento activo.
4. Envía un único recordatorio para ese periodo e incluye un enlace al carrito de la tienda.
5. El cliente puede retirar el consentimiento desde **Mi cuenta > Datos personales**. Sin consentimiento, no se envía el recordatorio.

### Endpoints y columnas

- `GET /api/customer/preferences/marketing`
- `PATCH /api/customer/preferences/marketing`
- `customers.marketing_consent`: preferencia del cliente.
- `shopping_carts.updated_at`: última actividad.
- `shopping_carts.recovery_email_sent_at`: último recordatorio procesado.

La tarea revisa como máximo 100 carritos por ejecución y se repite cada 30 minutos por defecto. Para configurar el enlace y el intervalo:

- `APP_FRONTEND_BASE_URL`: origen del frontend; por defecto `http://localhost:3000`.
- `CART_RECOVERY_INTERVAL_MS`: intervalo entre ejecuciones; por defecto `1800000` milisegundos.

El remitente de correo existente tiene modo de simulación por logs. En modo simulado, el backend registra que procesó el envío, pero no entrega un correo real. Para enviar correos reales hay que configurar el proveedor Resend con `MAIL_PROVIDER=resend`, `RESEND_API_KEY` y `MAIL_FROM`.

## Relación con las mermas y devoluciones

La merma se usa para unidades que estaban en el inventario de la tienda y se dañan o pierden en almacén: reduce el stock disponible y deja un movimiento `DAMAGE` o `LOSS`. En la recepción de compras, lo que llega dañado se registra como rechazado y nunca entra al inventario, por lo que no se vuelve a descontar como merma.

El seguimiento de entregas no constituye un proceso de devolución. Las devoluciones parciales, inspección, reposición o reembolso deben manejarse en un módulo aparte, para no mezclar el estado de la orden con el del retorno y el pago.

## Migración y despliegue

La migración `V15__procurement_tracking_and_cart_recovery.sql` crea las tablas de compras y agrega campos de seguimiento, consentimiento y actividad de carritos. Flyway la aplica cuando se inicia el backend con la base de datos configurada. La migración debe incluirse al desplegar backend y frontend juntos.

## Guion breve de demostración

1. En el panel de una tienda, abre **Compras**, registra un proveedor y crea una orden con una variante.
2. Registra una recepción parcial, con unidades aceptadas y al menos una rechazada. Comprueba que el stock solo aumentó por las aceptadas y que aparece una entrada en **Movimientos**.
3. En **Inventario**, establece un umbral mayor o igual al stock actual. Vuelve al dashboard y explica la cantidad mínima sugerida.
4. Abre una orden pagada, cámbiala a enviada, registra el seguimiento y abre esa misma orden desde la cuenta del cliente.
5. Para demostrar el carrito, registra una cuenta marcando el consentimiento, deja productos en el carrito y explica que el proceso lo recordará después de 24 horas. En modo simulado, verifica el mensaje en los logs del backend. Para una demostración inmediata en local puede ajustarse temporalmente la antigüedad del carrito, luego restaurar los datos de prueba.

## Archivos principales

- Compras backend: `src/main/java/com/reydiazz/bodeko/core/procurement/`
- Compras frontend: `frontend/src/features/procurement/`
- Alerta de stock: `src/main/java/com/reydiazz/bodeko/core/inventory/web/controller/LowStockController.java` y `frontend/src/features/dashboard/DashboardPage.tsx`
- Seguimiento: `src/main/java/com/reydiazz/bodeko/core/order/tracking/` y `frontend/src/features/orders/components/OrderTrackingForm.tsx`
- Carritos y correos: `src/main/java/com/reydiazz/bodeko/core/notification/service/AbandonedCartRecoveryService.java`
- Preferencias del cliente: `src/main/java/com/reydiazz/bodeko/core/customer/web/CustomerPreferenceController.java` y `frontend/src/features/customer-orders/AccountProfilePage.tsx`
- Migración: `src/main/resources/db/migration/V15__procurement_tracking_and_cart_recovery.sql`
