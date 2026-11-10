# Estructura y código completo — Bodeko

Código fuente del frontend, archivo por archivo, y scripts de desarrollo. Incluye la nueva portada y el ranking de tiendas. La documentación operativa está en frontend/README.md y frontend/docs. Se omiten dependencias, dist, lockfile, capturas y datos de QA.

## Estructura de archivos

```text
frontend/
├── src/
│   ├── assets/
│   │   ├── README.md
│   │   └── styles.css
│   ├── config/
│   │   ├── contracts.test.ts
│   │   └── index.ts
│   ├── context/
│   │   ├── CartContext.tsx
│   │   ├── SessionContext.tsx
│   │   └── StoreContext.tsx
│   ├── core/
│   │   ├── components/
│   │   │   ├── ErrorBoundary.tsx
│   │   │   └── index.tsx
│   │   ├── hooks/
│   │   │   ├── useAction.ts
│   │   │   └── useResource.ts
│   │   ├── layouts/
│   │   │   ├── AdminLayout.tsx
│   │   │   ├── ProtectedRoute.tsx
│   │   │   ├── ScrollToTop.tsx
│   │   │   ├── SiteLayout.tsx
│   │   │   └── StoreLayout.tsx
│   │   └── types/
│   │       └── index.ts
│   ├── features/
│   │   ├── auth/
│   │   │   ├── components/
│   │   │   │   └── AuthForm.tsx
│   │   │   ├── hooks/
│   │   │   │   └── useAuth.ts
│   │   │   ├── services/
│   │   │   │   └── authService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── AuthPage.tsx
│   │   │   └── index.ts
│   │   ├── cart/
│   │   │   ├── components/
│   │   │   │   └── CartLine.tsx
│   │   │   ├── hooks/
│   │   │   │   └── useCart.ts
│   │   │   ├── services/
│   │   │   │   └── cartService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── CartPage.tsx
│   │   │   └── index.ts
│   │   ├── catalog/
│   │   │   ├── components/
│   │   │   │   └── ProductCard.tsx
│   │   │   ├── hooks/
│   │   │   │   └── useCatalog.ts
│   │   │   ├── services/
│   │   │   │   └── catalogService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── CatalogPage.tsx
│   │   │   ├── index.ts
│   │   │   └── ProductDetailPage.tsx
│   │   ├── categories/
│   │   │   ├── components/
│   │   │   │   └── CategoryForm.tsx
│   │   │   ├── hooks/
│   │   │   │   └── useCategories.ts
│   │   │   ├── services/
│   │   │   │   └── categoryService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── CategoriesPage.tsx
│   │   │   └── index.ts
│   │   ├── checkout/
│   │   │   ├── components/
│   │   │   │   └── CheckoutForm.tsx
│   │   │   ├── hooks/
│   │   │   │   └── useCheckout.ts
│   │   │   ├── services/
│   │   │   │   └── checkoutService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── CheckoutPage.tsx
│   │   │   └── index.ts
│   │   ├── customer-orders/
│   │   │   ├── components/
│   │   │   │   ├── AccountLayout.tsx
│   │   │   │   ├── CancelOrderDialog.tsx
│   │   │   │   └── CustomerOrderCard.tsx
│   │   │   ├── hooks/
│   │   │   │   ├── useCustomerOrder.ts
│   │   │   │   └── useCustomerOrders.ts
│   │   │   ├── services/
│   │   │   │   ├── customerOrderService.test.ts
│   │   │   │   └── customerOrderService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── AccountProfilePage.tsx
│   │   │   ├── CustomerOrderDetailPage.tsx
│   │   │   ├── CustomerOrdersPage.tsx
│   │   │   └── index.ts
│   │   ├── dashboard/
│   │   │   ├── components/
│   │   │   │   └── MetricCard.tsx
│   │   │   ├── hooks/
│   │   │   │   └── useDashboard.ts
│   │   │   ├── services/
│   │   │   │   └── dashboardService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── DashboardPage.tsx
│   │   │   └── index.ts
│   │   ├── inventory/
│   │   │   ├── components/
│   │   │   │   └── InventoryEditor.tsx
│   │   │   ├── hooks/
│   │   │   │   └── useInventory.ts
│   │   │   ├── services/
│   │   │   │   └── inventoryService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── index.ts
│   │   │   └── InventoryPage.tsx
│   │   ├── notifications/
│   │   │   ├── components/
│   │   │   │   └── PurchaseConfirmation.tsx
│   │   │   ├── hooks/
│   │   │   │   └── usePurchaseConfirmation.ts
│   │   │   ├── services/
│   │   │   │   └── notificationService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   └── index.ts
│   │   ├── offers/
│   │   │   ├── components/
│   │   │   │   └── OfferForm.tsx
│   │   │   ├── hooks/
│   │   │   │   └── useOffers.ts
│   │   │   ├── services/
│   │   │   │   └── offerService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── index.ts
│   │   │   └── OffersPage.tsx
│   │   ├── orders/
│   │   │   ├── components/
│   │   │   │   └── OrderDetails.tsx
│   │   │   ├── hooks/
│   │   │   │   └── useOrders.ts
│   │   │   ├── services/
│   │   │   │   └── orderService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── index.ts
│   │   │   ├── OrderDetailPage.tsx
│   │   │   └── OrdersPage.tsx
│   │   ├── payments/
│   │   │   ├── components/
│   │   │   │   └── PaymentForm.tsx
│   │   │   ├── hooks/
│   │   │   │   └── usePayment.ts
│   │   │   ├── services/
│   │   │   │   ├── culqiService.ts
│   │   │   │   └── paymentService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── index.ts
│   │   │   └── PaymentPage.tsx
│   │   ├── products/
│   │   │   ├── components/
│   │   │   │   └── ProductForm.tsx
│   │   │   ├── hooks/
│   │   │   │   └── useProducts.ts
│   │   │   ├── services/
│   │   │   │   └── productService.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   ├── index.ts
│   │   │   └── ProductsPage.tsx
│   │   └── stores/
│   │       ├── components/
│   │       │   ├── FeaturedStoreCard.tsx
│   │       │   └── StoreForm.tsx
│   │       ├── hooks/
│   │       │   ├── useStores.ts
│   │       │   └── useTopSellingStores.ts
│   │       ├── services/
│   │       │   └── storeService.ts
│   │       ├── types/
│   │       │   └── index.ts
│   │       ├── index.ts
│   │       ├── landing.css
│   │       ├── ManageStoresPage.tsx
│   │       ├── StoresDirectoryPage.tsx
│   │       ├── StoreSettingsPage.tsx
│   │       └── StoresPage.tsx
│   ├── utils/
│   │   ├── errors.test.ts
│   │   ├── errors.ts
│   │   ├── format.ts
│   │   ├── validation.test.ts
│   │   └── validation.ts
│   ├── App.tsx
│   └── main.tsx
├── tools/
│   ├── culqi-simulator.py
│   ├── verify-api.mjs
│   ├── verify-customer-orders.mjs
│   └── verify-top-selling.mjs
├── .env.example
├── .gitignore
├── .prettierignore
├── .prettierrc.json
├── index.html
├── netlify.toml
├── package.json
├── tsconfig.json
├── vercel.json
└── vite.config.ts

scripts/
├── culqi-mock/
│   ├── README.md
│   └── server.py
└── db/
    ├── 00_compatibilidad_historial.sql
    ├── 01_datos_demo.sql
    ├── 02_consultas_verificacion.sql
    ├── ejecutar-demo.ps1
    └── README.md
```

## Código por archivo

### frontend/.env.example

````text
# URL del backend usada por el proxy de desarrollo (sin /api).
API_PROXY_TARGET=http://localhost:8080
# Vacío usa /api mediante proxy. En producción: https://tu-backend.example.com
VITE_API_URL=
# token: recibe un token del simulador del equipo; culqi: abre Checkout oficial.
VITE_PAYMENT_MODE=token
VITE_CULQI_PUBLIC_KEY=
# Dominio base opcional para reconocer tienda.midominio.com; vacío usa /tienda/:subdomain.
VITE_STORE_BASE_DOMAIN=
````

### frontend/.gitignore

````text
node_modules/
dist/
.env.local
.env.*.local
*.tsbuildinfo
.qa/
````

### frontend/.prettierignore

````text
node_modules
dist
.qa
package-lock.json
docs/CODIGO_COMPLETO.md
````

### frontend/.prettierrc.json

````json
{ "singleQuote": true, "trailingComma": "all", "printWidth": 100 }
````

### frontend/index.html

````html
<!doctype html>
<html lang="es">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="theme-color" content="#008060" />
    <meta name="description" content="Bodeko: tiendas, productos y compras en un solo lugar." />
    <title>Bodeko | Tu comercio, conectado</title>
  </head>
  <body>
    <div id="root"></div>
    <script type="module" src="/src/main.tsx"></script>
  </body>
</html>
````

### frontend/netlify.toml

````toml
[build]
  command = "npm run build"
  publish = "dist"

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
````

### frontend/package.json

````json
{
  "name": "bodeko-frontend",
  "private": true,
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "tsc -b && vite build",
    "preview": "vite preview",
    "test": "vitest run"
  },
  "dependencies": {
    "axios": "^1.13.2",
    "lucide-react": "^0.468.0",
    "react": "^19.0.0",
    "react-dom": "^19.0.0",
    "react-router-dom": "^7.1.1",
    "sonner": "^2.0.7"
  },
  "devDependencies": {
    "@types/node": "^22.10.0",
    "@types/react": "^19.0.0",
    "@types/react-dom": "^19.0.0",
    "@vitejs/plugin-react": "^4.3.4",
    "prettier": "^3.9.10",
    "typescript": "~5.7.2",
    "vite": "^6.1.0",
    "vitest": "^3.0.0"
  }
}
````

### frontend/src/App.tsx

````tsx
import { BrowserRouter, Link, Navigate, Route, Routes } from 'react-router-dom';
import { Toaster } from 'sonner';
import { SessionProvider } from '@/context/SessionContext';
import { SiteLayout } from '@/core/layouts/SiteLayout';
import { StoreLayout } from '@/core/layouts/StoreLayout';
import { AdminLayout } from '@/core/layouts/AdminLayout';
import { ProtectedRoute } from '@/core/layouts/ProtectedRoute';
import { ScrollToTop } from '@/core/layouts/ScrollToTop';
import { AuthPage } from '@/features/auth';
import {
  StoresPage,
  StoresDirectoryPage,
  ManageStoresPage,
  StoreSettingsPage,
} from '@/features/stores';
import { CatalogPage, ProductDetailPage } from '@/features/catalog';
import { ProductsPage } from '@/features/products';
import { CategoriesPage } from '@/features/categories';
import { InventoryPage } from '@/features/inventory';
import { OffersPage } from '@/features/offers';
import { CartPage } from '@/features/cart';
import { CheckoutPage } from '@/features/checkout';
import { PaymentPage } from '@/features/payments';
import { OrdersPage, OrderDetailPage } from '@/features/orders';
import { DashboardPage } from '@/features/dashboard';
import {
  AccountLayout,
  AccountProfilePage,
  CustomerOrdersPage,
  CustomerOrderDetailPage,
} from '@/features/customer-orders';

/** Abre la tienda correspondiente al subdominio de la URL. */
function Home() {
  const domain: string = import.meta.env.VITE_STORE_BASE_DOMAIN || '';
  const host = window.location.hostname;
  if (domain && host.endsWith(`.${domain}`)) {
    const subdomain = host.slice(0, -(domain.length + 1));
    if (subdomain && !subdomain.includes('.'))
      return <Navigate to={`/tienda/${encodeURIComponent(subdomain)}`} replace />;
  }
  return <StoresPage />;
}
/** Define las páginas públicas y las páginas protegidas por rol. */
export default function App() {
  return (
    <BrowserRouter>
      <ScrollToTop />
      <SessionProvider>
        <Toaster
          richColors
          position="top-right"
          closeButton
          containerAriaLabel="Notificaciones"
          toastOptions={{ duration: 5000, closeButtonAriaLabel: 'Cerrar notificación' }}
        />
        <Routes>
          <Route element={<SiteLayout />}>
            <Route index element={<Home />} />
            <Route path="tiendas" element={<StoresDirectoryPage />} />
            <Route path="ingresar" element={<AuthPage />} />
            <Route path="registro" element={<AuthPage register />} />
            <Route element={<ProtectedRoute role="CUSTOMER" />}>
              <Route path="cuenta" element={<AccountLayout />}>
                <Route index element={<Navigate to="ordenes" replace />} />
                <Route path="datos" element={<AccountProfilePage />} />
                <Route path="ordenes" element={<CustomerOrdersPage />} />
                <Route path="ordenes/:orderId" element={<CustomerOrderDetailPage />} />
              </Route>
            </Route>
            <Route path="tienda/:subdomain" element={<StoreLayout />}>
              <Route index element={<CatalogPage />} />
              <Route path="producto/:productId" element={<ProductDetailPage />} />
              <Route element={<ProtectedRoute role="CUSTOMER" />}>
                <Route path="carrito" element={<CartPage />} />
                <Route path="checkout" element={<CheckoutPage />} />
                <Route path="pago/:orderId" element={<PaymentPage />} />
              </Route>
            </Route>
            <Route element={<ProtectedRoute role="ADMIN" />}>
              <Route path="admin" element={<ManageStoresPage />} />
              <Route path="admin/:storeId" element={<AdminLayout />}>
                <Route index element={<DashboardPage />} />
                <Route path="productos" element={<ProductsPage />} />
                <Route path="categorias" element={<CategoriesPage />} />
                <Route path="inventario" element={<InventoryPage />} />
                <Route path="ofertas" element={<OffersPage />} />
                <Route path="ordenes" element={<OrdersPage />} />
                <Route path="ordenes/:orderId" element={<OrderDetailPage />} />
                <Route path="configuracion" element={<StoreSettingsPage />} />
              </Route>
            </Route>
            <Route
              path="*"
              element={
                <section className="empty">
                  <h1>Página no encontrada</h1>
                  <Link className="button primary" to="/">
                    Volver al inicio
                  </Link>
                </section>
              }
            />
          </Route>
        </Routes>
      </SessionProvider>
    </BrowserRouter>
  );
}
````

### frontend/src/assets/README.md

````markdown
Recursos multimedia generales. Las imágenes de productos proceden de la API y muestran una alternativa vectorial cuando no están disponibles.
````

### frontend/src/assets/styles.css

````css
/* Bodeko UI: escala de espacios de 4 px, superficies neutras y acentos verdes. */
:root {
  font-family:
    Inter,
    -apple-system,
    BlinkMacSystemFont,
    'Segoe UI',
    sans-serif;
  color: #202223;
  background: #f6f6f7;
  font-synthesis: none;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  --green: #008060;
  --green-dark: #006e52;
  --muted: #6d7175;
  --border: #e1e3e5;
  --surface: #fff;
  --radius: 8px;
}
* {
  box-sizing: border-box;
}
body {
  margin: 0;
  min-width: 320px;
}
button,
input,
select,
textarea {
  font: inherit;
}
button,
a,
input,
select {
  -webkit-tap-highlight-color: transparent;
}
button,
a,
select,
input {
  outline-offset: 3px;
}
a {
  color: var(--green-dark);
  text-decoration: none;
}
a:hover {
  text-decoration: underline;
}
button {
  cursor: pointer;
}
button:disabled {
  cursor: not-allowed;
  opacity: 0.55;
}
h1,
h2,
h3,
p {
  margin: 0;
}
h1 {
  font-size: 30px;
  line-height: 1.2;
  letter-spacing: -0.9px;
  font-weight: 650;
}
h2 {
  font-size: 19px;
  line-height: 1.35;
  letter-spacing: -0.35px;
  font-weight: 650;
}
h3 {
  font-size: 16px;
  line-height: 1.4;
  font-weight: 600;
}
p {
  line-height: 1.65;
}
small {
  font-size: 12px;
  line-height: 1.5;
  color: var(--muted);
}
svg {
  flex-shrink: 0;
  vertical-align: middle;
}
hr {
  border: 0;
  border-top: 1px solid var(--border);
  width: 100%;
}
del {
  font-size: 13px;
  color: var(--muted);
}
.muted {
  color: var(--muted);
}
.eyebrow {
  font-size: 11px;
  letter-spacing: 1.5px;
  font-weight: 700;
  color: var(--green-dark);
  display: block;
  margin-bottom: 14px;
}
.button {
  min-height: 38px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 9px 15px;
  font-size: 13px;
  font-weight: 600;
  line-height: 1.3;
  border-radius: 6px;
  border: 1px solid transparent;
  text-decoration: none;
  transition:
    background 0.15s,
    box-shadow 0.15s;
  white-space: normal;
}
.button:hover {
  text-decoration: none;
}
.primary {
  background: var(--green);
  color: white;
  border-color: var(--green);
}
.primary:hover {
  background: var(--green-dark);
}
.secondary {
  background: #fff;
  color: #303336;
  border-color: #c9cccf;
  box-shadow: 0 1px 0 #00000005;
}
.secondary:hover {
  background: #f6f6f7;
}
.danger {
  background: #c52a1a;
  color: white;
  border-color: #c52a1a;
}
.danger:hover {
  background: #a92315;
}
.card {
  border: 1px solid var(--border);
  border-radius: var(--radius);
  background: var(--surface);
  padding: 24px;
  box-shadow: 0 1px 2px #00000004;
}
.card + .card {
  margin-top: 20px;
}
.site-header {
  background: #fff;
  border-bottom: 1px solid var(--border);
  position: sticky;
  top: 0;
  z-index: 20;
}
.header-inner {
  max-width: 1440px;
  margin: auto;
  height: 76px;
  padding: 0 40px;
  display: flex;
  align-items: center;
  gap: 56px;
}
.brand {
  display: inline-flex;
  align-items: center;
  gap: 9px;
  color: #163c32;
  font-weight: 750;
  font-size: 27px;
  letter-spacing: -1.1px;
}
.brand:hover {
  text-decoration: none;
}
.brand-dot {
  color: #008060;
}
.header-inner nav {
  display: flex;
  gap: 24px;
  font-size: 13px;
  font-weight: 550;
}
.header-inner nav a {
  color: #41464a;
}
.header-account {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-left: auto;
  font-size: 13px;
}
.user-name {
  max-width: 180px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.main-container {
  max-width: 1360px;
  margin: auto;
  padding: 36px 40px 72px;
  min-height: calc(100vh - 180px);
}
.site-footer {
  padding: 32px 40px;
  border-top: 1px solid var(--border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  font-size: 12px;
  color: var(--muted);
  max-width: 1440px;
  margin: auto;
}
.site-footer .brand {
  font-size: 22px;
}
.page-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  margin-bottom: 28px;
}
.page-heading p {
  font-size: 14px;
  color: var(--muted);
  margin-top: 8px;
}
.section-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  margin: 30px 0 24px;
}
.section-heading .eyebrow {
  margin-bottom: 8px;
}
.card .section-heading {
  margin-top: 0;
}
.section-heading h2 {
  font-size: 23px;
}
.actions {
  display: flex;
  gap: 8px;
  align-items: center;
  flex-wrap: wrap;
}
.text-link,
.back-link {
  display: inline-flex;
  gap: 8px;
  align-items: center;
  font-size: 13px;
}
.back-link {
  margin-bottom: 22px;
}
.skip-link {
  position: fixed;
  top: -80px;
  left: 16px;
  background: white;
  border: 2px solid var(--green);
  padding: 12px;
  z-index: 100;
}
.skip-link:focus {
  top: 10px;
}
/* Directorio y escaparate: contenido real sin imágenes o métricas de muestra. */
.hero {
  display: grid;
  grid-template-columns: 1.15fr 1fr;
  align-items: center;
  gap: 48px;
  background: #eaf3ed;
  border: 1px solid #dce9df;
  border-radius: 8px;
  padding: 58px 64px;
  overflow: hidden;
  position: relative;
  margin-bottom: 56px;
}
.hero h1 {
  font-size: 52px;
  letter-spacing: -2px;
  line-height: 1.12;
  color: #153e32;
}
.hero h1 span {
  color: #008060;
}
.hero p {
  font-size: 15px;
  color: #4c6158;
  max-width: 390px;
  margin: 22px 0 28px;
}
.hero-art {
  min-height: 268px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 26px;
  color: #28735a;
  background: linear-gradient(135deg, #deeadf, #f6faf3);
  border: 1px solid #d1e1d5;
  border-radius: 8px;
  transform: rotate(-3deg);
  box-shadow: 12px 14px 0 #d4e5d9;
}
.hero-art-label {
  background: #fff;
  padding: 13px 25px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 600;
  box-shadow: 0 3px 12px #193d2a08;
}
.store-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 22px;
}
.store-grid > .card + .card {
  margin-top: 0;
}
.store-card {
  display: block;
  color: inherit;
  transition:
    border-color 0.15s,
    transform 0.15s;
  padding: 28px;
}
.store-card:hover {
  text-decoration: none;
  border-color: #99bfae;
  transform: translateY(-2px);
}
.store-card h3,
.store-card h2 {
  margin: 22px 0 6px;
}
.store-symbol {
  height: 100px;
  background: #edf3ef;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #347258;
}
.tone-1 {
  background: #eff1f6;
  color: #5d6c8d;
}
.tone-2 {
  background: #f7f1e8;
  color: #917a4f;
}
.store-link {
  display: flex;
  gap: 8px;
  align-items: center;
  color: var(--green-dark);
  font-size: 13px;
  font-weight: 600;
  margin-top: 24px;
}
.store-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 22px;
  margin-bottom: 28px;
  border-bottom: 1px solid var(--border);
  font-weight: 600;
  font-size: 14px;
}
.cart-link {
  display: flex;
  align-items: center;
  gap: 8px;
}
.cart-count {
  display: inline-flex;
  min-width: 23px;
  height: 23px;
  align-items: center;
  justify-content: center;
  background: #e2f2eb;
  font-size: 12px;
  border-radius: 6px;
  padding: 0 5px;
}
.filters {
  display: flex;
  align-items: center;
  gap: 22px;
  margin-bottom: 24px;
  padding: 18px 22px;
}
.filters > .field {
  flex: 1;
  max-width: 340px;
}
.filters .checkbox {
  padding-top: 4px;
  white-space: nowrap;
}
.search-form {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}
.search-form > .field {
  flex: 1;
}
.search-form > .button {
  margin-top: 0;
}
.catalog-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 20px;
  margin-top: 20px;
}
.catalog-card {
  min-width: 0;
  padding: 0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  transition:
    box-shadow 180ms ease,
    border-color 180ms ease;
}
/* Resalta la tarjeta al recorrer sus enlaces con el teclado. */
.catalog-card:focus-within {
  border-color: #86b49c;
  box-shadow: 0 6px 18px #163c3214;
}
.catalog-card .catalog-image > .product-image,
.catalog-card .catalog-image > .image-fallback > svg {
  transform-origin: center;
  transition: transform 220ms ease;
}
/* Amplía solo la imagen. La tarjeta y los productos vecinos no se mueven. */
@media (hover: hover) and (pointer: fine) {
  .catalog-card:hover {
    border-color: #86b49c;
    box-shadow: 0 10px 24px #163c321a;
  }
  .catalog-card:hover .catalog-image > .product-image,
  .catalog-card:hover .catalog-image > .image-fallback > svg {
    transform: scale(1.08);
  }
}
.catalog-card + .catalog-card {
  margin: 0;
}
.catalog-image {
  display: block;
  position: relative;
  aspect-ratio: 1.15;
  flex-shrink: 0;
  background: #eef0ed;
  overflow: hidden;
  contain: paint;
  isolation: isolate;
}
.product-image {
  display: block;
  object-fit: contain;
  width: 100%;
  height: 100%;
  background: #f5f5f2;
}
.image-fallback {
  display: flex;
  width: 100%;
  height: 100%;
  align-items: center;
  justify-content: center;
  background: #f1f3f0;
  color: #9daaa1;
  min-height: 60px;
}
.catalog-card-body {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  flex: 1;
}
.catalog-card h3 {
  flex: 1;
}
.catalog-card h3 a {
  color: inherit;
  overflow-wrap: anywhere;
}
.availability {
  font-size: 11px;
  color: #287858;
  display: flex;
  align-items: center;
  gap: 5px;
}
.availability-out-of-stock {
  color: #b42318;
  font-weight: 600;
}
.availability:before {
  content: '';
  width: 5px;
  height: 5px;
  background: currentColor;
  border-radius: 4px;
}
.price {
  display: flex;
  align-items: baseline;
  gap: 9px;
  flex-wrap: wrap;
}
.price strong {
  font-size: 20px;
  font-weight: 650;
}
.catalog-card-body .button {
  width: 100%;
  font-size: 12px;
}
.sale-label {
  position: absolute;
  z-index: 1;
  top: 12px;
  left: 12px;
  padding: 5px 9px;
  font-size: 11px;
  border-radius: 4px;
  background: #fff;
  color: #006e52;
  border: 1px solid #d3e6da;
  font-weight: 600;
}
.product-detail {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
}
.detail-image {
  aspect-ratio: 1;
  background: #eee;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid var(--border);
}
.detail-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 24px;
}
.detail-content .price strong {
  font-size: 30px;
}
.detail-content .field {
  width: 150px;
}
.description {
  white-space: pre-wrap;
  overflow-wrap: anywhere;
  color: #5c6265;
}
.trust {
  font-size: 12px;
  color: var(--muted);
  display: flex;
  gap: 8px;
  align-items: center;
}
/* Formularios y estados asíncronos accesibles. */
.field {
  display: flex;
  flex-direction: column;
  gap: 7px;
  min-width: 0;
}
.field label {
  font-size: 13px;
  font-weight: 550;
}
.field input,
.field select,
.actions select {
  border: 1px solid #aeb4b8;
  background: white;
  color: #202223;
  border-radius: 5px;
  padding: 9px 11px;
  min-height: 39px;
  width: 100%;
  font-size: 14px;
  transition: border-color 0.15s;
}
.field input:focus,
.field select:focus {
  outline: 2px solid #00806045;
  border-color: #008060;
}
.field input[aria-invalid='true'] {
  border-color: #c52a1a;
}
.field-error {
  color: #b52618;
}
.field input::placeholder {
  color: #91969a;
  opacity: 1;
  font-size: 13px;
}
.form-stack {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.form-stack > h2 {
  display: flex;
  gap: 8px;
  align-items: center;
}
.form-stack > .button {
  align-self: flex-start;
}
.form-stack > .muted {
  font-size: 13px;
}
.checkbox {
  display: flex;
  align-items: center;
  gap: 9px;
  font-size: 13px;
  line-height: 1.5;
  cursor: pointer;
}
.checkbox input {
  accent-color: var(--green);
  width: 16px;
  height: 16px;
}
.inline-form {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}
.inline-form .field {
  flex: 1;
}
.inline-form .button {
  margin-top: 25px;
}
.notice {
  display: flex;
  align-items: center;
  gap: 12px;
  border: 1px solid;
  border-radius: 6px;
  padding: 14px 16px;
  font-size: 13px;
  line-height: 1.6;
  margin: 16px 0;
}
.notice span {
  flex: 1;
}
.notice p {
  margin-top: 4px;
}
.notice.error {
  background: #fff4f1;
  border-color: #f0c6bc;
  color: #a12214;
}
.notice.success {
  background: #edf8f1;
  border-color: #b6ddc5;
  color: #1e6944;
}
.loading {
  padding: 60px 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  color: var(--muted);
  font-size: 14px;
}
.spin {
  animation: spin 1s linear infinite;
}
@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
.empty {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 18px;
  text-align: center;
  padding: 72px 24px;
  color: var(--muted);
  background: white;
  border: 1px dashed #d3d8d5;
  border-radius: 8px;
}
.empty h3 {
  color: #343a37;
}
.empty p {
  max-width: 420px;
  font-size: 14px;
}
.modal {
  border: 1px solid #d1d5d2;
  border-radius: 8px;
  padding: 28px;
  width: min(580px, calc(100vw - 32px));
  max-height: 90dvh;
  color: #202223;
  box-shadow: 0 20px 80px #0003;
}
.modal::backdrop {
  background: #13291f55;
  backdrop-filter: blur(2px);
}
.modal-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 24px;
}
.modal > p {
  margin-bottom: 24px;
}
.pagination {
  display: flex;
  gap: 18px;
  align-items: center;
  justify-content: center;
  margin-top: 28px;
  font-size: 13px;
}
.auth-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
  max-width: 1040px;
  margin: 40px auto;
}
.auth-copy h1 {
  font-size: 45px;
  letter-spacing: -1.8px;
  color: #173f33;
  line-height: 1.14;
}
.auth-copy p {
  margin-top: 25px;
  color: var(--muted);
  max-width: 350px;
  font-size: 15px;
}
.auth-mark {
  margin-top: 40px;
  background: #e5efe7;
  border: 1px solid #d8e7db;
  padding: 40px;
  border-radius: 8px;
  max-width: 320px;
  color: #30765a;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 25px;
}
.auth-mark span {
  font-size: 24px;
  font-weight: 700;
  letter-spacing: -1px;
}
.auth-card {
  padding: 32px;
}
.auth-card > p {
  margin: 10px 0 28px;
  font-size: 13px;
}
.auth-card .form-stack > .button {
  width: 100%;
}
.two-columns {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}
.two-columns > .card + .card {
  margin: 0;
}
.offer-row {
  padding: 20px 0;
  border-bottom: 1px solid var(--border);
}
.offer-row > strong {
  margin-right: 16px;
}
.offer-row > p {
  font-size: 12px;
  color: var(--muted);
  margin-top: 10px;
}
.data-list {
  display: grid;
  grid-template-columns: 120px 1fr;
  gap: 18px;
  font-size: 13px;
  margin: 8px 0;
}
.data-list dt {
  color: var(--muted);
}
.data-list dd {
  margin: 0;
  font-weight: 500;
  overflow-wrap: anywhere;
}
/* Administración: navegación lateral y tablas de baja densidad. */
.admin-layout {
  display: grid;
  grid-template-columns: 216px minmax(0, 1fr);
  gap: 38px;
  margin: -36px 0 -72px -12px;
  min-height: calc(100vh - 76px);
}
.admin-sidebar {
  padding: 28px 12px 32px 0;
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  gap: 25px;
}
.admin-sidebar .back-link {
  margin-bottom: 0;
  color: var(--muted);
  font-size: 12px;
}
.sidebar-store {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}
.sidebar-store strong {
  font-size: 13px;
  display: block;
  overflow-wrap: anywhere;
}
.sidebar-store small {
  display: block;
  margin-top: 4px;
}
.store-initial {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 6px;
  background: #ddece2;
  color: #1c6246;
  font-weight: 600;
  flex-shrink: 0;
}
.admin-sidebar nav {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.admin-sidebar nav a {
  display: flex;
  gap: 12px;
  align-items: center;
  color: #50575a;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 500;
  padding: 11px 12px;
}
.admin-sidebar nav a:hover {
  text-decoration: none;
  background: #e9edeb;
}
.admin-sidebar nav a.active {
  background: #dfeee5;
  color: #006e52;
  font-weight: 650;
}
.sidebar-preview {
  display: flex;
  align-items: center;
  gap: 9px;
  margin-top: auto;
  padding: 12px;
  font-size: 12px;
}
.admin-content {
  min-width: 0;
  padding: 38px 0 60px;
}
.admin-content .page-heading h1 {
  font-size: 27px;
}
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
  margin-bottom: 26px;
}
.metric-card {
  padding: 20px;
}
.metric-card + .metric-card {
  margin: 0;
}
.metric-card p {
  font-size: 12px;
  font-weight: 550;
  color: #4d5551;
  line-height: 1.4;
}
.metric-card > strong {
  display: block;
  font-size: 32px;
  letter-spacing: -1px;
  margin: 17px 0 8px;
}
.metric-card small {
  font-size: 10px;
}
.admin-welcome {
  padding: 36px;
  background: linear-gradient(120deg, #eff6ef, #fff);
}
.admin-welcome h2 {
  font-size: 25px;
}
.admin-welcome p {
  color: var(--muted);
  font-size: 14px;
  margin: 12px 0 26px;
  max-width: 500px;
}
.table-wrap {
  overflow-x: auto;
  padding: 0;
  max-width: 100%;
  border-radius: 8px;
}
table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
  font-size: 13px;
}
th {
  font-size: 11px;
  color: #616966;
  font-weight: 650;
  background: #fafbfa;
  border-bottom: 1px solid var(--border);
  padding: 15px 20px;
  white-space: nowrap;
}
td {
  padding: 18px 20px;
  border-bottom: 1px solid #edf0ee;
  vertical-align: middle;
}
tr:last-child td {
  border-bottom: 0;
}
td.actions {
  display: table-cell;
}
td.actions .button + .button {
  margin-left: 8px;
}
td select {
  max-width: 130px;
}
.product-cell {
  display: flex;
  align-items: center;
  gap: 13px;
  min-width: 180px;
}
.product-cell > .product-image,
.product-cell > .image-fallback {
  width: 44px;
  height: 44px;
  min-height: 44px;
  flex-shrink: 0;
  border: 1px solid var(--border);
  border-radius: 5px;
}
.product-cell small {
  display: block;
  margin-top: 5px;
  font-size: 11px;
}
.product-cell strong {
  font-weight: 550;
}
.badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  background: #eceeed;
  padding: 5px 9px;
  border-radius: 5px;
  font-size: 11px;
  font-weight: 550;
  white-space: nowrap;
  color: #515a55;
}
.badge-active,
.badge-paid,
.badge-approved,
.badge-delivered {
  background: #dff1e5;
  color: #1d6942;
}
.badge-pending,
.badge-draft {
  background: #fff0c9;
  color: #7d5e0f;
}
.badge-shipped {
  background: #e4edf9;
  color: #345880;
}
.badge-cancelled,
.badge-failed {
  background: #fbe6e3;
  color: #ad3729;
}
.fiscal-details {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 22px;
  margin: 24px 0;
}
.fiscal-details small,
.fiscal-details strong {
  display: block;
}
.fiscal-details strong {
  font-size: 13px;
  font-weight: 550;
  margin-top: 5px;
}
.order-totals {
  max-width: 320px;
  margin: 24px 0 0 auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.order-totals p {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  font-size: 13px;
}
.order-totals .total {
  font-size: 17px;
  padding-top: 16px;
  border-top: 1px solid var(--border);
}
/* Compra: el resumen acompaña al formulario y baja de columna en móvil. */
.checkout-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.5fr) minmax(300px, 1fr);
  gap: 26px;
  align-items: start;
}
.checkout-grid > .card + .card {
  margin: 0;
}
.summary {
  display: flex;
  flex-direction: column;
  gap: 20px;
  position: sticky;
  top: 100px;
}
.summary > p {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  font-size: 13px;
}
.summary > p span {
  max-width: 65%;
}
.summary small {
  line-height: 1.7;
}
.summary > .button {
  width: 100%;
}
.summary > a:not(.button) {
  font-size: 13px;
  text-align: center;
}
.cart-line {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  padding: 22px 0;
  border-bottom: 1px solid var(--border);
}
.cart-line:first-child {
  padding-top: 0;
}
.cart-line:last-child {
  border-bottom: 0;
  padding-bottom: 0;
}
.cart-line > .product-image,
.cart-line > .image-fallback {
  width: 70px;
  height: 80px;
  flex-shrink: 0;
  border-radius: 6px;
}
.cart-line-content {
  flex: 1;
  min-width: 0;
}
.cart-line-content > p {
  font-size: 12px;
  color: var(--muted);
  margin-top: 7px;
}
.cart-line .inline-form {
  gap: 8px;
  margin-top: 18px;
}
.cart-line .field {
  max-width: 95px;
}
.cart-line-total {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 20px;
  font-size: 14px;
}
.cart-line-total .button {
  font-size: 11px;
  padding: 7px;
}
/* Adaptación móvil sin reducir las áreas táctiles ni ocultar datos esenciales. */
@media (min-width: 1500px) {
  .main-container {
    padding-left: 30px;
    padding-right: 30px;
  }
}
@media (max-width: 1150px) {
  .catalog-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
  .hero {
    padding: 42px;
  }
  .hero h1 {
    font-size: 44px;
  }
  .metrics-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .admin-layout {
    gap: 24px;
    grid-template-columns: 190px minmax(0, 1fr);
  }
  .checkout-grid {
    grid-template-columns: minmax(0, 1.3fr) minmax(270px, 1fr);
  }
  .cart-line {
    flex-wrap: wrap;
  }
  .cart-line-total {
    width: 100%;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }
  .auth-layout {
    gap: 40px;
  }
  .two-columns {
    gap: 18px;
  }
}
@media (max-width: 850px) {
  .header-inner {
    padding: 0 24px;
    gap: 28px;
    height: 68px;
  }
  .main-container {
    padding: 28px 24px 48px;
  }
  .hero {
    grid-template-columns: 1fr 0.7fr;
    padding: 32px;
    gap: 30px;
  }
  .hero h1 {
    font-size: 36px;
  }
  .hero-art {
    min-height: 220px;
    box-shadow: 6px 8px 0 #d4e5d9;
  }
  .hero-art > svg {
    width: 90px;
  }
  .hero-art-label {
    font-size: 10px;
    padding: 10px 14px;
  }
  .store-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .catalog-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
  .filters {
    flex-wrap: wrap;
    gap: 16px;
  }
  .search-form {
    flex-basis: 100%;
  }
  .filters > .field {
    flex: 1;
    max-width: none;
  }
  .checkout-grid,
  .two-columns {
    grid-template-columns: 1fr;
  }
  .summary {
    position: static;
  }
  .admin-layout {
    display: block;
    margin: 0;
    min-height: auto;
  }
  .admin-sidebar {
    padding: 0 0 24px;
    border-right: 0;
    gap: 16px;
  }
  .admin-sidebar nav {
    flex-direction: row;
    overflow-x: auto;
    padding-bottom: 8px;
    gap: 5px;
  }
  .admin-sidebar nav a {
    white-space: nowrap;
    font-size: 12px;
    padding: 9px;
  }
  .sidebar-store,
  .sidebar-preview {
    display: none;
  }
  .admin-content {
    padding: 12px 0;
  }
  .admin-sidebar .back-link {
    margin-bottom: 5px;
  }
  .auth-copy h1 {
    font-size: 36px;
  }
  .auth-mark {
    padding: 25px;
  }
  .auth-mark span {
    font-size: 24px;
  }
  .product-detail {
    gap: 22px;
  }
  .detail-content {
    padding: 22px;
    gap: 20px;
  }
  .detail-content h1 {
    font-size: 25px;
  }
  .site-footer {
    padding: 24px;
    font-size: 11px;
  }
  .site-footer span:last-child {
    display: none;
  }
  .user-name {
    max-width: 130px;
  }
}
@media (max-width: 580px) {
  .header-inner {
    padding: 0 16px;
    gap: 20px;
    height: 64px;
  }
  .brand {
    font-size: 19px;
    gap: 6px;
  }
  .brand svg {
    width: 23px;
  }
  .header-inner nav {
    gap: 14px;
    font-size: 12px;
  }
  .header-account {
    gap: 10px;
    font-size: 12px;
  }
  .header-account > .button {
    font-size: 11px;
    padding: 8px 10px;
  }
  .header-account > a:first-child {
    display: none;
  }
  .header-account .button {
    padding: 8px 10px;
  }
  .desktop-label,
  .user-name {
    display: none;
  }
  .main-container {
    padding: 24px 16px 40px;
  }
  .hero {
    padding: 30px 24px;
    grid-template-columns: 1fr;
    gap: 24px;
    margin-bottom: 35px;
  }
  .hero h1 {
    font-size: 39px;
  }
  .hero .eyebrow {
    font-size: 9px;
    letter-spacing: 1.1px;
  }
  .hero p {
    font-size: 14px;
    margin: 18px 0 22px;
  }
  .hero-art {
    min-height: 140px;
    flex-direction: row;
    transform: none;
    gap: 20px;
  }
  .hero-art > svg {
    width: 65px;
    height: 65px;
  }
  .hero-art-label {
    font-size: 10px;
    max-width: 140px;
    line-height: 1.6;
  }
  .section-heading {
    align-items: flex-start;
  }
  .section-heading h2 {
    font-size: 20px;
  }
  .section-heading > .muted {
    font-size: 11px;
    white-space: nowrap;
    margin-top: 4px;
  }
  .store-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  .store-card {
    padding: 22px;
  }
  .store-symbol {
    height: 80px;
  }
  .page-heading {
    align-items: flex-start;
    flex-direction: column;
    gap: 16px;
    margin-bottom: 22px;
  }
  .page-heading h1 {
    font-size: 27px;
  }
  .page-heading p {
    font-size: 13px;
  }
  .page-heading > .button {
    width: 100%;
  }
  .filters {
    padding: 16px;
    gap: 14px;
  }
  .filters > .field {
    flex-basis: 100%;
  }
  .catalog-grid {
    gap: 12px;
  }
  .catalog-card-body {
    padding: 13px;
    gap: 12px;
  }
  .catalog-card h3 {
    font-size: 13px;
  }
  .catalog-card .price strong {
    font-size: 17px;
  }
  .catalog-card .price del {
    font-size: 11px;
  }
  .catalog-card-body .button {
    padding: 8px 7px;
    min-height: 42px;
    font-size: 11px;
  }
  .catalog-card-body .button svg {
    width: 13px;
  }
  .sale-label {
    top: 8px;
    left: 8px;
    font-size: 10px;
    padding: 4px 6px;
  }
  .product-detail {
    grid-template-columns: 1fr;
  }
  .detail-image {
    aspect-ratio: 1.15;
  }
  .card {
    padding: 20px;
  }
  .catalog-card {
    padding: 0;
  }
  .table-wrap {
    padding: 0;
  }
  .checkout-grid {
    gap: 20px;
  }
  .checkout-grid .card {
    min-width: 0;
  }
  .auth-layout {
    display: block;
    margin: 0;
  }
  .auth-copy {
    margin-bottom: 28px;
  }
  .auth-copy h1 {
    font-size: 34px;
  }
  .auth-copy p {
    font-size: 13px;
    margin-top: 14px;
  }
  .auth-mark {
    display: none;
  }
  .auth-copy .eyebrow {
    font-size: 10px;
  }
  .auth-card {
    padding: 22px;
  }
  .inline-form {
    flex-wrap: wrap;
  }
  .inline-form > .field {
    min-width: 120px;
  }
  .inline-form > .button {
    margin-top: 0;
    align-self: center;
    min-height: 40px;
  }
  .metrics-grid {
    gap: 10px;
  }
  .metric-card {
    padding: 16px;
  }
  .metric-card > strong {
    font-size: 29px;
    margin-top: 12px;
  }
  .metric-card small {
    font-size: 9px;
  }
  .admin-welcome {
    padding: 24px;
  }
  .admin-welcome h2 {
    font-size: 23px;
  }
  .admin-welcome .actions {
    align-items: stretch;
    flex-direction: column;
  }
  .cart-line {
    gap: 12px;
  }
  .cart-line > .product-image,
  .cart-line > .image-fallback {
    width: 52px;
    height: 65px;
  }
  .cart-line-content h3 {
    font-size: 14px;
  }
  .cart-line .inline-form {
    align-items: flex-start;
    flex-direction: column;
  }
  .cart-line .inline-form > .button {
    align-self: flex-start;
  }
  .fiscal-details {
    grid-template-columns: 1fr;
  }
  .order-totals {
    max-width: none;
  }
  .notice {
    flex-wrap: wrap;
  }
  .notice > .button {
    margin-left: 30px;
  }
  .site-footer {
    padding: 24px 16px;
    align-items: flex-start;
    flex-direction: column;
    gap: 10px;
  }
  .data-list {
    grid-template-columns: 100px 1fr;
    gap: 14px;
  }
  .modal {
    padding: 22px;
  }
  .modal-title h2 {
    font-size: 18px;
  }
  .store-nav {
    font-size: 13px;
    gap: 16px;
  }
  .store-nav > a:first-child {
    max-width: 65%;
    overflow-wrap: anywhere;
  }
}
@media (max-width: 400px) {
  .header-inner nav > a:first-child {
    display: none;
  }
  .header-inner {
    gap: 12px;
  }
}
/* Cuenta de cliente: menú lateral y paneles inspirados en la referencia del proyecto. */
.customer-account {
  padding: 0 0 16px;
}
.account-breadcrumb {
  display: flex;
  align-items: center;
  gap: 9px;
  color: var(--muted);
  font-size: 12px;
  margin: 0 0 36px;
}
.account-breadcrumb a {
  color: var(--muted);
}
.customer-account-layout {
  display: grid;
  grid-template-columns: 252px minmax(0, 1fr);
  gap: 38px;
  align-items: start;
}
.account-sidebar {
  padding: 8px 0 24px;
}
.account-greeting {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 0 8px 30px;
}
.account-avatar {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #edf1ee;
  color: #7b9285;
  flex-shrink: 0;
}
.account-greeting small {
  display: block;
  font-size: 13px;
  color: #606964;
}
.account-greeting strong {
  display: block;
  font-size: 14px;
  line-height: 1.5;
  margin: 4px 0;
  overflow-wrap: anywhere;
}
.account-greeting div > span {
  color: var(--muted);
  font-size: 11px;
}
.account-sidebar nav {
  display: flex;
  flex-direction: column;
  border-top: 1px solid var(--border);
}
.account-sidebar nav a,
.account-sidebar nav button {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 17px 14px;
  min-height: 54px;
  border: 0;
  border-bottom: 1px solid var(--border);
  background: transparent;
  color: #535e58;
  font-size: 13px;
  text-align: left;
}
.account-sidebar nav a > span,
.account-sidebar nav button > span {
  flex: 1;
}
.account-sidebar nav a > svg:first-child {
  color: #87958b;
}
.account-sidebar nav a:hover,
.account-sidebar nav button:hover {
  text-decoration: none;
  background: #edf3ef;
}
.account-sidebar nav a.active {
  color: #006e52;
  font-weight: 650;
  background: #e7f1eb;
  box-shadow: inset 3px 0 #008060;
}
.account-sidebar nav a.active > svg {
  color: #008060;
}
.account-sidebar nav button {
  margin-top: 6px;
  font-weight: 600;
}
.account-help {
  margin: 30px 12px 0;
  padding: 18px 0 0;
  border-top: 1px solid var(--border);
}
.account-help strong {
  font-size: 12px;
  color: #4b5f53;
}
.account-help p {
  margin-top: 9px;
  font-size: 12px;
  line-height: 1.8;
  color: var(--muted);
}
.account-main {
  min-width: 0;
}
.account-page-heading {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  margin-bottom: 26px;
}
.account-page-heading h1 {
  font-size: 27px;
  letter-spacing: -0.7px;
}
.account-page-heading .eyebrow {
  margin-bottom: 8px;
  font-size: 10px;
}
.account-page-heading p {
  color: var(--muted);
  font-size: 13px;
  margin-top: 9px;
}
.account-section-card {
  background: white;
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 26px;
  box-shadow: 0 1px 2px #00000003;
}
.account-section-card + .account-section-card {
  margin-top: 22px;
}
.order-list-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding-bottom: 23px;
}
.order-list-toolbar > div {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
}
.order-list-toolbar > div > svg {
  color: #687e70;
}
.order-list-toolbar > .button {
  font-size: 12px;
  min-height: 34px;
}
.order-status-filters {
  display: flex;
  gap: 5px;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border);
  overflow-x: auto;
}
.order-status-filters button {
  padding: 9px 12px;
  background: white;
  border: 1px solid transparent;
  border-radius: 5px;
  color: #647069;
  font-size: 12px;
  white-space: nowrap;
  min-height: 36px;
}
.order-status-filters button:hover {
  background: #f2f6f3;
}
.order-status-filters button.selected {
  background: #e7f2eb;
  border-color: #cfe2d6;
  color: #006e52;
  font-weight: 650;
}
.order-result-count {
  color: var(--muted);
  font-size: 12px;
  margin: 19px 0;
}
.customer-orders-list {
  display: flex;
  flex-direction: column;
  gap: 18px;
}
.customer-order-card {
  border: 1px solid #e0e6e1;
  border-radius: 7px;
  overflow: hidden;
}
.customer-order-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 18px 22px;
  background: #f8faf8;
  border-bottom: 1px solid #e6ebe7;
}
.order-label {
  font-size: 9px;
  letter-spacing: 1.2px;
  color: #7a877e;
  display: block;
  margin-bottom: 6px;
}
.order-number {
  display: block;
  font-size: 14px;
  font-weight: 650;
  color: #263e30;
  overflow-wrap: anywhere;
}
.customer-order-card-header time {
  display: block;
  margin-top: 6px;
  color: var(--muted);
  font-size: 11px;
}
.customer-order-card-body {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 24px 22px;
}
.order-store {
  display: flex;
  align-items: center;
  gap: 13px;
  min-width: 0;
}
.order-store-icon {
  width: 46px;
  height: 46px;
  border: 1px solid #e1ebe4;
  background: #f0f6f1;
  color: #538365;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.order-store a {
  font-size: 13px;
  font-weight: 600;
  color: #303f36;
  overflow-wrap: anywhere;
}
.order-store p {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 11px;
  color: var(--muted);
  margin-top: 7px;
}
.customer-order-amount {
  text-align: right;
  flex-shrink: 0;
}
.customer-order-amount small {
  font-size: 10px;
}
.customer-order-amount strong {
  display: block;
  margin-top: 6px;
  font-size: 22px;
  letter-spacing: -0.7px;
  font-weight: 650;
}
.customer-order-card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14px;
  border-top: 1px solid #edf0ed;
  padding: 14px 22px;
}
.order-detail-link {
  display: flex;
  align-items: center;
  gap: 7px;
  font-size: 12px;
  font-weight: 550;
}
.customer-order-card-footer .button {
  font-size: 11px;
  padding: 8px 11px;
  min-height: 34px;
}
.cancel-order-button {
  color: #8d493e;
  border-color: #e7d8d3;
}
.cancel-order-button:hover {
  background: #fdf6f3;
}
.account-order-note {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  margin: 22px 8px;
  color: #7a8980;
}
.account-order-note p {
  font-size: 11px;
  line-height: 1.8;
}
.customer-order-detail .section-heading {
  margin-top: 24px;
}
.order-detail-store {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border);
  font-size: 13px;
  color: #51765d;
}
.customer-order-management {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}
.customer-order-management h2 {
  font-size: 16px;
}
.customer-order-management p {
  font-size: 12px;
  color: var(--muted);
  margin-top: 9px;
  max-width: 420px;
}
.customer-order-management .actions {
  flex-shrink: 0;
}
.account-profile h2 {
  font-size: 17px;
  margin-bottom: 28px;
}
.account-profile dl {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 26px;
  margin: 0 0 28px;
}
.account-profile dt {
  display: flex;
  align-items: center;
  gap: 7px;
  color: #6a7c70;
  font-size: 12px;
  margin-bottom: 10px;
}
.account-profile dd {
  padding: 12px 14px;
  margin: 0;
  border: 1px solid #e0e5e2;
  background: #fafbf9;
  border-radius: 5px;
  font-size: 13px;
  overflow-wrap: anywhere;
}
.account-profile > p {
  font-size: 12px;
  padding-top: 20px;
  border-top: 1px solid var(--border);
}
.cancel-order-intro {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 20px;
}
.cancel-order-intro > span {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  background: #fff3ec;
  color: #a75b32;
  border-radius: 8px;
  flex-shrink: 0;
}
.cancel-order-intro h3 {
  overflow-wrap: anywhere;
}
.cancel-order-intro p {
  font-size: 13px;
  color: var(--muted);
  margin-top: 6px;
}
.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}
@media (max-width: 1100px) {
  .customer-account-layout {
    grid-template-columns: 218px minmax(0, 1fr);
    gap: 24px;
  }
  .account-greeting {
    gap: 10px;
    padding-left: 0;
  }
  .account-avatar {
    width: 48px;
    height: 48px;
  }
  .account-section-card {
    padding: 22px;
  }
  .customer-order-card-footer {
    flex-wrap: wrap;
  }
  .customer-order-management {
    align-items: flex-start;
    flex-direction: column;
  }
}
@media (max-width: 850px) {
  .customer-account-layout {
    grid-template-columns: 1fr;
    gap: 28px;
  }
  .account-breadcrumb {
    margin-bottom: 22px;
  }
  .account-sidebar {
    padding: 0;
  }
  .account-greeting {
    padding: 0 0 18px;
  }
  .account-sidebar nav {
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    border: 0;
    gap: 8px;
  }
  .account-sidebar nav a,
  .account-sidebar nav button {
    min-height: 48px;
    padding: 12px;
    font-size: 12px;
    border: 1px solid var(--border);
    border-radius: 5px;
    margin: 0;
    gap: 8px;
  }
  .account-sidebar nav a > svg:last-child {
    display: none;
  }
  .account-help {
    display: none;
  }
}
@media (max-width: 580px) {
  .account-sidebar nav {
    grid-template-columns: 1fr 1fr;
  }
  .account-page-heading {
    flex-direction: column;
    align-items: flex-start;
    gap: 14px;
  }
  .account-page-heading h1 {
    font-size: 25px;
  }
  .account-page-heading > .button {
    font-size: 12px;
  }
  .account-section-card {
    padding: 18px 14px;
  }
  .order-list-toolbar {
    padding-bottom: 18px;
  }
  .order-list-toolbar > .button span {
    display: none;
  }
  .order-status-filters {
    gap: 3px;
  }
  .order-status-filters button {
    min-height: 42px;
    padding: 8px 12px;
  }
  .customer-order-card-header {
    padding: 15px 14px;
    gap: 10px;
    align-items: flex-start;
  }
  .order-number {
    font-size: 12px;
  }
  .customer-order-card-header .badge {
    font-size: 10px;
    padding: 5px 7px;
  }
  .customer-order-card-body {
    padding: 20px 14px;
    gap: 12px;
  }
  .order-store {
    gap: 9px;
  }
  .order-store-icon {
    width: 34px;
    height: 38px;
  }
  .order-store-icon > svg {
    width: 18px;
  }
  .order-store a {
    font-size: 12px;
  }
  .customer-order-amount strong {
    font-size: 19px;
  }
  .customer-order-amount small {
    font-size: 9px;
  }
  .customer-order-card-footer {
    padding: 14px;
    gap: 16px;
  }
  .customer-order-card-footer .actions {
    width: 100%;
  }
  .customer-order-card-footer .actions > * {
    flex: 1;
    min-height: 42px;
  }
  .order-detail-link {
    font-size: 11px;
  }
  .account-profile dl {
    grid-template-columns: 1fr;
    gap: 22px;
  }
  .dialog-actions {
    flex-direction: column-reverse;
  }
  .dialog-actions .button {
    min-height: 42px;
  }
  .customer-order-detail .section-heading {
    align-items: flex-start;
    flex-wrap: wrap;
  }
  .customer-order-detail .section-heading h2 {
    font-size: 19px;
    overflow-wrap: anywhere;
  }
}
@media (prefers-reduced-motion: reduce) {
  .catalog-card:hover .catalog-image > .product-image,
  .catalog-card:hover .catalog-image > .image-fallback > svg {
    transform: none;
  }
  * {
    animation-duration: 0.01ms !important;
    transition: none !important;
    scroll-behavior: auto !important;
  }
}
````

