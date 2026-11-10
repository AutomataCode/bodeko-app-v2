# Cambios de backend para integrar el frontend

## Configuración sin credenciales en el código

`application.properties` lee la conexión de `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`, y la firma de `JWT_SECRET`. Los valores locales anteriores se conservaron únicamente en `.env.dev`, excluido de Git. `.env.example` contiene la estructura sin secretos. La clave JWT de CI se genera para cada ejecución. No se cambió la lógica del backend ni la base de datos en esta revisión.

El historial original todavía contiene una contraseña y una clave JWT. Ver el alcance, los archivos excluidos y las opciones de publicación en `SEGURIDAD_REPOSITORIO.md`. Las exclusiones no eliminan archivos que ya formen parte de un commit.

## Landing: tiendas con más ventas

Nuevo endpoint público: `GET /api/public/stores/top-selling`. Devuelve un arreglo de hasta tres tiendas, ordenadas por cantidad de órdenes con estado `PAID`, `SHIPPED` o `DELIVERED` en todo su historial. Excluye `PENDING`, `CANCELLED` y tiendas sin ventas. No suma importes ni unidades de productos. Los empates se resuelven por nombre, sin distinguir mayúsculas, y luego por identificador.

La respuesta contiene `id`, `name`, `subdomain`, `currency`, `taxRate` y `salesCount`. No incluye datos del propietario, documentos fiscales ni detalles de pagos. Si no hay ventas devuelve `[]`. No recibe parámetros; el máximo de tres se aplica en la consulta de base de datos.

Archivos modificados: `PublicStoreController.java` añade la ruta de lectura y los estados válidos; `StoreRepository.java` agrega una consulta agrupada; `TopSellingStoreResponse.java` define la respuesta pública. Se aprovecha el índice existente de órdenes por tienda y estado. No requiere migraciones, columnas nuevas ni cambios de seguridad. El directorio completo conserva `GET /api/public/stores`.

Verificado con PostgreSQL temporal y peticiones HTTP sin sesión: resultado vacío, estados incluidos y excluidos, límite de tres, desempates por nombre, orden por cantidad frente a importe, campos públicos y actualización tras cancelar ventas. La base de desarrollo del equipo no se modificó. La prueba reproducible está en `frontend/tools/verify-top-selling.mjs`, con instrucciones en `frontend/docs/LANDING.md`.

## Comentarios en español

Se tradujeron y simplificaron los comentarios Java del catálogo, registro, usuario y órdenes del cliente. Explican las consultas, los permisos y la cancelación con frases breves en español. Esta revisión no cambia la lógica, los contratos de API ni la base de datos.

Los comentarios de las migraciones SQL existentes se conservan para no cambiar las comprobaciones de Flyway de bases ya creadas. En el frontend también se simplificaron los comentarios de componentes, tipos, servicios y hooks, manteniendo sus descripciones JSDoc.

## Área de cliente: Mis órdenes y cancelación

Ampliación del 10 de noviembre de 2026: tres endpoints autenticados permiten consultar las compras del cliente en todas las tiendas y cancelarlas cuando corresponda. No requiere tablas, columnas, migraciones ni cambios en SecurityConfig.

```text
GET  /api/customer/orders?status=PENDING&page=0&size=6
GET  /api/customer/orders/{orderId}
POST /api/customer/orders/{orderId}/cancel
```

| Archivo                                                              | Cambio                                                                                                                                                                   |
| -------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `core/order/core/repository/OrderRepository.java`                    | Consultas paginadas por cliente/estado, detalle por cliente y consulta con bloqueo de escritura por cliente. EntityGraph carga la tienda en el listado.                  |
| `core/order/customer/web/controller/CustomerOrderController.java`    | Controlador nuevo bajo las rutas CUSTOMER existentes. La identidad proviene del principal JWT; no acepta customerId del navegador ni un estado arbitrario para cancelar. |
| `core/order/customer/service/CustomerOrderService.java`              | Contrato de lista, detalle y cancelación del cliente.                                                                                                                    |
| `core/order/customer/service/CustomerOrderServiceImpl.java`          | Lecturas con alcance de cliente. Para cancelar, bloquea la orden comprobando propiedad y delega al servicio de gestión existente con estado fijo CANCELLED.              |
| `core/order/customer/web/response/CustomerOrderSummaryResponse.java` | Resumen con tienda, moneda y enlace al pago, sin información de otras cuentas.                                                                                           |
| `core/order/customer/web/response/CustomerOrderDetailResponse.java`  | Detalle histórico existente envuelto con información pública de la tienda.                                                                                               |

