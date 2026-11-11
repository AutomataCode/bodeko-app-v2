# Datos de prueba de Bodeko

Estos scripts cargan **datos ficticios en una base de desarrollo** para probar el frontend y el backend reales. No se ejecutan automáticamente al iniciar la aplicación y no pertenecen a las migraciones Flyway.

## Orden de ejecución

1. Crea/selecciona tu base de desarrollo en PostgreSQL.
2. Configura el backend para conectarse a esa base y arráncalo una vez. Flyway debe aplicar **V1-V8**. No ejecutes manualmente los archivos de migración si ya usas Flyway.
3. Ejecuta `00_compatibilidad_historial.sql` para alinear el historial antiguo con los nombres que espera este backend.
4. Ejecuta `01_datos_demo.sql` completo.
5. Ejecuta `02_consultas_verificacion.sql` para inspeccionar los registros.
6. Inicia el frontend y entra con las cuentas de la tabla siguiente.

El ejecutor PowerShell realiza estos tres archivos en orden. El ajuste `00` renombra `previous_status` → `from_status`, `new_status` → `to_status` y `changed_at` → `created_at`, únicamente si encuentra la variante antigua. Añade `changed_by` con clave foránea a usuarios. Conserva las filas, estados y fechas; los registros antiguos quedan con administrador desconocido (`NULL`), sin inventar un autor. En una base creada con el esquema actual conserva las columnas y sus restricciones existentes. No modifica archivos ni checksums de Flyway.

Cada archivo tiene su propio ámbito: el ajuste del esquema se confirma antes de la carga de datos. Un error dentro de `01` revierte todos sus INSERT, aunque `psql` haya mostrado conteos de filas antes del error. El modo `-SoloVerificar` no aplica ajustes: es de solo lectura y requiere haber ejecutado antes la compatibilidad si tu base es antigua.

La carga utiliza una transacción y UUID fijos con prefijo `b0de`. Repetirla no duplica las filas ni sobrescribe tus cambios de precios, stock, contraseñas o estados. No reinicia una demostración ya utilizada: para repetir desde cero, usa otra base de desarrollo y deja que Flyway cree el esquema. Si un correo, RUC o subdominio demo ya pertenece a otro registro, la carga falla y se revierte; no reemplaza ese registro.

## Opción A: pgAdmin (sin terminal)

En pgAdmin selecciona la **misma base que usa el backend**, abre **Query Tool** y ejecuta completos `00_compatibilidad_historial.sql`, `01_datos_demo.sql` y `02_consultas_verificacion.sql`, en ese orden. Los dos primeros deben finalizar con `COMMIT`. Si hay un error y la sesión queda en transacción fallida, ejecuta `ROLLBACK;` antes de corregir y reintentar.

## Opción B: PowerShell

Desde la raíz `bodeko-core-api-v2`, reemplaza el nombre de base/usuario si son distintos:

El backend debe usar la misma base que indiques en `-Database`. El nombre efectivo se obtiene de DB_URL o de las variables estándar de Spring. Cargar el demo en `db_bodekos` no lo hace visible en el backend conectado a `db_bodekos_v2`: son bases distintas. El ejecutor avisa si el nombre no coincide con el declarado en `application.properties`; si arrancas Spring con variables de entorno, comprueba su valor efectivo.

```powershell
.\scripts\db\ejecutar-demo.ps1 -Database db_bodekos_v2 -DbUser postgres
```

Si PostgreSQL usa otro puerto o `psql.exe` no se detecta:

```powershell
.\scripts\db\ejecutar-demo.ps1 `
  -Database db_bodekos_v2 `
  -DbUser postgres `
  -DbHost localhost `
  -Port 5432 `
  -PsqlPath 'C:\Program Files\PostgreSQL\18\bin\psql.exe'
