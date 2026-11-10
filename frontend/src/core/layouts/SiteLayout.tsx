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