### frontend/src/config/contracts.test.ts

````typescript
import { beforeEach, describe, expect, it, vi } from 'vitest';
vi.mock('@/config', () => ({
  api: { get: vi.fn(), post: vi.fn(), put: vi.fn(), patch: vi.fn(), delete: vi.fn() },
}));
import { api } from '@/config';
import { catalogService } from '@/features/catalog/services/catalogService';
import { cartService } from '@/features/cart/services/cartService';
import { checkoutService } from '@/features/checkout/services/checkoutService';
import { paymentService } from '@/features/payments/services/paymentService';
import { inventoryService } from '@/features/inventory/services/inventoryService';

describe('Contratos de integración con Spring', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    for (const method of [api.get, api.post, api.patch])
      vi.mocked(method).mockResolvedValue({ data: {} });
  });
  it('envía filtros al catálogo público sin exigir un token de cliente', async () => {
    await catalogService.search('store', {
      search: 'café',
      categoryId: '',
      inStock: true,
      page: 2,
    });
    expect(api.get).toHaveBeenCalledWith('/public/stores/store/products', {
      params: { search: 'café', categoryId: undefined, inStock: true, page: 2, size: 12 },
    });
  });
  it('persiste cambios de cantidad mediante el endpoint del cliente', async () => {
    await cartService.update('store', 'product', 3);
    expect(api.patch).toHaveBeenCalledWith('/customer/stores/store/cart/items/product', {
      quantity: 3,
    });
  });
  it('no envía importes desde el navegador al cobrar una orden', async () => {
    await paymentService.pay('store', 'order', 'tkn_test_valid');
    expect(api.post).toHaveBeenCalledWith('/customer/stores/store/orders/order/payments', {
      sourceId: 'tkn_test_valid',
    });
  });
  it('recupera una orden por la ruta protegida del cliente', async () => {
    await checkoutService.get('store', 'order');
    expect(api.get).toHaveBeenCalledWith('/customer/stores/store/orders/order');
  });
  it('mantiene cantidad y umbral como operaciones separadas', async () => {
    await inventoryService.threshold('store', 'product', 5);
    expect(api.patch).toHaveBeenCalledWith(
      '/stores/store/products/product/inventory/low-stock-threshold',
      { lowStockThreshold: 5 },
    );
  });
});
````

### frontend/src/config/index.ts

````typescript
import axios from 'axios';
/** Marca del proyecto; el nombre del Excel corresponde únicamente a su plantilla. */
export const APP_NAME = 'Bodeko';
/** Clave de almacenamiento de la sesión; la API valida siempre el JWT. */
export const SESSION_KEY = 'bodeko.token';
/** Configura la conexión y el tiempo de espera de las peticiones a la API. */
export const api = axios.create({
  baseURL: `${(import.meta.env.VITE_API_URL || '').replace(/\/$/, '')}/api`,
  timeout: 35000,
});
api.interceptors.request.use((config) => {
  const token = sessionStorage.getItem(SESSION_KEY);
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});
api.interceptors.response.use(
  (response) => response,
  (error: unknown) => {
    // Un 401 del login no invalida otra sesión; los recursos protegidos sí.
    if (
      axios.isAxiosError(error) &&
      error.response?.status === 401 &&
      !error.config?.url?.includes('/auth/login')
    ) {
      sessionStorage.removeItem(SESSION_KEY);
      window.dispatchEvent(new Event('bodeko:unauthorized'));
    }
    return Promise.reject(error);
  },
);
````

### frontend/src/context/CartContext.tsx

````tsx
import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useRef,
  useState,
  type ReactNode,
} from 'react';
import { toast } from 'sonner';
import { useSession } from './SessionContext';
import { useStore } from './StoreContext';
import { cartService } from '@/features/cart/services/cartService';
import type { Cart } from '@/features/cart/types';
import { errorMessage } from '@/utils/errors';

/** Comparte el carrito entre las páginas de la tienda actual. */
interface CartState {
  cart: Cart | null;
  loading: boolean;
  pending: boolean;
  error: string;
  reload: () => void;
  add: (id: string, quantity?: number) => Promise<boolean>;
  update: (id: string, quantity: number) => Promise<boolean>;
  remove: (id: string) => Promise<boolean>;
}
const CartContext = createContext<CartState | null>(null);
/** Carga el carrito e ignora respuestas de una cuenta o tienda anterior. */
export function CartProvider({ children }: { children: ReactNode }) {
  const { user } = useSession();
  const store = useStore();
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState(false);
  const [pending, setPending] = useState(false);
  const [error, setError] = useState('');
  const [revision, setRevision] = useState(0);
  const generation = useRef(0);
  const busy = useRef(false);
  const reload = useCallback(() => setRevision((value) => value + 1), []);
  useEffect(() => {
    const current = ++generation.current;
    setCart(null);
    setError('');
    setLoading(user?.role === 'CUSTOMER');
    if (user?.role !== 'CUSTOMER') return;
    void cartService
      .get(store.id)
      .then((result) => {
        if (generation.current === current) setCart(result);
      })
      .catch((cause: unknown) => {
        if (generation.current === current) setError(errorMessage(cause));
      })
      .finally(() => {
        if (generation.current === current) setLoading(false);
      });
    return () => {
      generation.current++;
    };
  }, [user?.id, user?.role, store.id, revision]);
  async function mutate(operation: () => Promise<Cart>, success: string) {
    if (busy.current) return false;
    if (user?.role !== 'CUSTOMER') {
      toast.error('Inicia sesión con una cuenta de cliente para comprar.');
      return false;
    }
    const current = generation.current;
    busy.current = true;
    setPending(true);
    setError('');
    try {
      const result = await operation();
      if (generation.current !== current) return false;
      setCart(result);
      toast.success(success);
      return true;
    } catch (cause) {
      if (generation.current === current) {
        const message = errorMessage(cause);
        setError(message);
        toast.error(message);
      }
      return false;
    } finally {
      busy.current = false;
      setPending(false);
    }
  }
  return (
    <CartContext.Provider
      value={{
        cart,
        loading,
        pending,
        error,
        reload,
        add: (id, quantity = 1) =>
          mutate(() => cartService.add(store.id, id, quantity), 'Producto agregado al carrito.'),
        update: (id, quantity) =>
          mutate(() => cartService.update(store.id, id, quantity), 'Cantidad actualizada.'),
        remove: (id) =>
          mutate(() => cartService.remove(store.id, id), 'Producto eliminado del carrito.'),
      }}
    >
      {children}
    </CartContext.Provider>
  );
}
/** Permite consultar y modificar el carrito de la tienda actual. */
export function useCart(): CartState {
  const value = useContext(CartContext);
  if (!value) throw new Error('No se encontró el proveedor de carrito.');
  return value;
}
````

### frontend/src/context/SessionContext.tsx

````tsx
import { createContext, useCallback, useContext, useEffect, useState, type ReactNode } from 'react';
import { SESSION_KEY } from '@/config';
import { authService } from '@/features/auth/services/authService';
import type { User } from '@/core/types';
import { errorMessage } from '@/utils/errors';

/** Consulta /auth/me para comprobar la sesión y el rol del usuario. */
interface SessionState {
  user: User | null;
  loading: boolean;
  error: string;
  establish: (token: string) => Promise<User>;
  logout: () => void;
  retry: () => void;
}
const SessionContext = createContext<SessionState | null>(null);
/** Recupera la sesión al cargar la página y la limpia cuando vence. */
export function SessionProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [revision, setRevision] = useState(0);
  const logout = useCallback(() => {
    sessionStorage.removeItem(SESSION_KEY);
    setUser(null);
    setError('');
  }, []);
  useEffect(() => {
    let active = true;
    const expire = () => {
      logout();
    };
    window.addEventListener('bodeko:unauthorized', expire);
    if (!sessionStorage.getItem(SESSION_KEY)) {
      setLoading(false);
      return () => window.removeEventListener('bodeko:unauthorized', expire);
    }
    setLoading(true);
    setError('');
    void authService
      .me()
      .then((result) => {
        if (active) setUser(result);
      })
      .catch((cause: unknown) => {
        if (active && sessionStorage.getItem(SESSION_KEY)) setError(errorMessage(cause));
      })
      .finally(() => {
        if (active) setLoading(false);
      });
    return () => {
      active = false;
      window.removeEventListener('bodeko:unauthorized', expire);
    };
  }, [logout, revision]);
  async function establish(token: string) {
    sessionStorage.setItem(SESSION_KEY, token);
    try {
      const currentUser = await authService.me();
      setUser(currentUser);
      setError('');
      return currentUser;
    } catch (cause) {
      logout();
      throw cause;
    }
  }
  return (
    <SessionContext.Provider
      value={{
        user,
        loading,
        error,
        establish,
        logout,
        retry: () => setRevision((value) => value + 1),
      }}
    >
      {children}
    </SessionContext.Provider>
  );
}
/** Permite consultar la sesión desde los componentes y las rutas protegidas. */
export function useSession(): SessionState {
  const value = useContext(SessionContext);
  if (!value) throw new Error('No se encontró el proveedor de sesión.');
  return value;
}
````

### frontend/src/context/StoreContext.tsx

````tsx
import { createContext, useCallback, useContext, type ReactNode } from 'react';
import { storeService, type PublicStore } from '@/features/stores';
import { useResource } from '@/core/hooks/useResource';
import { ErrorNotice, Loading } from '@/core/components';

const StoreContext = createContext<PublicStore | null>(null);
/** Busca la tienda usando el subdominio de la URL. */
export function StoreProvider({ subdomain, children }: { subdomain: string; children: ReactNode }) {
  const resource = useResource(useCallback(() => storeService.bySubdomain(subdomain), [subdomain]));
  if (resource.loading) return <Loading />;
  if (!resource.data)
    return (
      <ErrorNotice
        message={resource.error || 'No se encontró la tienda.'}
        retry={resource.reload}
      />
    );
  return <StoreContext.Provider value={resource.data}>{children}</StoreContext.Provider>;
}
/** Permite consultar los datos de la tienda actual. */
export function useStore(): PublicStore {
  const value = useContext(StoreContext);
  if (!value) throw new Error('No se encontró la tienda actual.');
  return value;
}
````

### frontend/src/core/components/ErrorBoundary.tsx

````tsx
import { Component, type ErrorInfo, type ReactNode } from 'react';
/** Contenido que se mostrará si no ocurre un error de pantalla. */
interface ErrorBoundaryProps {
  children: ReactNode;
}
/** Indica si ocurrió un error al mostrar la pantalla. */
interface ErrorBoundaryState {
  failed: boolean;
}
/** Muestra un mensaje de recuperación si falla la pantalla. */
export class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  state: ErrorBoundaryState = { failed: false };
  static getDerivedStateFromError(): ErrorBoundaryState {
    return { failed: true };
  }
  componentDidCatch(_error: Error, _info: ErrorInfo) {
    /* El mensaje de recuperación evita mostrar detalles internos. */
  }
  render() {
    return this.state.failed ? (
      <main className="main-container">
        <h1>No pudimos mostrar esta página</h1>
        <p>Recarga para volver a intentarlo.</p>
        <button className="button primary" onClick={() => window.location.reload()}>
          Recargar página
        </button>
      </main>
    ) : (
      this.props.children
    );
  }
}
````

### frontend/src/core/components/index.tsx

````tsx
import {
  useEffect,
  useId,
  useRef,
  useState,
  type ButtonHTMLAttributes,
  type InputHTMLAttributes,
  type ReactNode,
} from 'react';
import { AlertCircle, ImageOff, LoaderCircle, PackageOpen, X } from 'lucide-react';
import { permits, type CharacterRule } from '@/utils/validation';
import type { Option } from '@/core/types';
import { labels } from '@/utils/format';

/** Opciones del botón. pending bloquea el botón mientras se procesa la acción. */
export interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  pending?: boolean;
  variant?: 'primary' | 'secondary' | 'danger';
}
/** Botón reutilizable para toda la aplicación. */
export function Button({ pending, variant = 'primary', children, ...props }: ButtonProps) {
  return (
    <button
      {...props}
      className={`button ${variant} ${props.className || ''}`}
      disabled={props.disabled || pending}
      aria-busy={pending}
    >
      {pending && <LoaderCircle size={16} className="spin" />}
      {children}
    </button>
  );
}
/** Datos del campo: etiqueta, ejemplo, valor y mensaje de error. */
export interface FieldProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'onChange'> {
  label: string;
  example: string;
  error?: string;
  rule?: CharacterRule;
  onChange: (value: string) => void;
}
/** El ejemplo permanece visible aunque el usuario ya haya escrito. */
export function Field({ label, example, error, rule, onChange, ...props }: FieldProps) {
  const id = useId();
  return (
    <div className="field">
      <label htmlFor={id}>
        {label}
        {props.required && <span aria-hidden="true"> *</span>}
      </label>
      <input
        {...props}
        id={id}
        placeholder={props.placeholder || example}
        aria-invalid={!!error}
        aria-describedby={`${id}-help`}
        onChange={(event) => {
          if (permits(event.target.value, rule)) onChange(event.target.value);
        }}
      />
      <small id={`${id}-help`} className={error ? 'field-error' : ''}>
        {error || `Ejemplo: ${example}`}
      </small>
    </div>
  );
}
/** Selector con etiqueta explícita y orientación de uso. */
export interface SelectProps {
  label: string;
  value: string;
  options: Option[];
  onChange: (value: string) => void;
  hint?: string;
  required?: boolean;
}
/** Selector nativo, operable con teclado y lectores de pantalla. */
export function Select({
  label,
  value,
  options,
  onChange,
  hint = 'Selecciona una opción.',
  required,
}: SelectProps) {
  const id = useId();
  return (
    <div className="field">
      <label htmlFor={id}>{label}</label>
      <select
        id={id}
        value={value}
        required={required}
        onChange={(event) => onChange(event.target.value)}
        aria-describedby={`${id}-help`}
      >
        {options.map((item) => (
          <option key={item.value} value={item.value}>
            {item.label}
          </option>
        ))}
      </select>
      <small id={`${id}-help`}>{hint}</small>
    </div>
  );
}
/** Muestra el error hasta que se resuelva o cambie la pantalla. */
export function ErrorNotice({ message, retry }: { message: string; retry?: () => void }) {
  if (!message) return null;
  return (
    <div role="alert" className="notice error">
      <AlertCircle size={20} />
      <span>{message}</span>
      {retry && (
        <Button variant="secondary" onClick={retry}>
          Reintentar
        </Button>
      )}
    </div>
  );
}
/** Indicador accesible para cualquier consulta pendiente. */
export function Loading() {
  return (
    <div className="loading" role="status">
      <LoaderCircle className="spin" /> Cargando información…
    </div>
  );
}
/** Indica que no hay resultados para mostrar. */
export function Empty({ title, children }: { title: string; children?: ReactNode }) {
  return (
    <div className="empty">
      <PackageOpen size={38} />
      <h3>{title}</h3>
      {children}
    </div>
  );
}
/** Muestra el título de la página y sus botones de acción. */
export function PageHeader({
  title,
  description,
  actions,
}: {
  title: string;
  description?: string;
  actions?: ReactNode;
}) {
  return (
    <header className="page-heading">
      <div>
        <h1>{title}</h1>
        {description && <p>{description}</p>}
      </div>
      {actions}
    </header>
  );
}
/** Muestra el estado en español con su color correspondiente. */
export function Badge({ value }: { value: string }) {
  return <span className={`badge badge-${value.toLowerCase()}`}>{labels[value] || value}</span>;
}
/** Muestra la imagen o un icono si falta o no se puede cargar. */
export function ProductImage({ src, name }: { src: string | null; name: string }) {
  const [broken, setBroken] = useState(false);
  useEffect(() => {
    setBroken(false);
  }, [src]);
  return src && /^https?:\/\//i.test(src) && !broken ? (
    <img
      className="product-image"
      src={src}
      alt={name}
      loading="lazy"
      decoding="async"
      onError={() => setBroken(true)}
    />
  ) : (
    <div className="image-fallback" aria-label={`Sin imagen de ${name}`}>
      <ImageOff size={36} />
    </div>
  );
}
/** Permite cambiar de página. La primera página tiene el índice 0. */
export function Pagination({
  page,
  totalPages,
  onChange,
}: {
  page: number;
  totalPages: number;
  onChange: (page: number) => void;
}) {
  if (totalPages <= 1) return null;
  return (
    <nav className="pagination" aria-label="Paginación">
      <Button variant="secondary" disabled={page === 0} onClick={() => onChange(page - 1)}>
        Anterior
      </Button>
      <span>
        Página {page + 1} de {totalPages}
      </span>
      <Button
        variant="secondary"
        disabled={page >= totalPages - 1}
        onClick={() => onChange(page + 1)}
      >
        Siguiente
      </Button>
    </nav>
  );
}
/** Abre un diálogo y permite cerrarlo con la tecla Escape. */
export function Modal({
  title,
  onClose,
  children,
}: {
  title: string;
  onClose: () => void;
  children: ReactNode;
}) {
  const dialog = useRef<HTMLDialogElement>(null);
  const titleId = useId();
  useEffect(() => {
    const element = dialog.current;
    element?.showModal();
    return () => element?.close();
  }, []);
  return (
    <dialog
      ref={dialog}
      className="modal"
      aria-labelledby={titleId}
      onCancel={(event) => {
        event.preventDefault();
        onClose();
      }}
    >
      <div className="modal-title">
        <h2 id={titleId}>{title}</h2>
        <Button variant="secondary" aria-label="Cerrar diálogo" onClick={onClose}>
          <X size={18} />
        </Button>
      </div>
      {children}
    </dialog>
  );
}
````

### frontend/src/core/hooks/useAction.ts

````typescript
import { useRef, useState } from 'react';
import { toast } from 'sonner';
import { errorMessage } from '@/utils/errors';

/** Ejecuta una acción, evita envíos repetidos y muestra el resultado con Sonner. */
export function useAction() {
  const [pending, setPending] = useState(false);
  const [error, setError] = useState('');
  const busy = useRef(false);
  async function run<T>(operation: () => Promise<T>, success?: string): Promise<T | undefined> {
    if (busy.current) return undefined;
    busy.current = true;
    setPending(true);
    setError('');
    try {
      const result = await operation();
      if (success) toast.success(success);
      return result;
    } catch (cause) {
      const message = errorMessage(cause);
      setError(message);
      toast.error(message);
      return undefined;
    } finally {
      busy.current = false;
      setPending(false);
    }
  }
  return { run, pending, error };
}
````

### frontend/src/core/hooks/useResource.ts

````typescript
import { useCallback, useEffect, useRef, useState } from 'react';
import { errorMessage } from '@/utils/errors';