```

`psql` solicita tu contraseña de PostgreSQL cuando corresponde. **No es la contraseña de las cuentas de la aplicación.** El script no la almacena. Si tu política de PowerShell impide scripts, usa pgAdmin o los comandos directos siguientes; no es necesario modificar la política del equipo.

```powershell
& 'C:\Program Files\PostgreSQL\18\bin\psql.exe' -X -h localhost -p 5432 -U postgres -d db_bodekos_v2 -v ON_ERROR_STOP=1 -f '.\scripts\db\00_compatibilidad_historial.sql'
& 'C:\Program Files\PostgreSQL\18\bin\psql.exe' -X -h localhost -p 5432 -U postgres -d db_bodekos_v2 -v ON_ERROR_STOP=1 -f '.\scripts\db\01_datos_demo.sql'
& 'C:\Program Files\PostgreSQL\18\bin\psql.exe' -X -h localhost -p 5432 -U postgres -d db_bodekos_v2 -v ON_ERROR_STOP=1 -f '.\scripts\db\02_consultas_verificacion.sql'
```

Para consultar sin volver a cargar datos:

```powershell
.\scripts\db\ejecutar-demo.ps1 -Database db_bodekos_v2 -DbUser postgres -SoloVerificar
```

## Cuentas listas para iniciar sesión

**Contraseña de las cuatro cuentas: `BodekoDemo2026!`**. Son credenciales públicas y ficticias de desarrollo. El SQL guarda un hash BCrypt compatible con Spring; no requiere `pgcrypto` ni otras extensiones.

| Correo                        | Rol      | Escenario                                                            |
| ----------------------------- | -------- | -------------------------------------------------------------------- |
| `admin.demo@bodeko.test`      | ADMIN    | Administra Bodeko Market Demo.                                       |
| `tecnologia.demo@bodeko.test` | ADMIN    | Administra Bodeko Tecnología Demo; no debe poder administrar Market. |
| `cliente.demo@bodeko.test`    | CUSTOMER | Tiene un carrito preparado en Market y órdenes históricas.           |
| `cliente2.demo@bodeko.test`   | CUSTOMER | Cliente sin órdenes; permite probar aislamiento de cuentas.          |

Todos los nombres, teléfonos, documentos y datos fiscales del script son ejemplos sintéticos. El dominio `.test` no sirve para recibir correos reales. No cambies el proveedor de correo a envío real para estas cuentas.

## Qué datos se cargan

- 4 cuentas, 2 tiendas con distintos propietarios, 6 categorías y 12 productos.
- Market: 6 productos activos, 1 borrador y 1 archivado. Tecnología: 3 activos y 1 borrador.
- 12 inventarios con stock normal, bajo y agotado.
- 4 ofertas: café y audífonos vigentes, detergente vencida y aceite futura. Las vigencias son relativas a la **primera carga**; reejecutar conserva las fechas originales.
- 1 carrito abierto de `cliente.demo@bodeko.test` con 1 café y 2 bolsas de arroz.
- 5 órdenes de Market, una por estado: pendiente, pagada, enviada, entregada y cancelada, con ítems, pagos ficticios e historial administrativo.

Las órdenes históricas se insertan como snapshots ya contabilizados en el stock de muestra. No disparan cobros, correos ni descuentos adicionales. El identificador `chr_test_seed_*` de sus pagos solo sirve con el simulador local, no con Culqi. Para probar Culqi sandbox, crea una orden nueva desde la aplicación.

## Recorrido de prueba

Inicia el frontend con `npm run dev` desde `frontend` y abre:

- [Market](http://localhost:3000/tienda/bodeko-market-demo).
- [Tecnología](http://localhost:3000/tienda/bodeko-tech-demo).

| Funcionalidad            | Qué hacer y qué esperar                                                                                                                                                                                      |
| ------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Sesión y roles           | Ingresa como administrador y como cliente. El panel administrativo corresponde a ADMIN; carrito/checkout a CUSTOMER.                                                                                         |
| Aislamiento de tiendas   | El administrador Market solo ve su tienda en “Mi negocio”; el de Tecnología solo la suya.                                                                                                                    |
| Catálogo                 | Busca “café”, filtra por categoría y activa “Solo en stock”. Leche y mouse agotados desaparecen con ese filtro. Borradores y archivados no aparecen en el catálogo público.                                  |
| Categorías               | Crea/edita una categoría. Puedes eliminar “Categoría vacía de prueba”. No puedes eliminar Abarrotes sin reasignar sus productos.                                                                             |
| Productos                | Edita un producto. Publica “Chocolate en borrador” después de asignarle stock; comprueba que aparece en el catálogo. Archívalo y verifica que se oculta.                                                     |
| Inventario               | Arroz tiene 4 unidades y umbral 5: alerta de stock bajo. Cambia cantidad y umbral y revisa la fecha de actualización.                                                                                        |
| Ofertas                  | Café usa S/19.90, no S/25.90. Detergente usa su precio normal porque su oferta venció. La oferta de aceite todavía no aplica.                                                                                |
| Persistencia del carrito | Entra como cliente demo: hay café y arroz. Cambia cantidades, cierra e inicia sesión y comprueba que se conservan.                                                                                           |
| Boleta e IGV             | Confirma el carrito inicial con DNI `12345678`. Si no has cambiado datos: subtotal S/29.90, IGV S/3.58 y total S/33.48. El arroz está exonerado; el café está afecto.                                        |
| Factura                  | Con otro carrito, selecciona Factura y completa RUC `20999999003` y razón social ficticia. No debe permitir confirmar sin esos datos.                                                                        |
| Órdenes                  | Como administrador filtra los cinco estados y abre sus detalles. “BOD-DEMO-PAGADA” puede enviarse o cancelarse; “BOD-DEMO-ENVIADA” puede entregarse.                                                         |
| Orden pendiente          | El cliente demo puede abrir directamente la [orden pendiente precargada](http://localhost:3000/tienda/bodeko-market-demo/pago/b0de7000-0000-4000-8000-000000000001). Otro cliente no debe poder consultarla. |
| Pago y devolución        | Usa el simulador explicado abajo. Rechazo conserva pendiente; aprobación marca pagada. Cancelar una pagada pide devolución y restaura el stock.                                                              |

Las cantidades, totales y estados iniciales cambian cuando pruebas la aplicación. `02_consultas_verificacion.sql` permite comprobar los efectos de las operaciones en la BD.

## Pago local sin claves de Culqi

Desde `frontend`, en una terminal:

```powershell
python ../scripts/culqi-mock/server.py
```

En `.env.dev`, en la raíz del backend, utiliza la configuración del equipo:

```dotenv
CULQI_API_URL=http://127.0.0.1:8199/v2
CULQI_SECRET_KEY=sk_test_mock
```

Reinicia el backend con `mvn spring-boot:run` desde la raíz del repositorio, o desde IntelliJ. Con Java 21 usa `mvn '-Djava.version=21' spring-boot:run`, como explica el README del frontend. El POM del proyecto conserva Java 23. En `frontend/.env.local` utiliza `VITE_PAYMENT_MODE=token` y reinicia Vite si cambias su configuración. El modo token es el predeterminado si no existe ese archivo.

Tokens para la pantalla de pago:

- `tkn_test_declined`: rechaza el pago sin mover dinero.
- `tkn_test_ok`: aprueba el pago de prueba.

Este simulador guarda cargos en memoria: prueba las devoluciones con órdenes nuevas pagadas en esa misma ejecución. Las órdenes pagadas precargadas por este SQL no pueden reembolsarse con este simulador porque sus cargos ficticios no existen en su memoria.

Para probar el registro, crea un correo distinto a los cuatro precargados. El SQL permite entrar con cuentas ya preparadas, pero no sustituye esa prueba del formulario.
