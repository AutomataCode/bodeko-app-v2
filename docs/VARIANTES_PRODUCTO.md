# Variantes de productos en Bodeko

## Modelo

El producto guarda la información compartida (nombre, descripción, imagen, categoría e impuestos). `product_variants` guarda cada unidad vendible con su SKU, opciones, precio y estado. Por ejemplo, un polo es un producto y `Talla=M, Color=Negro` es una variante.

Cada tienda debe tener SKU únicos. También se impide registrar dos veces la misma combinación de opciones dentro de un producto. El inventario, el carrito y cada línea de orden apuntan al identificador de la variante. Los datos de SKU y opciones se copian a la orden como una fotografía del momento de compra.

## Compatibilidad de datos existentes

La migración Flyway `V10__product_variants.sql` convierte cada producto previo en su variante estándar, conserva el UUID que ya tenía como ID de variante y conecta su inventario, carrito y líneas de órdenes. Esto permite conservar las referencias existentes. Al iniciar el backend, Flyway ejecuta la migración; respalda la base antes de desplegarla y no edites una migración que ya se haya aplicado en otro entorno.

## Flujo para el administrador de tienda

1. Crea o edita un producto con sus datos generales y precio estándar.
2. En el editor de ese producto, agrega variantes con SKU, opciones separadas por coma (`Talla=M, Color=Negro`) y precio.
3. Actualiza el stock de cada variante. La variante comienza con stock cero.
4. Pausa o activa una variante sin eliminarla ni borrar su historial.

Endpoints, autenticados como administrador autorizado de la tienda:

```http
GET   /api/stores/{storeId}/products/{productId}/variants
POST  /api/stores/{storeId}/products/{productId}/variants
PUT   /api/stores/{storeId}/products/{productId}/variants/{variantId}
PATCH /api/stores/{storeId}/products/{productId}/inventory/variants/{variantId}/quantity
PATCH /api/stores/{storeId}/products/{productId}/variants/{variantId}/status
```

Ejemplo para crear una variante:

```json
{
  "sku": "POLO-NEG-M",
  "options": { "Talla": "M", "Color": "Negro" },
  "basePrice": 49.90
}
```

Ejemplo para fijar el stock en 12 unidades:

```json
{ "quantity": 12 }
```

`PUT` usa el mismo formato del ejemplo de creación y actualiza SKU, opciones y precio. Conserva el ID de la variante; las órdenes anteriores conservan su propia copia de SKU y opciones.

Ejemplo para pausar la venta:

```json
{ "active": false }
```

## Flujo para el cliente

El catálogo sigue mostrando una sola tarjeta por producto agrupado y el menor precio entre sus variantes activas. Al abrir el detalle, el cliente elige la presentación/talla/color; ahí se actualizan precio, stock y SKU. El carrito envía `productId`, `variantId` y cantidad. No se puede añadir ni comprar una variante pausada o sin stock. El checkout bloquea el inventario de la variante para descontar unidades de forma segura ante compras simultáneas.

Ejemplo de agregar al carrito:

```json
{
  "productId": "UUID-del-producto",
  "variantId": "UUID-de-la-variante",
  "quantity": 1
}
```

La orden conserva SKU y opciones, además del precio cobrado, para que el detalle muestre qué presentación se compró aunque luego cambie el producto.

## Descuentos

Las ofertas actuales continúan configurándose por producto como porcentaje. Se aplica el mismo porcentaje al precio de cada variante. El detalle y el carrito muestran el importe resultante de la variante elegida.
