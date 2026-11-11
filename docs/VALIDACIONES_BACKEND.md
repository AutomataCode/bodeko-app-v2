# Validaciones, concurrencia e idempotencia - 11/11/2026

Esta revisión parte de la carpeta `bodeko` compartida con el equipo. No publica ni crea commits. No modifica las migraciones V1-V5 ni utiliza la base de trabajo del equipo para las pruebas.

## Cambios solicitados

1. **Tiendas:** RUC y subdominio continúan siendo únicos globalmente. Se añade nombre único por propietario, ignorando mayúsculas y espacios sobrantes. Propietarios distintos pueden tener el mismo nombre comercial; el RUC y el subdominio no se pueden repetir.
2. **Correo:** normalización con Locale.ROOT y un índice único sobre lower(trim(email)), que protege también escrituras simultáneas o registros antiguos con diferencias de mayúsculas.
3. **Productos:** nombre único dentro de cada tienda, ignorando mayúsculas y espacios sobrantes. Incluye borradores, activos, inactivos y archivados; se debe editar o reactivar el registro existente. Otra tienda puede usar el mismo nombre. Las variantes deben tener un nombre distinguible, porque el modelo no dispone de SKU de variante.
4. **Desactivar:** nuevo estado `INACTIVE`. El producto permanece en administración e historial, pero no aparece en el catálogo público ni puede agregarse al carrito o confirmarse en un checkout. Se puede reactivar. Las órdenes ya creadas conservan sus datos históricos.
5. **Descuento:** crear ofertas requiere `discountPercentage`, entre 0.01 y 99.99, con hasta dos decimales. El servidor calcula el importe sobre el precio base vigente, redondea HALF_UP a dos decimales y conserva como mínimo 0.01 de unidad monetaria. `offerPrice` sigue en la respuesta para los consumidores de lectura; ya no es la entrada para una oferta nueva.

Los duplicados se comprueban en servicios para dar un error claro y se refuerzan con índices únicos para cubrir la carrera entre comprobar e insertar. PostgreSQL es la última defensa cuando dos solicitudes llegan juntas. Una migración no elimina, fusiona ni renombra datos existentes automáticamente.

## Contrato de ofertas

`POST /api/stores/{storeId}/products/{productId}/offers` con JWT de propietario:

```json
{
  "discountPercentage": 25.0,
  "startsAt": "2026-12-01T09:00:00-05:00",
  "endsAt": "2026-12-15T18:00:00-05:00"
}
```

Un producto de 20.00 queda en 15.00; si su precio base cambia a 40.00, queda en 30.00 mientras la oferta siga vigente. El porcentaje no se calcula en el navegador. El rango final debe ser posterior al inicial y no debe superponerse con otra oferta del mismo producto. Se bloquea el producto durante la creación de ofertas para serializar solicitudes concurrentes.

V7 conserva `offer_price` y convierte las ofertas anteriores solo cuando un porcentaje de dos decimales reproduce exactamente su importe. Las demás conservan `discountPercentage: null` y su importe anterior; la interfaz las identifica como ofertas anteriores. No cambia precios históricos de órdenes. Los nuevos datos demo ya incluyen porcentaje cuando es representable.

## Cabecera obligatoria en operaciones críticas

Las siguientes rutas requieren `Idempotency-Key`:

- POST `/api/customer/stores/{storeId}/orders`.
- POST `/api/customer/stores/{storeId}/orders/{orderId}/payments`.
- POST `/api/customer/orders/{orderId}/cancel`.
- PATCH `/api/stores/{storeId}/orders/{orderId}/status`.

Usar un UUID por intención de operación. La clave admite de 8 a 128 caracteres alfanuméricos, punto, guion y dos puntos. **Un reintento de red conserva la misma clave y los mismos datos. Una compra o intento nuevo usa otra clave.** Cambiar los datos con una clave ya usada produce 409 `IDEMPOTENCY_CONFLICT`. Los ejemplos están en `docs/API_VALIDACIONES.http`.

La respuesta exitosa se guarda con los efectos locales en la misma transacción. Un reintento devuelve el resultado original y el mismo estado HTTP, sin repetir checkout, cobro, devolución o transición. Los errores de negocio ya registrados se reproducen con la misma clave; para corregir la operación se necesita un intento nuevo. La autorización y la propiedad de la orden se comprueban antes de reproducir sus datos.

