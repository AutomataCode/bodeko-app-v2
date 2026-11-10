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