La cancelación reutiliza `OrderManagementService.changeStatus` sin modificar sus reglas: admite PENDING o PAID; restaura stock, registra la transición con el usuario que la solicita y devuelve el pago aprobado cuando existe. Si el proveedor rechaza la devolución, se revierte la transacción local: ni estado, ni stock, ni auditoría quedan cambiados. Un cliente no puede cancelar una orden SHIPPED, DELIVERED o CANCELLED. El bloqueo de fila también evita reponer stock dos veces ante solicitudes simultáneas. Las consultas SQL de verificación ahora llaman “usuario” al actor, porque puede ser cliente o administrador.

Verificación nueva: 56 comprobaciones HTTP de cliente, regresión de las 55 comprobaciones anteriores, 14 pruebas frontend y `ApplicationTests` pasadas con PostgreSQL aislado. Se verificaron además en SQL el pago REFUNDED, la identidad CUSTOMER en la auditoría y la conservación de stock/estado tras rechazar un reembolso. La prueba Maven se ejecutó con Java 21 y salida de compilación aislada, porque el target usado por IntelliJ contenía clases Java 23; no se cambió el POM principal.

La interfaz y el recorrido están documentados en [Mis órdenes](MIS_ORDENES.md).

Configuración del simulador del equipo: se creó `.env.dev` (excluido de Git) con `CULQI_API_URL=http://127.0.0.1:8199/v2` y la clave ficticia `CULQI_SECRET_KEY=sk_test_mock`. Se copió el adjunto del equipo sin cambios a `scripts/culqi-mock/server.py`, se actualizó el ejemplo del campo de pago y la prueba HTTP permite configurar su token aprobado. No se cambió código Java ni la conexión a la base. El simulador anterior sigue disponible como alternativa; los cargos de uno no se transfieren al otro.

Actualización posterior: la marca visual se corrigió a **Bodeko** por indicación del usuario. Se añadieron scripts manuales de datos ficticios en `scripts/db` y un ejecutor PowerShell; esa actualización no cambia código Java, esquema ni migraciones. Los scripts se comprobaron en la base temporal de QA y no se ejecutaron sobre una base de trabajo del usuario.

Corrección de compatibilidad del historial: se inspeccionó la estructura real de `db_bodekos` en modo de solo lectura y se encontró la variante `previous_status`, `new_status`, `changed_at`, sin `changed_by`. El nuevo `scripts/db/00_compatibilidad_historial.sql` permite alinear manualmente esos nombres con la entidad Java antes de cargar el demo. Conserva datos, añade el actor como opcional para filas antiguas y mantiene la referencia a usuarios. No asigna actores ficticios ni modifica el historial de Flyway. El ejecutor aplica este ajuste antes del seed y se guarda con BOM UTF-8 para Windows PowerShell 5.1. La corrección se prueba en una copia temporal con la estructura antigua; no se aplica automáticamente desde esta conversación a la BD de trabajo.

Fecha: 10 de noviembre de 2026. Se mantuvieron los contratos existentes de escritura y las reglas de autorización. Las ampliaciones cubren lecturas que faltaban y el documento opcional de HU-01.

## Acceso público a tiendas y catálogo

| Archivo Java                                             | Cambio y motivo                                                                                                                            |
| -------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| `core/catalog/web/controller/PublicStoreController.java` | Nuevo controlador de solo lectura para descubrir tiendas, resolver subdominio y listar categorías públicas. Verifica que la tienda exista. |
| `core/catalog/web/response/PublicStoreResponse.java`     | Nueva proyección con id, nombre, subdominio, moneda e IGV. Excluye propietario, RUC y razón social.                                        |
| `core/store/repository/StoreRepository.java`             | Añade `findBySubdomainIgnoreCase` para resolver las URLs compartibles.                                                                     |
| `core/catalog/service/CatalogService.java`               | Añade consulta pública individual.                                                                                                         |
| `core/catalog/service/CatalogServiceImpl.java`           | Recupera un producto por id y tienda, exige estado ACTIVE e incorpora oferta y disponibilidad usando los servicios existentes.             |
| `core/catalog/web/controller/CatalogController.java`     | Expone el detalle público. Los borradores y archivados responden como recurso inexistente.                                                 |

Rutas añadidas:

```text
GET /api/public/stores
GET /api/public/stores/by-subdomain/{subdomain}
GET /api/public/stores/{storeId}/categories
GET /api/public/stores/{storeId}/products/{productId}
```

La configuración existente ya permite GET de `/api/public/**`; no se abrió ninguna escritura ni ruta administrativa. El listado público de tiendas no está paginado; para un volumen elevado deberá paginarse junto con el directorio frontend.

## Recuperación de una orden del cliente

| Archivo Java                                          | Cambio y motivo                                                              |
| ----------------------------------------------------- | ---------------------------------------------------------------------------- |
| `core/order/core/repository/OrderRepository.java`     | Consulta con tres condiciones: orden, tienda y cliente.                      |
| `core/order/core/service/OrderService.java`           | Añade `getByCustomer`.                                                       |
| `core/order/core/service/OrderServiceImpl.java`       | Consulta de solo lectura y mapeo con los ítems históricos existentes.        |
| `core/order/core/web/controller/OrderController.java` | Expone GET y toma el cliente del principal autenticado, nunca del navegador. |

```text
GET /api/customer/stores/{storeId}/orders/{orderId}
```

Permite recargar una página de pago sin volver a crear la orden ni consumir nuevamente el carrito. Otra cuenta no puede leer la orden: la prueba devuelve 404 para el cliente incorrecto. Spring conserva el requisito de rol CUSTOMER.

## Documento opcional en registro de cliente (HU-01)

| Archivo                                                                  | Cambio y motivo                                                                                                                             |
| ------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------- |
| `core/user/core/model/enums/UserDocumentType.java`                       | Amplía DNI con RUC, CE y PASSPORT.                                                                                                          |
| `core/user/core/model/entity/User.java`                                  | Dos columnas opcionales: tipo y número de documento.                                                                                        |
| `security/auth/web/request/RegisterCustomerRequest.java`                 | Dos campos opcionales, validación conjunta con `@AssertTrue` y límite de longitud.                                                          |
| `core/user/core/service/UserServiceImpl.java`                            | Persiste documento y el teléfono del cliente, que antes se recibía pero se omitía. Normaliza documento ausente a null.                      |
| `src/main/resources/db/migration/V5__optional_registration_document.sql` | Migración aditiva que restaura estas dos columnas, eliminadas en V2, con constraint de pareja/formato. Usuarios existentes quedan con null. |

El contrato previo sin documento sigue funcionando. No se modificaron V1–V4. DNI exige 8 dígitos, RUC 11 y CE/pasaporte entre 6 y 20 caracteres alfanuméricos. El frontend solo exige ambos campos si el usuario opta por asociarlos. La confirmación de contraseña se valida exclusivamente en el navegador y no se almacena.

## Archivos preexistentes y alcance

`src/main/resources/application.properties` ya tenía modificaciones locales al comenzar la integración. Se conservaron durante el trabajo funcional. En la revisión para publicación se reemplazaron los valores fijos de conexión y firma por variables de entorno, como se describe al inicio. No se copiaron sus secretos a esta documentación.

No se cambiaron `pom.xml`, SecurityConfig, los endpoints originales de categorías/productos/inventario/pagos, la lógica de impuestos, la autorización por propietario, los correos ni el código de Culqi. El simulador nuevo se encuentra en `frontend/tools` y se usa solo si se configura explícitamente en el entorno del backend.

El POM permanece en Java 23. Para verificar en este equipo, que tiene Java 21, se utilizó únicamente el argumento de ejecución `-Djava.version=21`.

## Validación realizada

- Compilación de 209 fuentes Java satisfactoria.
- Prueba existente `ApplicationTests`: 1 prueba pasada con PostgreSQL temporal.
- Flyway aplicó las cinco migraciones sobre una base vacía aislada.
- Prueba de integración de 55 respuestas HTTP, incluyendo acceso público sin autenticación, producto borrador/archivado oculto, documentos inválidos, duplicados, permisos y aislamiento de órdenes.
- Ciclo pago/reembolso probado con un simulador local; no se contactó a Culqi ni se enviaron correos reales.

## Aspectos existentes para una siguiente etapa

El procesamiento actual es síncrono. El repositorio no implementa webhooks, 3DS ni idempotencia del proveedor. No expone estado de entrega de correo, por lo que el frontend no afirma que un correo fue entregado. El listado y la cancelación de órdenes del cliente ya están implementados en la ampliación descrita al inicio de este documento.
