# Bodeko — Frontend del Entregable 2

Actualización 11/11/2026: antes de desplegar revisar [validaciones del backend](../docs/VALIDACIONES_BACKEND.md). Las ofertas nuevas reciben porcentajes y checkout/pagos/cancelaciones requieren Idempotency-Key; los clientes incluidos ya están adaptados. Flyway aplica ahora V1-V8.


SPA en React, TypeScript estricto y Vite, integrada con el backend Spring de este repositorio. La marca del proyecto es **Bodeko**; el otro nombre del Excel pertenece a su plantilla. La interfaz toma colores, espaciado y superficies de Polaris; no depende del paquete Polaris.

La portada muestra las tres tiendas con más órdenes vendidas mediante `GET /api/public/stores/top-selling`. El directorio completo está en `/tiendas`. Ver diseño, criterio de ventas y pruebas en [docs/LANDING.md](docs/LANDING.md).

## Inicio rápido

Requisitos: Node.js 22 (o una versión compatible con Vite 6), npm, backend y PostgreSQL. Python 3 solo hace falta para el simulador opcional.

Desde esta carpeta:

```powershell
npm ci
Copy-Item .env.example .env.local
npm run dev
```

Abre `http://localhost:3000`. El proxy envía `/api` al backend en `http://localhost:8080`. Si necesitas otra dirección, cambia `API_PROXY_TARGET` en `.env.local` y reinicia Vite. No hay tiendas, productos ni usuarios precargados en el código de la aplicación.

## Preparar el backend

Configura una base de desarrollo y ejecuta Spring desde la raíz del repositorio. Flyway aplica V1-V8 automáticamente. Las variables estándar de Spring permiten configurar la conexión sin editar `application.properties`:

```powershell
$env:SPRING_DATASOURCE_URL='jdbc:postgresql://localhost:5432/tu_base_de_desarrollo'
$env:SPRING_DATASOURCE_USERNAME='tu_usuario'
$env:SPRING_DATASOURCE_PASSWORD='<tu contraseña local>'
$env:JWT_SECRET='<clave aleatoria propia de al menos 32 bytes>'
$env:FRONTEND_URL='http://localhost:3000'
mvn spring-boot:run
```

El POM declara Java 23. Si utilizas Java 21, esta implementación se compiló y probó con `mvn '-Djava.version=21' test` y puede iniciarse con `mvn '-Djava.version=21' spring-boot:run`. No se modificó el POM.

La configuración carga opcionalmente `.env.dev` desde la raíz como archivo de propiedades. `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET` están enlazadas a `application.properties`; este archivo ya no contiene la contraseña ni la clave de firma. Las variables estándar `SPRING_DATASOURCE_*` anteriores también permiten configurar la conexión. Para Docker, completa además `POSTGRES_DB`, `POSTGRES_USER` y `POSTGRES_PASSWORD` en el entorno local.

Antes de publicar, consulta [SEGURIDAD_REPOSITORIO.md](../SEGURIDAD_REPOSITORIO.md). El historial original contiene credenciales antiguas: un commit que las elimina no las borra de commits anteriores.

## Pago simulado, sin claves externas

En una segunda terminal, desde `frontend`:

```powershell
python ../scripts/culqi-mock/server.py
```

El backend carga `.env.dev` desde la raíz del repositorio. Ese archivo se configuró con los valores del simulador del equipo:

```dotenv
CULQI_API_URL=http://127.0.0.1:8199/v2
CULQI_SECRET_KEY=sk_test_mock
```

Mantén `VITE_PAYMENT_MODE=token` en `.env.local`. En la pantalla de pago ingresa:

| Token               | Resultado simulado              |
| ------------------- | ------------------------------- |
| `tkn_test_ok`       | Pago aprobado                   |
| `tkn_test_declined` | Pago rechazado, orden pendiente |

Reinicia el backend después de cambiar `.env.dev`. El valor `sk_test_mock` es ficticio, no una clave real de Culqi. El servidor Python escucha solo en loopback, no recibe tarjetas y no mueve dinero. Los cargos viven en memoria: para probar devoluciones, crea y paga una orden nueva y cancélala sin reiniciar el simulador. Los cargos del seed o del simulador anterior no podrán reembolsarse aquí. Si hay variables de entorno `CULQI_*` en IntelliJ o la terminal, deben coincidir o eliminarse, porque prevalecen sobre el archivo.

