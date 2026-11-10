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
