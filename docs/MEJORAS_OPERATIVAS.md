# Mejoras operativas de Bodeko

## 1. Proveedores y órdenes de compra

El módulo **Compras** permite registrar proveedores y crear órdenes internas con variantes/SKU, cantidades y costo unitario. La orden queda como `ORDERED`; el administrador registra recepciones parciales. Cada línea separa unidades aceptadas, rechazadas y pendientes. Solo las aceptadas aumentan el inventario y generan `STOCK_ENTRY` en el kardex, con responsable y motivo. Las rechazadas quedan registradas en la orden y no se agregan al stock.

La orden es un registro de abastecimiento académico; no genera una factura fiscal de compra.

Endpoints, con sesión de administrador:

- `GET/POST /api/stores/{storeId}/procurement/suppliers`
- `GET/POST /api/stores/{storeId}/procurement/orders`
- `POST /api/stores/{storeId}/procurement/orders/{orderId}/receive`

Las escrituras requieren `Idempotency-Key`. Se valida que proveedor y variantes pertenezcan a la tienda, que no haya SKU/variante repetida en una orden y que lo recibido/rechazado no supere lo pendiente.

## 2. Alertas de stock bajo

El dashboard consulta las variantes activas cuyo stock está en o por debajo de `low_stock_threshold`. Las unidades se revisan por variante, no se mezclan entre tallas, tamaños o presentaciones. El administrador puede ajustar el umbral desde el editor de inventario. La cantidad sugerida es la mínima para llegar a una unidad por encima del umbral (`umbral - stock actual + 1`); sirve como guía inmediata, no como pronóstico de ventas.

- `GET /api/stores/{storeId}/inventory/low-stock`

La alerta es visible dentro del panel. No envía correo al administrador.

## 3. Seguimiento de entrega

Cuando una orden está `SHIPPED`, el administrador puede guardar transportista, número, enlace HTTPS/HTTP y fecha estimada. El cliente ve esos datos en el detalle de la orden y puede abrir el portal del transportista.

- `PATCH /api/stores/{storeId}/orders/{orderId}/tracking`

Solo el administrador propietario de la tienda puede actualizar seguimiento y solo en una orden enviada. La respuesta de orden añade `trackingCarrier`, `trackingNumber`, `trackingUrl` y `estimatedDeliveryAt`.

## 4. Recordatorio de carrito abandonado

Durante el registro de cliente, la casilla de correos promocionales queda desmarcada por defecto. Solo los clientes que aceptan reciben un recordatorio si conservan artículos en un carrito abierto sin actividad durante 24 horas. Se envía un recordatorio por periodo de actividad; si el cliente modifica el carrito, el periodo vuelve a contar. La preferencia se puede retirar desde **Mi cuenta > Datos personales**.

- `GET/PATCH /api/customer/preferences/marketing`
- La tarea de fondo consulta como máximo 100 carritos por ejecución y se repite cada 30 minutos por defecto.
- `APP_FRONTEND_BASE_URL` configura el origen del enlace de recuperación (por defecto `http://localhost:3000`).
- `CART_RECOVERY_INTERVAL_MS` permite ajustar el intervalo del proceso.
- Con proveedor de correo vacío, Bodeko usa el simulador de logs ya existente. Para entrega real, se requiere configurar las variables de Resend que ya usa el proyecto.

## Base de datos y despliegue

La migración `V15__procurement_tracking_and_cart_recovery.sql` agrega proveedores, órdenes y líneas de compra, campos de seguimiento, consentimiento y actividad del carrito. Aplicar la migración antes de usar estas rutas; en el despliegue normal Flyway la ejecuta al iniciar el backend.

Las recepciones, ajustes de stock y registros de merma son operaciones transaccionales. El kardex guarda entradas aceptadas, mientras que unidades rechazadas en tránsito no alteran las existencias disponibles.

## Archivos principales

- Backend de compras: `src/main/java/com/reydiazz/bodeko/core/procurement/`
- Pantalla de compras: `frontend/src/features/procurement/`
- Consulta de stock bajo: `src/main/java/com/reydiazz/bodeko/core/inventory/web/controller/LowStockController.java`
- Seguimiento: `src/main/java/com/reydiazz/bodeko/core/order/tracking/`
- Recuperación: `src/main/java/com/reydiazz/bodeko/core/notification/service/AbandonedCartRecoveryService.java`
- Consentimiento: `src/main/java/com/reydiazz/bodeko/core/customer/web/CustomerPreferenceController.java`