El simulador anterior permanece en `tools/culqi-simulator.py` como alternativa. Sus valores son puerto 8090, clave `simulator-local-only` y token `tkn_test_approved`; no son los configurados actualmente. Más detalles en [el README del simulador del equipo](../scripts/culqi-mock/README.md).

## Probar con Culqi sandbox

En `.env.local`:

```dotenv
VITE_PAYMENT_MODE=culqi
VITE_CULQI_PUBLIC_KEY=pk_test_REEMPLAZAR
```

En el backend configura `CULQI_API_URL=https://api.culqi.com/v2` y `CULQI_SECRET_KEY` con la `sk_test_…` del mismo comercio. Reinicia ambos procesos. Checkout Custom se carga desde el dominio oficial y devuelve un token; el servidor realiza el cargo. El frontend admite únicamente claves públicas `pk_test_…` en este modo. No pongas claves `sk_…` en variables `VITE_*`, porque se incluyen en el JavaScript público.

Este adaptador usa pagos con tarjeta de cargo único. No incluye flujos 3DS, pagos asíncronos ni webhooks, y no se certificó contra una cuenta real de Culqi. Para el Entregable 2 el PDF permite el pago simulado. Antes de habilitar pagos reales, el equipo debe completar y probar esos casos según su cuenta y los requisitos vigentes de Culqi.

