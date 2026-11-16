import { useEffect } from 'react';
import { Link, NavLink, Outlet, useParams } from 'react-router-dom';
import { LogOut, ShoppingBag } from 'lucide-react';
import { Button } from '@/core/components';
import { storeSessionKey } from '@/config';
import { SessionProvider, useSession } from '@/context/SessionContext';
import { StoreProvider, useStore } from '@/context/StoreContext';
import { CartProvider } from '@/context/CartContext';
import { useCart } from '@/features/cart';

/** Iniciales para el monograma de la tienda (por ejemplo, "Café Andino" → "CA"). */
export function storeInitials(name: string) {
  const [first = '', second = ''] = name.trim().split(/\s+/);
  return (second ? first.charAt(0) + second.charAt(0) : first.slice(0, 2)).toUpperCase();
}

/** Cabecera, contenido y pie con la identidad de la tienda, sin la marca de la plataforma. */
function StoreSite() {
  const store = useStore();
  const { cart } = useCart();
  const { user, logout } = useSession();
  const base = `/tienda/${store.subdomain}`;
  useEffect(() => {
    const previous = document.title;
    document.title = store.name;
    return () => {
      document.title = previous;
    };
  }, [store.name]);
  return (
    <div className="store-site">
      <a className="skip-link" href="#contenido">
        Ir al contenido principal
      </a>
      <header className="site-header">
        <div className="header-inner">
          <Link to={base} className="brand store-brand" aria-label={`${store.name}, inicio`}>
            <span className="store-monogram" aria-hidden="true">
              {storeInitials(store.name)}
            </span>
            <span className="store-brand-name">{store.name}</span>
          </Link>
          <nav aria-label={`Navegación de ${store.name}`}>
            <NavLink to={base} end>
              Productos
            </NavLink>
            {user?.role === 'CUSTOMER' && (
              <NavLink to={`${base}/cuenta/ordenes`}>Mis órdenes</NavLink>
            )}
          </nav>
          <div className="header-account store-header-account">
            {user?.role === 'CUSTOMER' && (
              <Link className="cart-link" to={`${base}/carrito`} aria-label="Carrito">
                <ShoppingBag size={18} />
                <span className="desktop-label">Carrito</span>
                <span className="cart-count">{cart?.totalQuantity || 0}</span>
              </Link>
            )}
            {user ? (
              <>
                <Link className="user-name" to={`${base}/cuenta/datos`}>
                  {user.fullName}
                </Link>
                <Button
                  variant="secondary"
                  aria-label="Cerrar sesión"
                  onClick={() => {
                    logout();
                    window.location.replace(base);
                  }}
                >
                  <LogOut size={16} />
                  <span className="desktop-label">Salir</span>
                </Button>
              </>
            ) : (
              <>
                <Link to={`${base}/ingresar`}>Iniciar sesión</Link>
                <Link className="button primary" to={`${base}/registro`}>
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
        <span className="brand store-brand">
          <span className="store-monogram" aria-hidden="true">
            {storeInitials(store.name)}
          </span>
          {store.name}
        </span>
        <span>
          © {new Date().getFullYear()} {store.name}. Todos los derechos reservados.
        </span>
      </footer>
    </div>
  );
}

/** Carga la tienda y su propia sesión de cliente: cada tienda tiene sus propios clientes. */
export function StoreLayout() {
  const { subdomain = '' } = useParams();
  return (
    <SessionProvider key={subdomain} tokenKey={storeSessionKey(subdomain)}>
      <StoreProvider key={subdomain} subdomain={subdomain}>
        <StoreCart />
      </StoreProvider>
    </SessionProvider>
  );
}

function StoreCart() {
  const { user } = useSession();
  return (
    <CartProvider key={user?.id || 'visitor'}>
      <StoreSite />
    </CartProvider>
  );
}
