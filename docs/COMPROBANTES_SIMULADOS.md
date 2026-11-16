# Boletas y facturas simuladas

## Flujo

1. El cliente elige boleta o factura en el checkout y guarda sus datos de identificación.
2. El pago aprobado cambia la orden a `PAID` y crea un documento interno idempotente. Los pagos fallidos no crean comprobantes.
3. Si el perfil fiscal de la tienda tiene RUC, razón social y domicilio, se genera el correlativo. Si falta información, queda `PENDING_CONFIGURATION` y no se bloquea la venta.
4. Al guardar el perfil fiscal, los documentos pendientes de las tiendas asociadas al emisor reciben su número.
5. Cliente y administrador pueden abrir la vista e imprimirla o guardarla como PDF desde el navegador.
6. Si se cancela y reembolsa una orden pagada, su documento simulado queda anulado; no se reutiliza su número.

## Emisores compartidos

Los perfiles se identifican por propietario y RUC. Varias tiendas del mismo propietario pueden asociar el mismo perfil y compartir su numeración para boletas (`B001`) y facturas (`F001`). Los documentos guardan una copia de RUC, razón social y domicilio para conservar los datos usados al emitir.

## Rutas

- `GET /api/stores/{storeId}/billing-profile`
- `PUT /api/stores/{storeId}/billing-profile`
- `GET /api/stores/{storeId}/orders/{orderId}/sales-document`
- `GET /api/customer/orders/{orderId}/sales-document`

Es una simulación académica local: no genera XML UBL, no firma, no envía datos a SUNAT ni crea una CDR. El PDF se obtiene con la función de impresión del navegador. Las migraciones `V13` y `V14` agregan perfiles compartidos, numeración y documentos.