/** Carga datos y descarta respuestas anteriores. Recibe una función estable de carga. */
export function useResource<T>(loader: () => Promise<T>, enabled = true) {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(enabled);
  const [error, setError] = useState('');
  const [version, setVersion] = useState(0);
  const generation = useRef(0);
  const reload = useCallback(() => setVersion((value) => value + 1), []);
  useEffect(() => {
    const current = ++generation.current;
    setData(null);
    setError('');
    setLoading(enabled);
    if (!enabled) return;
    void loader()
      .then((result) => {
        if (generation.current === current) setData(result);
      })
      .catch((cause: unknown) => {
        if (generation.current === current) setError(errorMessage(cause));
      })
      .finally(() => {
        if (generation.current === current) setLoading(false);
      });
    return () => {
      generation.current++;
    };
  }, [loader, enabled, version]);
  return { data, loading, error, reload };
}
````

### frontend/src/core/layouts/AdminLayout.tsx

````tsx
import { Link, NavLink, Outlet, useParams } from 'react-router-dom';
import {
  Boxes,
  ChevronLeft,
  House,
  LayoutGrid,
  Package,
  Settings,
  ShoppingBag,
  Tag,
  ExternalLink,
} from 'lucide-react';
import { ErrorNotice, Loading } from '@/core/components';
import { useStores } from '@/features/stores';

/** Rutas del panel; se construyen con el identificador de la tienda autorizada. */
const sections = [
  { path: '', label: 'Resumen', icon: House },
  { path: '/productos', label: 'Productos', icon: Package },
  { path: '/categorias', label: 'Categorías', icon: LayoutGrid },
  { path: '/inventario', label: 'Inventario', icon: Boxes },
  { path: '/ofertas', label: 'Ofertas', icon: Tag },
  { path: '/ordenes', label: 'Órdenes', icon: ShoppingBag },
  { path: '/configuracion', label: 'Configuración', icon: Settings },
];
/** Verifica pertenencia usando el listado del propietario antes de mostrar la tienda. */
export function AdminLayout() {
  const { storeId = '' } = useParams();
  const stores = useStores(true);
  if (stores.loading) return <Loading />;
  if (stores.error) return <ErrorNotice message={stores.error} retry={stores.reload} />;
  const store = stores.data?.find((item) => item.id === storeId);
  if (!store)
    return <ErrorNotice message="No se encontró esta tienda entre las que administras." />;
  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        <Link className="back-link" to="/admin">
          <ChevronLeft size={16} />
          Mis tiendas
        </Link>
        <div className="sidebar-store">
          <span className="store-initial">{store.name.slice(0, 1).toUpperCase()}</span>
          <div>
            <strong>{store.name}</strong>
            <small>Administración</small>
          </div>
        </div>
        <nav aria-label="Administración de tienda">
          {sections.map(({ path, label, icon: Icon }) => (
            <NavLink key={path} end={!path} to={`/admin/${store.id}${path}`}>
              <Icon size={18} />
              {label}
            </NavLink>
          ))}
        </nav>
        <Link className="sidebar-preview" to={`/tienda/${store.subdomain}`}>
          <ExternalLink size={17} />
          Ver tienda
        </Link>
      </aside>
      <div className="admin-content">
        <Outlet context={store} />
      </div>
    </div>
  );
}
````

### frontend/src/core/layouts/ProtectedRoute.tsx

````tsx
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useSession } from '@/context/SessionContext';
import type { Role } from '@/core/types';
import { ErrorNotice, Loading } from '@/core/components';
/** Protege la página según la sesión y el rol. El backend también valida los permisos. */
export function ProtectedRoute({ role }: { role: Role }) {
  const session = useSession();
  const location = useLocation();
  if (session.loading) return <Loading />;
  if (session.error && !session.user)
    return <ErrorNotice message={session.error} retry={session.retry} />;
  if (!session.user)
    return (
      <Navigate
        to={`/ingresar?next=${encodeURIComponent(location.pathname + location.search)}`}
        replace
      />
    );
  if (session.user.role !== role)
    return <ErrorNotice message="Tu cuenta no tiene permisos para acceder a esta sección." />;
  return <Outlet />;
}
````

### frontend/src/core/layouts/ScrollToTop.tsx

````tsx
import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

/** Inicia cada nueva pantalla arriba; los cambios de filtros conservan el desplazamiento. */
export function ScrollToTop() {
  const { pathname } = useLocation();
  useEffect(() => {
    window.scrollTo({ top: 0, left: 0, behavior: 'instant' });
  }, [pathname]);
  return null;
}
````

### frontend/src/core/layouts/SiteLayout.tsx

````tsx
import { Link, Outlet, useLocation } from 'react-router-dom';
import { LogOut, Store } from 'lucide-react';
import { Button } from '@/core/components';
import { useSession } from '@/context/SessionContext';
import { APP_NAME } from '@/config';

/** Marco global con identidad de marca y navegación de sesión. */
export function SiteLayout() {
  const { user, logout } = useSession();
  const isLanding = useLocation().pathname === '/';
  return (
    <div className={isLanding ? 'landing-site' : undefined}>
      <a className="skip-link" href="#contenido">
        Ir al contenido principal
      </a>
      <header className="site-header">
        <div className="header-inner">
          <Link to="/" className="brand" aria-label={`${APP_NAME}, inicio`}>
            <Store size={27} />
            <span>
              {APP_NAME}
              <span className="brand-dot">.</span>
            </span>
          </Link>
          <nav aria-label="Navegación principal">
            {isLanding ? (
              <>
                <a href="#tiendas">Tiendas</a>
                <a href="#como-funciona">Cómo funciona</a>
              </>
            ) : (
              <Link to="/tiendas">Tiendas</Link>
            )}
            {user?.role === 'CUSTOMER' && <Link to="/cuenta/ordenes">Mis órdenes</Link>}
            {user?.role === 'ADMIN' && <Link to="/admin">Mi negocio</Link>}
          </nav>
          <div className="header-account">
            {user ? (
              <>
                <span className="user-name">{user.fullName}</span>
                <Button
                  variant="secondary"
                  aria-label="Cerrar sesión"
                  onClick={() => {
                    logout();
                    // Vuelve al inicio después de cerrar la sesión.
                    window.location.replace('/');
                  }}
                >
                  <LogOut size={16} />
                  <span className="desktop-label">Salir</span>
                </Button>
              </>
            ) : (
              <>
                <Link to="/ingresar">Iniciar sesión</Link>
                <Link className="button primary" to="/registro">
                  Crear cuenta
                </Link>
              </>
            )}
          </div>
        </div>
      </header>
      <main id="contenido" className="main-container">
        <Outlet />
      </main>
      <footer className="site-footer">
        <span className="brand">{APP_NAME}.</span>
        <span>Conectamos comercios. Simplificamos compras.</span>
        <span>Hecho para crecer contigo.</span>
      </footer>
    </div>
  );
}
````

### frontend/src/core/layouts/StoreLayout.tsx

````tsx
import { Link, Outlet, useParams } from 'react-router-dom';
import { ShoppingBag } from 'lucide-react';
import { StoreProvider, useStore } from '@/context/StoreContext';
import { CartProvider } from '@/context/CartContext';
import { useCart } from '@/features/cart';
import { useSession } from '@/context/SessionContext';

/** Barra compartida de una tienda, con contador sincronizado desde el carrito global. */
function StoreNavigation() {
  const store = useStore();
  const { cart } = useCart();
  const { user } = useSession();
  return (
    <>
      <div className="store-nav">
        <Link to={`/tienda/${store.subdomain}`}>{store.name}</Link>
        {user?.role === 'CUSTOMER' && (
          <Link className="cart-link" to={`/tienda/${store.subdomain}/carrito`}>
            <ShoppingBag size={18} />
            Carrito<span className="cart-count">{cart?.totalQuantity || 0}</span>
          </Link>
        )}
      </div>
      <Outlet />
    </>
  );
}
/** Carga el comercio y comparte el carrito entre sus rutas hijas. */
export function StoreLayout() {
  const { subdomain = '' } = useParams();
  const { user } = useSession();
  return (
    <StoreProvider key={subdomain} subdomain={subdomain}>
      <CartProvider key={user?.id || 'visitor'}>
        <StoreNavigation />
      </CartProvider>
    </StoreProvider>
  );
}
````

### frontend/src/core/types/index.ts

````typescript
/** Identificador UUID recibido de la API. */
export type Id = string;
/** Resultados de una página y datos para navegar entre páginas. */
export interface Page<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
/** Roles exactos de Spring Security. */
export type Role = 'ADMIN' | 'CUSTOMER';
/** Usuario autenticado; nunca contiene contraseña. */
export interface User {
  id: Id;
  fullName: string;
  email: string;
  role: Role;
  phoneNumber: string | null;
}
/** Opciones de los selectores de los formularios. */
export interface Option {
  value: string;
  label: string;
}
````

### frontend/src/features/auth/AuthPage.tsx

````tsx
import { Store } from 'lucide-react';
import { AuthForm } from './components/AuthForm';
import { APP_NAME } from '@/config';
/** Vista pública de acceso y registro, centrada y adaptable a dispositivos móviles. */
export function AuthPage({ register = false }: { register?: boolean }) {
  return (
    <div className="auth-layout">
      <div className="auth-copy">
        <span className="eyebrow">COMERCIO LOCAL, CONECTADO</span>
        <h1>
          Una cuenta.
          <br />
          Muchas posibilidades.
        </h1>
        <p>Descubre tiendas, encuentra lo que necesitas y lleva tu negocio al siguiente nivel.</p>
        <div className="auth-mark">
          <Store size={72} />
          <span>{APP_NAME}</span>
        </div>
      </div>
      <section className="card auth-card">
        <h2>{register ? 'Crea tu cuenta' : 'Te damos la bienvenida'}</h2>
        <p className="muted">
          {register
            ? 'Empieza a comprar o a gestionar tu tienda.'
            : 'Ingresa para continuar con tu cuenta.'}
        </p>
        <AuthForm key={register ? 'register' : 'login'} register={register} />
      </section>
    </div>
  );
}
````

### frontend/src/features/auth/components/AuthForm.tsx

````tsx
import { useState, type FormEvent } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { Button, ErrorNotice, Field, Select } from '@/core/components';
import { isDocument, isEmail, type DocumentType } from '@/utils/validation';
import { useAuth } from '../hooks/useAuth';

/** register indica si se muestra el registro o el inicio de sesión. */
export interface AuthFormProps {
  register: boolean;
}
/** Formulario compartido de inicio de sesión y registro. */
export function AuthForm({ register }: AuthFormProps) {
  const navigate = useNavigate();
  const [query] = useSearchParams();
  const auth = useAuth();
  const [owner, setOwner] = useState(false);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirm, setConfirm] = useState('');
  const [phone, setPhone] = useState('');
  const [documentType, setDocumentType] = useState('');
  const [documentNumber, setDocumentNumber] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const errors = {
    name:
      register && name.trim().length < 2 ? 'Ingresa tu nombre completo (mínimo 2 caracteres).' : '',
    email: !isEmail(email) ? 'Ingresa un correo válido.' : '',
    password:
      password.length < (register ? 8 : 1)
        ? register
          ? 'Usa al menos 8 caracteres.'
          : 'Ingresa tu contraseña.'
        : register && new TextEncoder().encode(password).length > 72
          ? 'La contraseña es demasiado larga. Usa una más corta.'
          : '',
    confirm: register && confirm !== password ? 'Las contraseñas no coinciden.' : '',
    phone:
      register && ((owner && !phone) || (phone && !/^\+[1-9]\d{7,14}$/.test(phone)))
        ? 'Usa el código de país: +51987654321.'
        : '',
    document:
      register && !owner && documentType && !isDocument(documentNumber, documentType)
        ? 'Revisa la longitud y el formato del documento.'
        : '',
  };
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (Object.values(errors).some(Boolean)) return;
    const body = {
      email: email.trim().toLowerCase(),
      password,
      fullName: name.trim(),
      ...(phone ? { phoneNumber: phone } : {}),
      ...(!owner && documentType
        ? { documentType: documentType as DocumentType, documentNumber }
        : {}),
    };
    const result = register ? await auth.register(body, owner) : await auth.login(body);
    if (result) {
      const next = query.get('next');
      navigate(
        next?.startsWith('/') && !next.startsWith('//')
          ? next
          : result.role === 'ADMIN'
            ? '/admin'
            : '/',
        { replace: true },
      );
    }
  }
  return (
    <form onSubmit={(event) => void submit(event)} noValidate className="form-stack">
      <ErrorNotice message={auth.error} />
      {register && (
        <>
          <Select
            label="Tipo de cuenta"
            value={owner ? 'owner' : 'customer'}
            onChange={(value) => setOwner(value === 'owner')}
            options={[
              { value: 'customer', label: 'Cliente: quiero comprar' },
              { value: 'owner', label: 'Propietario: quiero vender' },
            ]}
            hint="Ejemplo: Cliente para comprar en las tiendas."
          />
          <Field
            label="Nombre completo"
            example="María García López"
            value={name}
            onChange={setName}
            rule="name"
            maxLength={150}
            autoComplete="name"
            required
            error={submitted ? errors.name : ''}
          />
        </>
      )}
      <Field
        label="Correo electrónico"
        example="maria@correo.com"
        type="email"
        rule="email"
        value={email}
        onChange={setEmail}
        maxLength={255}
        autoComplete="email"
        required
        error={submitted ? errors.email : ''}
      />
      <Field
        label="Contraseña"
        example={
          register ? 'Una frase segura de al menos 8 caracteres' : 'No compartas tu contraseña con nadie'
        }
        type="password"
        value={password}
        onChange={setPassword}
        autoComplete={register ? 'new-password' : 'current-password'}
        required
        error={submitted ? errors.password : ''}
      />
      {register && (
        <>
          <Field
            label="Confirmar contraseña"
            example="Repite exactamente tu contraseña"
            type="password"
            value={confirm}
            onChange={setConfirm}
            autoComplete="new-password"
            required
            error={submitted ? errors.confirm : ''}
          />
          <Field
            label={`Teléfono${owner ? '' : ' (opcional)'}`}
            example="+51987654321"
            type="tel"
            rule="phone"
            value={phone}
            onChange={setPhone}
            maxLength={16}
            autoComplete="tel"
            required={owner}
            error={submitted ? errors.phone : ''}
          />
          {!owner && (
            <>
              <Select
                label="Documento (opcional)"
                value={documentType}
                onChange={(value) => {
                  setDocumentType(value);
                  setDocumentNumber('');
                }}
                options={[
                  { value: '', label: 'No agregar documento' },
                  { value: 'DNI', label: 'DNI' },
                  { value: 'RUC', label: 'RUC' },
                  { value: 'CE', label: 'Carné de extranjería' },
                  { value: 'PASSPORT', label: 'Pasaporte' },
                ]}
                hint="Ejemplo: DNI para un documento peruano."
              />
              {documentType && (
                <Field
                  label="Número de documento"
                  example={
                    documentType === 'DNI'
                      ? '12345678'
                      : documentType === 'RUC'
                        ? '20123456789'
                        : 'AB123456'
                  }
                  rule={['DNI', 'RUC'].includes(documentType) ? 'digits' : 'document'}
                  maxLength={documentType === 'DNI' ? 8 : documentType === 'RUC' ? 11 : 20}
                  value={documentNumber}
                  onChange={setDocumentNumber}
                  required
                  error={submitted ? errors.document : ''}
                />
              )}
            </>
          )}
        </>
      )}
      <Button type="submit" pending={auth.pending}>
        {register ? 'Crear cuenta' : 'Iniciar sesión'}
      </Button>
      <p className="muted">
        {register ? '¿Ya tienes cuenta? ' : '¿Aún no tienes cuenta? '}
        <Link
          to={`${register ? '/ingresar' : '/registro'}${query.toString() ? `?${query.toString()}` : ''}`}
        >
          {register ? 'Inicia sesión' : 'Regístrate'}
        </Link>
      </p>
    </form>
  );
}
````

### frontend/src/features/auth/hooks/useAuth.ts

````typescript
import { useAction } from '@/core/hooks/useAction';
import { useSession } from '@/context/SessionContext';
import { authService } from '../services/authService';
import type { Credentials, Registration } from '../types';
/** Inicia sesión o registra al usuario y carga sus datos. */
export function useAuth() {
  const action = useAction();
  const { establish } = useSession();
  return {
    ...action,
    login: (body: Credentials) =>
      action.run(async () => establish((await authService.login(body)).token), 'Sesión iniciada.'),
    register: (body: Registration, owner: boolean) =>
      action.run(
        async () => establish((await authService.register(body, owner)).token),
        'Tu cuenta fue creada correctamente.',
      ),
  };
}
````

### frontend/src/features/auth/index.ts

````typescript
export { AuthPage } from './AuthPage';
export type { Credentials, Registration } from './types';
````

### frontend/src/features/auth/services/authService.ts

````typescript
import { api } from '@/config';
import type { User } from '@/core/types';
import type { Credentials, Registration, TokenResponse } from '../types';
/** Peticiones para iniciar sesión, registrarse y consultar la cuenta. */
export const authService = {
  login: async (body: Credentials) => (await api.post<TokenResponse>('/auth/login', body)).data,
  register: async (body: Registration, owner: boolean) =>
    (
      await api.post<TokenResponse>(
        owner ? '/auth/register-owner' : '/auth/register-customer',
        body,
      )
    ).data,
  me: async () => (await api.get<User>('/auth/me')).data,
};
````

### frontend/src/features/auth/types/index.ts

````typescript
import type { DocumentType } from '@/utils/validation';
/** Datos de autenticación; contraseña transmitida únicamente al servidor por HTTPS. */
export interface Credentials {
  email: string;
  password: string;
}
/** Registro de cliente o propietario; confirmación solo existe en la vista. */
export interface Registration extends Credentials {
  fullName: string;
  phoneNumber?: string;
  documentType?: DocumentType;
  documentNumber?: string;
}
/** Token recibido al iniciar sesión o crear una cuenta. */
export interface TokenResponse {
  token: string;
}
````

### frontend/src/features/cart/CartPage.tsx

````tsx
import { Link } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';
import { Empty, ErrorNotice, Loading, PageHeader } from '@/core/components';
import { useStore } from '@/context/StoreContext';
import { money } from '@/utils/format';
import { useCart } from './hooks/useCart';
import { CartLine } from './components/CartLine';
/** Muestra el carrito guardado y permite continuar con la compra. */
export function CartPage() {
  const store = useStore();
  const state = useCart();
  return (
    <>
      <PageHeader title="Tu carrito" description={`Productos de ${store.name}`} />
      <ErrorNotice message={state.error} retry={state.reload} />
      {state.loading ? (
        <Loading />
      ) : !state.cart?.items.length ? (
        <Empty title="Tu carrito está esperando tus favoritos">
          <Link className="button primary" to={`/tienda/${store.subdomain}`}>
            Explorar productos
          </Link>
        </Empty>
      ) : (
        <div className="checkout-grid">
          <section className="card">
            {state.cart.items.map((item) => (
              <CartLine
                key={`${item.productId}-${item.quantity}`}
                item={item}
                currency={store.currency}
              />
            ))}
          </section>
          <aside className="card summary">
            <h2>Resumen del carrito</h2>
            <p>
              <span>Unidades</span>
              <strong>{state.cart.totalQuantity}</strong>
            </p>
            <p>
              <span>Subtotal de productos</span>
              <strong>{money(state.cart.subtotal, store.currency)}</strong>
            </p>
            <small>El IGV y el total definitivo se calculan al confirmar la orden.</small>
            <Link className="button primary" to={`/tienda/${store.subdomain}/checkout`}>
              Continuar al checkout <ArrowRight size={16} />
            </Link>
            <Link to={`/tienda/${store.subdomain}`}>Seguir comprando</Link>
          </aside>
        </div>
      )}
    </>
  );
}
````

### frontend/src/features/cart/components/CartLine.tsx

````tsx
import { useState } from 'react';
import { Trash2 } from 'lucide-react';
import { Button, Field, ProductImage } from '@/core/components';
import { useCart } from '../hooks/useCart';
import type { CartItem } from '../types';
import { money } from '@/utils/format';
import { isInteger } from '@/utils/validation';
/** Producto del carrito y moneda de la tienda. */
export interface CartLineProps {
  item: CartItem;
  currency: string;
}
/** Cantidad editable con guardado explícito; evita envíos parciales al escribir. */
export function CartLine({ item, currency }: CartLineProps) {
  const cart = useCart();
  const [quantity, setQuantity] = useState(String(item.quantity));
  return (
    <article className="cart-line">
      <ProductImage src={item.imageUrl} name={item.name} />
      <div className="cart-line-content">
        <h3>{item.name}</h3>
        <p>
          {money(item.chargedUnitPrice, currency)} por unidad{' '}
          {item.chargedUnitPrice < item.listUnitPrice && (
            <del>{money(item.listUnitPrice, currency)}</del>
          )}
        </p>
        <form
          className="inline-form"
          onSubmit={(event) => {
            event.preventDefault();
            if (isInteger(quantity, 1)) void cart.update(item.productId, Number(quantity));
          }}
        >
          <Field
            label="Cantidad"
            example="2"
            rule="digits"
            inputMode="numeric"
            maxLength={10}
            value={quantity}
            onChange={setQuantity}
            required
            error={!isInteger(quantity, 1) ? 'Ingresa un entero mayor que cero.' : ''}
          />
          <Button
            type="submit"
            variant="secondary"
            pending={cart.pending}
            disabled={!isInteger(quantity, 1) || Number(quantity) === item.quantity}
          >
            Actualizar
          </Button>
        </form>
      </div>
      <div className="cart-line-total">
        <strong>{money(item.itemSubtotal, currency)}</strong>
        <Button
          variant="secondary"
          aria-label={`Quitar ${item.name}`}
          disabled={cart.pending}
          onClick={() => void cart.remove(item.productId)}
        >
          <Trash2 size={16} />
          Quitar
        </Button>
      </div>
    </article>
  );
}
````

### frontend/src/features/cart/hooks/useCart.ts

````typescript
/** Comparte el carrito con los demás módulos mediante el contexto de React. */
export { useCart } from '@/context/CartContext';
````

### frontend/src/features/cart/index.ts

````typescript
export { CartPage } from './CartPage';
export { useCart } from './hooks/useCart';
````

### frontend/src/features/cart/services/cartService.ts

````typescript
import { api } from '@/config';
import type { Cart } from '../types';
/** El carrito vive en PostgreSQL para recuperarlo al volver a iniciar sesión. */
export const cartService = {
  get: async (storeId: string) => (await api.get<Cart>(`/customer/stores/${storeId}/cart`)).data,
  add: async (storeId: string, productId: string, quantity: number) =>
    (await api.post<Cart>(`/customer/stores/${storeId}/cart/items`, { productId, quantity })).data,
  update: async (storeId: string, productId: string, quantity: number) =>
    (await api.patch<Cart>(`/customer/stores/${storeId}/cart/items/${productId}`, { quantity }))
      .data,
  remove: async (storeId: string, productId: string) =>
    (await api.delete<Cart>(`/customer/stores/${storeId}/cart/items/${productId}`)).data,
};
````

### frontend/src/features/cart/types/index.ts

````typescript
/** Producto del carrito con los precios calculados por el servidor. */
export interface CartItem {
  productId: string;
  name: string;
  imageUrl: string | null;
  quantity: number;
  listUnitPrice: number;
  chargedUnitPrice: number;
  itemSubtotal: number;
}
/** Carrito abierto por cliente y tienda; id es null cuando aún está vacío. */
export interface Cart {
  id: string | null;
  storeId: string;
  items: CartItem[];
  totalQuantity: number;
  subtotal: number;
}
````

### frontend/src/features/catalog/CatalogPage.tsx