Referencias: [Checkout Custom oficial](https://docs.culqi.com/es/documentacion/checkout/checkout-custom), [llaves](https://docs.culqi.com/es/documentacion/pagos-online/llaves), [tarjetas de pruebas](https://docs.culqi.com/es/documentacion/pagos-online/tarjetas-de-prueba).

## Recorrido de demostración

Para iniciar con cuentas, dos tiendas y un catálogo ya preparado, ejecuta los [scripts de datos de prueba](../scripts/db/README.md). La carga es manual, posterior a Flyway, y no forma parte del bundle del frontend.

1. Registra una cuenta de **propietario** con teléfono en formato internacional.
2. Crea una tienda con RUC de 11 dígitos, razón social y subdominio único.
3. Crea una categoría y un producto; guarda inicialmente como borrador.
4. En Inventario, asigna cantidad y umbral; publica el producto como Activo.
5. Opcionalmente crea una oferta con fecha final posterior al inicio.
6. Cierra sesión y registra una cuenta de **cliente**, con confirmación de contraseña.
7. Abre la tienda, busca/filtra, visita el detalle y agrega productos al carrito.
8. Cambia cantidades, cierra e inicia sesión para comprobar la persistencia del carrito en la base.
9. Confirma boleta con DNI o factura con RUC y razón social. Revisa los importes finales antes de pagar.
10. Prueba el rechazo y luego la aprobación con los tokens del simulador. Recarga la URL de pago para comprobar que la orden se recupera del servidor.
11. Como propietario, revisa el detalle, envía/entrega una orden pagada o cancélala para probar la devolución y reposición de stock.

## Mi cuenta y mis órdenes

Con una sesión **CUSTOMER**, el menú superior muestra **Mis órdenes**, en `/cuenta/ordenes`. La vista utiliza un menú lateral inspirado en la referencia del usuario y la paleta verde de Bodeko. Reúne compras de todas las tiendas, permite filtrar por estado, consultar productos/comprobante y retomar pagos pendientes.

El cliente puede cancelar órdenes pendientes y pagadas antes del envío, con confirmación. La cancelación restaura stock y solicita el reembolso de un pago aprobado; si la devolución falla, la compra conserva su estado. Las órdenes enviadas, entregadas o ya canceladas son solo de consulta. Cada operación está restringida a la cuenta autenticada también en Spring.

`/cuenta/datos` muestra los datos reales de la sesión en modo de lectura. No se añadieron cambios de contraseña, direcciones, favoritos ni edición de perfil, porque no forman parte de esta ampliación de órdenes ni tienen API disponible.

No necesitas volver a cargar el seed ni ejecutar SQL nuevo. Reinicia el backend para registrar los tres endpoints nuevos; el servidor Vite recarga los archivos de frontend. Para devolver un pago con el simulador del equipo, crea y paga una orden nueva y mantén esa ejecución del simulador activa.

Detalles de implementación y pruebas en [MIS_ORDENES.md](docs/MIS_ORDENES.md).

## Estructura y mantenimiento

```text
src/
├── assets/       # CSS global y recursos generales
├── config/       # Axios, nombre de aplicación y contratos de prueba
├── context/      # Sesión, tienda y carrito compartido
├── core/         # UI, layouts, hooks y tipos reutilizables
├── features/     # Negocio: cada módulo tiene components/hooks/services/types/index.ts
│   ├── auth/          # HU-01 y HU-02
│   ├── stores/        # HU-03
│   ├── categories/    # HU-04
│   ├── products/      # HU-05
│   ├── inventory/     # HU-06
│   ├── offers/        # HU-07
│   ├── cart/          # HU-08
│   ├── checkout/      # HU-09
│   ├── payments/      # HU-10
│   ├── orders/        # HU-11
│   ├── customer-orders/ # Cuenta del cliente: historial, detalle y cancelación
│   ├── catalog/       # HU-12
│   ├── notifications/ # Confirmación visual HU-13; correo lo envía Spring
│   └── dashboard/     # Resumen administrativo adicional
├── utils/        # Validaciones puras, formato y mensajes de error
├── App.tsx       # Árbol de rutas
└── main.tsx      # Entrada de Vite
```

Las features publican solo lo necesario en `index.ts`; los componentes comunes se importan desde `@/core/components` y la red desde `@/config`. Para cambiar una regla de entrada, revisa `utils/validation.ts`; para traducir nuevos códigos del backend, `utils/errors.ts`; para cambiar el aspecto, `assets/styles.css`. El nombre visual se centraliza en `APP_NAME`, y el título HTML en `index.html`.

La sesión se guarda en `sessionStorage`, se verifica con `/auth/me` al recargar y se elimina al cerrar sesión/recibir 401. El carrito persiste en PostgreSQL por cuenta y tienda. No se guardan contraseñas, tarjetas, CVV ni tokens de pago. Los guards del frontend son navegación; Spring sigue autorizando cada petición.

## Verificación

```powershell
npm test
npm run build
```

Prueba de integración reproducible, únicamente con base desechable y el simulador local activo:

```powershell
node tools/verify-api.mjs --disposable
```

La prueba crea datos sintéticos y no los elimina. Verifica 55 respuestas HTTP y aserciones de negocio contra Spring/PostgreSQL, incluyendo aislamiento de órdenes por cliente. No se debe ejecutar en una base con datos reales. `npm test` verifica validaciones, traducciones y contratos de servicios; sus dobles de prueba no se usan en la aplicación.

Para la ampliación de cliente, aplica el seed en una base desechable y usa el simulador del equipo sin reiniciarlo durante la prueba:

```powershell
$env:QA_API_URL='http://localhost:8081/api'
node tools/verify-customer-orders.mjs --disposable
```

Esta segunda prueba realiza 56 comprobaciones HTTP: historial multitienda, paginación/filtros, privacidad de cuenta, cancelaciones pendientes/pagadas, concurrencia y rollback por rechazo de reembolso. El puerto 8081 es el backend de QA; ajusta la URL a tu entorno desechable. Nunca apuntes esta prueba a una base de trabajo.

## Despliegue posterior

Se incluyen `vercel.json` y `netlify.toml` para el fallback de rutas SPA. Selecciona `frontend` como raíz del proyecto, `npm run build` y salida `dist`. Define `VITE_API_URL=https://tu-backend.example.com` sin `/api` al final; en Spring configura `FRONTEND_URL` con el origen HTTPS exacto del frontend. Las variables Vite se incorporan al compilar: requiere nueva compilación si cambian. El proxy local no existe en producción.

El acceso por `/tienda/:subdomain` funciona sin DNS especial. Si más adelante se configura DNS wildcard, HTTPS y hosting, `VITE_STORE_BASE_DOMAIN` habilita redirección desde `tienda.midominio.com` al mismo árbol de rutas. No se publicó el sitio ni se configuraron servicios cloud.

## Documentación entregada

- [Matriz del Entregable 2, arquitectura y evidencias](docs/ENTREGABLE_2.md).
- [Cambios de backend, archivo por archivo](docs/CAMBIOS_BACKEND.md).
- [Área de cliente y reglas de cancelación](docs/MIS_ORDENES.md).
- [Árbol completo seguido del código completo por archivo](docs/CODIGO_COMPLETO.md).

No falta una especificación para ejecutar la demostración local. El simulador del equipo ya está incorporado en `scripts/culqi-mock`. Para conectarse a otro entorno se requiere su URL de backend y configuración de desarrollo. Culqi sandbox requiere el par de claves correspondiente. Correo real y publicación quedan sujetos a credenciales y servicios del Entregable 3.
