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
