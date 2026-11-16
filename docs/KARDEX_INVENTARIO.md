# Kardex de inventario por tienda

## Alcance

El panel de administración incluye la opción **Movimientos**. La consulta usa la tienda seleccionada en la ruta y el backend limita todos los resultados a ese `storeId`; la autorización existente permite el acceso solo al administrador propietario de esa tienda.

## Eventos registrados

- **Reserva:** al crear el pedido se descuenta el stock disponible y se registra una cantidad negativa.
- **Venta concretada:** al aprobarse el pago se registra el cierre de la reserva. Su cambio de stock es cero porque las unidades ya se descontaron al reservar.
- **Reserva liberada:** al cancelar un pedido pendiente se reponen las unidades.
- **Devolución:** al cancelar una orden pagada se repone el stock, después de gestionar el reembolso.
- **Entrada, ajuste, daño o pérdida:** al guardar la nueva cantidad en inventario se registra la diferencia, el motivo y el administrador responsable.

Los registros guardan SKU, opciones de variante, saldos antes y después, pedido/motivo, usuario y fecha. La tabla está indexada por tienda, tipo, variante y fecha. La consulta se pagina y admite filtros de producto/SKU, movimiento, responsable, fechas y pedido/motivo.

## Endpoint

`GET /api/stores/{storeId}/inventory/movements`

Parámetros opcionales: `search`, `type`, `actor`, `reference`, `from`, `to`, `page` y `size`.

La migración `V12__inventory_movement_audit.sql` crea la tabla e índices y registra el saldo actual de cada variante con stock como un ajuste inicial de referencia. Los eventos históricos previos no se reconstruyen; el historial detallado empieza con los eventos registrados a partir de la activación.
