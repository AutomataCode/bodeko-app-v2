# Gestión de cuentas con Super Admin

## Roles

`SUPER_ADMIN` administra la plataforma; `ADMIN` representa al propietario/vendedor; `CUSTOMER` representa a clientes registrados por tienda. Solo se permite cambiar el estado de cuentas ADMIN y CUSTOMER. El panel no permite crear superadmins ni elevar otros roles. El primer acceso de desarrollo se crea desde `scripts/db/01_datos_demo.sql`; sus credenciales demo son `superadmin.demo@bodeko.test` / `BodekoDemo2026!`. No uses esa cuenta en producción.

## Funciones del panel

El dashboard muestra totales de vendedores y clientes, permite filtrar por texto, estado, tienda y fechas, y pagina los resultados. Suspender una cuenta es reversible y requiere un motivo, que queda en `platform_account_status_history`. El sistema conserva órdenes, tiendas y demás referencias. Una cuenta suspendida no puede iniciar sesión y sus tokens dejan de autorizar solicitudes porque el filtro JWT vuelve a consultar el estado.

El estado del propietario solo bloquea su cuenta de administración. No elimina ni suspende automáticamente su tienda pública. La política de baja de tiendas debe gestionarse por separado si se necesita detener ventas.

## API privada

Todas las rutas requieren JWT de `SUPER_ADMIN`:

```http
GET   /api/super-admin/summary
GET   /api/super-admin/stores
GET   /api/super-admin/vendors?search=&active=true&storeId=&from=2026-01-01&to=2026-12-31&page=0&size=20
GET   /api/super-admin/customers?search=&active=false&storeId=&from=&to=&page=0&size=20
PATCH /api/super-admin/vendors/{id}/status
PATCH /api/super-admin/customers/{id}/status
```

Ejemplo para suspender:

```json
{ "active": false, "reason": "Cuenta reportada por el equipo de soporte" }
```

Flyway aplica `V11__superadmin_account_management.sql` al iniciar el backend. El superadmin de demostración se crea al volver a ejecutar la carga demo después de aplicar V11.
