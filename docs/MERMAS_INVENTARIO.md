# Control de mermas de inventario

## Funcionamiento

El panel de administración de cada tienda incluye **Inventario > Mermas**. El administrador selecciona producto y variante, registra unidades, clasifica el caso como daño o pérdida y describe el motivo. El backend valida que la variante pertenezca a esa tienda, esté activa y tenga stock suficiente; además bloquea la fila de inventario durante la operación para evitar que una venta concurrente consuma las mismas unidades.

Al guardar, el stock disponible se reduce dentro de una transacción y se crea un movimiento `DAMAGE` o `LOSS` en el kardex, con unidades, saldo anterior y nuevo, SKU, variante, tienda, responsable, fecha y motivo. No se elimina el producto ni el historial. El historial tiene filtros y paginación.

## API

- `POST /api/stores/{storeId}/inventory/shrinkages`
- `GET /api/stores/{storeId}/inventory/shrinkages`

Ejemplo de alta:

```json
{
  "productId": "UUID_DEL_PRODUCTO",
  "variantId": "UUID_DE_LA_VARIANTE",
  "quantity": 2,
  "type": "DAMAGE",
  "reason": "Dos unidades con el envase roto durante el almacenamiento."
}
```

La operación exige usuario autenticado propietario de la tienda. Cantidad: entero entre 1 y 10 000 y no mayor que el stock disponible. El tipo solo acepta `DAMAGE` o `LOSS`; el motivo es obligatorio y admite hasta 500 caracteres. No requiere migración porque aprovecha el kardex existente.

## Devoluciones

Propuesta para la siguiente fase: permitir que el cliente solicite dentro de los 30 días de la entrega la devolución de uno o varios artículos y cantidades, sin cancelar toda la orden. El vendedor revisa la solicitud, confirma recepción e inspecciona cada artículo. Los artículos revendibles vuelven al inventario con movimiento `RETURN`; los defectuosos quedan fuera del stock vendible y se registran como `DAMAGE`. La devolución y el reembolso deben tener estados propios, registrar actor y timestamps, y usar una clave de idempotencia para evitar duplicados al reintentar. Antes de integrar reembolsos, hay que confirmar las reglas académicas de cálculo de impuestos y el alcance del simulador de pago.