El frontend incluido genera y conserva claves opacas para reintentos. La identidad del checkout también incorpora el id del carrito: una compra futura con los mismos datos fiscales recibe una clave distinta. No se guarda el token de pago en sessionStorage: solo una huella y la clave. Los otros clientes del equipo deben enviar esta cabecera por su cuenta.

## Cómo se evita una venta doble

El carrito no reserva unidades al agregar productos. La reserva se efectúa al crear la orden, como en el diseño previo. Dos clientes pueden tener la última unidad en sus carritos; al confirmar, solo uno obtiene la orden y el otro recibe 409 por stock insuficiente.

Se conservan los bloqueos de carrito e inventario existentes y se refuerzan las escrituras concurrentes:

- La creación/modificación del carrito bloquea su fila; un cerrojo transaccional por cliente y tienda evita crear dos carritos abiertos o perder incrementos simultáneos.
- Checkout bloquea productos por id ordenado y refresca sus datos después de esperar el cerrojo. Así no usa un estado o precio anterior a una modificación concurrente.
- El inventario se consulta con bloqueo antes de comprobar y descontar. La actualización administrativa también usa ese bloqueo.
- Las órdenes siguen bloqueándose antes de pagar o cancelar y su estado se refresca dentro del bloqueo para descartar la versión cargada por una lectura previa. El índice parcial de pagos evita más de un pago APPROVED por orden.
- La creación de órdenes y su resultado idempotente se confirman juntos. Los bloqueos se toman en un orden consistente para reducir interbloqueos. Los conflictos de concurrencia esperados se devuelven como 409.

## Resultado incierto del proveedor

La tabla `api_idempotency` conserva IN_PROGRESS, COMPLETED, FAILED o UNCERTAIN. Antes de llamar al proveedor, se confirma por separado el marcador `external_started`. El cuerpo recibido no se guarda; su huella SHA-256 permite comparar reintentos. Las respuestas guardadas son datos privados del servidor y no se deben exportar con datos reales.

Si el proveedor pudo haber cobrado o devuelto dinero y se pierde su respuesta, no existe una transacción distribuida que lo deshaga. El marcador queda UNCERTAIN y bloquea nuevas operaciones externas sobre esa orden, incluso con otra clave o desde administración. Una caída del proceso puede dejar IN_PROGRESS y también requiere revisión antes de repetir un movimiento externo.

**No borrar estos registros ni cambiar claves para saltar el bloqueo.** El equipo debe conciliar la orden con el proveedor usando su número y metadatos, y completar o anular el movimiento mediante un procedimiento auditado. No se agregó un endpoint de conciliación ni se implementaron webhooks de Culqi. No se afirma idempotencia nativa del proveedor: esta protección está en la aplicación. No hay reintentos automáticos de cobros inciertos.

## Otras entradas protegidas

- Contraseñas de hasta 72 bytes UTF-8, también en login, para no disparar una excepción de BCrypt con entradas largas.
- Nombres y cadenas de las solicitudes sin el carácter nulo, que PostgreSQL no admite en texto.
- Precio nuevo de producto entre 0.01 y 100000.00, con dos decimales; descripción de hasta 10000 caracteres.
- URL de imagen opcional: http/https con host y sin credenciales.
- Hasta 10000 unidades por producto en carrito, incluyendo incrementos acumulados, y hasta 1000000 en los campos administrativos de inventario.
- El total de checkout y los importes enviados a la pasarela deben caber en un entero positivo de céntimos. Un exceso devuelve un error controlado.
- Las cantidades fraccionarias y los estados enviados como números se rechazan: no se truncan ni se convierten silenciosamente. Errores por UUID, enum, parámetros ausentes, JSON inválido y tipo de contenido incorrecto tienen respuestas 4xx. Los mensajes generales y de campos son legibles en español. Los códigos previos se mantienen.
- Los conflictos de integridad registran el tipo del conflicto, sin escribir el SQL ni el valor de un correo o documento en el log de ese manejador.

## Migraciones antes del despliegue

