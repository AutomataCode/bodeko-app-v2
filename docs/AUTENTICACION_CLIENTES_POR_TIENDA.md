# Clientes registrados por tienda

Esta mejora separa las cuentas de propietarios de las cuentas de clientes. El propietario mantiene su cuenta de Bodeko y administra una o más tiendas. El cliente crea una cuenta desde una tienda concreta y solo puede usar el carrito, las órdenes y los pagos de esa tienda.

## Rutas

- `POST /api/auth/register-owner`: registro de propietario.
- `POST /api/auth/login`: inicio de sesión de propietario.
- `POST /api/auth/stores/{storeId}/register`: registro de cliente en esa tienda.
- `POST /api/auth/stores/{storeId}/login`: inicio de sesión de cliente en esa tienda.
- `GET /api/auth/me`: datos de la cuenta autenticada.

Los clientes pueden usar el mismo correo en tiendas diferentes. El correo debe ser único dentro de cada tienda. Los endpoints de cliente requieren un JWT de cliente y el backend comprueba que la tienda de la ruta coincida con la tienda asociada a la cuenta.

## Persistencia y migración

Los propietarios continúan en `users`. Las cuentas de cliente se guardan en `customers`, que relaciona cada cuenta con una tienda. Las contraseñas se protegen con el mismo encoder BCrypt del sistema. Las referencias desde carritos y órdenes apuntan al identificador de cliente.

Flyway aplica esta transición mediante V9. Para los clientes existentes que tengan carrito u órdenes, la migración crea una cuenta por cada tienda donde registran actividad y actualiza esas referencias. Cuentas antiguas sin carrito ni órdenes no permiten inferir una tienda; V9 ahora se detiene antes de modificar datos si encuentra alguna. El equipo debe decidir y documentar su asignación antes de desplegar.

Antes de aplicar V9 en un entorno con datos, hacer respaldo y revisar los clientes antiguos y sus actividades. No ejecutar la migración directamente en producción sin comprobar el resultado de esa revisión.

## Alcance

La separación de cuentas no implementa variantes de producto ni compras a proveedores. El inventario sigue representando existencias disponibles; el abastecimiento mediante facturas o documentos de compra queda fuera de este módulo.
