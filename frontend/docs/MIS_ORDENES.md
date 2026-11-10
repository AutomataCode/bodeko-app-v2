# Mi cuenta y mis órdenes — Bodeko

## Acceso y diseño

Entra con una cuenta de cliente y abre **Mis órdenes** en el menú superior o visita `/cuenta/ordenes`. La cuenta ADMIN conserva su panel de ventas; el área de compras requiere CUSTOMER.

El diseño toma de la referencia la distribución con saludo/avatar y navegación a la izquierda y paneles de contenido a la derecha. Utiliza colores de Bodeko: verde #008060, fondos #F6F6F7, tarjetas blancas, bordes suaves e iconos Lucide. En móvil, el menú pasa a una rejilla y los filtros se desplazan dentro de su propia franja; el documento no desborda horizontalmente.

Se incluyen opciones operativas: datos personales (consulta), mis órdenes, explorar tiendas y cerrar sesión. No se muestran pestañas vacías para direcciones, favoritos o cambio de contraseña.

## Acciones disponibles

| Estado    | Ver detalle | Pagar                               | Cancelar como cliente                 |
| --------- | ----------- | ----------------------------------- | ------------------------------------- |
| Pendiente | Sí          | Sí, desde la tienda correspondiente | Sí, repone stock                      |
| Pagada    | Sí          | No                                  | Sí, solicita reembolso y repone stock |
| Enviada   | Sí          | No                                  | No                                    |
| Entregada | Sí          | No                                  | No                                    |
| Cancelada | Sí          | No                                  | No                                    |

La lista reúne compras de todas las tiendas del cliente, ordenadas por fecha descendente, y permite filtrar por estado con paginación de seis resultados por página. El detalle conserva nombres, importes y datos fiscales históricos. Los filtros y la página se guardan en la URL para permitir navegación y recarga.

La cancelación requiere una confirmación explícita. La UI bloquea el envío mientras hay una solicitud pendiente y presenta los errores en español mediante Sonner y un aviso persistente. El servidor comprueba propiedad y estado, bloquea la fila y reutiliza la transacción de cancelación existente. Un error de devolución revierte los cambios locales; no se declara la orden cancelada si el reembolso se rechazó. Dos solicitudes concurrentes no duplican la reposición de stock.

Los tiempos reales de abono de una devolución no se estiman en la UI: dependen de la pasarela. Los pagos de las órdenes históricas del seed no existen en la memoria del simulador del equipo. Para una cancelación pagada exitosa, crea una orden nueva, págala con `tkn_test_ok` y cancélala sin reiniciar el simulador. Las cancelaciones de órdenes pendientes no requieren contactar a la pasarela.

## Archivos de frontend

```text
src/features/customer-orders/
├── components/
│   ├── AccountLayout.tsx
│   ├── CancelOrderDialog.tsx
│   └── CustomerOrderCard.tsx
├── hooks/
│   ├── useCustomerOrder.ts
│   └── useCustomerOrders.ts
├── services/
│   ├── customerOrderService.ts
│   └── customerOrderService.test.ts
├── types/index.ts
├── AccountProfilePage.tsx
├── CustomerOrderDetailPage.tsx
├── CustomerOrdersPage.tsx
└── index.ts
```

`App.tsx` registra las rutas protegidas, `SiteLayout.tsx` añade el acceso y `PaymentPage.tsx` enlaza con el historial y detalle de cuenta. Los estilos nuevos viven en `assets/styles.css`. Se reutilizan `OrderDetails`, `Modal`, `Pagination`, `ErrorNotice`, `useResource` y `useAction`.

## Backend y seguridad

```text
GET  /api/customer/orders?status=PAID&page=0&size=6
GET  /api/customer/orders/{orderId}
POST /api/customer/orders/{orderId}/cancel
```

El listado y el detalle obtienen la identidad del JWT. La cancelación no admite un estado ni un customerId en el cuerpo: solo puede solicitar CANCELLED. Los ids de otra cuenta devuelven 404; una cuenta ADMIN recibe 403 en las rutas de cliente. El tamaño de página se limita entre 1 y 100.

No se agregaron tablas ni migraciones. El detalle del cambio se encuentra en [CAMBIOS_BACKEND.md](CAMBIOS_BACKEND.md).

## Pruebas realizadas

- Compilación de frontend y 14 pruebas Vitest aprobadas.
- Compilación de 214 fuentes Java y prueba `ApplicationTests` aprobadas. Se usó Java 21 con salida Maven aislada del target generado por IntelliJ con Java 23; el POM principal sigue declarando Java 23.
- 56 comprobaciones HTTP de la nueva funcionalidad contra Spring/PostgreSQL reales y el simulador del equipo en una instancia de QA separada.
- 55 comprobaciones HTTP de regresión del flujo anterior de administración/compra aprobadas.
- SQL: pago aprobado convertido a REFUNDED al cancelar, actor CUSTOMER registrado en historial y orden/stock intactos tras un reembolso rechazado.
- Navegador: filtro de pendientes, abrir confirmación y conservar orden, confirmar cancelación, actualizar lista, recargar detalle cancelado, error de devolución en español y navegación de datos personales.
- Revisión visual a 1440 px y 390 px; sin desbordamiento horizontal del documento.

Capturas: [escritorio](capturas/mis-ordenes-escritorio.png) y [móvil](capturas/mis-ordenes-movil.png). Los datos visibles son sintéticos. Las pruebas se ejecutaron sobre una base nueva `bodeko_customer_orders_qa_20261110` en el PostgreSQL temporal del puerto 55432, backend 8081, frontend 3001 y simulador 8198. No se cargaron ni cancelaron órdenes en `db_bodekos_v2` ni se reinició el simulador del usuario.

La prueba reproducible `tools/verify-customer-orders.mjs --disposable` requiere el seed y una API local conectada a una base desechable. `QA_API_URL` selecciona la API y `QA_APPROVED_TOKEN` permite adaptar el token de otro simulador compatible.