1. Ejecutar **solo lectura** `scripts/db/03_prevalidar_mejoras.sql` en la base que se actualizará. Revisar las filas devueltas con el equipo; el resultado puede contener datos privados y no debe subirse al repositorio.
2. Resolver duplicados de correos, nombres y subdominios, y pagos APPROVED múltiples si existieran. No se eliminan automáticamente porque pueden tener órdenes asociadas.
3. Dejar que Flyway aplique V6 (unicidad e INACTIVE), V7 (ofertas) y V8 (idempotencia). V1-V5 se conservaron.
4. Desplegar conjuntamente el contrato de ofertas y las cabeceras de operaciones críticas en todos los clientes.

Si existen duplicados, V6 se detiene en lugar de perder datos o desplegar una protección incompleta. No se cambiaron pom.xml, credenciales ni servicios cloud. Las pruebas se ejecutaron con Java 21 usando `-Djava.version=21`; el proyecto conserva Java 23 declarado.

## Respuestas que los clientes deben manejar

| Estado / código                                             | Significado                                                                                 |
| ----------------------------------------------------------- | ------------------------------------------------------------------------------------------- |
| 400 VALIDATION_ERROR, INVALID_REQUEST, IDEMPOTENCY_REQUIRED | Revisar campos, tipos o clave ausente.                                                      |
| 401 / 403                                                   | Sesión o permisos.                                                                          |
| 404                                                         | Recurso inexistente o no accesible por esa cuenta.                                          |
| 409 PRODUCT_DUPLICATE, STORE_DUPLICATE, USER_EMAIL_EXIST    | Duplicado controlado.                                                                       |
| 409 INVENTORY_INSUFFICIENT_STOCK                            | Otra compra consumió las unidades; actualizar el carrito.                                   |
| 409 IDEMPOTENCY_CONFLICT                                    | No reutilizar una clave con datos diferentes.                                               |
| 409 OPERATION_IN_PROGRESS                                   | Conservar clave y consultar/reintentar después de que termine.                              |
| 409 OPERATION_UNCERTAIN                                     | Revisar con el proveedor; no repetir el movimiento.                                         |
| 402 PAYMENT_DECLINED                                        | Rechazo de pago conocido y persistido.                                                      |
| 422 PAYMENT_REFUND_REJECTED                                 | Devolución rechazada; los cambios locales se revierten.                                     |
| 502 PAYMENT_GATEWAY_UNAVAILABLE                             | No se confirmó una respuesta de la pasarela. Consultar estado; puede requerir conciliación. |

Una respuesta 4xx ante una entrada inválida o un conflicto es comportamiento correcto. No es posible prometer que una caída de PostgreSQL o del proveedor nunca produzca un error. Las pruebas buscan evitar 500 inesperados en los escenarios verificados, sobreventa y efectos duplicados.

## Verificación y pruebas

- 115 comprobaciones HTTP nuevas: duplicados concurrentes, INACTIVE, porcentajes, entrada inválida, última unidad, carrito concurrente, replay, conflicto de huellas, cobro/devolución únicos y resultado incierto.
- Regresión: 55 comprobaciones del flujo inicial y 56 de órdenes del cliente, ambas adaptadas al contrato nuevo.
- 18 pruebas del frontend y compilación de producción.
- Pruebas Java: contexto de Spring, reglas monetarias y actualización V5 a V8 con ofertas antiguas en PostgreSQL temporal. Consultar los reportes JUnit al ejecutarlas.

Nueva batería: `scripts/qa/verificar-validaciones.mjs --disposable`. Exige QA_API_URL, QA_MOCK_URL, una API local conectada a base **vacía desechable**, y `python scripts/qa/culqi-pruebas.py --disposable` en otra terminal. El simulador de QA escucha solo en 127.0.0.1:8210 y permite el caso de respuesta perdida; no es el simulador de uso cotidiano ni una pasarela real.

Para la prueba Java de actualización: activar BODEKO_UPGRADE_TEST=true y BODEKO_UPGRADE_DB=jdbc:postgresql://127.0.0.1:55433/bodeko_upgrade_qa, con esa base vacía y desechable. Sin la variable de activación, esa prueba se omite para no crear ni migrar una base externa por accidente. No ejecutar las pruebas que crean datos contra una base de trabajo.

Los archivos cambiados se enumeran en `docs/ARCHIVOS_VALIDACIONES.md`. No se crearon commits ni se publicó el repositorio.

Referencia de configuración JSON: las propiedades de EnumFeature en Jackson 3 usan `spring.jackson.datatype.enum.*`, según la [documentación oficial de Spring Boot](https://docs.spring.io/spring-boot/how-to/spring-mvc.html).