````tsx
import { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Search } from 'lucide-react';
import {
  Button,
  Empty,
  ErrorNotice,
  Field,
  Loading,
  PageHeader,
  Pagination,
  Select,
} from '@/core/components';
import { useStore } from '@/context/StoreContext';
import { useCategories } from '@/features/categories';
import { useCatalog } from './hooks/useCatalog';
import { ProductCard } from './components/ProductCard';
/** Muestra el catálogo y guarda los filtros en la URL. */
export function CatalogPage() {
  const store = useStore();
  const [query, setQuery] = useSearchParams();
  const [draftSearch, setDraftSearch] = useState(query.get('buscar') || '');
  const search = query.get('buscar') || '';
  const categoryId = query.get('categoria') || '';
  const inStock = query.get('stock') === 'si';
  const page = Math.max(0, Math.floor(Number(query.get('pagina')) || 0));
  const resource = useCatalog(store.id, { search, categoryId, inStock, page });
  const categories = useCategories(store.id, true);
  function filter(key: string, value: string) {
    const next = new URLSearchParams(query);
    value ? next.set(key, value) : next.delete(key);
    if (key !== 'pagina') next.delete('pagina');
    setQuery(next);
  }
  return (
    <>
      <PageHeader title={store.name} description="Descubre productos elegidos para tu día a día." />
      <div className="card filters">
        <form
          className="search-form"
          onSubmit={(event) => {
            event.preventDefault();
            filter('buscar', draftSearch.trim());
          }}
        >
          <Field
            label="Buscar en la tienda"
            example="Café"
            maxLength={200}
            value={draftSearch}
            onChange={setDraftSearch}
          />
          <Button type="submit" variant="secondary" aria-label="Buscar productos">
            <Search size={18} />
          </Button>
        </form>
        <Select
          label="Categoría"
          value={categoryId}
          onChange={(value) => filter('categoria', value)}
          options={[
            { value: '', label: 'Todas las categorías' },
            ...(categories.data || []).map((item) => ({ value: item.id, label: item.name })),
          ]}
        />
        <label className="checkbox">
          <input
            type="checkbox"
            checked={inStock}
            onChange={(event) => filter('stock', event.target.checked ? 'si' : '')}
          />
          Solo en stock
        </label>
      </div>
      <ErrorNotice message={categories.error} retry={categories.reload} />
      <ErrorNotice message={resource.error} retry={resource.reload} />
      {resource.loading ? (
        <Loading />
      ) : resource.data?.content.length === 0 ? (
        <Empty title="No encontramos productos">
          <p>Prueba otra búsqueda o cambia los filtros.</p>
        </Empty>
      ) : (
        <>
          <p className="muted">{resource.data?.totalElements ?? 0} productos</p>
          <div className="catalog-grid">
            {resource.data?.content.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
          <Pagination
            page={page}
            totalPages={resource.data?.totalPages || 0}
            onChange={(value) => filter('pagina', String(value))}
          />
        </>
      )}
    </>
  );
}
````

### frontend/src/features/catalog/ProductDetailPage.tsx

````tsx
import { useCallback, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { ArrowLeft, ShieldCheck } from 'lucide-react';
import { Button, ErrorNotice, Field, Loading, ProductImage } from '@/core/components';
import { useResource } from '@/core/hooks/useResource';
import { useSession } from '@/context/SessionContext';
import { useStore } from '@/context/StoreContext';
import { useCart } from '@/features/cart';
import { money } from '@/utils/format';
import { isInteger } from '@/utils/validation';
import { catalogService } from './services/catalogService';
/** Consulta el detalle de un producto usando su URL. */
export function ProductDetailPage() {
  const { productId = '' } = useParams();
  const store = useStore();
  const cart = useCart();
  const { user } = useSession();
  const [quantity, setQuantity] = useState('1');
  const resource = useResource(
    useCallback(() => catalogService.detail(store.id, productId), [store.id, productId]),
  );
  if (resource.loading) return <Loading />;
  if (!resource.data) return <ErrorNotice message={resource.error} retry={resource.reload} />;
  const product = resource.data;
  return (
    <>
      <Link className="back-link" to={`/tienda/${store.subdomain}`}>
        <ArrowLeft size={16} />
        Volver al catálogo
      </Link>
      <div className="product-detail">
        <div className="detail-image">
          <ProductImage src={product.imageUrl} name={product.name} />
        </div>
        <section className="card detail-content">
          <span className="eyebrow">{store.name}</span>
          <h1>{product.name}</h1>
          <div className="price">
            <strong>{money(product.offerPrice ?? product.basePrice, store.currency)}</strong>
            {product.offerPrice !== null && <del>{money(product.basePrice, store.currency)}</del>}
          </div>
          <span className={`badge ${product.inStock ? 'badge-active' : ''}`}>
            {product.inStock ? 'En stock' : 'Agotado'}
          </span>
          <p className="description">
            {product.description || 'Este producto todavía no tiene descripción.'}
          </p>
          <Field
            label="Cantidad"
            example="2"
            rule="digits"
            inputMode="numeric"
            maxLength={10}
            value={quantity}
            onChange={setQuantity}
            error={!isInteger(quantity, 1) ? 'Ingresa un entero mayor que cero.' : ''}
          />
          {!user ? (
            <Link
              className="button primary"
              to={`/ingresar?next=${encodeURIComponent(`/tienda/${store.subdomain}/producto/${product.id}`)}`}
            >
              Inicia sesión para comprar
            </Link>
          ) : (
            user.role === 'CUSTOMER' && (
              <Button
                pending={cart.pending}
                disabled={!product.inStock || !isInteger(quantity, 1) || cart.loading}
                onClick={() => void cart.add(product.id, Number(quantity))}
              >
                Agregar al carrito
              </Button>
            )
          )}
          <ErrorNotice message={cart.error} />
          <p className="trust">
            <ShieldCheck size={18} />
            Los importes finales se confirman en tu orden.
          </p>
        </section>
      </div>
    </>
  );
}
````

### frontend/src/features/catalog/components/ProductCard.tsx

````tsx
import { Link, useLocation } from 'react-router-dom';
import { Plus } from 'lucide-react';
import { Button, ProductImage } from '@/core/components';
import { useSession } from '@/context/SessionContext';
import { useStore } from '@/context/StoreContext';
import { useCart } from '@/features/cart';
import { money } from '@/utils/format';
import type { CatalogProduct } from '../types';
/** Producto del catálogo con su precio actual. */
export interface ProductCardProps {
  product: CatalogProduct;
}
/** Tarjeta con enlace al detalle y acción de compra según permisos y disponibilidad. */
export function ProductCard({ product }: ProductCardProps) {
  const store = useStore();
  const cart = useCart();
  const { user } = useSession();
  const location = useLocation();
  const destination = `/tienda/${store.subdomain}/producto/${product.id}`;
  return (
    <article className="card catalog-card">
      <Link className="catalog-image" to={destination}>
        <ProductImage src={product.imageUrl} name={product.name} />
        {product.offerPrice !== null && <span className="sale-label">En oferta</span>}
      </Link>
      <div className="catalog-card-body">
        <span className={`availability${product.inStock ? '' : ' availability-out-of-stock'}`}>
          {product.inStock ? 'Disponible' : 'Agotado'}
        </span>
        <h3>
          <Link to={destination}>{product.name}</Link>
        </h3>
        <div className="price">
          <strong>{money(product.offerPrice ?? product.basePrice, store.currency)}</strong>
          {product.offerPrice !== null && <del>{money(product.basePrice, store.currency)}</del>}
        </div>
        {!user ? (
          <Link
            className="button secondary"
            to={`/ingresar?next=${encodeURIComponent(location.pathname)}`}
          >
            Inicia sesión para comprar
          </Link>
        ) : user.role === 'CUSTOMER' ? (
          <Button
            variant="secondary"
            disabled={!product.inStock || cart.pending || cart.loading}
            onClick={() => void cart.add(product.id)}
          >
            <Plus size={16} />
            Agregar al carrito
          </Button>
        ) : (
          <Link className="button secondary" to={destination}>
            Ver producto
          </Link>
        )}
      </div>
    </article>
  );
}
````

### frontend/src/features/catalog/hooks/useCatalog.ts

````typescript
import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { catalogService } from '../services/catalogService';
import type { CatalogFilters } from '../types';
/** Carga los productos según los filtros y la página elegida. */
export function useCatalog(storeId: string, filters: CatalogFilters) {
  const { search, categoryId, inStock, page } = filters;
  return useResource(
    useCallback(
      () => catalogService.search(storeId, { search, categoryId, inStock, page }),
      [storeId, search, categoryId, inStock, page],
    ),
  );
}
````

### frontend/src/features/catalog/index.ts

````typescript
export { CatalogPage } from './CatalogPage';
export { ProductDetailPage } from './ProductDetailPage';
````

### frontend/src/features/catalog/services/catalogService.ts

````typescript
import { api } from '@/config';
import type { Page } from '@/core/types';
import type { CatalogFilters, CatalogProduct } from '../types';
/** Catálogo real con búsqueda, filtros y paginación en el servidor. */
export const catalogService = {
  search: async (storeId: string, filters: CatalogFilters) =>
    (
      await api.get<Page<CatalogProduct>>(`/public/stores/${storeId}/products`, {
        params: {
          search: filters.search || undefined,
          categoryId: filters.categoryId || undefined,
          inStock: filters.inStock || undefined,
          page: filters.page,
          size: 12,
        },
      })
    ).data,
  detail: async (storeId: string, productId: string) =>
    (await api.get<CatalogProduct>(`/public/stores/${storeId}/products/${productId}`)).data,
};
````

### frontend/src/features/catalog/types/index.ts

````typescript
/** Producto activo del catálogo con su oferta y disponibilidad actuales. */
export interface CatalogProduct {
  id: string;
  categoryId: string | null;
  name: string;
  description: string | null;
  imageUrl: string | null;
  basePrice: number;
  offerPrice: number | null;
  inStock: boolean;
}
/** Filtros y página enviados al catálogo público. */
export interface CatalogFilters {
  search: string;
  categoryId: string;
  inStock: boolean;
  page: number;
}
````

### frontend/src/features/categories/CategoriesPage.tsx

````tsx
import { useState } from 'react';
import { Pencil, Plus, Trash2 } from 'lucide-react';
import { useOutletContext } from 'react-router-dom';
import { Button, Empty, ErrorNotice, Loading, Modal, PageHeader } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import type { Store } from '@/features/stores';
import { useCategories } from './hooks/useCategories';
import { categoryService } from './services/categoryService';
import { CategoryForm } from './components/CategoryForm';
import type { Category } from './types';
/** Permite crear, editar y eliminar categorías de la tienda. */
export function CategoriesPage() {
  const store = useOutletContext<Store>();
  const resource = useCategories(store.id);
  const action = useAction();
  const [editor, setEditor] = useState<Category | 'new' | null>(null);
  const [deleting, setDeleting] = useState<Category | null>(null);
  return (
    <>
      <PageHeader
        title="Categorías"
        description="Organiza tu catálogo para facilitar cada compra."
        actions={
          <Button onClick={() => setEditor('new')}>
            <Plus size={16} />
            Agregar categoría
          </Button>
        }
      />
      <ErrorNotice message={resource.error} retry={resource.reload} />
      <ErrorNotice message={action.error} />
      {resource.loading ? (
        <Loading />
      ) : !resource.data?.length ? (
        <Empty title="Tu catálogo empieza con una categoría" />
      ) : (
        <div className="card table-wrap">
          <table>
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {resource.data.map((category) => (
                <tr key={category.id}>
                  <td>{category.name}</td>
                  <td className="actions">
                    <Button
                      variant="secondary"
                      aria-label={`Editar ${category.name}`}
                      onClick={() => setEditor(category)}
                    >
                      <Pencil size={16} />
                    </Button>
                    <Button
                      variant="secondary"
                      aria-label={`Eliminar ${category.name}`}
                      onClick={() => setDeleting(category)}
                    >
                      <Trash2 size={16} />
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      {editor && (
        <Modal
          title={editor === 'new' ? 'Nueva categoría' : 'Editar categoría'}
          onClose={() => setEditor(null)}
        >
          <CategoryForm
            storeId={store.id}
            category={editor === 'new' ? undefined : editor}
            onSaved={() => {
              setEditor(null);
              resource.reload();
            }}
          />
        </Modal>
      )}
      {deleting && (
        <Modal title="Eliminar categoría" onClose={() => setDeleting(null)}>
          <p>
            ¿Eliminar «{deleting.name}»? Si tiene productos asociados, primero tendrás que
            reasignarlos.
          </p>
          <Button
            variant="danger"
            pending={action.pending}
            onClick={() => {
              void action
                .run(() => categoryService.remove(store.id, deleting.id), 'Categoría eliminada.')
                .then((done) => {
                  if (done) {
                    setDeleting(null);
                    resource.reload();
                  }
                });
            }}
          >
            Eliminar categoría
          </Button>
        </Modal>
      )}
    </>
  );
}
````

### frontend/src/features/categories/components/CategoryForm.tsx

````tsx
import { useState, type FormEvent } from 'react';
import { Button, ErrorNotice, Field } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { categoryService } from '../services/categoryService';
import type { Category } from '../types';
/** Datos del editor. Sin category se crea una categoría nueva. */
export interface CategoryFormProps {
  storeId: string;
  category?: Category;
  onSaved: () => void;
}
/** Editor de nombre con límite de 100 caracteres definido en PostgreSQL. */
export function CategoryForm({ storeId, category, onSaved }: CategoryFormProps) {
  const [name, setName] = useState(category?.name || '');
  const [submitted, setSubmitted] = useState(false);
  const action = useAction();
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (!name.trim()) return;
    const result = await action.run(
      () => categoryService.save(storeId, name.trim(), category?.id),
      'Categoría guardada.',
    );
    if (result) onSaved();
  }
  return (
    <form noValidate className="form-stack" onSubmit={(event) => void submit(event)}>
      <ErrorNotice message={action.error} />
      <Field
        label="Nombre de categoría"
        example="Abarrotes"
        value={name}
        onChange={setName}
        maxLength={100}
        required
        error={submitted && !name.trim() ? 'Ingresa un nombre de categoría.' : ''}
      />
      <Button type="submit" pending={action.pending}>
        Guardar categoría
      </Button>
    </form>
  );
}
````

### frontend/src/features/categories/hooks/useCategories.ts

````typescript
import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { categoryService } from '../services/categoryService';
/** Carga categorías según el permiso requerido por la pantalla. */
export function useCategories(storeId: string, publicRead = false) {
  return useResource(
    useCallback(() => categoryService.list(storeId, publicRead), [storeId, publicRead]),
  );
}
````

### frontend/src/features/categories/index.ts

````typescript
export { CategoriesPage } from './CategoriesPage';
export { useCategories } from './hooks/useCategories';
export type { Category } from './types';
````

### frontend/src/features/categories/services/categoryService.ts

````typescript
import { api } from '@/config';
import type { Category } from '../types';
/** Operaciones reales de categorías con alcance de tienda. */
export const categoryService = {
  list: async (storeId: string, publicRead = false) =>
    (await api.get<Category[]>(`${publicRead ? '/public' : ''}/stores/${storeId}/categories`)).data,
  save: async (storeId: string, name: string, id?: string) =>
    id
      ? (await api.put<Category>(`/stores/${storeId}/categories/${id}`, { name })).data
      : (await api.post<Category>(`/stores/${storeId}/categories`, { name })).data,
  remove: async (storeId: string, id: string) => {
    await api.delete(`/stores/${storeId}/categories/${id}`);
    return true;
  },
};
````

### frontend/src/features/categories/types/index.ts

````typescript
/** Categoría de producto perteneciente a una única tienda. */
export interface Category {
  id: string;
  name: string;
}
````

### frontend/src/features/checkout/CheckoutPage.tsx

````tsx
import { Link, useNavigate } from 'react-router-dom';
import { Empty, ErrorNotice, Loading, PageHeader } from '@/core/components';
import { useCart } from '@/features/cart';
import { useStore } from '@/context/StoreContext';
import { money } from '@/utils/format';
import { CheckoutForm } from './components/CheckoutForm';
/** Muestra el resumen y crea la orden antes de iniciar el pago. */
export function CheckoutPage() {
  const store = useStore();
  const cart = useCart();
  const navigate = useNavigate();
  return (
    <>
      <PageHeader
        title="Finaliza tu compra"
        description="1. Revisa tu carrito   /   2. Confirma tus datos   /   3. Realiza el pago"
      />
      <ErrorNotice message={cart.error} retry={cart.reload} />
      {cart.loading ? (
        <Loading />
      ) : !cart.cart?.items.length ? (
        <Empty title="Necesitas productos para continuar">
          <Link to={`/tienda/${store.subdomain}`}>Volver al catálogo</Link>
        </Empty>
      ) : (
        <div className="checkout-grid">
          <CheckoutForm
            storeId={store.id}
            onCreated={(order) => {
              cart.reload();
              navigate(`/tienda/${store.subdomain}/pago/${order.id}`, { replace: true });
            }}
          />
          <aside className="card summary">
            <h2>Tu compra</h2>
            {cart.cart.items.map((item) => (
              <p key={item.productId}>
                <span>
                  {item.quantity} × {item.name}
                </span>
                <strong>{money(item.itemSubtotal, store.currency)}</strong>
              </p>
            ))}
            <hr />
            <p>
              <span>Subtotal</span>
              <strong>{money(cart.cart.subtotal, store.currency)}</strong>
            </p>
            <small>
              El IGV aplica según cada producto. Verás el desglose definitivo antes de pagar.
            </small>
            <Link to={`/tienda/${store.subdomain}/carrito`}>Editar carrito</Link>
          </aside>
        </div>
      )}
    </>
  );
}
````

### frontend/src/features/checkout/components/CheckoutForm.tsx

````tsx
import { useState, type FormEvent } from 'react';
import { Button, ErrorNotice, Field, Select } from '@/core/components';
import { isDocument } from '@/utils/validation';
import type { Order, InvoiceType } from '@/features/orders';
import { useCheckout } from '../hooks/useCheckout';
/** onCreated recibe la orden creada para continuar con el pago. */
export interface CheckoutFormProps {
  storeId: string;
  onCreated: (order: Order) => void;
}
/** Datos fiscales con DNI para boleta y RUC/razón social para factura. */
export function CheckoutForm({ storeId, onCreated }: CheckoutFormProps) {
  const [invoiceType, setInvoiceType] = useState<InvoiceType>('RECEIPT');
  const [document, setDocument] = useState('');
  const [businessName, setBusinessName] = useState('');
  const [address, setAddress] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const checkout = useCheckout(storeId);
  const invoice = invoiceType === 'INVOICE';
  const documentError = !isDocument(document, invoice ? 'RUC' : 'DNI')
    ? `Ingresa ${invoice ? 'un RUC de 11' : 'un DNI de 8'} dígitos.`
    : '';
  const businessError =
    invoice && !businessName.trim() ? 'Ingresa la razón social de la empresa.' : '';
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (documentError || businessError) return;
    const result = await checkout.create({
      invoiceType,
      documentType: invoice ? 'RUC' : 'DNI',
      documentNumber: document,
      businessName: invoice ? businessName.trim() : null,
      billingAddress: address.trim() || null,
    });
    if (result) onCreated(result);
  }
  return (
    <form noValidate className="card form-stack" onSubmit={(event) => void submit(event)}>
      <h2>Datos del comprobante</h2>
      <ErrorNotice message={checkout.error} />
      <Select
        label="Tipo de comprobante"
        value={invoiceType}
        onChange={(value) => {
          setInvoiceType(value as InvoiceType);
          setDocument('');
        }}
        options={[
          { value: 'RECEIPT', label: 'Boleta de venta' },
          { value: 'INVOICE', label: 'Factura' },
        ]}
        hint="Ejemplo: Factura si compras a nombre de una empresa."
      />
      <Field
        label={invoice ? 'RUC' : 'DNI'}
        example={invoice ? '20123456789' : '12345678'}
        rule="digits"
        inputMode="numeric"
        maxLength={invoice ? 11 : 8}
        value={document}
        onChange={setDocument}
        required
        error={submitted ? documentError : ''}
      />
      {invoice && (
        <Field
          label="Razón social"
          example="Comercial María S.A.C."
          maxLength={200}
          value={businessName}
          onChange={setBusinessName}
          required
          error={submitted ? businessError : ''}
        />
      )}
      <Field
        label="Dirección fiscal (opcional)"
        example="Av. Arequipa 123, Lima"
        maxLength={300}
        value={address}
        onChange={setAddress}
      />
      <p className="muted">
        Al confirmar se crea tu orden y se reserva el stock. Los precios y el IGV quedarán fijados
        en el comprobante.
      </p>
      <Button type="submit" pending={checkout.pending}>
        Confirmar orden y continuar
      </Button>
    </form>
  );
}
````

### frontend/src/features/checkout/hooks/useCheckout.ts

````typescript
import { useAction } from '@/core/hooks/useAction';
import { checkoutService } from '../services/checkoutService';
import type { CheckoutInput } from '../types';
/** Coordina confirmación de la orden y manejo de errores de stock/datos fiscales. */
export function useCheckout(storeId: string) {
  const action = useAction();
  return {
    ...action,
    create: (body: CheckoutInput) =>
      action.run(
        () => checkoutService.create(storeId, body),
        'Orden creada. Continúa con el pago.',
      ),
  };
}
````

### frontend/src/features/checkout/index.ts

````typescript
export { CheckoutPage } from './CheckoutPage';
````

### frontend/src/features/checkout/services/checkoutService.ts

````typescript
import { api } from '@/config';
import type { Order } from '@/features/orders';
import type { CheckoutInput } from '../types';
/** Crea la orden, fija los precios y descuenta stock en una sola operación del servidor. */
export const checkoutService = {
  create: async (storeId: string, body: CheckoutInput) =>
    (await api.post<Order>(`/customer/stores/${storeId}/orders`, body)).data,
  get: async (storeId: string, orderId: string) =>
    (await api.get<Order>(`/customer/stores/${storeId}/orders/${orderId}`)).data,
};
````

### frontend/src/features/checkout/types/index.ts

````typescript
import type { InvoiceType } from '@/features/orders';
/** Datos fiscales enviados al confirmar la orden; nunca incluye precios del cliente. */
export interface CheckoutInput {
  invoiceType: InvoiceType;
  documentType: 'DNI' | 'RUC';
  documentNumber: string;
  businessName: string | null;
  billingAddress: string | null;
}
````

### frontend/src/features/customer-orders/AccountProfilePage.tsx

````tsx
import { BadgeCheck, Mail, Phone, UserRound } from 'lucide-react';
import { useSession } from '@/context/SessionContext';

/** Muestra los datos guardados de la cuenta, sin permitir editarlos. */
export function AccountProfilePage() {
  const { user } = useSession();
  return (
    <>
      <header className="account-page-heading">
        <div>
          <span className="eyebrow">MI CUENTA</span>
          <h1>Mis datos personales</h1>
          <p>Información asociada a tu cuenta de Bodeko.</p>
        </div>
      </header>
      <section className="account-section-card account-profile">
        <h2>Datos de tu cuenta</h2>
        <dl>
          <div>
            <dt>
              <UserRound size={16} />
              Nombre completo
            </dt>
            <dd>{user?.fullName}</dd>
          </div>
          <div>
            <dt>
              <Mail size={16} />
              Correo electrónico
            </dt>
            <dd>{user?.email}</dd>
          </div>
          <div>
            <dt>
              <Phone size={16} />
              Teléfono / celular
            </dt>
            <dd>{user?.phoneNumber || 'No registrado'}</dd>
          </div>
          <div>
            <dt>
              <BadgeCheck size={16} />
              Tipo de cuenta
            </dt>
            <dd>Cliente</dd>
          </div>
        </dl>
        <p className="muted">
          Estos datos corresponden a tu registro. Puedes consultar y gestionar tus compras desde
          “Mis órdenes”.
        </p>
      </section>
    </>
  );
}
````

### frontend/src/features/customer-orders/CustomerOrderDetailPage.tsx

````tsx
import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { ArrowLeft, CreditCard, RefreshCw, Store, X } from 'lucide-react';
import { Button, ErrorNotice, Loading } from '@/core/components';
import { OrderDetails } from '@/features/orders';
import { useCustomerOrder } from './hooks/useCustomerOrder';
import { CancelOrderDialog } from './components/CancelOrderDialog';

/** Muestra los datos guardados de la orden y las acciones permitidas al cliente. */
export function CustomerOrderDetailPage() {
  const { orderId = '' } = useParams();
  const resource = useCustomerOrder(orderId);
  const [cancelling, setCancelling] = useState(false);
  if (resource.loading) return <Loading />;
  if (!resource.data)
    return (
      <>
        <Link className="back-link" to="/cuenta/ordenes">
          <ArrowLeft size={15} />
          Mis órdenes
        </Link>
        <ErrorNotice message={resource.error} retry={resource.reload} />
      </>
    );
  const { order, storeName, storeSubdomain, currency } = resource.data;
  const canCancel = order.status === 'PENDING' || order.status === 'PAID';
  return (
    <>
      <Link className="back-link" to="/cuenta/ordenes">
        <ArrowLeft size={15} />
        Volver a mis órdenes
      </Link>
      <header className="account-page-heading">
        <div>
          <span className="eyebrow">DETALLE DE COMPRA</span>
          <h1>Tu orden, en detalle</h1>
          <p>Consulta tus productos, comprobante e importe final.</p>
        </div>
        <Button variant="secondary" onClick={resource.reload}>
          <RefreshCw size={15} />
          Actualizar
        </Button>
      </header>
      <section className="account-section-card customer-order-detail">
        <div className="order-detail-store">
          <Store size={20} />
          <Link to={`/tienda/${storeSubdomain}`}>{storeName}</Link>
        </div>
        <OrderDetails order={order} currency={currency} />
      </section>
      <section className="account-section-card customer-order-management">
        <div>
          <h2>{order.status === 'CANCELLED' ? 'Orden cancelada' : 'Gestiona tu compra'}</h2>
          <p>
            {order.status === 'PENDING'
              ? 'Tu orden está pendiente de pago. Puedes continuar con el pago o cancelarla.'
              : order.status === 'PAID'
                ? 'Tu pago está confirmado. Puedes cancelar la orden antes de que sea enviada.'
                : order.status === 'CANCELLED'
                  ? 'Esta compra ya no admite cambios. Puedes seguir explorando los productos de la tienda.'
                  : 'Tu orden ya no admite cancelaciones desde la cuenta. Conserva su número para consultar a la tienda.'}
          </p>
        </div>
        <div className="actions">
          {canCancel && (
            <Button
              variant="secondary"
              className="cancel-order-button"
              onClick={() => setCancelling(true)}
            >
              <X size={15} />
              Cancelar orden
            </Button>
          )}
          {order.status === 'PENDING' && (
            <Link className="button primary" to={`/tienda/${storeSubdomain}/pago/${order.id}`}>
              <CreditCard size={15} />
              Pagar ahora
            </Link>
          )}
        </div>
      </section>
      {cancelling && (
        <CancelOrderDialog
          order={{
            id: order.id,
            orderNumber: order.orderNumber,
            status: order.status,
            total: order.total,
            currency,
          }}
          onClose={() => setCancelling(false)}
          onCancelled={() => {
            setCancelling(false);
            resource.reload();
          }}
        />
      )}
    </>
  );
}
````

### frontend/src/features/customer-orders/CustomerOrdersPage.tsx

````tsx
import { useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { ArrowUpRight, RefreshCw, ShoppingBag } from 'lucide-react';
import { Button, Empty, ErrorNotice, Loading, Pagination } from '@/core/components';
import { labels } from '@/utils/format';
import { useCustomerOrders } from './hooks/useCustomerOrders';
import { CustomerOrderCard } from './components/CustomerOrderCard';
import { CancelOrderDialog } from './components/CancelOrderDialog';
import type { CustomerOrderSummary } from './types';

/** Estados aceptados por el contrato de órdenes; evita enviar filtros arbitrarios desde la URL. */
const statuses = ['PENDING', 'PAID', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

/** Lista las compras del cliente y permite pagar o cancelar cuando corresponde. */
export function CustomerOrdersPage() {
  const [query, setQuery] = useSearchParams();
  const requestedStatus = query.get('estado') || '';
  const status = statuses.includes(requestedStatus) ? requestedStatus : '';
  const page = Math.min(2147483647, Math.max(0, Math.floor(Number(query.get('pagina')) || 0)));
  const resource = useCustomerOrders(status, page);
  const [cancelling, setCancelling] = useState<CustomerOrderSummary | null>(null);
  function filter(value: string) {
    setQuery(value ? { estado: value } : {});
  }
  function changePage(value: number) {
    const next = new URLSearchParams(query);
    value ? next.set('pagina', String(value)) : next.delete('pagina');
    setQuery(next);
  }
  return (
    <>
      <header className="account-page-heading">
        <div>
          <span className="eyebrow">MI CUENTA</span>
          <h1>Mis órdenes</h1>
          <p>Revisa tus compras y gestiona cada pedido desde aquí.</p>
        </div>
        <Link className="button secondary" to="/">
          Explorar tiendas <ArrowUpRight size={16} />
        </Link>
      </header>
      <div className="account-section-card">
        <div className="order-list-toolbar">
          <div>
            <ShoppingBag size={19} />
            <strong>Historial de compras</strong>
          </div>
          <Button variant="secondary" aria-label="Actualizar mis órdenes" onClick={resource.reload}>
            <RefreshCw size={15} />
            <span>Actualizar</span>
          </Button>
        </div>
        <div className="order-status-filters" role="group" aria-label="Filtrar órdenes por estado">
          {['', ...statuses].map((value) => (
            <button
              key={value}
              type="button"
              aria-pressed={status === value}
              className={status === value ? 'selected' : ''}
              onClick={() => filter(value)}
            >
              {value ? labels[value] : 'Todas'}
            </button>
          ))}
        </div>
        <ErrorNotice message={resource.error} retry={resource.reload} />
        {resource.loading ? (
          <Loading />
        ) : (
          resource.data && (
            <>
              <p className="order-result-count" role="status">
                {resource.data.totalElements}{' '}
                {resource.data.totalElements === 1 ? 'orden encontrada' : 'órdenes encontradas'}
                {status ? ` · ${labels[status]}` : ' en todas tus tiendas'}
              </p>
              {!resource.data.content.length ? (
                <Empty
                  title={
                    status ? 'No tienes órdenes con este estado' : 'Tu próxima compra empieza aquí'
                  }
                >
                  <p>
                    {status
                      ? 'Selecciona otro estado para consultar tus compras.'
                      : 'Cuando hagas tu primera compra, podrás encontrarla en este espacio.'}
                  </p>
                  {status ? (
                    <Button variant="secondary" onClick={() => filter('')}>
                      Ver todas mis órdenes
                    </Button>
                  ) : (
                    <Link className="button primary" to="/">
                      Descubrir tiendas
                    </Link>
                  )}
                  {page > 0 && (
                    <Button variant="secondary" onClick={() => changePage(0)}>
                      Volver a la primera página
                    </Button>
                  )}
                </Empty>
              ) : (
                <div className="customer-orders-list">
                  {resource.data.content.map((order) => (
                    <CustomerOrderCard
                      key={order.id}
                      order={order}
                      onCancel={() => setCancelling(order)}
                    />
                  ))}
                </div>
              )}
              <Pagination page={page} totalPages={resource.data.totalPages} onChange={changePage} />
            </>
          )
        )}
      </div>
      <div className="account-order-note">
        <ShoppingBag size={18} />
        <p>
          Puedes cancelar las órdenes pendientes o pagadas antes de su envío. Las órdenes enviadas y
          entregadas permanecen disponibles para consulta.
        </p>
      </div>
      {cancelling && (
        <CancelOrderDialog
          order={cancelling}
          onClose={() => setCancelling(null)}
          onCancelled={() => {
            setCancelling(null);
            resource.reload();
          }}
        />
      )}
    </>
  );
}
````

### frontend/src/features/customer-orders/components/AccountLayout.tsx

````tsx
import { Link, NavLink, Outlet, useLocation } from 'react-router-dom';
import { ChevronRight, LogOut, ShoppingBag, Store, UserRound } from 'lucide-react';
import { useSession } from '@/context/SessionContext';

/** Organiza el área del cliente con un menú lateral. */
export function AccountLayout() {
  const { user, logout } = useSession();
  const { pathname } = useLocation();
  const section = pathname.includes('/datos') ? 'Datos personales' : 'Mis órdenes';
  return (
    <div className="customer-account">
      <div className="account-breadcrumb">
        <Link to="/">Inicio</Link>
        <ChevronRight size={13} />
        <span>Mi cuenta</span>
        <ChevronRight size={13} />
        <span>{section}</span>
      </div>
      <div className="customer-account-layout">
        <aside className="account-sidebar">
          <div className="account-greeting">
            <span className="account-avatar">
              <UserRound size={32} strokeWidth={1.6} />
            </span>
            <div>
              <small>Hola,</small>
              <strong>{user?.fullName}</strong>
              <span>Bienvenido a tu cuenta</span>
            </div>
          </div>
          <nav aria-label="Mi cuenta">
            <NavLink to="/cuenta/datos">
              <UserRound size={19} />
              <span>Datos personales</span>
              <ChevronRight size={16} />
            </NavLink>
            <NavLink to="/cuenta/ordenes">
              <ShoppingBag size={19} />
              <span>Mis órdenes</span>
              <ChevronRight size={16} />
            </NavLink>
            <Link to="/">
              <Store size={19} />
              <span>Explorar tiendas</span>
              <ChevronRight size={16} />
            </Link>
            <button
              type="button"
              onClick={() => {
                logout();
                window.location.replace('/');
              }}
            >
              <LogOut size={19} />
              <span>Cerrar sesión</span>
            </button>
          </nav>
          <div className="account-help">
            <strong>Tus compras, en un solo lugar</strong>
            <p>
              Consulta los detalles de cada orden y conserva su número para cualquier consulta a la
              tienda.
            </p>
          </div>
        </aside>
        <section className="account-main" aria-label={section}>
          <Outlet />
        </section>
      </div>
    </div>
  );
}
````

### frontend/src/features/customer-orders/components/CancelOrderDialog.tsx

````tsx
import { AlertCircle } from 'lucide-react';
import { Button, ErrorNotice, Modal } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { money } from '@/utils/format';
import { customerOrderService } from '../services/customerOrderService';
import type { CancelableOrder } from '../types';

/** Orden que se va a cancelar y función para actualizar la pantalla. */
export interface CancelOrderDialogProps {
  order: CancelableOrder;
  onClose: () => void;
  onCancelled: () => void;
}

/** Confirmación explícita; evita cerrar mientras la transacción de cancelación está pendiente. */
export function CancelOrderDialog({ order, onClose, onCancelled }: CancelOrderDialogProps) {
  const action = useAction();
  const paid = order.status === 'PAID';
  async function confirm() {
    const result = await action.run(
      () => customerOrderService.cancel(order.id),
      'Orden cancelada correctamente.',
    );
    if (result) onCancelled();
  }
  return (
    <Modal
      title="Cancelar mi orden"
      onClose={() => {
        if (!action.pending) onClose();
      }}
    >
      <div className="cancel-order-intro">
        <span>
          <AlertCircle size={24} />
        </span>
        <div>
          <h3>{order.orderNumber}</h3>
          <p>Importe: {money(order.total, order.currency)}</p>
        </div>
      </div>
      <p>
        {paid
          ? 'Al confirmar se cancelará tu compra y se solicitará la devolución del pago. Si la pasarela no acepta la devolución, la orden conservará su estado.'
          : 'Al confirmar se cancelará tu compra y los productos volverán a estar disponibles en la tienda. Esta acción no se puede deshacer.'}
      </p>
      <ErrorNotice message={action.error} />
      <div className="dialog-actions">
        <Button variant="secondary" disabled={action.pending} onClick={onClose}>
          Conservar orden
        </Button>
        <Button variant="danger" pending={action.pending} onClick={() => void confirm()}>
          Sí, cancelar orden
        </Button>
      </div>
    </Modal>
  );
}
````

### frontend/src/features/customer-orders/components/CustomerOrderCard.tsx

````tsx
import { Link } from 'react-router-dom';
import { ArrowRight, CreditCard, ReceiptText, Store, X } from 'lucide-react';
import { Badge, Button } from '@/core/components';
import { dateTime, labels, money } from '@/utils/format';
import type { CustomerOrderSummary } from '../types';

/** Compra del cliente y función para abrir la confirmación de cancelación. */
export interface CustomerOrderCardProps {
  order: CustomerOrderSummary;
  onCancel: () => void;
}

/** Tarjeta del historial con acciones disponibles según el estado actual de la compra. */
export function CustomerOrderCard({ order, onCancel }: CustomerOrderCardProps) {
  const canCancel = order.status === 'PENDING' || order.status === 'PAID';
  return (
    <article className="customer-order-card">
      <header className="customer-order-card-header">
        <div>
          <span className="order-label">NÚMERO DE ORDEN</span>
          <Link className="order-number" to={`/cuenta/ordenes/${order.id}`}>
            {order.orderNumber}
          </Link>
          <time dateTime={order.createdAt}>{dateTime(order.createdAt)}</time>
        </div>
        <Badge value={order.status} />
      </header>
      <div className="customer-order-card-body">
        <div className="order-store">
          <span className="order-store-icon">
            <Store size={23} />
          </span>
          <div>
            <Link to={`/tienda/${order.storeSubdomain}`}>{order.storeName}</Link>
            <p>
              <ReceiptText size={14} />
              {labels[order.invoiceType]}
            </p>
          </div>
        </div>
        <div className="customer-order-amount">
          <small>Total de tu compra</small>
          <strong>{money(order.total, order.currency)}</strong>
        </div>
      </div>
      <footer className="customer-order-card-footer">
        <Link className="order-detail-link" to={`/cuenta/ordenes/${order.id}`}>
          Ver detalle de la orden <ArrowRight size={15} />
        </Link>
        <div className="actions">
          {canCancel && (
            <Button className="cancel-order-button" variant="secondary" onClick={onCancel}>
              <X size={15} />
              Cancelar orden
            </Button>
          )}
          {order.status === 'PENDING' && (
            <Link
              className="button primary"
              to={`/tienda/${order.storeSubdomain}/pago/${order.id}`}
            >
              <CreditCard size={15} />
              Pagar ahora
            </Link>
          )}
        </div>
      </footer>
    </article>
  );
}
````

### frontend/src/features/customer-orders/hooks/useCustomerOrder.ts

````typescript
import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { customerOrderService } from '../services/customerOrderService';

/** Recupera el detalle directamente desde la URL, incluida una recarga del navegador. */
export function useCustomerOrder(orderId: string) {
  return useResource(useCallback(() => customerOrderService.detail(orderId), [orderId]));
}
````

### frontend/src/features/customer-orders/hooks/useCustomerOrders.ts

````typescript
import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { customerOrderService } from '../services/customerOrderService';

/** Historial remoto con filtros y paginación; descarta respuestas de filtros anteriores. */
export function useCustomerOrders(status: string, page: number) {
  return useResource(useCallback(() => customerOrderService.list(status, page), [status, page]));
}
````

### frontend/src/features/customer-orders/index.ts

````typescript
export { AccountLayout } from './components/AccountLayout';
export { AccountProfilePage } from './AccountProfilePage';
export { CustomerOrdersPage } from './CustomerOrdersPage';
export { CustomerOrderDetailPage } from './CustomerOrderDetailPage';
````

### frontend/src/features/customer-orders/services/customerOrderService.test.ts

````typescript
import { beforeEach, describe, expect, it, vi } from 'vitest';
vi.mock('@/config', () => ({ api: { get: vi.fn(), post: vi.fn() } }));
import { api } from '@/config';
import { customerOrderService } from './customerOrderService';

describe('Contrato del área de cliente', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(api.get).mockResolvedValue({ data: {} });
    vi.mocked(api.post).mockResolvedValue({ data: {} });
  });
  it('no transmite identificadores de cliente para obtener el historial', async () => {
    await customerOrderService.list('PENDING', 2);
    expect(api.get).toHaveBeenCalledWith('/customer/orders', {
      params: { status: 'PENDING', page: 2, size: 6 },
    });
  });
  it('solo solicita cancelar, sin permitir estados arbitrarios en el payload', async () => {
    await customerOrderService.cancel('order');
    expect(api.post).toHaveBeenCalledWith('/customer/orders/order/cancel');
  });
});
````

### frontend/src/features/customer-orders/services/customerOrderService.ts

````typescript
import { api } from '@/config';
import type { Page } from '@/core/types';
import type { CustomerOrderDetail, CustomerOrderSummary } from '../types';

/** La API obtiene el cliente del JWT: ninguna operación acepta un customerId del navegador. */
export const customerOrderService = {
  list: async (status: string, page: number) =>
    (
      await api.get<Page<CustomerOrderSummary>>('/customer/orders', {
        params: { status: status || undefined, page, size: 6 },
      })
    ).data,
  detail: async (orderId: string) =>
    (await api.get<CustomerOrderDetail>(`/customer/orders/${orderId}`)).data,
  cancel: async (orderId: string) =>
    (await api.post<CustomerOrderDetail>(`/customer/orders/${orderId}/cancel`)).data,
};
````

### frontend/src/features/customer-orders/types/index.ts

````typescript
import type { InvoiceType, Order, OrderStatus } from '@/features/orders';

/** Datos mínimos para confirmar una cancelación; no permite editar precios ni estados. */
export interface CancelableOrder {
  id: string;
  orderNumber: string;
  status: OrderStatus;
  total: number;
  currency: string;
}

/** Compra del cliente en cualquier tienda, según la página real enviada por el servidor. */
export interface CustomerOrderSummary extends CancelableOrder {
  storeId: string;
  storeName: string;
  storeSubdomain: string;
  invoiceType: InvoiceType;
  createdAt: string;
}

/** Datos guardados de la compra y de la tienda para continuar el pago. */
export interface CustomerOrderDetail {
  order: Order;
  storeName: string;
  storeSubdomain: string;
  currency: string;
}
````

### frontend/src/features/dashboard/DashboardPage.tsx

````tsx
import { Link, useOutletContext } from 'react-router-dom';
import { ArrowUpRight, Package, ShoppingBag } from 'lucide-react';
import { Button, ErrorNotice, Loading, PageHeader } from '@/core/components';
import type { Store } from '@/features/stores';
import { useSession } from '@/context/SessionContext';
import { useDashboard } from './hooks/useDashboard';
import { MetricCard } from './components/MetricCard';
/** Inicio del panel con actividad y accesos vinculados a la tienda elegida. */
export function DashboardPage() {
  const store = useOutletContext<Store>();
  const { user } = useSession();
  const resource = useDashboard(store.id);
  return (
    <>
      <PageHeader
        title={`Hola, ${user?.fullName.split(' ')[0] || 'bienvenido'}`}
        description={`Esto es lo que está pasando en ${store.name}.`}
        actions={
          <Button variant="secondary" onClick={resource.reload}>
            Actualizar resumen
          </Button>
        }
      />
      <ErrorNotice message={resource.error} retry={resource.reload} />
      {resource.loading ? (
        <Loading />
      ) : (
        resource.data && (
          <div className="metrics-grid">
            <MetricCard label="Productos" value={resource.data.products} />
            <MetricCard label="Productos activos" value={resource.data.activeProducts} />
            <MetricCard label="Órdenes pendientes" value={resource.data.pendingOrders} />
            <MetricCard label="Órdenes pagadas" value={resource.data.paidOrders} />
          </div>
        )
      )}
      <section className="card admin-welcome">
        <span className="eyebrow">SIGUE CONSTRUYENDO TU NEGOCIO</span>
        <h2>Todo listo para tu próxima venta.</h2>
        <p>Actualiza tu catálogo, revisa las existencias y acompaña cada orden.</p>
        <div className="actions">
          <Link className="button primary" to={`/admin/${store.id}/productos`}>
            <Package size={17} />
            Gestionar productos
          </Link>
          <Link className="button secondary" to={`/admin/${store.id}/ordenes`}>
            <ShoppingBag size={17} />
            Ver órdenes
          </Link>
        </div>
      </section>
      <section className="card">
        <h2>Tu escaparate, siempre disponible</h2>
        <p className="muted">Comprueba cómo ven tus clientes la tienda y sus productos.</p>
        <Link className="text-link" to={`/tienda/${store.subdomain}`}>
          Visitar {store.name}
          <ArrowUpRight size={16} />
        </Link>
      </section>
    </>
  );
}
````

### frontend/src/features/dashboard/components/MetricCard.tsx

````tsx
/** Etiqueta y cantidad que muestra el indicador. */
export interface MetricCardProps {
  label: string;
  value: number;
}
/** Tarjeta de indicador administrativo de lectura rápida. */
export function MetricCard({ label, value }: MetricCardProps) {
  return (
    <article className="card metric-card">
      <p>{label}</p>
      <strong>{value}</strong>
      <small>Datos actuales de tu tienda</small>
    </article>
  );
}
````

### frontend/src/features/dashboard/hooks/useDashboard.ts

````typescript
import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { getDashboardMetrics } from '../services/dashboardService';
/** Consulta métricas al entrar o actualizar el resumen de una tienda. */
export function useDashboard(storeId: string) {
  return useResource(useCallback(() => getDashboardMetrics(storeId), [storeId]));
}
````

### frontend/src/features/dashboard/index.ts

````typescript
export { DashboardPage } from './DashboardPage';
````

### frontend/src/features/dashboard/services/dashboardService.ts

````typescript
import { api } from '@/config';
import type { Page } from '@/core/types';
import type { Product } from '@/features/products';
import type { DashboardMetrics } from '../types';
/** Consulta el total de productos y órdenes de la tienda. */
export async function getDashboardMetrics(storeId: string): Promise<DashboardMetrics> {
  const [products, pending, paid] = await Promise.all([
    api.get<Product[]>(`/stores/${storeId}/products`),
    api.get<Page<unknown>>(`/stores/${storeId}/orders`, { params: { status: 'PENDING', size: 1 } }),
    api.get<Page<unknown>>(`/stores/${storeId}/orders`, { params: { status: 'PAID', size: 1 } }),
  ]);
  return {
    products: products.data.length,
    activeProducts: products.data.filter((item) => item.status === 'ACTIVE').length,
    pendingOrders: pending.data.totalElements,
    paidOrders: paid.data.totalElements,
  };
}
````

### frontend/src/features/dashboard/types/index.ts

````typescript
/** Métricas obtenidas del catálogo y los totales paginados de órdenes. */
export interface DashboardMetrics {
  products: number;
  activeProducts: number;
  pendingOrders: number;
  paidOrders: number;
}
````

### frontend/src/features/inventory/InventoryPage.tsx

````tsx
import { useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { Empty, ErrorNotice, Loading, PageHeader, Select } from '@/core/components';
import { useProducts } from '@/features/products';
import type { Store } from '@/features/stores';
import { useInventory } from './hooks/useInventory';
import { InventoryEditor } from './components/InventoryEditor';
/** Consulta y actualización de inventario del producto seleccionado. */
export function InventoryPage() {
  const store = useOutletContext<Store>();
  const products = useProducts(store.id);
  const [selected, setSelected] = useState('');
  const inventory = useInventory(store.id, selected);
  return (
    <>
      <PageHeader
        title="Inventario"
        description="Mantén tus existencias al día y anticipa el stock bajo."
      />
      <div className="card">
        <Select
          label="Producto"
          value={selected}
          onChange={setSelected}
          options={[
            { value: '', label: 'Selecciona un producto' },
            ...(products.data || []).map((product) => ({ value: product.id, label: product.name })),
          ]}
          hint="Ejemplo: Café de especialidad 250 g."
        />
      </div>
      <ErrorNotice message={products.error} retry={products.reload} />
      <ErrorNotice message={inventory.error} retry={inventory.reload} />
      {products.loading || inventory.loading ? (
        <Loading />
      ) : inventory.data ? (
        <InventoryEditor
          key={`${selected}-${inventory.data.updatedAt}`}
          storeId={store.id}
          inventory={inventory.data}
          onSaved={inventory.reload}
        />
      ) : (
        <Empty title="Selecciona un producto para gestionar su stock" />
      )}
    </>
  );
}
````

### frontend/src/features/inventory/components/InventoryEditor.tsx

````tsx
import { useState } from 'react';
import { Button, ErrorNotice, Field } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { isInteger } from '@/utils/validation';
import { dateTime } from '@/utils/format';
import { inventoryService } from '../services/inventoryService';
import type { Inventory } from '../types';
/** Inventario consultado y función para actualizarlo después de guardar. */
export interface InventoryEditorProps {
  storeId: string;
  inventory: Inventory;
  onSaved: () => void;
}
/** Permite guardar la cantidad y el umbral por separado. */
export function InventoryEditor({ storeId, inventory, onSaved }: InventoryEditorProps) {
  const [quantity, setQuantity] = useState(String(inventory.quantity));
  const [threshold, setThreshold] = useState(String(inventory.lowStockThreshold));
  const action = useAction();
  return (
    <section className="card form-stack">
      <div className="section-heading">
        <h2>Stock disponible</h2>
        <span className={`badge ${inventory.lowStock ? 'badge-pending' : 'badge-active'}`}>
          {inventory.lowStock ? 'Stock bajo' : 'Stock saludable'}
        </span>
      </div>
      <p className="muted">Última actualización: {dateTime(inventory.updatedAt)}</p>
      <ErrorNotice message={action.error} />
      <form
        className="inline-form"
        onSubmit={(event) => {
          event.preventDefault();
          if (isInteger(quantity))
            void action
              .run(
                () => inventoryService.quantity(storeId, inventory.productId, Number(quantity)),
                'Cantidad actualizada.',
              )
              .then((result) => {
                if (result) onSaved();
              });
        }}
      >
        <Field
          label="Cantidad de unidades"
          example="20"
          rule="digits"
          inputMode="numeric"
          maxLength={10}
          value={quantity}
          onChange={setQuantity}
          required
          error={!isInteger(quantity) ? 'Ingresa un entero entre 0 y 2147483647.' : ''}
        />
        <Button type="submit" pending={action.pending} disabled={!isInteger(quantity)}>
          Guardar cantidad
        </Button>
      </form>
      <form
        className="inline-form"
        onSubmit={(event) => {
          event.preventDefault();
          if (isInteger(threshold))
            void action
              .run(
                () => inventoryService.threshold(storeId, inventory.productId, Number(threshold)),
                'Umbral actualizado.',
              )
              .then((result) => {
                if (result) onSaved();
              });
        }}
      >
        <Field
          label="Umbral de stock bajo"
          example="5"
          rule="digits"
          inputMode="numeric"
          maxLength={10}
          value={threshold}
          onChange={setThreshold}
          required
          error={!isInteger(threshold) ? 'Ingresa un entero no negativo.' : ''}
        />
        <Button
          type="submit"
          variant="secondary"
          pending={action.pending}
          disabled={!isInteger(threshold)}
        >
          Guardar umbral
        </Button>
      </form>
    </section>
  );
}
````

### frontend/src/features/inventory/hooks/useInventory.ts

````typescript
import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { inventoryService } from '../services/inventoryService';
/** Consulta stock del producto seleccionado, sin disparar una petición por cada fila. */
export function useInventory(storeId: string, productId: string) {
  return useResource(
    useCallback(() => inventoryService.get(storeId, productId), [storeId, productId]),
    !!productId,
  );
}
````

### frontend/src/features/inventory/index.ts

````typescript
export { InventoryPage } from './InventoryPage';
````

### frontend/src/features/inventory/services/inventoryService.ts

````typescript
import { api } from '@/config';
import type { Inventory } from '../types';
/** Cambios separados de cantidad y umbral; no hay inventarios ficticios en el cliente. */
export const inventoryService = {
  get: async (storeId: string, productId: string) =>
    (await api.get<Inventory>(`/stores/${storeId}/products/${productId}/inventory`)).data,
  quantity: async (storeId: string, productId: string, quantity: number) =>
    (
      await api.patch<Inventory>(`/stores/${storeId}/products/${productId}/inventory/quantity`, {
        quantity,
      })
    ).data,
  threshold: async (storeId: string, productId: string, lowStockThreshold: number) =>
    (
      await api.patch<Inventory>(
        `/stores/${storeId}/products/${productId}/inventory/low-stock-threshold`,
        { lowStockThreshold },
      )
    ).data,
};
````

### frontend/src/features/inventory/types/index.ts

````typescript
/** Stock actual y punto de alerta calculado por el backend. */
export interface Inventory {
  productId: string;
  quantity: number;
  lowStockThreshold: number;
  lowStock: boolean;
  updatedAt: string;
}
````

### frontend/src/features/notifications/components/PurchaseConfirmation.tsx

````tsx
import { CircleCheck } from 'lucide-react';
import type { PurchaseConfirmationProps } from '../types';
import { usePurchaseConfirmation } from '../hooks/usePurchaseConfirmation';
/** Confirmación persistente después de verificar el estado pagado de la orden. */
export function PurchaseConfirmation({ orderNumber }: PurchaseConfirmationProps) {
  const { customerName } = usePurchaseConfirmation();
  return (
    <div className="notice success" role="status">
      <CircleCheck size={24} />
      <div>
        <strong>Tu compra fue confirmada, {customerName}.</strong>
        <p>Orden {orderNumber}. Conserva este número para consultar sobre tu compra.</p>
      </div>
    </div>
  );
}
````

### frontend/src/features/notifications/hooks/usePurchaseConfirmation.ts

````typescript
import { useSession } from '@/context/SessionContext';
/** Obtiene el nombre del cliente para confirmar su compra. */
export function usePurchaseConfirmation() {
  const { user } = useSession();
  return { customerName: user?.fullName || 'Cliente' };
}
````

### frontend/src/features/notifications/index.ts

````typescript
export { PurchaseConfirmation } from './components/PurchaseConfirmation';
````

### frontend/src/features/notifications/services/notificationService.ts

````typescript
/** El backend envía el correo al aprobar el pago; no se solicita desde el navegador. */
export const notificationPolicy = {
  deliveryOwnedBy: 'backend',
  automaticOnApprovedPayment: true,
} as const;
````

### frontend/src/features/notifications/types/index.ts

````typescript
/** Confirmación visible de una compra aprobada; no implica entrega de un correo. */
export interface PurchaseConfirmationProps {
  orderNumber: string;
}
````

### frontend/src/features/offers/OffersPage.tsx

````tsx
import { useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { Empty, ErrorNotice, Loading, PageHeader, Select } from '@/core/components';
import { useProducts } from '@/features/products';
import type { Store } from '@/features/stores';
import { dateTime, money } from '@/utils/format';
import { useOffers } from './hooks/useOffers';
import { OfferForm } from './components/OfferForm';
/** Configuración e historial de promociones por producto. */
export function OffersPage() {
  const store = useOutletContext<Store>();
  const products = useProducts(store.id);
  const [selected, setSelected] = useState('');
  const offers = useOffers(store.id, selected);
  return (
    <>
      <PageHeader title="Ofertas" description="Dale a cada promoción su momento." />
      <div className="card">
        <Select
          label="Producto"
          value={selected}
          onChange={setSelected}
          options={[
            { value: '', label: 'Selecciona un producto' },
            ...(products.data || []).map((product) => ({ value: product.id, label: product.name })),
          ]}
          hint="Ejemplo: Café de especialidad 250 g."
        />
      </div>
      <ErrorNotice message={products.error} retry={products.reload} />
      <ErrorNotice message={offers.error} retry={offers.reload} />
      {products.loading || offers.loading ? (
        <Loading />
      ) : selected ? (
        <div className="two-columns">
          <OfferForm
            key={selected}
            storeId={store.id}
            productId={selected}
            onSaved={offers.reload}
          />
          <section className="card">
            <h2>Historial de ofertas</h2>
            {offers.data?.length ? (
              offers.data.map((offer) => (
                <article className="offer-row" key={`${offer.startsAt}-${offer.endsAt}`}>
                  <strong>{money(offer.offerPrice, store.currency)}</strong>
                  <span className={`badge ${offer.active ? 'badge-active' : ''}`}>
                    {offer.active ? 'Vigente' : 'Fuera de vigencia'}
                  </span>
                  <p>
                    {dateTime(offer.startsAt)} — {dateTime(offer.endsAt)}
                  </p>
                </article>
              ))
            ) : (
              <Empty title="Este producto aún no tiene ofertas" />
            )}
          </section>
        </div>
      ) : (
        <Empty title="Selecciona un producto para ver sus ofertas" />
      )}
    </>
  );
}
````

### frontend/src/features/offers/components/OfferForm.tsx

````tsx
import { useState, type FormEvent } from 'react';
import { Button, ErrorNotice, Field } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { isMoney } from '@/utils/validation';
import { offerService } from '../services/offerService';
/** Producto y tienda a los que pertenece la oferta. */
export interface OfferFormProps {
  storeId: string;
  productId: string;
  onSaved: () => void;
}
/** Convierte fechas locales a ISO con zona para evitar promociones desplazadas. */
export function OfferForm({ storeId, productId, onSaved }: OfferFormProps) {
  const [price, setPrice] = useState('');
  const [start, setStart] = useState('');
  const [end, setEnd] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const action = useAction();
  const validStart = Number.isFinite(Date.parse(start));
  const validEnd = Number.isFinite(Date.parse(end)) && Date.parse(end) > Date.parse(start);
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (!isMoney(price) || !validStart || !validEnd) return;
    const result = await action.run(
      () =>
        offerService.create(storeId, productId, {
          offerPrice: Number(price),
          startsAt: new Date(start).toISOString(),
          endsAt: new Date(end).toISOString(),
        }),
      'Oferta creada.',
    );
    if (result) {
      setPrice('');
      setStart('');
      setEnd('');
      setSubmitted(false);
      onSaved();
    }
  }
  return (
    <form noValidate className="card form-stack" onSubmit={(event) => void submit(event)}>
      <h2>Nueva oferta</h2>
      <ErrorNotice message={action.error} />
      <Field
        label="Precio de oferta (S/)"
        example="19.90"
        rule="money"
        inputMode="decimal"
        required
        value={price}
        onChange={setPrice}
        error={submitted && !isMoney(price) ? 'Ingresa un precio válido no negativo.' : ''}
      />
      <Field
        label="Fecha y hora de inicio"
        example="20/11/2026, 09:00"
        type="datetime-local"
        required
        value={start}
        onChange={setStart}
        error={submitted && !validStart ? 'Selecciona una fecha de inicio.' : ''}
      />
      <Field
        label="Fecha y hora de fin"
        example="30/11/2026, 18:00"
        type="datetime-local"
        required
        value={end}
        onChange={setEnd}
        error={submitted && !validEnd ? 'La fecha final debe ser posterior al inicio.' : ''}
      />
      <Button type="submit" pending={action.pending}>
        Crear oferta
      </Button>
    </form>
  );
}
````

### frontend/src/features/offers/hooks/useOffers.ts

````typescript
import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { offerService } from '../services/offerService';
/** Recupera ofertas únicamente cuando hay un producto seleccionado. */
export function useOffers(storeId: string, productId: string) {
  return useResource(
    useCallback(() => offerService.list(storeId, productId), [storeId, productId]),
    !!productId,
  );
}
````

### frontend/src/features/offers/index.ts

````typescript
export { OffersPage } from './OffersPage';
````

### frontend/src/features/offers/services/offerService.ts

````typescript
import { api } from '@/config';
import type { Offer, OfferInput } from '../types';
/** Historial de ofertas y creación con control de solapamientos en el servidor. */
export const offerService = {
  list: async (storeId: string, productId: string) =>
    (await api.get<Offer[]>(`/stores/${storeId}/products/${productId}/offers`)).data,
  create: async (storeId: string, productId: string, body: OfferInput) =>
    (await api.post<Offer>(`/stores/${storeId}/products/${productId}/offers`, body)).data,
};
````

### frontend/src/features/offers/types/index.ts

````typescript
/** Oferta temporal serializada con offset ISO 8601. */
export interface Offer {
  productId: string;
  offerPrice: number;
  startsAt: string;
  endsAt: string;
  active: boolean;
  createdAt: string;
}
/** Precio y ventana temporal de una nueva oferta. */
export interface OfferInput {
  offerPrice: number;
  startsAt: string;
  endsAt: string;
}
````

### frontend/src/features/orders/OrderDetailPage.tsx

````tsx
import { useCallback, useState } from 'react';
import { Link, useOutletContext, useParams } from 'react-router-dom';
import { Button, ErrorNotice, Loading, Modal, PageHeader, Select } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { useResource } from '@/core/hooks/useResource';
import type { Store } from '@/features/stores';
import { labels } from '@/utils/format';
import { orderService } from './services/orderService';
import { OrderDetails } from './components/OrderDetails';
import type { OrderStatus } from './types';
/** Transiciones permitidas del contrato de OrderManagementService. */
const transitions: Record<OrderStatus, OrderStatus[]> = {
  PENDING: ['CANCELLED'],
  PAID: ['SHIPPED', 'CANCELLED'],
  SHIPPED: ['DELIVERED'],
  DELIVERED: [],
  CANCELLED: [],
};
/** Detalle administrativo con confirmación explícita antes de cancelar o reembolsar. */
export function OrderDetailPage() {
  const store = useOutletContext<Store>();
  const { orderId = '' } = useParams();
  const resource = useResource(
    useCallback(() => orderService.detail(store.id, orderId), [store.id, orderId]),
  );
  const action = useAction();
  const [nextStatus, setNextStatus] = useState('');
  const [confirming, setConfirming] = useState(false);
  async function save() {
    const result = await action.run(
      () => orderService.status(store.id, orderId, nextStatus as OrderStatus),
      'Estado de orden actualizado.',
    );
    if (result) {
      setConfirming(false);
      setNextStatus('');
      resource.reload();
    }
  }
  if (resource.loading) return <Loading />;
  if (!resource.data) return <ErrorNotice message={resource.error} retry={resource.reload} />;
  const order = resource.data;
  return (
    <>
      <PageHeader
        title="Detalle de orden"
        actions={<Link to={`/admin/${store.id}/ordenes`}>Volver a órdenes</Link>}
      />
      <section className="card">
        <OrderDetails order={order} currency={store.currency} />
      </section>
      <ErrorNotice message={action.error} />
      {transitions[order.status].length > 0 && (
        <section className="card inline-form">
          <Select
            label="Cambiar estado"
            value={nextStatus}
            onChange={setNextStatus}
            options={[
              { value: '', label: 'Selecciona un estado' },
              ...transitions[order.status].map((value) => ({
                value,
                label: labels[value] || value,
              })),
            ]}
          />
          <Button
            disabled={!nextStatus}
            pending={action.pending}
            onClick={() => setConfirming(true)}
          >
            Actualizar estado
          </Button>
        </section>
      )}
      {confirming && (
        <Modal title="Confirmar cambio de estado" onClose={() => setConfirming(false)}>
          <p>
            {nextStatus === 'CANCELLED'
              ? `Se cancelará esta orden y se restaurará el stock.${order.status === 'PAID' ? ' También se solicitará el reembolso del pago aprobado.' : ''}`
              : `La orden pasará a «${labels[nextStatus]}».`}
          </p>
          <ErrorNotice message={action.error} />
          <Button
            variant={nextStatus === 'CANCELLED' ? 'danger' : 'primary'}
            pending={action.pending}
            onClick={() => void save()}
          >
            Confirmar cambio
          </Button>
        </Modal>
      )}
    </>
  );
}
````

### frontend/src/features/orders/OrdersPage.tsx

````tsx
import { useState } from 'react';
import { Link, useOutletContext } from 'react-router-dom';
import {
  Badge,
  Empty,
  ErrorNotice,
  Loading,
  PageHeader,
  Pagination,
  Select,
} from '@/core/components';
import type { Store } from '@/features/stores';
import { dateTime, labels, money } from '@/utils/format';
import { useOrders } from './hooks/useOrders';
/** Listado de ventas con paginación y filtro por estados reales de la API. */
export function OrdersPage() {
  const store = useOutletContext<Store>();
  const [status, setStatus] = useState('');
  const [page, setPage] = useState(0);
  const resource = useOrders(store.id, status, page);
  return (
    <>
      <PageHeader
        title="Órdenes"
        description="Acompaña cada venta desde su recepción hasta la entrega."
      />
      <div className="card filters">
        <Select
          label="Estado de la orden"
          value={status}
          onChange={(value) => {
            setStatus(value);
            setPage(0);
          }}
          options={[
            { value: '', label: 'Todos los estados' },
            ...['PENDING', 'PAID', 'SHIPPED', 'DELIVERED', 'CANCELLED'].map((value) => ({
              value,
              label: labels[value] || value,
            })),
          ]}
        />
      </div>
      <ErrorNotice message={resource.error} retry={resource.reload} />
      {resource.loading ? (
        <Loading />
      ) : !resource.data?.content.length ? (
        <Empty title="No hay órdenes con este estado" />
      ) : (
        <>
          <div className="card table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Orden</th>
                  <th>Fecha</th>
                  <th>Documento</th>
                  <th>Estado</th>
                  <th>Total</th>
                </tr>
              </thead>
              <tbody>
                {resource.data.content.map((order) => (
                  <tr key={order.id}>
                    <td>
                      <Link to={`/admin/${store.id}/ordenes/${order.id}`}>{order.orderNumber}</Link>
                    </td>
                    <td>{dateTime(order.createdAt)}</td>
                    <td>{order.customerDocumentNumber}</td>
                    <td>
                      <Badge value={order.status} />
                    </td>
                    <td>{money(order.total, store.currency)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <Pagination page={page} totalPages={resource.data.totalPages} onChange={setPage} />
        </>
      )}
    </>
  );
}
````

### frontend/src/features/orders/components/OrderDetails.tsx

````tsx
import { Badge } from '@/core/components';
import { dateTime, labels, money } from '@/utils/format';
import type { Order } from '../types';
/** Orden guardada y moneda que se usará para mostrar los importes. */
export interface OrderDetailsProps {
  order: Order;
  currency: string;
}
/** Muestra productos, datos fiscales y totales de la orden. */
export function OrderDetails({ order, currency }: OrderDetailsProps) {
  return (
    <>
      <div className="section-heading">
        <div>
          <h2>{order.orderNumber}</h2>
          <p className="muted">{dateTime(order.createdAt)}</p>
        </div>
        <Badge value={order.status} />
      </div>
      <div className="fiscal-details">
        <div>
          <small>Comprobante</small>
          <strong>{labels[order.invoiceType]}</strong>
        </div>
        <div>
          <small>{order.customerDocumentType}</small>
          <strong>{order.customerDocumentNumber}</strong>
        </div>
        {order.customerBusinessName && (
          <div>
            <small>Razón social</small>
            <strong>{order.customerBusinessName}</strong>
          </div>
        )}
        {order.customerBillingAddress && (
          <div>
            <small>Dirección fiscal</small>
            <strong>{order.customerBillingAddress}</strong>
          </div>
        )}
      </div>
      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Producto</th>
              <th>Cantidad</th>
              <th>Precio de lista</th>
              <th>Precio cobrado</th>
              <th>Subtotal</th>
            </tr>
          </thead>
          <tbody>
            {order.items.map((item) => (
              <tr key={item.productId}>
                <td>{item.productName}</td>
                <td>{item.quantity}</td>
                <td>{money(item.listUnitPrice, currency)}</td>
                <td>{money(item.chargedUnitPrice, currency)}</td>
                <td>{money(item.itemSubtotal, currency)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="order-totals">
        <p>
          <span>Subtotal</span>
          <strong>{money(order.subtotal, currency)}</strong>
        </p>
        <p>
          <span>IGV aplicado ({Math.round(order.appliedTaxRate * 100)} %)</span>
          <strong>{money(order.taxAmount, currency)}</strong>
        </p>
        <p className="total">
          <span>Total</span>
          <strong>{money(order.total, currency)}</strong>
        </p>
      </div>
    </>
  );
}
````

### frontend/src/features/orders/hooks/useOrders.ts

````typescript
import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { orderService } from '../services/orderService';
/** Recupera la página de órdenes filtradas; el orden descendente pertenece al backend. */
export function useOrders(storeId: string, status: string, page: number) {
  return useResource(
    useCallback(() => orderService.list(storeId, status, page), [storeId, status, page]),
  );
}
````

### frontend/src/features/orders/index.ts

````typescript
export { OrdersPage } from './OrdersPage';
export { OrderDetailPage } from './OrderDetailPage';
export { OrderDetails } from './components/OrderDetails';
export type { Order, OrderStatus, InvoiceType } from './types';
````

### frontend/src/features/orders/services/orderService.ts

````typescript
import { api } from '@/config';
import type { Page } from '@/core/types';
import type { Order, OrderStatus, OrderSummary } from '../types';
/** Gestión de órdenes del propietario; el servidor verifica cada transición. */
export const orderService = {
  list: async (storeId: string, status: string, page: number) =>
    (
      await api.get<Page<OrderSummary>>(`/stores/${storeId}/orders`, {
        params: { status: status || undefined, page, size: 15 },
      })
    ).data,
  detail: async (storeId: string, orderId: string) =>
    (await api.get<Order>(`/stores/${storeId}/orders/${orderId}`)).data,
  status: async (storeId: string, orderId: string, status: OrderStatus) =>
    (await api.patch<Order>(`/stores/${storeId}/orders/${orderId}/status`, { status })).data,
};
````

### frontend/src/features/orders/types/index.ts

````typescript
/** Estados de órdenes del backend; etiquetas traducidas en la UI. */
export type OrderStatus = 'PENDING' | 'PAID' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
/** Comprobante fiscal elegido al confirmar una compra. */
export type InvoiceType = 'RECEIPT' | 'INVOICE';
/** Línea histórica: el backend congela nombres e importes al crear la orden. */
export interface OrderItem {
  productId: string;
  productName: string;
  quantity: number;
  listUnitPrice: number;
  chargedUnitPrice: number;
  itemSubtotal: number;
}
/** Resumen para listados paginados de administración. */
export interface OrderSummary {
  id: string;
  orderNumber: string;
  invoiceType: InvoiceType;
  customerDocumentType: 'DNI' | 'RUC';
  customerDocumentNumber: string;
  customerBusinessName: string | null;
  total: number;
  status: OrderStatus;
  createdAt: string;
}
/** Detalle completo de una orden con datos tributarios y totales remotos. */
export interface Order extends OrderSummary {
  storeId: string;
  customerBillingAddress: string | null;
  subtotal: number;
  taxAmount: number;
  appliedTaxRate: number;
  items: OrderItem[];
}
````

### frontend/src/features/payments/PaymentPage.tsx

````tsx
import { useCallback } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, ErrorNotice, Loading, PageHeader } from '@/core/components';
import { useResource } from '@/core/hooks/useResource';
import { useStore } from '@/context/StoreContext';
import { useCart } from '@/features/cart';
import { OrderDetails } from '@/features/orders';
import { PurchaseConfirmation } from '@/features/notifications';
import { checkoutService } from '@/features/checkout/services/checkoutService';
import { PaymentForm } from './components/PaymentForm';
/** Orden recuperada del servidor, incluso al recargar la página de pago. */
export function PaymentPage() {
  const { orderId = '' } = useParams();
  const store = useStore();
  const cart = useCart();
  const resource = useResource(
    useCallback(() => checkoutService.get(store.id, orderId), [store.id, orderId]),
  );
  if (resource.loading) return <Loading />;
  if (!resource.data) return <ErrorNotice message={resource.error} retry={resource.reload} />;
  const order = resource.data;
  return (
    <>
      <PageHeader
        title={order.status === 'PENDING' ? 'Revisa tu orden y paga' : 'Estado de tu compra'}
        actions={
          <div className="actions">
            <Link className="button secondary" to={`/cuenta/ordenes/${order.id}`}>
              Ver en mis órdenes
            </Link>
            <Button variant="secondary" onClick={resource.reload}>
              Actualizar estado
            </Button>
          </div>
        }
      />
      {['PAID', 'SHIPPED', 'DELIVERED'].includes(order.status) && (
        <PurchaseConfirmation orderNumber={order.orderNumber} />
      )}
      <div className="checkout-grid">
        <section className="card">
          <OrderDetails order={order} currency={store.currency} />
        </section>
        {order.status === 'PENDING' ? (
          <PaymentForm
            key={order.id}
            storeId={store.id}
            storeName={store.name}
            currency={store.currency}
            order={order}
            onPaid={() => {
              cart.reload();
              resource.reload();
            }}
          />
        ) : (
          <aside className="card summary">
            <h2>{order.status === 'CANCELLED' ? 'Orden cancelada' : 'Gracias por tu compra'}</h2>
            <p>Conserva el número de tu orden para cualquier consulta.</p>
            <Link className="button secondary" to="/cuenta/ordenes">
              Ver mis órdenes
            </Link>
            <Link className="button primary" to={`/tienda/${store.subdomain}`}>
              Volver a la tienda
            </Link>
          </aside>
        )}
      </div>
    </>
  );
}
````

### frontend/src/features/payments/components/PaymentForm.tsx

````tsx
import { useEffect, useRef, useState, type FormEvent } from 'react';
import { CreditCard, LockKeyhole } from 'lucide-react';
import { toast } from 'sonner';
import { Button, ErrorNotice, Field } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { useSession } from '@/context/SessionContext';
import type { Order } from '@/features/orders';
import { usePayment } from '../hooks/usePayment';
import { loadCulqi } from '../services/culqiService';
import type { CulqiInstance, Payment } from '../types';
import { UserFacingError } from '@/utils/errors';
import { APP_NAME } from '@/config';

/** Datos de la orden que se pagará y función que recibe el resultado. */
export interface PaymentFormProps {
  storeId: string;
  storeName: string;
  currency: string;
  order: Order;
  onPaid: (payment: Payment) => void;
}
/** Recibe un token de prueba y envía el pago al backend. */
export function PaymentForm({ storeId, storeName, currency, order, onPaid }: PaymentFormProps) {
  const [sourceId, setSourceId] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const payment = usePayment(storeId, order.id);
  const sdkAction = useAction();
  const { user } = useSession();
  const instance = useRef<CulqiInstance | null>(null);
  const mounted = useRef(true);
  const externalCheckout = import.meta.env.VITE_PAYMENT_MODE === 'culqi';
  const validToken = /^[a-zA-Z0-9_-]{1,100}$/.test(sourceId);
  useEffect(() => {
    mounted.current = true;
    return () => {
      mounted.current = false;
      if (instance.current) {
        instance.current.culqi = () => {};
        instance.current.close();
      }
    };
  }, []);
  async function openCheckout() {
    await sdkAction.run(async () => {
      const publicKey = import.meta.env.VITE_CULQI_PUBLIC_KEY || '';
      if (!publicKey.startsWith('pk_test_'))
        throw new UserFacingError(
          'El pago de prueba no está configurado. Contacta al administrador.',
        );
      instance.current?.close();
      setSourceId('');
      const Checkout = await loadCulqi();
      if (!mounted.current) return false;
      const checkout = new Checkout(publicKey, {
        settings: { title: storeName, currency, amount: Math.round(order.total * 100) },
        client: { email: user?.email },
        options: {
          lang: 'es',
          modal: true,
          installments: false,
          paymentMethods: {
            tarjeta: true,
            yape: false,
            billetera: false,
            bancaMovil: false,
            agente: false,
            cuotealo: false,
          },
          paymentMethodsSort: ['tarjeta'],
        },
        appearance: {
          theme: 'default',
          defaultStyle: { buttonBackground: '#008060', buttonTextColor: '#ffffff' },
        },
      });
      checkout.culqi = () => {
        if (checkout.token?.id) {
          setSourceId(checkout.token.id);
          checkout.close();
          toast.success('Medio de pago listo. Confirma el pago para continuar.');
        } else {
          toast.error(
            'No se pudo validar el medio de pago. Revisa los datos e inténtalo nuevamente.',
          );
        }
      };
      instance.current = checkout;
      checkout.open();
      return true;
    });
  }
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (!validToken || order.total <= 0) return;
    const result = await payment.pay(sourceId);
    setSourceId('');
    if (result) {
      if (result.status === 'APPROVED') {
        toast.success('Pago aprobado.');
        onPaid(result);
      } else {
        toast.error(
          'El pago no fue aprobado. Consulta el estado de tu orden antes de volver a intentarlo.',
        );
      }
    }
  }
  return (
    <form noValidate className="card form-stack" onSubmit={(event) => void submit(event)}>
      <h2>
        <CreditCard size={22} /> Realizar pago
      </h2>
      <ErrorNotice message={payment.error || sdkAction.error} />
      {externalCheckout ? (
        <>
          <p>Ingresa tu medio de pago en el formulario seguro de Culqi.</p>
          <Button
            type="button"
            variant="secondary"
            pending={sdkAction.pending}
            disabled={payment.pending || order.total <= 0}
            onClick={() => void openCheckout()}
          >
            Elegir medio de pago
          </Button>
          {sourceId && (
            <p role="status" className="notice success">
              Medio de pago listo para confirmar.
            </p>
          )}
        </>
      ) : (
        <>
          <p className="muted">
            Pago de prueba: utiliza el token de pago proporcionado para la demostración.
          </p>
          <Field
            label="Token de pago de prueba"
            example="tkn_test_ok"
            rule="token"
            maxLength={100}
            value={sourceId}
            onChange={setSourceId}
            required
            error={
              submitted && !validToken ? 'Ingresa un token válido de hasta 100 caracteres.' : ''
            }
          />
        </>
      )}
      {order.total <= 0 && (
        <ErrorNotice message="La orden debe tener un importe mayor que cero para pagar." />
      )}
      <Button
        type="submit"
        pending={payment.pending}
        disabled={order.total <= 0 || (externalCheckout && !sourceId)}
      >
        Confirmar pago
      </Button>
      <p className="trust">
        <LockKeyhole size={16} />
        Los datos de tu tarjeta no se guardan en {APP_NAME}.
      </p>
    </form>
  );
}
````

### frontend/src/features/payments/hooks/usePayment.ts

````typescript
import { useAction } from '@/core/hooks/useAction';
import { paymentService } from '../services/paymentService';
/** Ejecuta el cobro; no reintenta automáticamente operaciones financieras. */
export function usePayment(storeId: string, orderId: string) {
  const action = useAction();
  return {
    ...action,
    pay: (token: string) => action.run(() => paymentService.pay(storeId, orderId, token)),
  };
}
````

### frontend/src/features/payments/index.ts

````typescript
export { PaymentPage } from './PaymentPage';
````

### frontend/src/features/payments/services/culqiService.ts

````typescript
import type { CulqiConstructor } from '../types';
import { UserFacingError } from '@/utils/errors';

/** Carga única del SDK oficial; se elimina el script fallido para permitir reintentos. */
let sdkPromise: Promise<CulqiConstructor> | undefined;
/** Carga el formulario oficial de Culqi con un tiempo de espera limitado. */
export function loadCulqi(): Promise<CulqiConstructor> {
  const sdkWindow = window as Window & { CulqiCheckout?: CulqiConstructor };
  if (sdkWindow.CulqiCheckout) return Promise.resolve(sdkWindow.CulqiCheckout);
  if (sdkPromise) return sdkPromise;
  sdkPromise = new Promise<CulqiConstructor>((resolve, reject) => {
    const script = document.createElement('script');
    script.src = 'https://js.culqi.com/checkout-js';
    script.async = true;
    const fail = () => {
      clearTimeout(timer);
      script.remove();
      sdkPromise = undefined;
      reject(
        new UserFacingError(
          'No se pudo cargar el formulario de Culqi. Revisa tu conexión e inténtalo nuevamente.',
        ),
      );
    };
    const timer = window.setTimeout(fail, 15000);
    script.onload = () => {
      clearTimeout(timer);
      if (sdkWindow.CulqiCheckout) resolve(sdkWindow.CulqiCheckout);
      else fail();
    };
    script.onerror = fail;
    document.head.appendChild(script);
  });
  return sdkPromise;
}
````

### frontend/src/features/payments/services/paymentService.ts

````typescript
import { api } from '@/config';
import type { Payment } from '../types';
/** Envía solo el token: importe y correo se obtienen del pedido en el servidor. */
export const paymentService = {
  pay: async (storeId: string, orderId: string, sourceId: string) =>
    (
      await api.post<Payment>(`/customer/stores/${storeId}/orders/${orderId}/payments`, {
        sourceId,
      })
    ).data,
};
````

### frontend/src/features/payments/types/index.ts

````typescript
import type { OrderStatus } from '@/features/orders';
/** Resultado real del endpoint de pagos; no se determina éxito en el navegador. */
export interface Payment {
  id: string;
  orderId: string;
  orderNumber: string;
  provider: string;
  transactionId: string;
  amount: number;
  status: 'PENDING' | 'APPROVED' | 'FAILED' | 'REFUNDED';
  orderStatus: OrderStatus;
  message: string | null;
}
/** Interfaz mínima del SDK externo usada para tokenización; nunca expone tarjeta o CVV. */
export interface CulqiInstance {
  token?: { id: string };
  error?: unknown;
  culqi: () => void;
  open: () => void;
  close: () => void;
}
/** Constructor público del Checkout Custom documentado por Culqi. */
export type CulqiConstructor = new (
  publicKey: string,
  config: Record<string, unknown>,
) => CulqiInstance;
````

### frontend/src/features/products/ProductsPage.tsx

````tsx
import { useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { Pencil, Plus } from 'lucide-react';
import {
  Badge,
  Button,
  Empty,
  ErrorNotice,
  Field,
  Loading,
  Modal,
  PageHeader,
  ProductImage,
  Select,
} from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import type { Store } from '@/features/stores';
import { money } from '@/utils/format';
import { useProducts } from './hooks/useProducts';
import { ProductForm } from './components/ProductForm';
import { productService } from './services/productService';
import type { Product, ProductStatus } from './types';
/** Administración del ciclo borrador/activo/archivado sin destruir historial. */
export function ProductsPage() {
  const store = useOutletContext<Store>();
  const resource = useProducts(store.id);
  const action = useAction();
  const [editor, setEditor] = useState<Product | 'new' | null>(null);
  const [search, setSearch] = useState('');
  const [status, setStatus] = useState('');
  const products = resource.data?.filter(
    (item) =>
      item.name.toLocaleLowerCase().includes(search.toLocaleLowerCase()) &&
      (!status || item.status === status),
  );
  return (
    <>
      <PageHeader
        title="Productos"
        description="Un catálogo cuidado, una mejor experiencia de compra."
        actions={
          <Button onClick={() => setEditor('new')}>
            <Plus size={16} />
            Agregar producto
          </Button>
        }
      />
      <div className="card filters">
        <Field
          label="Buscar producto"
          example="Café"
          value={search}
          maxLength={200}
          onChange={setSearch}
        />
        <Select
          label="Estado"
          value={status}
          onChange={setStatus}
          options={[
            { value: '', label: 'Todos los estados' },
            { value: 'ACTIVE', label: 'Activos' },
            { value: 'DRAFT', label: 'Borradores' },
            { value: 'ARCHIVED', label: 'Archivados' },
          ]}
        />
      </div>
      <ErrorNotice message={resource.error} retry={resource.reload} />
      <ErrorNotice message={action.error} />
      {resource.loading ? (
        <Loading />
      ) : !products?.length ? (
        <Empty title="No hay productos para mostrar">
          <p>Agrega un producto o cambia los filtros.</p>
        </Empty>
      ) : (
        <div className="card table-wrap">
          <table>
            <thead>
              <tr>
                <th>Producto</th>
                <th>Precio base</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {products.map((product) => (
                <tr key={product.id}>
                  <td>
                    <div className="product-cell">
                      <ProductImage src={product.imageUrl} name={product.name} />
                      <div>
                        <strong>{product.name}</strong>
                        <small>{product.taxExempt ? 'Exonerado de IGV' : 'Afecto a IGV'}</small>
                      </div>
                    </div>
                  </td>
                  <td>{money(product.basePrice, store.currency)}</td>
                  <td>
                    <Badge value={product.status} />
                  </td>
                  <td>
                    <div className="actions">
                      <Button
                        variant="secondary"
                        onClick={() => setEditor(product)}
                        aria-label={`Editar ${product.name}`}
                      >
                        <Pencil size={16} />
                      </Button>
                      <select
                        aria-label={`Estado de ${product.name}`}
                        value={product.status}
                        disabled={action.pending}
                        onChange={(event) => {
                          void action
                            .run(
                              () =>
                                productService.status(
                                  store.id,
                                  product.id,
                                  event.target.value as ProductStatus,
                                ),
                              'Estado actualizado.',
                            )
                            .then((result) => {
                              if (result) resource.reload();
                            });
                        }}
                      >
                        <option value="DRAFT">Borrador</option>
                        <option value="ACTIVE">Activo</option>
                        <option value="ARCHIVED">Archivado</option>
                      </select>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      {editor && (
        <Modal
          title={editor === 'new' ? 'Agregar producto' : 'Editar producto'}
          onClose={() => setEditor(null)}
        >
          <ProductForm
            storeId={store.id}
            product={editor === 'new' ? undefined : editor}
            onSaved={() => {
              setEditor(null);
              resource.reload();
            }}
          />
        </Modal>
      )}
    </>
  );
}
````

### frontend/src/features/products/components/ProductForm.tsx

````tsx
import { useState, type FormEvent } from 'react';
import { Button, ErrorNotice, Field, Loading, Select } from '@/core/components';
import { useCategories } from '@/features/categories';
import { useAction } from '@/core/hooks/useAction';
import { isHttpUrl, isMoney } from '@/utils/validation';
import { productService } from '../services/productService';
import type { Product } from '../types';
/** Datos del editor. Sin product se crea un producto en borrador. */
export interface ProductFormProps {
  storeId: string;
  product?: Product;
  onSaved: () => void;
}
/** Editor completo con validaciones numéricas y selección de categoría real. */
export function ProductForm({ storeId, product, onSaved }: ProductFormProps) {
  const categories = useCategories(storeId);
  const action = useAction();
  const [name, setName] = useState(product?.name || '');
  const [description, setDescription] = useState(product?.description || '');
  const [price, setPrice] = useState(product?.basePrice.toString() || '');
  const [imageUrl, setImageUrl] = useState(product?.imageUrl || '');
  const [categoryId, setCategoryId] = useState(product?.categoryId || '');
  const [taxExempt, setTaxExempt] = useState(product?.taxExempt || false);
  const [submitted, setSubmitted] = useState(false);
  const errors = {
    name: !name.trim() ? 'Ingresa el nombre del producto.' : '',
    price: !isMoney(price) ? 'Ingresa un precio no negativo con hasta dos decimales.' : '',
    image:
      imageUrl && !isHttpUrl(imageUrl) ? 'Ingresa una URL que empiece con https:// o http://.' : '',
  };
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (Object.values(errors).some(Boolean)) return;
    const result = await action.run(
      () =>
        productService.save(
          storeId,
          {
            name: name.trim(),
            description: description.trim(),
            basePrice: Number(price),
            imageUrl: imageUrl || null,
            categoryId: categoryId || null,
            taxExempt,
          },
          product?.id,
        ),
      'Producto guardado.',
    );
    if (result) onSaved();
  }
  return (
    <form noValidate className="form-stack" onSubmit={(event) => void submit(event)}>
      <ErrorNotice message={action.error} />
      <Field
        label="Nombre del producto"
        example="Café de especialidad 250 g"
        required
        maxLength={200}
        value={name}
        onChange={setName}
        error={submitted ? errors.name : ''}
      />
      <Field
        label="Descripción (opcional)"
        example="Café tostado de origen peruano"
        maxLength={5000}
        value={description}
        onChange={setDescription}
      />
      <Field
        label="Precio base (S/)"
        example="25.90"
        rule="money"
        inputMode="decimal"
        value={price}
        onChange={setPrice}
        required
        error={submitted ? errors.price : ''}
      />
      <Field
        label="URL de imagen (opcional)"
        example="https://mitienda.com/cafe.jpg"
        maxLength={500}
        type="url"
        value={imageUrl}
        onChange={setImageUrl}
        error={submitted ? errors.image : ''}
      />
      <ErrorNotice message={categories.error} retry={categories.reload} />
      {categories.loading ? (
        <Loading />
      ) : (
        <Select
          label="Categoría"
          value={categoryId}
          onChange={setCategoryId}
          options={[
            { value: '', label: 'Sin categoría' },
            ...(categories.data || []).map((item) => ({ value: item.id, label: item.name })),
          ]}
          hint="Ejemplo: Abarrotes."
        />
      )}
      <label className="checkbox">
        <input
          type="checkbox"
          checked={taxExempt}
          onChange={(event) => setTaxExempt(event.target.checked)}
        />
        Producto exonerado de IGV
      </label>
      <Button
        type="submit"
        pending={action.pending}
        disabled={categories.loading || !!categories.error}
      >
        {product ? 'Guardar cambios' : 'Guardar como borrador'}
      </Button>
    </form>
  );
}
````

### frontend/src/features/products/hooks/useProducts.ts

````typescript
import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { productService } from '../services/productService';
/** Recupera el catálogo completo del propietario, incluidos borradores y archivados. */
export function useProducts(storeId: string) {
  return useResource(useCallback(() => productService.list(storeId), [storeId]));
}
````

### frontend/src/features/products/index.ts

````typescript
export { ProductsPage } from './ProductsPage';
export { useProducts } from './hooks/useProducts';
export type { Product } from './types';
````

### frontend/src/features/products/services/productService.ts

````typescript
import { api } from '@/config';
import type { Product, ProductInput, ProductStatus } from '../types';
/** Productos aislados por tienda; nunca elimina historial de ventas. */
export const productService = {
  list: async (storeId: string) => (await api.get<Product[]>(`/stores/${storeId}/products`)).data,
  save: async (storeId: string, body: ProductInput, id?: string) =>
    id
      ? (await api.put<Product>(`/stores/${storeId}/products/${id}`, body)).data
      : (await api.post<Product>(`/stores/${storeId}/products`, body)).data,
  status: async (storeId: string, id: string, status: ProductStatus) =>
    (await api.patch<Product>(`/stores/${storeId}/products/${id}/status`, { status })).data,
};
````

### frontend/src/features/products/types/index.ts

````typescript
/** Estados de visibilidad aceptados por el backend. */
export type ProductStatus = 'DRAFT' | 'ACTIVE' | 'ARCHIVED';
/** Producto administrativo con información de publicación e impuestos. */
export interface Product {
  id: string;
  categoryId: string | null;
  name: string;
  description: string | null;
  basePrice: number;
  imageUrl: string | null;
  taxExempt: boolean;
  status: ProductStatus;
  createdAt: string;
}
/** Datos editables del producto; estado se modifica con endpoint separado. */
export interface ProductInput {
  name: string;
  description: string;
  basePrice: number;
  imageUrl: string | null;
  categoryId: string | null;
  taxExempt: boolean;
}
````

### frontend/src/features/stores/ManageStoresPage.tsx

````tsx
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Plus, Store as StoreIcon } from 'lucide-react';
import { Button, Empty, ErrorNotice, Loading, Modal, PageHeader } from '@/core/components';
import { useStores } from './hooks/useStores';
import { StoreForm } from './components/StoreForm';
/** Selector de tiendas del propietario; una cuenta puede administrar varios comercios. */
export function ManageStoresPage() {
  const resource = useStores(true);
  const [creating, setCreating] = useState(false);
  const navigate = useNavigate();
  return (
    <>
      <PageHeader
        title="Tus tiendas"
        description="Cada negocio tiene su propio espacio. Elige uno para comenzar."
        actions={
          <Button onClick={() => setCreating(true)}>
            <Plus size={16} />
            Crear tienda
          </Button>
        }
      />
      <ErrorNotice message={resource.error} retry={resource.reload} />
      {resource.loading ? (
        <Loading />
      ) : !resource.data?.length ? (
        <Empty title="Haz espacio para tu próximo negocio">
          <p>Crea tu primera tienda para agregar productos y recibir órdenes.</p>
          <Button onClick={() => setCreating(true)}>Crear mi primera tienda</Button>
        </Empty>
      ) : (
        <div className="store-grid">
          {resource.data.map((store) => (
            <Link key={store.id} to={`/admin/${store.id}`} className="card store-card">
              <StoreIcon size={32} />
              <h2>{store.name}</h2>
              <p className="muted">{store.subdomain}</p>
              <span className="store-link">Administrar tienda</span>
            </Link>
          ))}
        </div>
      )}
      {creating && (
        <Modal title="Crea tu tienda" onClose={() => setCreating(false)}>
          <StoreForm onCreated={(store) => navigate(`/admin/${store.id}`)} />
        </Modal>
      )}
    </>
  );
}
````

### frontend/src/features/stores/StoreSettingsPage.tsx

````tsx
import { useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { toast } from 'sonner';
import { Button, ErrorNotice, Field, PageHeader } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { storeService } from './services/storeService';
import type { Store } from './types';
/** Configuración fiscal: muestra porcentaje entero y envía la fracción esperada por Spring. */
export function StoreSettingsPage() {
  const store = useOutletContext<Store>();
  const [percentage, setPercentage] = useState(String(Math.round(store.taxRate * 100)));
  const [savedRate, setSavedRate] = useState(store.taxRate);
  const action = useAction();
  const valid = /^\d{1,3}$/.test(percentage) && Number(percentage) <= 100;
  return (
    <>
      <PageHeader
        title="Configuración de tienda"
        description="Información fiscal y preferencias de tu negocio."
      />
      <div className="two-columns">
        <section className="card form-stack">
          <h2>Datos de la tienda</h2>
          <dl className="data-list">
            <dt>Nombre comercial</dt>
            <dd>{store.name}</dd>
            <dt>RUC</dt>
            <dd>{store.taxId}</dd>
            <dt>Razón social</dt>
            <dd>{store.legalName}</dd>
            <dt>Subdominio</dt>
            <dd>{store.subdomain}</dd>
            <dt>Moneda</dt>
            <dd>{store.currency}</dd>
          </dl>
          <Button
            variant="secondary"
            onClick={() => {
              void navigator.clipboard
                .writeText(`${window.location.origin}/tienda/${store.subdomain}`)
                .then(() => toast.success('Enlace de tienda copiado.'))
                .catch(() =>
                  toast.error(
                    'No se pudo copiar el enlace. Cópialo desde la barra de direcciones de tu tienda.',
                  ),
                );
            }}
          >
            Copiar enlace de tienda
          </Button>
        </section>
        <form
          className="card form-stack"
          onSubmit={(event) => {
            event.preventDefault();
            if (valid)
              void action
                .run(() => storeService.tax(store.id, Number(percentage) / 100), 'IGV actualizado.')
                .then((result) => {
                  if (result) setSavedRate(result.taxRate);
                });
          }}
        >
          <h2>Impuesto general a las ventas</h2>
          <p className="muted">
            IGV actual: {Math.round(savedRate * 100)} %. El cambio se aplicará a las nuevas órdenes.
          </p>
          <ErrorNotice message={action.error} />
          <Field
            label="IGV (%)"
            example="18"
            rule="digits"
            inputMode="numeric"
            maxLength={3}
            value={percentage}
            onChange={setPercentage}
            required
            error={!valid ? 'Ingresa un porcentaje entero entre 0 y 100.' : ''}
          />
          <Button type="submit" disabled={!valid} pending={action.pending}>
            Guardar IGV
          </Button>
        </form>
      </div>
    </>
  );
}
````

### frontend/src/features/stores/StoresDirectoryPage.tsx

````tsx
import { Link } from 'react-router-dom';
import { ArrowRight, Store } from 'lucide-react';
import { Empty, ErrorNotice, Loading } from '@/core/components';
import { useStores } from './hooks/useStores';

/** Directorio completo, independiente del ranking de ventas de la portada. */
export function StoresDirectoryPage() {
  const resource = useStores();
  return (
    <section>
      <div className="page-heading">
        <div>
          <span className="eyebrow">DIRECTORIO BODEKO</span>
          <h1>Todas las tiendas</h1>
          <p>Descubre cada comercio y explora sus productos.</p>
        </div>
      </div>
      <ErrorNotice message={resource.error} retry={resource.reload} />
      {resource.loading ? (
        <Loading />
      ) : resource.error ? null : resource.data?.length === 0 ? (
        <Empty title="Aún no hay tiendas publicadas">
          <p>Los comercios aparecerán aquí cuando se registren.</p>
        </Empty>
      ) : (
        <div className="store-grid">
          {resource.data?.map((store, index) => (
            <Link
              className="card store-card"
              key={store.id}
              to={`/tienda/${encodeURIComponent(store.subdomain)}`}
            >
              <div className={`store-symbol tone-${index % 3}`}>
                <Store size={32} />
              </div>
              <h2>{store.name}</h2>
              <p className="muted">{store.subdomain}</p>
              <span className="store-link">
                Visitar tienda <ArrowRight size={16} />
              </span>
            </Link>
          ))}
        </div>
      )}
    </section>
  );
}
````

### frontend/src/features/stores/StoresPage.tsx

````tsx
import { Link } from 'react-router-dom';
import { useState } from 'react';
import { ArrowRight, ArrowUpRight, MapPin, Search, Store } from 'lucide-react';
import { Empty, ErrorNotice, Loading } from '@/core/components';
import { useSession } from '@/context/SessionContext';
import { FeaturedStoreCard } from './components/FeaturedStoreCard';
import { useTopSellingStores } from './hooks/useTopSellingStores';
import './landing.css';

/** Presenta Bodeko y las tiendas con más compras confirmadas desde la API. */
export function StoresPage() {
  const resource = useTopSellingStores();
  const { user } = useSession();
  const [search, setSearch] = useState('');
  const stores = resource.data ?? [];
  const normalizedSearch = search.trim().toLocaleLowerCase('es');
  const visibleStores = stores.filter((store) =>
    `${store.name} ${store.subdomain}`.toLocaleLowerCase('es').includes(normalizedSearch),
  );
  const accountDestination =
    user?.role === 'ADMIN' ? '/admin' : user ? '/cuenta/ordenes' : '/registro';

  return (
    <div className="bodeko-landing" id="inicio">
      <section className="landing-hero" aria-labelledby="landing-title">
        <div>
          <span className="landing-eyebrow">Un lugar para descubrir</span>
          <h1 id="landing-title">
            Detrás de cada tienda hay algo <em>especial.</em>
          </h1>
          <p className="landing-hero-copy">
            Encuentra negocios con personalidad, descubre lo que ofrecen y explora sus productos
            desde un solo lugar.
          </p>
          <div className="landing-hero-actions">
            <a href="#tiendas" className="button primary">
              Descubrir tiendas <ArrowRight size={17} aria-hidden="true" />
            </a>
            <span className="landing-caption">
              <i aria-hidden="true" />
              Comercios independientes
            </span>
          </div>
        </div>
        <div className="landing-art" aria-hidden="true">
          <div className="landing-art-main">
            <div className="landing-art-image" />
            <span className="landing-art-kicker">TIENDAS · HISTORIAS · IDEAS</span>
            <div className="landing-art-text">
              Encuentra tu
              <br />
              próxima favorita.<small>DESCUBRE BODEKO</small>
            </div>
          </div>
          <div className="landing-art-badge">
            <Store size={23} />
            Comercios independientes
          </div>
          <div className="landing-art-stamp">
            EXPLORA
            <br />
            LO LOCAL
            <ArrowUpRight size={22} />
          </div>
        </div>
      </section>

      <section id="tiendas" className="landing-section" aria-labelledby="top-stores-title">
        <div className="landing-section-heading">
          <div>
            <span className="landing-eyebrow">Las más elegidas</span>
            <h2 id="top-stores-title">
              Tiendas que destacan<span>.</span>
            </h2>
            <p>Los comercios con más compras confirmadas en Bodeko.</p>
          </div>
          <Link to="/tiendas" className="landing-directory-link">
            Ver todas las tiendas <ArrowRight size={16} aria-hidden="true" />
          </Link>
        </div>
        <div className="landing-toolbar">
          <div className="landing-ranking-label">
            Top 3 en ventas <span>Por cantidad de órdenes · Todo el historial</span>
          </div>
          <div className="landing-search">
            <label htmlFor="featured-store-search">Buscar entre las destacadas</label>
            <div>
              <Search size={17} aria-hidden="true" />
              <input
                id="featured-store-search"
                type="search"
                maxLength={100}
                aria-describedby="featured-store-example"
                placeholder="Ejemplo: Bodeko Market"
                value={search}
                onChange={(event) => setSearch(event.target.value)}
              />
            </div>
            <small id="featured-store-example">Ejemplo: Bodeko Market</small>
          </div>
        </div>
        <ErrorNotice message={resource.error} retry={resource.reload} />
        <div aria-busy={resource.loading}>
          {resource.loading ? (
            <Loading />
          ) : resource.error ? null : stores.length === 0 ? (
            <Empty title="Las primeras ventas están por llegar">
              <p>Cuando una tienda reciba compras confirmadas, podrá aparecer aquí.</p>
              <Link to="/tiendas" className="button secondary">
                Explorar todas las tiendas
              </Link>
            </Empty>
          ) : visibleStores.length === 0 ? (
            <Empty title="No encontramos tiendas destacadas">
              <p>Prueba con otro nombre o explora el directorio completo.</p>
              <button type="button" className="button secondary" onClick={() => setSearch('')}>
                Limpiar búsqueda
              </button>
            </Empty>
          ) : (
            <div className="landing-store-grid">
              {visibleStores.map((store) => (
                <FeaturedStoreCard
                  key={store.id}
                  store={store}
                  position={stores.findIndex((ranked) => ranked.id === store.id)}
                />
              ))}
            </div>
          )}
        </div>
        <p className="landing-mid-note">
          <MapPin size={14} aria-hidden="true" />
          Un espacio para descubrir negocios, a tu manera. Fotografías referenciales.
        </p>
      </section>

      <section id="como-funciona" className="landing-how" aria-labelledby="how-title">
        <div>
          <span className="landing-eyebrow">Así de sencillo</span>
          <h2 id="how-title">Tu próxima compra empieza aquí.</h2>
        </div>
        <div>
          <span className="landing-step-number">01 / DESCUBRE</span>
          <h3>Encuentra una tienda</h3>
          <p>Explora el directorio y encuentra negocios por su nombre.</p>
        </div>
        <div>
          <span className="landing-step-number">02 / EXPLORA</span>
          <h3>Mira sus productos</h3>
          <p>Entra al catálogo de cada comercio y conoce lo que ofrece.</p>
        </div>
        <div>
          <span className="landing-step-number">03 / ELIGE</span>
          <h3>Arma tu carrito</h3>
          <p>Inicia sesión, elige tus favoritos y continúa con tu compra.</p>
        </div>
      </section>
      <section className="landing-cta" aria-labelledby="sell-title">
        <div>
          <span className="landing-eyebrow">Para quienes venden</span>
          <h2 id="sell-title">
            Tu tienda también merece
            <br />
            su propio espacio.
          </h2>
          <p>
            Presenta tus productos y haz que más personas conozcan tu negocio. Bodeko reúne
            comercios, cada uno con su personalidad.
          </p>
        </div>
        <Link className="button" to={accountDestination}>
          {user ? 'Ir a mi cuenta' : 'Crear mi cuenta'}
          <ArrowUpRight size={17} aria-hidden="true" />
        </Link>
      </section>
    </div>
  );
}
````

### frontend/src/features/stores/components/FeaturedStoreCard.tsx

````tsx
import { Link } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';
import type { TopSellingStore } from '../types';

/** Tienda real y posición que ocupa en el listado de ventas. */
interface FeaturedStoreCardProps {
  store: TopSellingStore;
  position: number;
}

/** Tarjeta de tienda. La fotografía decorativa no representa su local real. */
export function FeaturedStoreCard({ store, position }: FeaturedStoreCardProps) {
  return (
    <article className={`landing-store${position === 0 ? ' landing-store-featured' : ''}`}>
      <div className={`landing-store-cover landing-cover-${position % 3}`} aria-hidden="true">
        <span className="landing-rank">{String(position + 1).padStart(2, '0')} / Más vendidas</span>
        <span className="landing-cover-caption">BODEKO / DESCUBRE</span>
      </div>
      <div className="landing-store-info">
        <p className="landing-sales">
          {store.salesCount} {store.salesCount === 1 ? 'compra' : 'compras'}
        </p>
        <h3>{store.name}</h3>
        <p className="landing-store-slug">{store.subdomain}</p>
        <Link to={`/tienda/${encodeURIComponent(store.subdomain)}`} className="landing-store-link">
          Explorar tienda <ArrowRight size={17} aria-hidden="true" />
          <span className="sr-only">: {store.name}</span>
        </Link>
      </div>
    </article>
  );
}
````

### frontend/src/features/stores/components/StoreForm.tsx

````tsx
import { useState, type FormEvent } from 'react';
import { Button, ErrorNotice, Field } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { storeService } from '../services/storeService';
import type { Store } from '../types';
/** onCreated recibe la tienda después de guardarla en el servidor. */
export interface StoreFormProps {
  onCreated: (store: Store) => void;
}
/** Alta de tienda con validación fiscal y de subdominio. */
export function StoreForm({ onCreated }: StoreFormProps) {
  const [name, setName] = useState('');
  const [taxId, setTaxId] = useState('');
  const [legalName, setLegalName] = useState('');
  const [subdomain, setSubdomain] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const action = useAction();
  const errors = {
    name: !name.trim() ? 'Ingresa el nombre de la tienda.' : '',
    taxId: !/^\d{11}$/.test(taxId) ? 'El RUC debe tener exactamente 11 dígitos.' : '',
    legalName: !legalName.trim() ? 'Ingresa la razón social.' : '',
    subdomain: !/^[a-z0-9]([a-z0-9-]*[a-z0-9])?$/.test(subdomain)
      ? 'Usa letras minúsculas, números o guiones, sin guiones en los extremos.'
      : '',
  };
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (Object.values(errors).some(Boolean)) return;
    const result = await action.run(
      () =>
        storeService.create({ name: name.trim(), taxId, legalName: legalName.trim(), subdomain }),
      'Tienda creada.',
    );
    if (result) onCreated(result);
  }
  return (
    <form noValidate onSubmit={(event) => void submit(event)} className="form-stack">
      <ErrorNotice message={action.error} />
      <Field
        label="Nombre de tienda"
        example="La Bodeguita de María"
        required
        maxLength={150}
        value={name}
        onChange={setName}
        error={submitted ? errors.name : ''}
      />
      <Field
        label="RUC"
        example="20123456789"
        required
        rule="digits"
        inputMode="numeric"
        maxLength={11}
        value={taxId}
        onChange={setTaxId}
        error={submitted ? errors.taxId : ''}
      />
      <Field
        label="Razón social"
        example="Comercial María S.A.C."
        required
        maxLength={200}
        value={legalName}
        onChange={setLegalName}
        error={submitted ? errors.legalName : ''}
      />
      <Field
        label="Subdominio"
        example="bodeguita-maria"
        required
        rule="slug"
        maxLength={63}
        value={subdomain}
        onChange={setSubdomain}
        error={submitted ? errors.subdomain : ''}
      />
      <p className="muted">
        Se creará en soles (PEN) con IGV del 18 %. Puedes modificar el IGV después.
      </p>
      <Button type="submit" pending={action.pending}>
        Crear tienda
      </Button>
    </form>
  );
}
````

### frontend/src/features/stores/hooks/useStores.ts

````typescript
import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { storeService } from '../services/storeService';
/** Recupera tiendas públicas o únicamente las del propietario autenticado. */
export function useStores(owned = false) {
  return useResource(
    useCallback(() => (owned ? storeService.owned() : storeService.publicList()), [owned]),
  );
}
````

### frontend/src/features/stores/hooks/useTopSellingStores.ts

````typescript
import { useResource } from '@/core/hooks/useResource';
import { storeService } from '../services/storeService';

/** Carga las tres tiendas más vendidas y permite reintentar si falla la API. */
export function useTopSellingStores() {
  return useResource(storeService.topSelling);
}
````

### frontend/src/features/stores/index.ts

````typescript
export { StoresPage } from './StoresPage';
export { StoresDirectoryPage } from './StoresDirectoryPage';
export { ManageStoresPage } from './ManageStoresPage';
export { StoreSettingsPage } from './StoreSettingsPage';
export { StoreForm } from './components/StoreForm';
export { useStores } from './hooks/useStores';
export { storeService } from './services/storeService';
export type { Store, PublicStore } from './types';
````

### frontend/src/features/stores/landing.css

````css
/* Estilos de la portada. No modifican el catálogo ni las páginas de cuenta. */
.landing-site {
  background: #faf9f5;
  color: #123b30;
}
.landing-site .main-container {
  max-width: 1330px;
  padding: 0 36px 68px;
}
.landing-site .site-header {
  background: #faf9f5f5;
  border-color: #123b3014;
  backdrop-filter: blur(15px);
}
.landing-site .header-inner {
  max-width: 1330px;
  height: 82px;
  padding: 0 36px;
  gap: 44px;
}
.landing-site .site-footer {
  max-width: 1330px;
  padding: 26px 36px 32px;
  border-color: #dce3dc;
}
.bodeko-landing {
  --landing-ink: #123b30;
  --landing-muted: #63746c;
}
.bodeko-landing a:focus-visible,
.bodeko-landing input:focus-visible,
.bodeko-landing button:focus-visible {
  outline: 3px solid #70baa5;
  outline-offset: 4px;
}
.bodeko-landing .button {
  min-height: 43px;
  padding: 11px 21px;
  font-weight: 700;
}
.landing-eyebrow {
  display: block;
  text-transform: uppercase;
  letter-spacing: 2px;
  font-size: 10px;
  font-weight: 800;
  color: #007e62;
}
.landing-eyebrow::before {
  content: '';
  display: inline-block;
  width: 21px;
  height: 1px;
  background: currentColor;
  vertical-align: middle;
  margin-right: 10px;
}
.landing-hero {
  padding: 55px 0 25px;
  display: grid;
  grid-template-columns: minmax(0, 1.06fr) minmax(0, 0.94fr);
  gap: 46px;
  min-height: 530px;
  align-items: center;
}
.landing-hero h1 {
  font-size: clamp(48px, 5.15vw, 73px);
  line-height: 1.065;
  letter-spacing: -3.9px;
  margin: 23px 0;
  font-weight: 750;
}
.landing-hero h1 em {
  font-family: Georgia, serif;
  color: #007e62;
  font-weight: 400;
  letter-spacing: -3px;
}
.landing-hero-copy {
  font-size: 15px;
  line-height: 1.8;
  color: #586b62;
  max-width: 460px;
  margin-bottom: 28px;
}
.landing-hero-actions {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}
.landing-caption {
  font-size: 12px;
  color: #63746c;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}
.landing-caption i {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #258465;
}
.landing-art {
  height: 425px;
  position: relative;
  isolation: isolate;
  min-width: 0;
}
.landing-art::before {
  content: '';
  position: absolute;
  inset: 20px 31px 15px 4px;
  border-radius: 8px;
  background: #dce8df;
  transform: rotate(-4deg);
  z-index: -1;
}
.landing-art-main {
  position: absolute;
  inset: 0 25px 15px;
  overflow: hidden;
  border-radius: 8px;
  background: #32614c;
}
.landing-art-image {
  position: absolute;
  inset: 0;
  background:
    linear-gradient(135deg, #07312426, #0f463384),
    url('https://images.unsplash.com/photo-1441986300917-64674bd600d8?auto=format&fit=crop&w=1050&q=85')
      center/cover;
}
.landing-art-kicker {
  position: absolute;
  left: 23px;
  top: 24px;
  color: #f1f7f1;
  font-size: 9px;
  letter-spacing: 2px;
  font-weight: 800;
}
.landing-art-text {
  position: absolute;
  left: 32px;
  bottom: 31px;
  color: white;
  font-family: Georgia, serif;
  font-size: 48px;
  font-style: italic;
  line-height: 1.1;
  letter-spacing: -1.5px;
}
.landing-art-text small {
  display: block;
  color: white;
  font-family: inherit;
  letter-spacing: 2px;
  font-size: 10px;
  font-weight: 700;
  margin-top: 20px;
  font-style: normal;
}
.landing-art-badge {
  position: absolute;
  top: 35px;
  right: 0;
  display: flex;
  align-items: center;
  gap: 9px;
  background: white;
  border: 1px solid #d7e4da;
  border-radius: 8px;
  padding: 12px 14px;
  box-shadow: 0 20px 55px #143e3117;
  font-size: 11px;
  font-weight: 750;
  transform: rotate(5deg);
}
.landing-art-badge svg {
  color: #007e62;
}
.landing-art-stamp {
  position: absolute;
  bottom: -3px;
  right: 0;
  width: 101px;
  height: 101px;
  border-radius: 50%;
  background: #cef0da;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  font-size: 10px;
  letter-spacing: 1px;
  font-weight: 800;
  line-height: 1.7;
  transform: rotate(13deg);
  border: 6px solid #faf9f5;
}
.landing-section {
  padding: 48px 0 55px;
  scroll-margin-top: 100px;
}
.landing-section-heading {
  display: flex;
  align-items: end;
  justify-content: space-between;
  gap: 24px;
  margin-bottom: 26px;
}
.bodeko-landing h2 {
  font-size: 36px;
  line-height: 1.15;
  letter-spacing: -1.6px;
  margin: 13px 0 0;
  font-weight: 750;
}
.landing-section-heading h2 span {
  color: #007e62;
}
.landing-section-heading p {
  margin: 9px 0 0;
  color: var(--landing-muted);
  font-size: 12px;
}
.landing-directory-link {
  font-size: 12px;
  font-weight: 700;
  display: inline-flex;
  align-items: center;
  gap: 9px;
}
.landing-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  margin-bottom: 22px;
}
.landing-ranking-label {
  font-size: 12px;
  font-weight: 750;
}
.landing-ranking-label span {
  display: block;
  font-size: 11px;
  font-weight: 400;
  color: var(--landing-muted);
  margin-top: 6px;
}
.landing-search {
  width: min(310px, 100%);
}
.landing-search label {
  display: block;
  font-size: 11px;
  color: #425f50;
  margin-bottom: 6px;
}
.landing-search small {
  display: block;
  margin-top: 5px;
  font-size: 10px;
  color: #63746c;
}
.landing-search > div {
  position: relative;
}
.landing-search svg {
  position: absolute;
  left: 13px;
  top: 13px;
  color: #63746c;
}
.landing-search input {
  background: white;
  color: #123b30;
  border: 1px solid #dce3dc;
  border-radius: 6px;
  width: 100%;
  padding: 12px 14px 12px 39px;
  font-size: 12px;
}
.landing-store-grid {
  display: grid;
  grid-template-columns: 1.12fr 1fr;
  grid-auto-rows: minmax(245px, auto);
  gap: 18px;
}
.landing-store {
  min-width: 0;
  overflow: hidden;
  border: 1px solid #dce3dc;
  background: white;
  border-radius: 8px;
  display: flex;
  transition:
    box-shadow 0.2s,
    border-color 0.2s;
}
.landing-store-featured {
  grid-row: span 2;
  flex-direction: column;
}
.landing-store-cover {
  position: relative;
  isolation: isolate;
  flex: 0 0 47%;
  background-color: #58705d;
  overflow: hidden;
}
.landing-store-cover::before {
  content: '';
  position: absolute;
  inset: 0;
  z-index: -1;
  background-position: center;
  background-size: cover;
  transition: transform 0.3s;
}
.landing-cover-0::before {
  background-image:
    linear-gradient(0deg, #0c2d2259, #0c2d2205 74%),
    url('https://images.unsplash.com/photo-1441986300917-64674bd600d8?auto=format&fit=crop&w=900&q=85');
}
.landing-cover-1::before {
  background-image:
    linear-gradient(0deg, #0a1c2966, #0a1c2905 74%),
    url('https://images.unsplash.com/photo-1491933382434-500287f9b54b?auto=format&fit=crop&w=700&q=85');
}
.landing-cover-2::before {
  background-image:
    linear-gradient(0deg, #3e1d0f61, #3e1d0f05 74%),
    url('https://images.unsplash.com/photo-1555529669-e69e7aa0ba9a?auto=format&fit=crop&w=700&q=85');
}
.landing-store-featured .landing-store-cover {
  flex: 1 1 auto;
  min-height: 0;
}
.landing-rank {
  position: absolute;
  top: 16px;
  left: 16px;
  background: white;
  padding: 7px 10px;
  border-radius: 6px;
  font-size: 10px;
  font-weight: 750;
}
.landing-cover-caption {
  position: absolute;
  left: 18px;
  bottom: 17px;
  color: white;
  font-size: 9px;
  letter-spacing: 1.6px;
  font-weight: 750;
  text-shadow: 0 1px 6px #000;
}
.landing-store-info {
  min-width: 0;
  padding: 20px;
  display: flex;
  flex: 1;
  flex-direction: column;
}
.landing-store-featured .landing-store-info {
  flex: 0 0 auto;
}
.landing-store-info h3 {
  color: #123b30;
  font-size: 20px;
  font-weight: 750;
  line-height: 1.23;
  letter-spacing: -0.5px;
  overflow-wrap: anywhere;
}
.landing-sales {
  color: #007e62;
  font-size: 10px;
  font-weight: 750;
  margin-bottom: 8px;
}
.landing-store-slug {
  font-size: 12px;
  color: #63746c;
  margin: 8px 0 20px;
  overflow-wrap: anywhere;
}
.landing-store-link {
  margin-top: auto;
  padding-top: 13px;
  border-top: 1px solid #e9eee8;
  font-weight: 750;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}
.landing-store-link .sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  overflow: hidden;
  clip-path: inset(50%);
  white-space: nowrap;
}
.landing-mid-note {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  color: #63746c;
  font-size: 11px;
  text-align: center;
  margin-top: 22px;
}
.landing-how {
  scroll-margin-top: 100px;
  background: #edf3ec;
  border: 1px solid #dce8dd;
  border-radius: 8px;
  padding: 34px 40px;
  display: grid;
  grid-template-columns: 1.1fr 1fr 1fr 1fr;
  gap: 23px;
  margin-bottom: 64px;
}
.landing-how h2 {
  font-size: 27px;
  letter-spacing: -0.9px;
  margin-top: 7px;
}
.landing-step-number {
  color: #007e62;
  font-size: 11px;
  font-weight: 750;
  letter-spacing: 1px;
}
.landing-how h3 {
  font-size: 14px;
  font-weight: 750;
  margin: 10px 0 5px;
}
.landing-how p {
  font-size: 12px;
  line-height: 1.65;
  color: #586d60;
}
.landing-cta {
  background: #103c2e;
  color: white;
  min-height: 240px;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  gap: 30px;
  align-items: center;
  padding: 38px 58px;
  position: relative;
  overflow: hidden;
}
.landing-cta::before,
.landing-cta::after {
  content: '';
  width: 340px;
  height: 340px;
  border: 1px solid #ffffff35;
  border-radius: 50%;
  position: absolute;
  right: -60px;
  top: -100px;
  pointer-events: none;
}
.landing-cta::after {
  width: 230px;
  height: 230px;
  right: 44px;
  top: -27px;
}
.landing-cta .landing-eyebrow {
  color: #a8e7c6;
}
.landing-cta h2 {
  font-size: 37px;
  letter-spacing: -1.5px;
  margin: 11px 0 12px;
}
.landing-cta p {
  font-size: 13px;
  line-height: 1.8;
  color: #c6dbce;
  max-width: 480px;
}
.landing-cta .button {
  background: #d8f0df;
  color: #113b2d;
  flex-shrink: 0;
  z-index: 1;
}
.landing-cta .button:hover {
  background: white;
}
@media (hover: hover) and (pointer: fine) {
  .landing-store:hover {
    border-color: #96bbae;
    box-shadow: 0 12px 30px #143e3114;
  }
  .landing-store:hover .landing-store-cover::before {
    transform: scale(1.035);
  }
}
@media (max-width: 979px) {
  .landing-hero {
    gap: 25px;
  }
  .landing-hero h1 {
    font-size: 58px;
  }
  .landing-store-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
    grid-auto-rows: auto;
  }
  .landing-store,
  .landing-store-featured {
    grid-row: auto;
    flex-direction: column;
  }
  .landing-store .landing-store-cover {
    flex: 0 0 210px;
  }
  .landing-store .landing-store-info {
    flex: 1;
  }
  .landing-how {
    grid-template-columns: 1fr 1fr;
  }
  .landing-cta {
    padding: 32px;
  }
}
@media (max-width: 820px) {
  .landing-site .main-container {
    padding: 0 24px 45px;
  }
  .landing-site .header-inner {
    padding: 0 24px;
    gap: 25px;
  }
  .landing-hero {
    grid-template-columns: 1fr;
    padding-top: 35px;
    gap: 32px;
  }
  .landing-hero h1 {
    max-width: 640px;
    font-size: 62px;
  }
  .landing-art {
    height: 335px;
  }
  .landing-art-text {
    font-size: 40px;
  }
  .landing-store-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
  .landing-section-heading {
    align-items: start;
  }
  .landing-cta h2 {
    font-size: 30px;
  }
  .landing-site .header-inner nav {
    gap: 15px;
  }
}
@media (max-width: 580px) {
  .landing-site .main-container {
    padding: 0 18px 35px;
  }
  .landing-site .header-inner {
    height: 66px;
    padding: 0 18px;
    gap: 12px;
  }
  .landing-site .header-inner nav {
    display: none;
  }
  .landing-site .header-account {
    gap: 12px;
  }
  .landing-site .header-account > a:first-child {
    display: none;
  }
  .landing-hero {
    gap: 26px;
  }
  .landing-hero h1 {
    font-size: 48px;
    letter-spacing: -2.6px;
  }
  .landing-hero h1 em {
    letter-spacing: -2px;
  }
  .landing-art {
    height: 270px;
  }
  .landing-art-main {
    inset: 0 13px 12px;
  }
  .landing-art-text {
    font-size: 32px;
    left: 20px;
    bottom: 20px;
  }
  .landing-art-stamp {
    width: 76px;
    height: 76px;
    font-size: 8px;
    border-width: 4px;
  }
  .landing-art-badge {
    padding: 8px;
    font-size: 9px;
  }
  .landing-section {
    padding: 34px 0;
  }
  .landing-section-heading {
    flex-direction: column;
    gap: 16px;
  }
  .landing-section-heading h2 {
    font-size: 29px;
  }
  .landing-toolbar {
    flex-direction: column;
    align-items: stretch;
  }
  .landing-search {
    width: 100%;
  }
  .landing-store-grid {
    grid-template-columns: 1fr;
  }
  .landing-store .landing-store-cover {
    flex-basis: 225px;
  }
  .landing-how {
    grid-template-columns: 1fr;
    padding: 25px 22px;
    gap: 24px;
    margin-bottom: 35px;
  }
  .landing-how h2 {
    font-size: 25px;
  }
  .landing-cta {
    display: block;
    padding: 32px 25px;
  }
  .landing-cta h2 {
    font-size: 28px;
  }
  .landing-cta .button {
    margin-top: 24px;
  }
  .landing-site .site-footer {
    padding: 26px 18px;
  }
}
@media (prefers-reduced-motion: reduce) {
  .landing-store:hover .landing-store-cover::before {
    transform: none;
  }
}
````

### frontend/src/features/stores/services/storeService.ts

````typescript
import { api } from '@/config';
import type { PublicStore, Store, StoreInput, TopSellingStore } from '../types';
/** Descubrimiento público separado de las operaciones administrativas. */
export const storeService = {
  publicList: async () => (await api.get<PublicStore[]>('/public/stores')).data,
  topSelling: async () => (await api.get<TopSellingStore[]>('/public/stores/top-selling')).data,
  bySubdomain: async (subdomain: string) =>
    (await api.get<PublicStore>(`/public/stores/by-subdomain/${encodeURIComponent(subdomain)}`))
      .data,
  owned: async () => (await api.get<Store[]>('/stores')).data,
  create: async (body: StoreInput) => (await api.post<Store>('/stores', body)).data,
  tax: async (id: string, taxRate: number) =>
    (await api.patch<Store>(`/stores/${id}/tax-rate`, { taxRate })).data,
};
````

### frontend/src/features/stores/types/index.ts

````typescript
import type { Id } from '@/core/types';
/** Información pública de una tienda disponible para visitantes. */
export interface PublicStore {
  id: Id;
  name: string;
  subdomain: string;
  currency: string;
  taxRate: number;
}
/** Tienda destacada por cantidad histórica de órdenes vendidas. */
export interface TopSellingStore extends PublicStore {
  salesCount: number;
}
/** Información fiscal privada visible únicamente al propietario. */
export interface Store extends PublicStore {
  taxId: string;
  legalName: string;
  createdAt: string;
}
/** Datos obligatorios al crear una tienda. */
export interface StoreInput {
  name: string;
  taxId: string;
  legalName: string;
  subdomain: string;
}
````

### frontend/src/main.tsx

````tsx
import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import { ErrorBoundary } from '@/core/components/ErrorBoundary';
import '@/assets/styles.css';

/** Punto de entrada de la SPA; StrictMode detecta efectos sin limpieza en desarrollo. */
const root = document.getElementById('root');
if (!root) throw new Error('No se encontró el elemento raíz de la aplicación.');
createRoot(root).render(
  <StrictMode>
    <ErrorBoundary>
      <App />
    </ErrorBoundary>
  </StrictMode>,
);
````

### frontend/src/utils/errors.test.ts

````typescript
import { AxiosError, AxiosHeaders } from 'axios';
import { describe, expect, it } from 'vitest';
import { errorMessage } from './errors';

/** Error remoto de prueba sin conectarse a cuentas externas. */
function remoteError(code: string, status = 400) {
  const config = { headers: new AxiosHeaders() };
  return new AxiosError('Technical error', 'ERR_BAD_RESPONSE', config, undefined, {
    status,
    statusText: 'error',
    config,
    headers: {},
    data: { code, message: 'Internal details in English' },
  });
}
describe('Errores visibles en español', () => {
  it('traduce rechazos de pago y conserva la privacidad del servidor', () => {
    expect(errorMessage(remoteError('PAYMENT_DECLINED'))).toContain('rechazado');
    expect(errorMessage(remoteError('CATEGORY_HAS_PRODUCTS'))).toContain('Reasigna');
    expect(errorMessage(remoteError('UNKNOWN', 500))).not.toContain('English');
  });
  it('explica fallos de red y expiración de sesión', () => {
    expect(errorMessage(new AxiosError('Network Error'))).toContain('conectar');
    expect(errorMessage(remoteError('UNKNOWN', 401))).toContain('sesión');
  });
});
````

### frontend/src/utils/errors.ts

````typescript
import axios from 'axios';

/** Error local deliberadamente redactado para el usuario, siempre en español. */
export class UserFacingError extends Error {}

/** Traducciones estables por código; no se muestran mensajes técnicos del servidor. */
const messages: Record<string, string> = {
  BAD_CREDENTIALS: 'El correo o la contraseña son incorrectos.',
  USER_EMAIL_EXIST: 'Este correo ya está registrado.',
  UNAUTHORIZED: 'Inicia sesión para continuar.',
  AUTHENTICATION_REQUIRED: 'Inicia sesión para continuar.',
  ACCOUNT_DISABLED: 'Tu cuenta está deshabilitada. Contacta al administrador.',
  ACCESS_DENIED: 'No tienes permiso para realizar esta acción.',
  CATEGORY_NAME_ALREADY_IN_USE: 'Ya existe una categoría con ese nombre.',
  CATEGORY_HAS_PRODUCTS: 'Reasigna los productos antes de eliminar esta categoría.',
  STORE_TAX_ID_ALREADY_IN_USE: 'Este RUC ya está registrado.',
  STORE_SUBDOMAIN_ALREADY_IN_USE: 'Ese subdominio ya está en uso.',
  INVENTORY_INSUFFICIENT_STOCK: 'No hay suficiente stock para completar la compra.',
  ORDER_EMPTY_CART: 'Agrega productos al carrito antes de continuar.',
  ORDER_INVALID_STATUS_TRANSITION: 'Ese cambio de estado no está permitido.',
  ORDER_INVOICE_REQUIRES_RUC: 'Para una factura necesitas un RUC de 11 dígitos.',
  ORDER_INVOICE_REQUIRES_BUSINESS_NAME: 'Ingresa la razón social para emitir la factura.',
  ORDER_RECEIPT_REQUIRES_DNI: 'Para una boleta necesitas un DNI de 8 dígitos.',
  ORDER_INVALID_DOCUMENT_NUMBER: 'Revisa el número de documento.',
  ORDER_PRODUCT_NOT_AVAILABLE: 'Un producto ya no está disponible. Revisa tu carrito.',
  CART_PRODUCT_NOT_AVAILABLE: 'El producto no está disponible.',
  OFFER_INVALID_DATE_RANGE: 'La fecha final debe ser posterior a la fecha inicial.',
  OFFER_DATE_RANGE_OVERLAP: 'La oferta se superpone con otra oferta existente.',
  PAYMENT_ORDER_NOT_PENDING: 'Esta orden ya no está pendiente de pago.',
  PAYMENT_INVALID_AMOUNT: 'El total a pagar debe ser mayor que cero.',
  PAYMENT_DECLINED: 'El pago fue rechazado. Revisa tu medio de pago o intenta con otro.',
  PAYMENT_REFUND_REJECTED: 'No se pudo procesar la devolución. Contacta al administrador.',
  PAYMENT_GATEWAY_INVALID_REQUEST:
    'La pasarela no aceptó los datos del pago. Genera un nuevo token e inténtalo nuevamente.',
  PAYMENT_GATEWAY_UNAVAILABLE: 'La pasarela de pagos no está disponible. Inténtalo más tarde.',
  VALIDATION_ERROR: 'Revisa los campos del formulario.',
  INVALID_REQUEST: 'Los datos enviados no son válidos.',
  INVALID_PARAMETER_TYPE: 'Revisa los filtros o identificadores enviados.',
  DATA_INTEGRITY_VIOLATION: 'No se pudo guardar: hay datos duplicados o registros relacionados.',
  INTERNAL_SERVER_ERROR: 'Ocurrió un error en el servidor. Inténtalo más tarde.',
};
/** Normaliza errores desconocidos y de Axios a un mensaje seguro en español. */
export function errorMessage(error: unknown): string {
  if (axios.isAxiosError<{ code?: string }>(error)) {
    if (!error.response)
      return 'No se pudo conectar con el servidor. Revisa tu conexión y que el backend esté disponible.';
    const code = error.response.data?.code;
    if (code && messages[code]) return messages[code];
    if (error.response.status === 401)
      return 'Tu sesión no es válida o expiró. Vuelve a iniciar sesión.';
    if (error.response.status === 403) return 'No tienes permiso para realizar esta acción.';
    if (error.response.status === 404) return 'No se encontró el recurso solicitado.';
    if (error.response.status === 409)
      return 'Los datos entran en conflicto con un registro existente. Actualiza la página e inténtalo de nuevo.';
    return 'No se pudo completar la solicitud. Inténtalo de nuevo.';
  }
  return error instanceof UserFacingError
    ? error.message
    : 'Ocurrió un error inesperado. Inténtalo nuevamente.';
}
````

### frontend/src/utils/format.ts

````typescript
/** Moneda del comercio, con formato peruano y dos decimales. */
export const money = (value: number, currency = 'PEN'): string =>
  new Intl.NumberFormat('es-PE', { style: 'currency', currency }).format(value);
/** Fecha de la API mostrada en la zona horaria del usuario. */
export const dateTime = (value: string): string =>
  new Intl.DateTimeFormat('es-PE', { dateStyle: 'medium', timeStyle: 'short' }).format(
    new Date(value),
  );
/** Traduce estados internos sin alterar sus valores de transporte. */
export const labels: Record<string, string> = {
  ACTIVE: 'Activo',
  DRAFT: 'Borrador',
  ARCHIVED: 'Archivado',
  PENDING: 'Pendiente',
  PAID: 'Pagada',
  SHIPPED: 'Enviada',
  DELIVERED: 'Entregada',
  CANCELLED: 'Cancelada',
  APPROVED: 'Aprobado',
  FAILED: 'Rechazado',
  REFUNDED: 'Reembolsado',
  RECEIPT: 'Boleta',
  INVOICE: 'Factura',
};
````

### frontend/src/utils/validation.test.ts

````typescript
import { describe, expect, it } from 'vitest';
import { isDocument, isEmail, isHttpUrl, isInteger, isMoney, permits } from './validation';

describe('Validación de entradas y pegado', () => {
  it('rechaza exponentes, signos y letras en cantidades e importes', () => {
    for (const value of ['1e3', '-1', '+4', '2a', '1,50']) {
      expect(permits(value, 'digits')).toBe(false);
      expect(permits(value, 'money')).toBe(false);
    }
    expect(permits('12.34', 'money')).toBe(true);
    expect(permits('12.345', 'money')).toBe(false);
  });
  it('admite nombres con acentos y evita dígitos', () => {
    expect(permits('María O’Neill', 'name')).toBe(true);
    expect(permits('María123', 'name')).toBe(false);
  });
  it('valida límites numéricos de los contratos Java y PostgreSQL', () => {
    expect(isInteger('0')).toBe(true);
    expect(isInteger('0', 1)).toBe(false);
    expect(isInteger('2147483648')).toBe(false);
    expect(isMoney('0.00')).toBe(true);
    expect(isMoney('9999999999.99')).toBe(true);
    expect(isMoney('10000000000')).toBe(false);
    expect(isMoney('')).toBe(false);
  });
  it('distingue tipos de documento', () => {
    expect(isDocument('12345678', 'DNI')).toBe(true);
    expect(isDocument('123456789', 'DNI')).toBe(false);
    expect(isDocument('20123456789', 'RUC')).toBe(true);
    expect(isDocument('AB123456', 'PASSPORT')).toBe(true);
    expect(isDocument('AB 1234', 'CE')).toBe(false);
  });
  it('admite correo con etiquetas y bloquea imágenes de protocolos ejecutables', () => {
    expect(isEmail('maria+tienda@correo.com')).toBe(true);
    expect(isEmail('maria@')).toBe(false);
    expect(isHttpUrl('https://example.com/product.png')).toBe(true);
    expect(isHttpUrl('javascript:alert(1)')).toBe(false);
  });
});
````

### frontend/src/utils/validation.ts

````typescript
/** Reglas de edición: se aplican también al pegado, conservando la accesibilidad. */
export type CharacterRule =
  'text' | 'name' | 'digits' | 'money' | 'slug' | 'email' | 'phone' | 'document' | 'token';
/** Rechaza la edición completa si introduce caracteres no admitidos. */
export function permits(value: string, rule: CharacterRule = 'text'): boolean {
  const rules: Record<CharacterRule, RegExp> = {
    text: /^[^\u0000-\u001f\u007f]*$/u,
    name: /^[\p{L}\p{M} '\u2019.-]*$/u,
    digits: /^\d*$/,
    money: /^\d{0,10}(\.\d{0,2})?$/,
    slug: /^[a-z0-9-]*$/,
    email: /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~@-]*$/,
    phone: /^\+?\d{0,15}$/,
    document: /^[a-zA-Z0-9]*$/,
    token: /^[a-zA-Z0-9_-]*$/,
  };
  return rules[rule].test(value);
}
/** Valida correo completo sin prohibir direcciones con etiquetas +. */
export const isEmail = (value: string): boolean => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
/** Valida enteros no negativos en el rango de Integer del backend. */
export const isInteger = (value: string, min = 0): boolean =>
  /^\d+$/.test(value) && Number(value) >= min && Number(value) <= 2147483647;
/** Valida importes con dos decimales y hasta diez cifras enteras. */
export const isMoney = (value: string): boolean => /^\d{1,10}(\.\d{1,2})?$/.test(value);
/** Valida imágenes remotas y evita protocolos ejecutables. */
export function isHttpUrl(value: string): boolean {
  try {
    return ['http:', 'https:'].includes(new URL(value).protocol);
  } catch {
    return false;
  }
}
/** Tipos de documento requeridos por historias de registro. */
export type DocumentType = 'DNI' | 'RUC' | 'CE' | 'PASSPORT';
/** Valida longitud y caracteres sin inventar una verificación tributaria. */
export function isDocument(value: string, kind: string): boolean {
  if (kind === 'DNI') return /^\d{8}$/.test(value);
  if (kind === 'RUC') return /^\d{11}$/.test(value);
  return /^[a-zA-Z0-9]{6,20}$/.test(value);
}
````

### frontend/tools/culqi-simulator.py

````python
"""Simulador LOCAL de cargos y devoluciones para el Entregable 2.

No conecta a Culqi, no recibe tarjetas y no realiza cobros. Escucha solo en loopback.
Tokens admitidos: tkn_test_approved y tkn_test_declined. No usar en producción.
Ejecutar: python tools/culqi-simulator.py
Backend: CULQI_API_URL=http://127.0.0.1:8090/v2
         CULQI_SECRET_KEY=simulator-local-only
"""
import json
import uuid
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer


class Handler(BaseHTTPRequestHandler):
    """Emula exclusivamente los campos consumidos por CulqiPaymentGateway.java."""

    def send_json(self, status, payload):
        body = json.dumps(payload).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def do_POST(self):
        if self.headers.get("Authorization") != "Bearer simulator-local-only":
            return self.send_json(401, {"object": "error", "type": "authentication_error", "user_message": "Configuración de simulador inválida."})
        length = int(self.headers.get("Content-Length", "0"))
        if length > 16384:
            return self.send_json(413, {"object": "error"})
        try:
            data = json.loads(self.rfile.read(length))
        except (ValueError, UnicodeDecodeError):
            return self.send_json(400, {"object": "error", "type": "parameter_error"})
        amount = data.get("amount")
        if not isinstance(amount, int) or isinstance(amount, bool) or amount <= 0:
            return self.send_json(400, {"object": "error", "type": "parameter_error", "user_message": "Importe inválido."})
        if self.path == "/v2/charges":
            token = data.get("source_id")
            if token not in ("tkn_test_approved", "tkn_test_declined"):
                return self.send_json(400, {"object": "error", "type": "parameter_error", "user_message": "Token de simulación desconocido."})
            charge_id = "chr_test_" + uuid.uuid4().hex[:16]
            if token == "tkn_test_declined":
                return self.send_json(402, {"object": "error", "type": "card_error", "charge_id": charge_id, "decline_code": "insufficient_funds", "user_message": "Saldo insuficiente en la simulación.", "merchant_message": "Simulated decline"})
            return self.send_json(201, {"object": "charge", "id": charge_id, "outcome": {"user_message": "Pago de prueba aprobado."}})
        if self.path == "/v2/refunds" and str(data.get("charge_id", "")).startswith("chr_test_"):
            return self.send_json(201, {"object": "refund", "id": "ref_test_" + uuid.uuid4().hex[:16], "status": "exitoso"})
        return self.send_json(404, {"object": "error", "type": "not_found"})

    def log_message(self, format_string, *args):
        # No registra datos de la solicitud ni cabeceras de autorización.
        print("Simulador local:", format_string % args)


if __name__ == "__main__":
    print("Simulador sin cobros reales: http://127.0.0.1:8090/v2", flush=True)
    try:
        ThreadingHTTPServer(("127.0.0.1", 8090), Handler).serve_forever()
    except KeyboardInterrupt:
        pass
````

### frontend/tools/verify-api.mjs

````javascript
/**
 * Prueba de integración con datos sintéticos en una base desechable.
 * Requiere Spring conectado al simulador del equipo en scripts/culqi-mock/server.py.
 * QA_APPROVED_TOKEN permite utilizar otro simulador compatible.
 * No ejecutar contra producción: crea cuentas, tiendas, productos y órdenes.
 * Ejecutar: node tools/verify-api.mjs --disposable
 */
import assert from 'node:assert/strict';

if (!process.argv.includes('--disposable')) {
  throw new Error('Esta prueba crea datos. Usa --disposable únicamente con una base desechable.');
}
const base = process.env.QA_API_URL || 'http://localhost:8080/api';
if (!['localhost', '127.0.0.1'].includes(new URL(base).hostname)) {
  throw new Error('La prueba solo admite servidores locales de desarrollo.');
}
const suffix = Date.now().toString();
const password = 'BodekoTest2026!';
const approvedToken = process.env.QA_APPROVED_TOKEN || 'tkn_test_ok';
let assertions = 0;
async function request(path, { method = 'GET', body, token, expected = 200 } = {}) {
  const response = await fetch(`${base}${path}`, {
    method,
    headers: {
      ...(body ? { 'Content-Type': 'application/json' } : {}),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  });
  assert.equal(
    response.status,
    expected,
    `${method} ${path}: esperaba ${expected}, recibió ${response.status}`,
  );
  assertions++;
  return response.status === 204 ? null : response.json();
}
const ownerEmail = `qa.owner.${suffix}@example.test`;
const customerEmail = `qa.customer.${suffix}@example.test`;
const owner = await request('/auth/register-owner', {
  method: 'POST',
  expected: 201,
  body: {
    email: ownerEmail,
    password,
    fullName: 'Propietario de Prueba',
    phoneNumber: '+51999999999',
  },
});
const customer = await request('/auth/register-customer', {
  method: 'POST',
  expected: 201,
  body: {
    email: customerEmail,
    password,
    fullName: 'Cliente de Prueba',
    documentType: 'DNI',
    documentNumber: '12345678',
  },
});
await request('/auth/register-customer', {
  method: 'POST',
  expected: 409,
  body: { email: customerEmail, password, fullName: 'Duplicado de Prueba' },
});
await request('/auth/register-customer', {
  method: 'POST',
  expected: 400,
  body: {
    email: `qa.invalid.${suffix}@example.test`,
    password,
    fullName: 'Documento Invalido',
    documentType: 'DNI',
    documentNumber: '123',
  },
});
await request('/auth/me', { token: owner.token });
const store = await request('/stores', {
  method: 'POST',
  token: owner.token,
  expected: 201,
  body: {
    name: 'Tienda QA Bodeko',
    taxId: `20${suffix.slice(-9)}`,
    legalName: 'Comercio de Prueba S.A.C.',
    subdomain: `qa-${suffix}`,
  },
});
assert.equal(store.taxRate, 0.18);
await request(`/public/stores/by-subdomain/${store.subdomain}`);
const publicStores = await request('/public/stores');
assert.ok(publicStores.some((item) => item.id === store.id));
assert.equal(publicStores.find((item) => item.id === store.id).taxId, undefined);
await request(`/stores/${store.id}/products`, { token: customer.token, expected: 403 });
const category = await request(`/stores/${store.id}/categories`, {
  method: 'POST',
  token: owner.token,
  expected: 201,
  body: { name: 'Abarrotes QA' },
});
await request(`/stores/${store.id}/categories`, {
  method: 'POST',
  token: owner.token,
  expected: 409,
  body: { name: 'Abarrotes QA' },
});
await request(`/public/stores/${store.id}/categories`);
const product = await request(`/stores/${store.id}/products`, {
  method: 'POST',
  token: owner.token,
  expected: 201,
  body: {
    name: 'Café peruano de prueba',
    description: 'Producto sintético para validar la integración.',
    basePrice: 25.9,
    imageUrl: null,
    categoryId: category.id,
    taxExempt: false,
  },
});
assert.equal(product.status, 'DRAFT');
await request(`/public/stores/${store.id}/products/${product.id}`, { expected: 404 });
const stock = await request(`/stores/${store.id}/products/${product.id}/inventory`, {
  token: owner.token,
});
assert.equal(stock.quantity, 0);
assert.equal(stock.lowStockThreshold, 5);
await request(`/stores/${store.id}/products/${product.id}/inventory/quantity`, {
  method: 'PATCH',
  token: owner.token,
  body: { quantity: 20 },
});
await request(`/stores/${store.id}/products/${product.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'ACTIVE' },
});
await request(`/stores/${store.id}/categories/${category.id}`, {
  method: 'DELETE',
  token: owner.token,
  expected: 409,
});
await request(`/stores/${store.id}/products/${product.id}/offers`, {
  method: 'POST',
  token: owner.token,
  expected: 201,
  body: {
    offerPrice: 20,
    startsAt: new Date(Date.now() - 60000).toISOString(),
    endsAt: new Date(Date.now() + 86400000).toISOString(),
  },
});
const catalog = await request(`/public/stores/${store.id}/products?search=caf&inStock=true`);
assert.equal(catalog.totalElements, 1);
assert.equal(catalog.content[0].offerPrice, 20);
await request(`/public/stores/${store.id}/products/${product.id}`);
await request(`/customer/stores/${store.id}/cart/items`, {
  method: 'POST',
  token: customer.token,
  body: { productId: product.id, quantity: 1 },
});
const cart = await request(`/customer/stores/${store.id}/cart/items`, {
  method: 'POST',
  token: customer.token,
  body: { productId: product.id, quantity: 1 },
});
assert.equal(cart.totalQuantity, 2);
assert.equal(cart.items.length, 1);
assert.equal(cart.subtotal, 40);
const login = await request('/auth/login', {
  method: 'POST',
  body: { email: customerEmail, password },
});
assert.equal(
  (await request(`/customer/stores/${store.id}/cart`, { token: login.token })).totalQuantity,
  2,
);
const order = await request(`/customer/stores/${store.id}/orders`, {
  method: 'POST',
  token: customer.token,
  expected: 201,
  body: {
    invoiceType: 'RECEIPT',
    documentType: 'DNI',
    documentNumber: '12345678',
    businessName: null,
    billingAddress: 'Dirección sintética de prueba',
  },
});
assert.equal(order.subtotal, 40);
assert.equal(order.taxAmount, 7.2);
assert.equal(order.total, 47.2);
const other = await request('/auth/register-customer', {
  method: 'POST',
  expected: 201,
  body: { email: `qa.other.${suffix}@example.test`, password, fullName: 'Otro Cliente' },
});
await request(`/customer/stores/${store.id}/orders/${order.id}`, {
  token: other.token,
  expected: 404,
});
await request(`/customer/stores/${store.id}/orders/${order.id}`, { token: customer.token });
assert.equal(
  (await request(`/stores/${store.id}/products/${product.id}/inventory`, { token: owner.token }))
    .quantity,
  18,
);
await request(`/customer/stores/${store.id}/orders/${order.id}/payments`, {
  method: 'POST',
  token: customer.token,
  expected: 402,
  body: { sourceId: 'tkn_test_declined' },
});
assert.equal(
  (await request(`/customer/stores/${store.id}/orders/${order.id}`, { token: customer.token }))
    .status,
  'PENDING',
);
const payment = await request(`/customer/stores/${store.id}/orders/${order.id}/payments`, {
  method: 'POST',
  token: customer.token,
  expected: 201,
  body: { sourceId: approvedToken },
});
assert.equal(payment.status, 'APPROVED');
assert.equal(
  (await request(`/stores/${store.id}/orders/${order.id}`, { token: owner.token })).status,
  'PAID',
);
await request(`/stores/${store.id}/orders/${order.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'CANCELLED' },
});
assert.equal(
  (await request(`/stores/${store.id}/products/${product.id}/inventory`, { token: owner.token }))
    .quantity,
  20,
);
await request(`/stores/${store.id}/tax-rate`, {
  method: 'PATCH',
  token: owner.token,
  body: { taxRate: 0.18 },
});
await request(`/stores/${store.id}/products/${product.id}/inventory/low-stock-threshold`, {
  method: 'PATCH',
  token: owner.token,
  body: { lowStockThreshold: 4 },
});
await request(`/stores/${store.id}/products/${product.id}/inventory/quantity`, {
  method: 'PATCH',
  token: owner.token,
  expected: 400,
  body: { quantity: -1 },
});
await request(`/stores/${store.id}/products/${product.id}/offers`, {
  method: 'POST',
  token: owner.token,
  expected: 400,
  body: { offerPrice: 10, startsAt: '2026-11-20T10:00:00Z', endsAt: '2026-11-19T10:00:00Z' },
});
const emptyCategory = await request(`/stores/${store.id}/categories`, {
  method: 'POST',
  token: owner.token,
  expected: 201,
  body: { name: 'Temporal QA' },
});
await request(`/stores/${store.id}/categories/${emptyCategory.id}`, {
  method: 'PUT',
  token: owner.token,
  body: { name: 'Temporal editada QA' },
});
await request(`/stores/${store.id}/categories/${emptyCategory.id}`, {
  method: 'DELETE',
  token: owner.token,
  expected: 204,
});
await request(`/customer/stores/${store.id}/cart/items`, {
  method: 'POST',
  token: customer.token,
  body: { productId: product.id, quantity: 1 },
});
await request(`/customer/stores/${store.id}/cart/items/${product.id}`, {
  method: 'PATCH',
  token: customer.token,
  body: { quantity: 3 },
});
const emptyCart = await request(`/customer/stores/${store.id}/cart/items/${product.id}`, {
  method: 'DELETE',
  token: customer.token,
});
assert.equal(emptyCart.totalQuantity, 0);
await request(`/customer/stores/${store.id}/cart/items`, {
  method: 'POST',
  token: customer.token,
  body: { productId: product.id, quantity: 1 },
});
const invoice = await request(`/customer/stores/${store.id}/orders`, {
  method: 'POST',
  token: customer.token,
  expected: 201,
  body: {
    invoiceType: 'INVOICE',
    documentType: 'RUC',
    documentNumber: '20123456789',
    businessName: 'Empresa de Prueba S.A.C.',
  },
});
await request(`/customer/stores/${store.id}/orders/${invoice.id}/payments`, {
  method: 'POST',
  token: customer.token,
  expected: 201,
  body: { sourceId: approvedToken },
});
await request(`/stores/${store.id}/orders/${invoice.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'SHIPPED' },
});
await request(`/stores/${store.id}/orders/${invoice.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'DELIVERED' },
});
await request(`/stores/${store.id}/orders/${invoice.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  expected: 409,
  body: { status: 'CANCELLED' },
});
await request(`/stores/${store.id}/products/${product.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'ARCHIVED' },
});
await request(`/public/stores/${store.id}/products/${product.id}`, { expected: 404 });
await request(`/stores/${store.id}/products/${product.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'ACTIVE' },
});
console.log(
  JSON.stringify(
    {
      status: 'PASS',
      httpChecks: assertions,
      subdomain: store.subdomain,
      storeId: store.id,
      productId: product.id,
      orderId: order.id,
      syntheticAccounts: { ownerEmail, customerEmail },
      note: 'Solo datos sintéticos en base desechable. Pago simulado; API y PostgreSQL reales.',
    },
    null,
    2,
  ),
);
````

### frontend/tools/verify-customer-orders.mjs

````javascript
/**
 * Regresión del área de cliente contra Spring y PostgreSQL reales.
 * Solo para una BD desechable y el simulador del equipo, sin cobros reales.
 * QA_API_URL=http://localhost:8081/api node tools/verify-customer-orders.mjs --disposable
 * Crea cuentas, tiendas, productos y órdenes sintéticas. No elimina esos datos.
 */
import assert from 'node:assert/strict';

if (!process.argv.includes('--disposable'))
  throw new Error('Usa --disposable con una base de pruebas.');
const base = process.env.QA_API_URL || 'http://localhost:8081/api';
if (!['localhost', '127.0.0.1'].includes(new URL(base).hostname))
  throw new Error('Solo se permiten servidores locales.');
const suffix = Date.now().toString();
const password = 'BodekoOrders2026!';
let checks = 0;
async function request(path, { token, method = 'GET', body, status = 200 } = {}) {
  const response = await fetch(base + path, {
    method,
    headers: {
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(body ? { 'Content-Type': 'application/json' } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  });
  assert.equal(response.status, status, `${method} ${path}`);
  checks++;
  return response.json();
}
async function register(email, owner = false) {
  return (
    await request(`/auth/register-${owner ? 'owner' : 'customer'}`, {
      method: 'POST',
      status: 201,
      body: {
        fullName: owner ? 'Propietario QA Ordenes' : 'Cliente QA Ordenes',
        email,
        password,
        phoneNumber: '+51999999888',
      },
    })
  ).token;
}
const admin = await register(`orders.owner.${suffix}@example.test`, true);
const customer = await register(`orders.customer.${suffix}@example.test`);
const other = await register(`orders.other.${suffix}@example.test`);
await request('/customer/orders', { status: 401 });
await request('/customer/orders', { token: admin, status: 403 });
assert.equal((await request('/customer/orders', { token: customer })).totalElements, 0);

async function fixtureStore(index) {
  const store = await request('/stores', {
    method: 'POST',
    token: admin,
    status: 201,
    body: {
      name: `Tienda QA Ordenes ${index}`,
      taxId: `29${suffix.slice(-8)}${index}`,
      legalName: 'Comercio de Prueba',
      subdomain: `orders-${suffix}-${index}`,
    },
  });
  const product = await request(`/stores/${store.id}/products`, {
    method: 'POST',
    token: admin,
    status: 201,
    body: {
      name: 'Producto de prueba de ordenes',
      basePrice: 10,
      taxExempt: false,
    },
  });
  await request(`/stores/${store.id}/products/${product.id}/inventory/quantity`, {
    method: 'PATCH',
    token: admin,
    body: { quantity: 40 },
  });
  await request(`/stores/${store.id}/products/${product.id}/status`, {
    method: 'PATCH',
    token: admin,
    body: { status: 'ACTIVE' },
  });
  return { store, product };
}
const first = await fixtureStore(1);
const second = await fixtureStore(2);
async function checkout(fixture, token = customer) {
  await request(`/customer/stores/${fixture.store.id}/cart/items`, {
    method: 'POST',
    token,
    body: { productId: fixture.product.id, quantity: 2 },
  });
  return request(`/customer/stores/${fixture.store.id}/orders`, {
    method: 'POST',
    token,
    status: 201,
    body: { invoiceType: 'RECEIPT', documentType: 'DNI', documentNumber: '12345678' },
  });
}
async function pay(fixture, order) {
  return request(`/customer/stores/${fixture.store.id}/orders/${order.id}/payments`, {
    method: 'POST',
    token: customer,
    status: 201,
    body: { sourceId: process.env.QA_APPROVED_TOKEN || 'tkn_test_ok' },
  });
}
async function stock(fixture) {
  return (
    await request(`/stores/${fixture.store.id}/products/${fixture.product.id}/inventory`, {
      token: admin,
    })
  ).quantity;
}
const pending = await checkout(first);
const paid = await checkout(first);
await pay(first, paid);
const shipped = await checkout(second);
await pay(second, shipped);
await request(`/stores/${second.store.id}/orders/${shipped.id}/status`, {
  method: 'PATCH',
  token: admin,
  body: { status: 'SHIPPED' },
});
const delivered = await checkout(second);
await pay(second, delivered);
await request(`/stores/${second.store.id}/orders/${delivered.id}/status`, {
  method: 'PATCH',
  token: admin,
  body: { status: 'SHIPPED' },
});
await request(`/stores/${second.store.id}/orders/${delivered.id}/status`, {
  method: 'PATCH',
  token: admin,
  body: { status: 'DELIVERED' },
});
const foreign = await checkout(first, other);
const list = await request('/customer/orders?size=2', { token: customer });
assert.equal(list.totalElements, 4);
assert.equal(list.content.length, 2);
assert.equal(list.totalPages, 2);
const all = await request('/customer/orders', { token: customer });
assert.equal(new Set(all.content.map((order) => order.storeId)).size, 2);
assert.ok(!all.content.some((order) => order.id === foreign.id));
assert.equal(
  (await request('/customer/orders?status=PAID', { token: customer })).content[0].id,
  paid.id,
);
assert.equal((await request('/customer/orders?page=-1&size=0', { token: customer })).size, 1);
await request('/customer/orders?status=INVALID', { token: customer, status: 400 });
await request(`/customer/orders/${foreign.id}`, { token: customer, status: 404 });
await request(`/customer/orders/${foreign.id}/cancel`, {
  method: 'POST',
  token: customer,
  status: 404,
});
await request(`/customer/orders/${pending.id}/cancel`, {
  method: 'POST',
  token: other,
  status: 404,
});
const before = await stock(first);
const cancellation = await request(`/customer/orders/${pending.id}/cancel`, {
  method: 'POST',
  token: customer,
});
assert.equal(cancellation.order.status, 'CANCELLED');
assert.equal(await stock(first), before + 2);
await request(`/customer/orders/${pending.id}/cancel`, {
  method: 'POST',
  token: customer,
  status: 409,
});
assert.equal(await stock(first), before + 2);
assert.equal(
  (await request(`/customer/orders/${paid.id}/cancel`, { method: 'POST', token: customer })).order
    .status,
  'CANCELLED',
);
assert.equal(await stock(first), before + 4);
for (const order of [shipped, delivered]) {
  await request(`/customer/orders/${order.id}/cancel`, {
    method: 'POST',
    token: customer,
    status: 409,
  });
}
// Dos cancelaciones simultáneas: una gana el bloqueo y la otra no repone stock otra vez.
const concurrent = await checkout(first);
const beforeConcurrent = await stock(first);
const attempts = await Promise.all(
  [1, 2].map(() =>
    fetch(`${base}/customer/orders/${concurrent.id}/cancel`, {
      method: 'POST',
      headers: { Authorization: `Bearer ${customer}` },
    }),
  ),
);
assert.deepEqual(attempts.map((response) => response.status).sort(), [200, 409]);
checks += 2;
assert.equal(await stock(first), beforeConcurrent + 2);
// Se requiere el seed demo para este caso: el simulador no conoce su cargo histórico.
const login = await request('/auth/login', {
  method: 'POST',
  body: { email: 'cliente.demo@bodeko.test', password: 'BodekoDemo2026!' },
});
const seedPaid = 'b0de7000-0000-4000-8000-000000000002';
await request(`/customer/orders/${seedPaid}/cancel`, {
  method: 'POST',
  token: login.token,
  status: 422,
});
assert.equal(
  (await request(`/customer/orders/${seedPaid}`, { token: login.token })).order.status,
  'PAID',
);
console.log(
  JSON.stringify(
    {
      result: 'PASS',
      httpChecks: checks,
      scenarios: [
        'listado de varias tiendas',
        'paginación y filtro',
        'aislamiento por cliente y rol',
        'cancelación pendiente y pagada',
        'restricción enviada/entregada',
        'concurrencia sin doble stock',
        'rollback al rechazar reembolso',
      ],
      paidOrderId: paid.id,
      cancelledOrderId: pending.id,
    },
    null,
    2,
  ),
);
````

### frontend/tools/verify-top-selling.mjs

````javascript
/** Prueba el ranking con datos sintéticos. Usar solo con una base vacía y desechable. */
import assert from 'node:assert/strict';
import { execFileSync } from 'node:child_process';

if (!process.argv.includes('--disposable'))
  throw new Error('Añade --disposable solo para una base de pruebas vacía.');
const database = new URL(process.env.QA_DATABASE_URL || '');
const api = new URL(process.env.QA_API_URL || '');
if (
  ![database, api].every((url) => ['127.0.0.1', 'localhost'].includes(url.hostname)) ||
  !/(qa|test)/i.test(database.pathname) ||
  database.password
) {
  throw new Error(
    'Usa servidores locales y una base cuyo nombre contenga qa o test. La contraseña se configura con PGPASSWORD.',
  );
}
const psql = process.env.QA_PSQL || 'psql';
function sql(statement) {
  return execFileSync(psql, [database.href, '-X', '-v', 'ON_ERROR_STOP=1', '-At'], {
    input: statement,
    encoding: 'utf8',
  }).trim();
}
async function ranking() {
  const response = await fetch(`${api.href.replace(/\/$/, '')}/public/stores/top-selling`);
  assert.equal(response.status, 200, 'El ranking debe ser público.');
  return response.json();
}
assert.equal(
  sql('SELECT count(*) FROM stores;'),
  '0',
  'La base debe estar vacía antes de crear datos.',
);
assert.deepEqual(await ranking(), []);
sql(`
BEGIN;
INSERT INTO users(id,email,password_hash,full_name,role) VALUES
 ('10000000-0000-0000-0000-000000000001','ranking.owner@example.test','sin-acceso','Propietario QA','ADMIN'),
 ('10000000-0000-0000-0000-000000000002','ranking.customer@example.test','sin-acceso','Cliente QA','CUSTOMER');
INSERT INTO stores(id,owner_id,name,tax_id,legal_name,subdomain)
SELECT ('20000000-0000-0000-0000-' || lpad(n::text,12,'0'))::uuid,
 '10000000-0000-0000-0000-000000000001', name, '20' || lpad(n::text,9,'0'), name, slug
FROM (VALUES (1,'Bodeko Market','bodeko-market'),(2,'Casa y Diseño','casa-diseno'),(3,'Tecnología Bodeko','tecnologia-bodeko'),(4,'Taller Local','taller-local'),(5,'Sin ventas confirmadas','sin-ventas')) s(n,name,slug);
INSERT INTO orders(id,store_id,customer_id,order_number,customer_document_type,customer_document_number,subtotal,tax_amount,total,applied_tax_rate,status)
SELECT gen_random_uuid(), ('20000000-0000-0000-0000-' || lpad(store_number::text,12,'0'))::uuid,
 '10000000-0000-0000-0000-000000000002', 'RANK-' || store_number || '-' || n, 'DNI','12345678',
 CASE WHEN store_number=4 THEN 10000 ELSE 10 END, 0, CASE WHEN store_number=4 THEN 10000 ELSE 10 END, 0,
 CASE WHEN store_number=5 THEN CASE WHEN n%2=0 THEN 'PENDING' ELSE 'CANCELLED' END
      WHEN n%3=0 THEN 'DELIVERED' WHEN n%3=1 THEN 'PAID' ELSE 'SHIPPED' END
FROM (VALUES(1,8),(2,5),(3,5),(4,2),(5,100)) s(store_number, quantity)
CROSS JOIN LATERAL generate_series(1,quantity) n;
COMMIT;
`);
let stores = await ranking();
assert.deepEqual(
  stores.map((store) => [store.subdomain, store.salesCount]),
  [
    ['bodeko-market', 8],
    ['casa-diseno', 5],
    ['tecnologia-bodeko', 5],
  ],
);
assert.equal(stores.length, 3, 'El resultado debe estar limitado a tres tiendas.');
assert.ok(
  stores.every((store) => !('taxId' in store) && !('ownerId' in store) && !('legalName' in store)),
);
sql("UPDATE orders SET status='CANCELLED' WHERE store_id='20000000-0000-0000-0000-000000000001';");
stores = await ranking();
assert.deepEqual(
  stores.map((store) => store.subdomain),
  ['casa-diseno', 'tecnologia-bodeko', 'taller-local'],
  'Las cancelaciones deben actualizar el ranking.',
);
sql("UPDATE orders SET status='PAID' WHERE store_id='20000000-0000-0000-0000-000000000001';");
assert.equal((await ranking())[0].salesCount, 8);
console.log(
  'Ranking verificado: acceso público, vacío, estados válidos, límite, desempates, cantidad frente a monto, privacidad y cancelaciones.',
);
````

### frontend/tsconfig.json

````json
{
  "compilerOptions": {
    "target": "ES2022",
    "lib": ["ES2022", "DOM", "DOM.Iterable"],
    "module": "ESNext",
    "moduleResolution": "Bundler",
    "jsx": "react-jsx",
    "strict": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noUncheckedIndexedAccess": true,
    "skipLibCheck": true,
    "resolveJsonModule": true,
    "esModuleInterop": true,
    "isolatedModules": true,
    "noEmit": true,
    "baseUrl": ".",
    "paths": { "@/*": ["src/*"] },
    "types": ["vite/client", "node"]
  },
  "include": ["src", "vite.config.ts"]
}
````

### frontend/vercel.json

````json
{
  "rewrites": [{ "source": "/(.*)", "destination": "/index.html" }]
}
````

### frontend/vite.config.ts

````typescript
import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import { fileURLToPath, URL } from 'node:url';

/** Redirige las peticiones /api al backend durante el desarrollo. */
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    plugins: [react()],
    resolve: { alias: { '@': fileURLToPath(new URL('./src', import.meta.url)) } },
    server: {
      port: 3000,
      strictPort: true,
      proxy: {
        '/api': { target: env.API_PROXY_TARGET || 'http://localhost:8080', changeOrigin: true },
      },
    },
  };
});
````

### scripts/culqi-mock/README.md

````markdown
# Simulador Culqi del equipo

`server.py` es el archivo proporcionado por tus compañeros, copiado sin modificaciones desde el adjunto.

Desde la raíz del repositorio:

```powershell
python .\scripts\culqi-mock\server.py
```

Mantén esa terminal abierta. En `.env.dev`, en la raíz del backend:

```dotenv
CULQI_API_URL=http://127.0.0.1:8199/v2
CULQI_SECRET_KEY=sk_test_mock
```

Reinicia el backend desde la raíz del proyecto para que cargue el archivo. Si IntelliJ o tu terminal tienen variables `CULQI_API_URL` o `CULQI_SECRET_KEY` del simulador anterior, elimínalas o actualízalas: las variables del proceso tienen prioridad sobre este archivo.

El frontend usa `VITE_PAYMENT_MODE=token` (es también el modo predeterminado). En la pantalla de pago:

| Token | Resultado |
| --- | --- |
| `tkn_test_ok` | Pago aprobado |
| `tkn_test_declined` | Pago rechazado |

El simulador no mueve dinero ni contacta a Culqi. Conserva los cargos en memoria y valida los reembolsos: una orden debe haber sido pagada en esa misma ejecución del simulador para poder devolverla. Los pagos históricos del SQL demo y los realizados con el simulador anterior no existen en su memoria. Para probar una devolución, crea una orden nueva, págala con `tkn_test_ok` y cancélala como administrador sin reiniciar el simulador.

La prueba `frontend/tools/verify-api.mjs` usa `tkn_test_ok` por defecto. El simulador anterior sigue disponible en `frontend/tools/culqi-simulator.py`, pero usa otro puerto, otra clave y otro token; no es el configurado en `.env.dev`. Para ejecutar la prueba contra aquel, configura `QA_APPROVED_TOKEN=tkn_test_approved` además de sus valores de backend.
````

### scripts/culqi-mock/server.py

````python
"""
Culqi simulado (mock) para desarrollo local de bodeko-core-api.

Imita los endpoints de la API v2 de Culqi que usa el backend:
  POST /v2/charges  -> cobros
  POST /v2/refunds  -> reembolsos

No mueve dinero real ni se conecta a Culqi. Solo usa la libreria estandar de Python.

Uso:
  python server.py                  (puerto 8199, llave sk_test_mock)
  MOCK_PORT=8200 python server.py   (otro puerto)

Tokens (campo sourceId del backend):
  tkn_test_ok        -> cargo aprobado
  tkn_test_declined  -> cargo rechazado (fondos insuficientes)
  cualquier otro     -> token invalido
"""
import json
import os
import threading
import time
import uuid
from datetime import datetime
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

PORT = int(os.environ.get("MOCK_PORT", "8199"))
SECRET_KEY = os.environ.get("MOCK_SECRET_KEY", "sk_test_mock")

OK_TOKEN = "tkn_test_ok"
DECLINED_TOKEN = "tkn_test_declined"

# charge_id -> {"amount": int (centimos), "refunded": int (centimos), "email": str}
CHARGES = {}
LOCK = threading.Lock()


def new_id(prefix):
    return f"{prefix}_test_{uuid.uuid4().hex[:16]}"


def log(message):
    print(f"[{datetime.now():%H:%M:%S}] {message}", flush=True)


def soles(cents):
    return f"S/ {cents / 100:.2f}"


def error(status, error_type, user_message, merchant_message, **extra):
    body = {
        "object": "error",
        "type": error_type,
        "user_message": user_message,
        "merchant_message": merchant_message,
    }
    body.update(extra)
    return status, body


def create_charge(body):
    source_id = body.get("source_id")
    amount = body.get("amount")
    email = body.get("email")
    if not isinstance(amount, int) or amount <= 0:
        return error(400, "invalid_request_error", "El monto es invalido.",
                     "amount debe ser un entero mayor a 0 (en centimos).", param="amount")

    if source_id == OK_TOKEN:
        charge_id = new_id("chr")
        with LOCK:
            CHARGES[charge_id] = {"amount": amount, "refunded": 0, "email": email}
        log(f"CARGO APROBADO   {charge_id}  {soles(amount)}  {email}")
        return 201, {
            "object": "charge",
            "id": charge_id,
            "creation_date": int(time.time() * 1000),
            "amount": amount,
            "currency_code": body.get("currency_code"),
            "email": email,
            "description": body.get("description"),
            "metadata": body.get("metadata") or {},
            "outcome": {
                "type": "venta_exitosa",
                "code": "AUT0000",
                "merchant_message": "La operacion de venta ha sido autorizada exitosamente",
                "user_message": "Su compra ha sido exitosa.",
            },
        }

    if source_id == DECLINED_TOKEN:
        charge_id = new_id("chr")
        log(f"CARGO RECHAZADO  {charge_id}  {soles(amount)}  {email}  (fondos insuficientes)")
        return error(402, "card_error",
                     "Su tarjeta no tiene fondos suficientes. Para realizar la compra, "
                     "verifica tus fondos disponibles con el banco emisor o intenta nuevamente con otra tarjeta.",
                     "La tarjeta no tiene fondos suficientes.",
                     charge_id=charge_id, code="card_declined", decline_code="insufficient_funds")

    log(f"TOKEN INVALIDO   {source_id!r}")
    return error(400, "invalid_request_error",
                 "El token de la tarjeta es invalido o ya fue usado. Intenta nuevamente.",
                 "El source_id enviado no existe o ya fue usado.", param="source_id")


def create_refund(body):
    charge_id = body.get("charge_id")
    amount = body.get("amount")
    if not isinstance(amount, int) or amount <= 0:
        return error(400, "invalid_request_error", "El monto a devolver es invalido.",
                     "amount debe ser un entero mayor a 0 (en centimos).", param="amount")
    with LOCK:
        charge = CHARGES.get(charge_id)
        if charge is None:
            log(f"REEMBOLSO FALLIDO {charge_id}  (el cargo no existe)")
            return error(400, "invalid_request_error", "No se encontro el cargo a devolver.",
                         "El charge_id no existe o no fue aprobado.", param="charge_id")
        available = charge["amount"] - charge["refunded"]
        if amount > available:
            log(f"REEMBOLSO FALLIDO {charge_id}  {soles(amount)} > disponible {soles(available)}")
            return error(400, "invalid_request_error", "El monto a devolver supera lo cobrado.",
                         f"Monto disponible para devolucion: {available} centimos.", param="amount")
        charge["refunded"] += amount

    refund_id = new_id("ref")
    log(f"REEMBOLSO OK     {refund_id}  {soles(amount)}  del cargo {charge_id}  (motivo: {body.get('reason')})")
    return 201, {
        "object": "refund",
        "id": refund_id,
        "charge_id": charge_id,
        "creation_date": int(time.time() * 1000),
        "amount": amount,
        "reason": body.get("reason"),
        "status": "aprobado",
    }


ROUTES = {
    "/v2/charges": create_charge,
    "/v2/refunds": create_refund,
}


class Handler(BaseHTTPRequestHandler):

    def do_POST(self):
        if self.headers.get("Authorization") != f"Bearer {SECRET_KEY}":
            log(f"401 llave invalida en {self.path}")
            return self.reply(*error(401, "authentication_error", "Error de autenticacion.",
                                     "La llave secreta es invalida."))
        handler = ROUTES.get(self.path)
        if handler is None:
            return self.reply(*error(404, "invalid_request_error", "Recurso no encontrado.",
                                     f"La ruta {self.path} no existe en el mock."))
        try:
            length = int(self.headers.get("Content-Length") or 0)
            body = json.loads(self.rfile.read(length) or b"{}")
        except (ValueError, json.JSONDecodeError):
            return self.reply(*error(400, "invalid_request_error", "Solicitud invalida.", "El cuerpo no es JSON valido."))
        self.reply(*handler(body))

    def reply(self, status, body):
        data = json.dumps(body).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)

    def log_message(self, *args):
        pass  # los mensajes propios ya se imprimen con log()


if __name__ == "__main__":
    log(f"Culqi simulado escuchando en http://127.0.0.1:{PORT}/v2  (llave: Bearer {SECRET_KEY})")
    log(f"Tokens: {OK_TOKEN} = aprobado | {DECLINED_TOKEN} = rechazado | otro = invalido")
    try:
        ThreadingHTTPServer(("127.0.0.1", PORT), Handler).serve_forever()
    except KeyboardInterrupt:
        log("Detenido.")
````

### scripts/db/00_compatibilidad_historial.sql

````sql
-- Compatibilidad de la estructura antigua de order_status_history con este backend.
-- No elimina filas ni modifica estados. No cambia el historial de Flyway.
-- En registros antiguos no se conoce el actor: changed_by permanece NULL.
BEGIN;

DO $$
DECLARE
    mapping record;
    has_expected boolean;
    has_legacy boolean;
BEGIN
    IF to_regclass('public.order_status_history') IS NULL THEN
        RAISE EXCEPTION 'No existe order_status_history. Aplica primero las migraciones del backend.';
    END IF;

    FOR mapping IN SELECT * FROM (VALUES
        ('previous_status', 'from_status'),
        ('new_status', 'to_status'),
        ('changed_at', 'created_at')
    ) AS names(legacy_name, expected_name)
    LOOP
        SELECT EXISTS (SELECT 1 FROM information_schema.columns
                       WHERE table_schema = 'public' AND table_name = 'order_status_history'
                       AND column_name = mapping.expected_name) INTO has_expected;
        SELECT EXISTS (SELECT 1 FROM information_schema.columns
                       WHERE table_schema = 'public' AND table_name = 'order_status_history'
                       AND column_name = mapping.legacy_name) INTO has_legacy;

        IF has_expected AND has_legacy THEN
            RAISE EXCEPTION 'Existen ambas columnas % y %. Revisa sus datos antes de normalizar el historial.',
                mapping.legacy_name, mapping.expected_name;
        ELSIF NOT has_expected AND has_legacy THEN
            EXECUTE format('ALTER TABLE public.order_status_history RENAME COLUMN %I TO %I',
                mapping.legacy_name, mapping.expected_name);
        ELSIF NOT has_expected THEN
            RAISE EXCEPTION 'Falta la columna % (o su equivalente antiguo %). No se modifico el historial.',
                mapping.expected_name, mapping.legacy_name;
        END IF;
    END LOOP;

    -- No se asignan administradores inventados a los cambios históricos.
    ALTER TABLE public.order_status_history ADD COLUMN IF NOT EXISTS changed_by UUID;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint c
        JOIN pg_attribute a ON a.attrelid = c.conrelid AND a.attnum = ANY(c.conkey)
        WHERE c.conrelid = 'public.order_status_history'::regclass
          AND c.contype = 'f' AND a.attname = 'changed_by'
    ) THEN
        ALTER TABLE public.order_status_history
            ADD CONSTRAINT fk_order_status_history_user FOREIGN KEY (changed_by)
            REFERENCES public.users(id) ON DELETE RESTRICT;
    END IF;

    CREATE INDEX IF NOT EXISTS idx_order_status_history_order_created
        ON public.order_status_history (order_id, created_at);
END $$;

COMMIT;
````

### scripts/db/01_datos_demo.sql

````sql
-- BODEKO: datos sintéticos para desarrollo, posteriores a Flyway V1-V5.
-- Ejecutar primero 00_compatibilidad_historial.sql y después este archivo completo.
-- Esta carga no crea/elimina tablas ni modifica datos existentes.
-- Contraseña de TODAS las cuentas demo: BodekoDemo2026!
-- El hash BCrypt está precalculado; NO requiere la extensión pgcrypto.
-- Los UUID son fijos. Reejecutar conserva cambios hechos desde la aplicación.
BEGIN;

DO $$
BEGIN
    IF to_regclass('public.order_status_history') IS NULL
       OR NOT EXISTS (SELECT 1 FROM information_schema.columns
                      WHERE table_schema = 'public' AND table_name = 'users' AND column_name = 'document_number') THEN
        RAISE EXCEPTION 'Primero inicia el backend para aplicar las migraciones Flyway V1-V5.';
    END IF;
END $$;

-- Detectar una BD antigua antes de insertar cualquier dato de prueba.
DO $$
BEGIN
    IF (SELECT count(*) FROM information_schema.columns
        WHERE table_schema = 'public' AND table_name = 'order_status_history'
          AND column_name IN ('from_status', 'to_status', 'changed_by', 'created_at')) <> 4 THEN
        RAISE EXCEPTION 'La estructura del historial no coincide con el backend. Ejecuta primero scripts/db/00_compatibilidad_historial.sql.';
    END IF;
END $$;

-- Dos propietarios: permiten comprobar que cada uno solo administra sus propias tiendas.
-- Dos clientes: permiten comprobar que una cuenta no puede consultar las órdenes de otra.
INSERT INTO users (id, email, password_hash, full_name, role, phone_number, document_type, document_number)
VALUES
('b0de0000-0000-4000-8000-000000000001', 'admin.demo@bodeko.test', '$2a$10$tlJgtV/apg1y/X5xe7Rju.oEx/hcwRcQSu8OQTtEE4pYA8YP6xy.m', 'Administrador Demo', 'ADMIN', '+51999999001', NULL, NULL),
('b0de0000-0000-4000-8000-000000000002', 'tecnologia.demo@bodeko.test', '$2a$10$tlJgtV/apg1y/X5xe7Rju.oEx/hcwRcQSu8OQTtEE4pYA8YP6xy.m', 'Administrador Tecnología', 'ADMIN', '+51999999002', NULL, NULL),
('b0de0000-0000-4000-8000-000000000003', 'cliente.demo@bodeko.test', '$2a$10$tlJgtV/apg1y/X5xe7Rju.oEx/hcwRcQSu8OQTtEE4pYA8YP6xy.m', 'Cliente Demo', 'CUSTOMER', '+51999999003', 'DNI', '12345678'),
('b0de0000-0000-4000-8000-000000000004', 'cliente2.demo@bodeko.test', '$2a$10$tlJgtV/apg1y/X5xe7Rju.oEx/hcwRcQSu8OQTtEE4pYA8YP6xy.m', 'Segundo Cliente Demo', 'CUSTOMER', '+51999999004', 'DNI', '87654321')
ON CONFLICT (id) DO NOTHING;

INSERT INTO stores (id, owner_id, name, tax_id, legal_name, subdomain, currency, tax_rate)
VALUES
('b0de1000-0000-4000-8000-000000000001', 'b0de0000-0000-4000-8000-000000000001', 'Bodeko Market Demo', '20999999001', 'Comercio Ficticio Demo S.A.C.', 'bodeko-market-demo', 'PEN', 0.18),
('b0de1000-0000-4000-8000-000000000002', 'b0de0000-0000-4000-8000-000000000002', 'Bodeko Tecnología Demo', '20999999002', 'Tecnología Ficticia Demo S.A.C.', 'bodeko-tech-demo', 'PEN', 0.18)
ON CONFLICT (id) DO NOTHING;

INSERT INTO categories (id, store_id, name)
VALUES
('b0de2000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000001', 'Abarrotes'),
('b0de2000-0000-4000-8000-000000000002', 'b0de1000-0000-4000-8000-000000000001', 'Bebidas y café'),
('b0de2000-0000-4000-8000-000000000003', 'b0de1000-0000-4000-8000-000000000001', 'Limpieza'),
('b0de2000-0000-4000-8000-000000000004', 'b0de1000-0000-4000-8000-000000000001', 'Categoría vacía de prueba'),
('b0de2000-0000-4000-8000-000000000005', 'b0de1000-0000-4000-8000-000000000002', 'Audio'),
('b0de2000-0000-4000-8000-000000000006', 'b0de1000-0000-4000-8000-000000000002', 'Periféricos')
ON CONFLICT (id) DO NOTHING;

-- Los datos ficticios no dependen de servicios externos de imágenes.
INSERT INTO products (id, store_id, category_id, name, description, base_price, is_tax_exempt, status)
VALUES
('b0de3000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000002', 'Café peruano 250 g', 'Producto demo con oferta activa y stock disponible.', 25.90, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000002', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Arroz extra 1 kg', 'Producto demo exonerado de IGV y con stock bajo.', 5.00, TRUE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000003', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Leche evaporada', 'Producto demo agotado para comprobar el filtro de stock.', 4.50, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000004', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Quinua 500 g', 'Producto demo exonerado de IGV.', 12.00, TRUE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000005', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000003', 'Detergente 1 kg', 'Producto demo con una oferta ya vencida.', 18.00, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000006', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Chocolate en borrador', 'Solo visible para el administrador hasta su publicación.', 8.50, FALSE, 'DRAFT'),
('b0de3000-0000-4000-8000-000000000007', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Azúcar archivada', 'Producto archivado que no debe aparecer en el catálogo público.', 4.20, FALSE, 'ARCHIVED'),
('b0de3000-0000-4000-8000-000000000008', 'b0de1000-0000-4000-8000-000000000001', 'b0de2000-0000-4000-8000-000000000001', 'Aceite de oliva 250 ml', 'Producto demo con una oferta programada para más adelante.', 22.00, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000009', 'b0de1000-0000-4000-8000-000000000002', 'b0de2000-0000-4000-8000-000000000005', 'Audífonos inalámbricos', 'Producto de la segunda tienda para probar el aislamiento.', 89.00, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000010', 'b0de1000-0000-4000-8000-000000000002', 'b0de2000-0000-4000-8000-000000000006', 'Teclado compacto', 'Producto demo con pocas unidades.', 129.00, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000011', 'b0de1000-0000-4000-8000-000000000002', 'b0de2000-0000-4000-8000-000000000006', 'Mouse inalámbrico', 'Producto demo agotado.', 49.00, FALSE, 'ACTIVE'),
('b0de3000-0000-4000-8000-000000000012', 'b0de1000-0000-4000-8000-000000000002', 'b0de2000-0000-4000-8000-000000000006', 'Cámara web en borrador', 'Producto demo pendiente de publicación.', 159.00, FALSE, 'DRAFT')
ON CONFLICT (id) DO NOTHING;

-- El SQL no pasa por ProductService: por eso crea explícitamente los inventarios.
INSERT INTO inventory (id, product_id, quantity, low_stock_threshold)
SELECT replace(product_id, 'b0de3000', 'b0de4000')::uuid, product_id::uuid, quantity, threshold
FROM (VALUES
('b0de3000-0000-4000-8000-000000000001', 40, 5),
('b0de3000-0000-4000-8000-000000000002', 4, 5),
('b0de3000-0000-4000-8000-000000000003', 0, 5),
('b0de3000-0000-4000-8000-000000000004', 25, 5),
('b0de3000-0000-4000-8000-000000000005', 12, 5),
('b0de3000-0000-4000-8000-000000000006', 0, 5),
('b0de3000-0000-4000-8000-000000000007', 0, 5),
('b0de3000-0000-4000-8000-000000000008', 20, 5),
('b0de3000-0000-4000-8000-000000000009', 8, 3),
('b0de3000-0000-4000-8000-000000000010', 2, 3),
('b0de3000-0000-4000-8000-000000000011', 0, 3),
('b0de3000-0000-4000-8000-000000000012', 0, 3)
) AS stock(product_id, quantity, threshold)
ON CONFLICT (product_id) DO NOTHING;

-- Se insertan solo si el producto aún no tiene ofertas: repetir no crea solapamientos.
INSERT INTO product_offers (store_id, product_id, offer_price, starts_at, ends_at)
SELECT p.store_id, p.id, offers.price, now() + offers.starts_offset, now() + offers.ends_offset
FROM (VALUES
('b0de3000-0000-4000-8000-000000000001'::uuid, 19.90, interval '-1 day', interval '30 days'),
('b0de3000-0000-4000-8000-000000000005'::uuid, 14.00, interval '-10 days', interval '-1 day'),
('b0de3000-0000-4000-8000-000000000008'::uuid, 18.00, interval '7 days', interval '14 days'),
('b0de3000-0000-4000-8000-000000000009'::uuid, 69.00, interval '-1 day', interval '30 days')
) AS offers(product_id, price, starts_offset, ends_offset)
JOIN products p ON p.id = offers.product_id
WHERE NOT EXISTS (SELECT 1 FROM product_offers existing WHERE existing.product_id = p.id);

INSERT INTO shopping_carts (id, user_id, store_id, status)
VALUES ('b0de6000-0000-4000-8000-000000000001', 'b0de0000-0000-4000-8000-000000000003', 'b0de1000-0000-4000-8000-000000000001', 'OPEN')
ON CONFLICT DO NOTHING;

INSERT INTO shopping_cart_items (store_id, cart_id, product_id, quantity)
SELECT c.store_id, c.id, items.product_id, items.quantity
FROM shopping_carts c
CROSS JOIN (VALUES
('b0de3000-0000-4000-8000-000000000001'::uuid, 1),
('b0de3000-0000-4000-8000-000000000002'::uuid, 2)
) AS items(product_id, quantity)
WHERE c.id = 'b0de6000-0000-4000-8000-000000000001' AND c.status = 'OPEN'
ON CONFLICT (cart_id, product_id) DO NOTHING;

-- Historial ficticio ya contabilizado en el stock inicial; no son operaciones reales.
-- Todas las líneas usan un café a S/25.90, IGV S/4.66, total S/30.56.
INSERT INTO orders (id, store_id, customer_id, order_number, invoice_type,
    customer_document_type, customer_document_number, customer_business_name,
    customer_billing_address, subtotal, tax_amount, total, applied_tax_rate, status, created_at)
SELECT fixture.id, 'b0de1000-0000-4000-8000-000000000001', 'b0de0000-0000-4000-8000-000000000003',
    fixture.number, fixture.invoice, CASE WHEN fixture.invoice = 'INVOICE' THEN 'RUC' ELSE 'DNI' END,
    CASE WHEN fixture.invoice = 'INVOICE' THEN '20999999003' ELSE '12345678' END,
    CASE WHEN fixture.invoice = 'INVOICE' THEN 'Cliente Ficticio Demo S.A.C.' ELSE NULL END,
    'Dirección ficticia de prueba 123', 25.90, 4.66, 30.56, 0.18, fixture.status, now() - fixture.age
FROM (VALUES
('b0de7000-0000-4000-8000-000000000001'::uuid, 'BOD-DEMO-PENDIENTE', 'RECEIPT', 'PENDING', interval '2 hours'),
('b0de7000-0000-4000-8000-000000000002'::uuid, 'BOD-DEMO-PAGADA', 'INVOICE', 'PAID', interval '1 day'),
('b0de7000-0000-4000-8000-000000000003'::uuid, 'BOD-DEMO-ENVIADA', 'RECEIPT', 'SHIPPED', interval '2 days'),
('b0de7000-0000-4000-8000-000000000004'::uuid, 'BOD-DEMO-ENTREGADA', 'INVOICE', 'DELIVERED', interval '3 days'),
('b0de7000-0000-4000-8000-000000000005'::uuid, 'BOD-DEMO-CANCELADA', 'RECEIPT', 'CANCELLED', interval '4 days')
) AS fixture(id, number, invoice, status, age)
ON CONFLICT (id) DO NOTHING;

INSERT INTO order_items (store_id, order_id, product_id, product_name, quantity, list_unit_price, charged_unit_price, item_subtotal)
SELECT store_id, id, 'b0de3000-0000-4000-8000-000000000001', 'Café peruano 250 g', 1, 25.90, 25.90, 25.90
FROM orders WHERE id IN (
'b0de7000-0000-4000-8000-000000000001', 'b0de7000-0000-4000-8000-000000000002',
'b0de7000-0000-4000-8000-000000000003', 'b0de7000-0000-4000-8000-000000000004',
'b0de7000-0000-4000-8000-000000000005')
ON CONFLICT (order_id, product_id) DO NOTHING;

INSERT INTO payments (id, order_id, provider, transaction_id, amount, status)
VALUES
('b0de8000-0000-4000-8000-000000000001', 'b0de7000-0000-4000-8000-000000000001', 'CULQI', 'chr_test_seed_declined', 30.56, 'FAILED'),
('b0de8000-0000-4000-8000-000000000002', 'b0de7000-0000-4000-8000-000000000002', 'CULQI', 'chr_test_seed_paid', 30.56, 'APPROVED'),
('b0de8000-0000-4000-8000-000000000003', 'b0de7000-0000-4000-8000-000000000003', 'CULQI', 'chr_test_seed_shipped', 30.56, 'APPROVED'),
('b0de8000-0000-4000-8000-000000000004', 'b0de7000-0000-4000-8000-000000000004', 'CULQI', 'chr_test_seed_delivered', 30.56, 'APPROVED'),
('b0de8000-0000-4000-8000-000000000005', 'b0de7000-0000-4000-8000-000000000005', 'CULQI', 'chr_test_seed_refunded', 30.56, 'REFUNDED')
ON CONFLICT (id) DO NOTHING;

-- Las transiciones de preparación/envío/cancelación que administra el backend.
INSERT INTO order_status_history (id, order_id, from_status, to_status, changed_by, created_at)
VALUES
('b0de9000-0000-4000-8000-000000000001', 'b0de7000-0000-4000-8000-000000000003', 'PAID', 'SHIPPED', 'b0de0000-0000-4000-8000-000000000001', now() - interval '1 day'),
('b0de9000-0000-4000-8000-000000000002', 'b0de7000-0000-4000-8000-000000000004', 'PAID', 'SHIPPED', 'b0de0000-0000-4000-8000-000000000001', now() - interval '2 days'),
('b0de9000-0000-4000-8000-000000000003', 'b0de7000-0000-4000-8000-000000000004', 'SHIPPED', 'DELIVERED', 'b0de0000-0000-4000-8000-000000000001', now() - interval '1 day'),
('b0de9000-0000-4000-8000-000000000004', 'b0de7000-0000-4000-8000-000000000005', 'PAID', 'CANCELLED', 'b0de0000-0000-4000-8000-000000000001', now() - interval '3 days')
ON CONFLICT (id) DO NOTHING;

COMMIT;
````

### scripts/db/02_consultas_verificacion.sql

````sql
-- Consultas de SOLO LECTURA. Ejecutar después del seed y repetir tras usar la app.

-- 1. Usuarios de prueba. No muestra hashes de contraseña.
SELECT full_name, email, role, is_active
FROM users WHERE id::text LIKE 'b0de0000-%' ORDER BY email;

-- 2. Catálogo e inventario de ambas tiendas.
SELECT s.name AS tienda, p.name AS producto, p.status, p.base_price,
       p.is_tax_exempt, i.quantity, i.low_stock_threshold,
       i.quantity <= i.low_stock_threshold AS alerta_stock_bajo
FROM products p JOIN stores s ON s.id = p.store_id
JOIN inventory i ON i.product_id = p.id
WHERE s.id IN ('b0de1000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000002')
ORDER BY s.name, p.name;

-- 3. Ofertas y vigencia (la fecha cambia naturalmente con el paso del tiempo).
SELECT p.name, o.offer_price, o.starts_at, o.ends_at,
       now() >= o.starts_at AND now() < o.ends_at AS vigente
FROM product_offers o JOIN products p ON p.id = o.product_id
WHERE p.id::text LIKE 'b0de3000-%' ORDER BY p.name;

-- 4. Carritos: confirma que permanecen tras cerrar e iniciar sesión.
SELECT u.email, s.name AS tienda, c.status, p.name AS producto, ci.quantity
FROM shopping_carts c JOIN users u ON u.id = c.user_id JOIN stores s ON s.id = c.store_id
LEFT JOIN shopping_cart_items ci ON ci.cart_id = c.id LEFT JOIN products p ON p.id = ci.product_id
WHERE u.id IN ('b0de0000-0000-4000-8000-000000000003', 'b0de0000-0000-4000-8000-000000000004')
ORDER BY c.created_at DESC, p.name;

-- 5. Órdenes y pagos: incluye las órdenes nuevas creadas desde la aplicación.
SELECT o.order_number, o.status AS estado_orden, o.invoice_type,
       o.subtotal, o.tax_amount, o.total, pay.status AS estado_pago, pay.amount
FROM orders o LEFT JOIN payments pay ON pay.order_id = o.id
WHERE o.store_id IN ('b0de1000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000002')
ORDER BY o.created_at DESC, pay.created_at;

-- 6. Debe devolver CERO filas: subtotal e IGV cuadran con el total y las líneas.
SELECT o.order_number, o.subtotal, sum(oi.item_subtotal) AS subtotal_items, o.tax_amount, o.total
FROM orders o JOIN order_items oi ON oi.order_id = o.id
WHERE o.store_id IN ('b0de1000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000002')
GROUP BY o.id
HAVING o.subtotal <> sum(oi.item_subtotal) OR o.total <> o.subtotal + o.tax_amount;

-- 7. Historial de cambios del usuario (administrador o cliente), incluidas tus pruebas nuevas.
SELECT o.order_number, h.from_status, h.to_status,
       COALESCE(u.email, 'No registrado (histórico)') AS usuario, h.created_at
FROM order_status_history h JOIN orders o ON o.id = h.order_id LEFT JOIN users u ON u.id = h.changed_by
WHERE o.store_id IN ('b0de1000-0000-4000-8000-000000000001', 'b0de1000-0000-4000-8000-000000000002')
ORDER BY h.created_at DESC;
````

### scripts/db/README.md

````markdown
# Datos de prueba de Bodeko

Estos scripts cargan **datos ficticios en una base de desarrollo** para probar el frontend y el backend reales. No se ejecutan automáticamente al iniciar la aplicación y no pertenecen a las migraciones Flyway.

## Orden de ejecución

1. Crea/selecciona tu base de desarrollo en PostgreSQL.
2. Configura el backend para conectarse a esa base y arráncalo una vez. Flyway debe aplicar **V1–V5**. No ejecutes manualmente los archivos de migración si ya usas Flyway.
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

El backend debe usar la misma base que indiques en `-Database`. La configuración local actual apunta a `db_bodekos_v2`. Cargar el demo en `db_bodekos` no lo hace visible en el backend conectado a `db_bodekos_v2`: son bases distintas. El ejecutor avisa si el nombre no coincide con el declarado en `application.properties`; si arrancas Spring con variables de entorno, comprueba su valor efectivo.

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
````

### scripts/db/ejecutar-demo.ps1

````powershell
<#
.SYNOPSIS
Carga datos ficticios de Bodeko y ejecuta consultas de verificación sobre una BD de desarrollo.
.EXAMPLE
.\scripts\db\ejecutar-demo.ps1 -Database db_bodekos_v2 -DbUser postgres
#>
[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)][string]$Database,
    [Parameter(Mandatory = $true)][string]$DbUser,
    [string]$DbHost = 'localhost',
    [ValidateRange(1, 65535)][int]$Port = 5432,
    [string]$PsqlPath,
    [switch]$SoloVerificar
)

$ErrorActionPreference = 'Stop'
if (-not $PsqlPath) {
    $command = Get-Command psql.exe -ErrorAction SilentlyContinue
    if ($command) { $PsqlPath = $command.Source }
    else {
        $candidates = @('C:\Program Files\PostgreSQL\18\bin\psql.exe', 'C:\Program Files\PostgreSQL\17\bin\psql.exe', 'C:\Program Files\PostgreSQL\16\bin\psql.exe')
        $PsqlPath = $candidates | Where-Object { Test-Path -LiteralPath $_ } | Select-Object -First 1
    }
}
if (-not $PsqlPath -or -not (Test-Path -LiteralPath $PsqlPath)) {
    throw 'No se encontró psql.exe. Indica su ruta con -PsqlPath.'
}

Write-Host "Destino: $DbHost`:$Port / $Database. Usa una base de desarrollo."
# Aviso informativo: las variables de entorno pueden sobrescribir la configuración Java.
$backendConfigPath = Join-Path $PSScriptRoot '..\..\src\main\resources\application.properties'
if (Test-Path -LiteralPath $backendConfigPath) {
    $connectionLine = Get-Content -LiteralPath $backendConfigPath | Where-Object { $_ -match '^spring\.datasource\.url\s*=' } | Select-Object -First 1
    $databaseMatch = [regex]::Match([string]$connectionLine, '^spring\.datasource\.url\s*=\s*jdbc:postgresql://[^/]+/([^?\s]+)')
    if ($databaseMatch.Success -and $databaseMatch.Groups[1].Value -ne $Database) {
        Write-Warning "El backend declara la base '$($databaseMatch.Groups[1].Value)', pero estás usando '$Database'. Los usuarios y tiendas solo aparecerán si ambos apuntan a la misma base. Comprueba también las variables de entorno del backend."
    }
}
# psql solicita la contraseña si hace falta. El script no la recibe ni la guarda.
# PostgreSQL interpreta los SQL como UTF-8 incluso desde Windows PowerShell 5.1.
$previousClientEncoding = $env:PGCLIENTENCODING
$previousConsoleEncoding = [Console]::OutputEncoding
$files = if ($SoloVerificar) { @('02_consultas_verificacion.sql') } else { @('00_compatibilidad_historial.sql', '01_datos_demo.sql', '02_consultas_verificacion.sql') }
try {
    $env:PGCLIENTENCODING = 'UTF8'
    [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding($false)
    foreach ($file in $files) {
        & $PsqlPath -X -h $DbHost -p $Port -U $DbUser -d $Database -v ON_ERROR_STOP=1 -f (Join-Path $PSScriptRoot $file)
        if ($LASTEXITCODE -ne 0) { throw "Falló $file. Revisa el mensaje de PostgreSQL; no continúes con la siguiente carga." }
    }
}
finally {
    if ($null -eq $previousClientEncoding) { Remove-Item Env:PGCLIENTENCODING -ErrorAction SilentlyContinue }
    else { $env:PGCLIENTENCODING = $previousClientEncoding }
    [Console]::OutputEncoding = $previousConsoleEncoding
}
Write-Host 'Scripts completados. Revisa scripts/db/README.md para las cuentas y los escenarios.'
````
