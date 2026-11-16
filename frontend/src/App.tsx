import { BrowserRouter, Link, Navigate, Route, Routes, useParams } from 'react-router-dom';
import { Toaster } from 'sonner';
import { SessionProvider } from '@/context/SessionContext';
import { SESSION_KEY } from '@/config';
import { SiteLayout } from '@/core/layouts/SiteLayout';
import { StoreLayout } from '@/core/layouts/StoreLayout';
import { AdminLayout } from '@/core/layouts/AdminLayout';
import { ProtectedRoute } from '@/core/layouts/ProtectedRoute';
import { ScrollToTop } from '@/core/layouts/ScrollToTop';
import { AuthPage, StoreAuthPage } from '@/features/auth';
import {
  StoresPage,
  StoresDirectoryPage,
  ManageStoresPage,
  StoreSettingsPage,
} from '@/features/stores';
import { CatalogPage, ProductDetailPage } from '@/features/catalog';
import { ProductsPage } from '@/features/products';
import { CategoriesPage } from '@/features/categories';
import { InventoryPage, InventoryMovementsPage, InventoryShrinkagesPage } from '@/features/inventory';
import { PurchaseOrdersPage } from '@/features/procurement';
import { OffersPage } from '@/features/offers';
import { CartPage } from '@/features/cart';
import { CheckoutPage } from '@/features/checkout';
import { PaymentPage } from '@/features/payments';
import { OrdersPage, OrderDetailPage } from '@/features/orders';
import { DashboardPage } from '@/features/dashboard';
import { SuperAdminPage } from '@/features/superadmin';
import { CustomerSalesDocumentPage, StoreSalesDocumentPage } from '@/features/billing-documents';
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
/** Página no encontrada; dentro de una tienda vuelve a su catálogo. */
function NotFound() {
  const { subdomain } = useParams();
  return (
    <section className="empty">
      <h1>Página no encontrada</h1>
      <Link className="button primary" to={subdomain ? `/tienda/${subdomain}` : '/'}>
        Volver al inicio
      </Link>
    </section>
  );
}
/** Define las páginas públicas y las páginas protegidas por rol. */
export default function App() {
  return (
    <BrowserRouter>
      <ScrollToTop />
      <Toaster
        richColors
        position="top-right"
        closeButton
        containerAriaLabel="Notificaciones"
        toastOptions={{ duration: 5000, closeButtonAriaLabel: 'Cerrar notificación' }}
      />
      <Routes>
        {/* Cada tienda tiene su propia cabecera, sus propios clientes y su propia sesión. */}
        <Route path="tienda/:subdomain" element={<StoreLayout />}>
          <Route index element={<CatalogPage />} />
          <Route path="producto/:productId" element={<ProductDetailPage />} />
          <Route path="ingresar" element={<StoreAuthPage />} />
          <Route path="registro" element={<StoreAuthPage register />} />
          <Route element={<ProtectedRoute role="CUSTOMER" />}>
            <Route path="carrito" element={<CartPage />} />
            <Route path="checkout" element={<CheckoutPage />} />
            <Route path="pago/:orderId" element={<PaymentPage />} />
            <Route path="cuenta" element={<AccountLayout />}>
              <Route index element={<Navigate to="ordenes" replace />} />
              <Route path="datos" element={<AccountProfilePage />} />
              <Route path="ordenes" element={<CustomerOrdersPage />} />
              <Route path="ordenes/:orderId" element={<CustomerOrderDetailPage />} />
              <Route path="ordenes/:orderId/comprobante" element={<CustomerSalesDocumentPage />} />
            </Route>
          </Route>
          <Route path="*" element={<NotFound />} />
        </Route>
        <Route
          element={
            <SessionProvider tokenKey={SESSION_KEY}>
              <SiteLayout />
            </SessionProvider>
          }
        >
          <Route index element={<Home />} />
          <Route path="tiendas" element={<StoresDirectoryPage />} />
          <Route path="ingresar" element={<AuthPage />} />
          <Route path="registro" element={<AuthPage register />} />
          <Route element={<ProtectedRoute role="SUPER_ADMIN" />}><Route path="superadmin" element={<SuperAdminPage />} /></Route>
          <Route element={<ProtectedRoute role="ADMIN" />}>
            <Route path="admin" element={<ManageStoresPage />} />
            <Route path="admin/:storeId" element={<AdminLayout />}>
              <Route index element={<DashboardPage />} />
              <Route path="productos" element={<ProductsPage />} />
              <Route path="categorias" element={<CategoriesPage />} />
              <Route path="inventario" element={<InventoryPage />} />
              <Route path="inventario/movimientos" element={<InventoryMovementsPage />} />
              <Route path="inventario/mermas" element={<InventoryShrinkagesPage />} />
              <Route path="compras" element={<PurchaseOrdersPage />} />
              <Route path="ofertas" element={<OffersPage />} />
              <Route path="ordenes" element={<OrdersPage />} />
              <Route path="ordenes/:orderId" element={<OrderDetailPage />} />
              <Route path="ordenes/:orderId/comprobante" element={<StoreSalesDocumentPage />} />
              <Route path="configuracion" element={<StoreSettingsPage />} />
            </Route>
          </Route>
          <Route path="*" element={<NotFound />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
