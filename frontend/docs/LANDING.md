# Portada y ranking de ventas

La ruta `/` adapta `bodeko-landing-prototipo.html` al proyecto React. Conserva la composición del prototipo, los colores de Bodeko, los iconos vectoriales y bordes de hasta 8 px. El directorio completo queda en `/tiendas`; los enlaces de sesión, registro y tiendas usan rutas reales.

La portada consulta `GET /api/public/stores/top-selling`. Muestra hasta tres tiendas ordenadas por cantidad histórica de órdenes pagadas, enviadas o entregadas. Una cancelación deja de contar al volver a consultar. Las órdenes pendientes y tiendas sin ventas no entran en el ranking. No se usan nombres ni cantidades de muestra en el código de producción.

El buscador filtra únicamente las tiendas destacadas por nombre o subdominio, con un máximo de 100 caracteres y ejemplo visible como texto de ayuda inicial. No inventa categorías de comercios: el modelo actual no dispone de ellas. Las fotografías proceden del prototipo, son decorativas y se identifican como referenciales. Su carga requiere conexión a images.unsplash.com; si no cargan, se conserva el fondo y todo el contenido de la tienda sigue disponible.

Se incluyen carga, reintento de errores en español, ausencia de ventas y búsqueda sin coincidencias. Los estilos están limitados a la portada y respetan movimiento reducido. Se revisaron escritorio, móvil de 390 px y navegación al directorio; las pruebas de frontend y compilación pasaron.

## Archivos principales

- `src/features/stores/StoresPage.tsx`: vista de portada.
- `src/features/stores/landing.css`: diseño adaptable.
- `src/features/stores/components/FeaturedStoreCard.tsx`: tarjeta de tienda destacada.
- `src/features/stores/hooks/useTopSellingStores.ts`: carga y reintento.
- `src/features/stores/services/storeService.ts`: comunicación con el nuevo endpoint.
- `src/features/stores/types/index.ts`: contrato `TopSellingStore`.
- `src/features/stores/StoresDirectoryPage.tsx`: directorio completo.
- `src/core/layouts/SiteLayout.tsx`: navegación y marco visual de la portada.

Reiniciar el backend después de incorporar los cambios Java. No ejecutar migraciones adicionales.

## Prueba reproducible del ranking

Usar una base PostgreSQL **vacía y desechable**, con las migraciones de la aplicación aplicadas. Iniciar una instancia de Spring que apunte exclusivamente a esa base. La prueba crea cinco tiendas y órdenes sintéticas; no realiza cobros ni llamadas a Culqi. Conserva los datos para inspección visual.

Ejemplo en PowerShell, desde `frontend`, ajustando los puertos a la instancia temporal:

```powershell
$env:QA_DATABASE_URL = 'postgresql://postgres@127.0.0.1:55433/bodeko_landing_qa'
$env:QA_API_URL = 'http://127.0.0.1:8081/api'
$env:QA_PSQL = 'C:/Program Files/PostgreSQL/18/bin/psql.exe'
# Si PostgreSQL exige contraseña, definir PGPASSWORD en la sesión.
node tools/verify-top-selling.mjs --disposable
```

La prueba exige dirección local, nombre de base con `qa` o `test`, y ninguna tienda existente. Comprueba ranking vacío, máximo de tres, estados, desempates, cantidades frente a montos, privacidad y cancelaciones.
