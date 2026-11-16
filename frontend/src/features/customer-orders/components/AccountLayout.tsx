import { Link, NavLink, Outlet, useLocation } from 'react-router-dom';
import { ChevronRight, LogOut, ShoppingBag, Store, UserRound } from 'lucide-react';
import { useSession } from '@/context/SessionContext';
import { useStore } from '@/context/StoreContext';

/** Organiza el área del cliente con un menú lateral. */
export function AccountLayout() {
  const { user, logout } = useSession();
  const { pathname } = useLocation();
  const store = useStore();
  const base = `/tienda/${store.subdomain}`;
  const section = pathname.includes('/datos') ? 'Datos personales' : 'Mis órdenes';
  return (
    <div className="customer-account">
      <div className="account-breadcrumb">
        <Link to={base}>Inicio</Link>
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
              <span>Tu cuenta en {store.name}</span>
            </div>
          </div>
          <nav aria-label="Mi cuenta">
            <NavLink to={`${base}/cuenta/datos`}>
              <UserRound size={19} />
              <span>Datos personales</span>
              <ChevronRight size={16} />
            </NavLink>
            <NavLink to={`${base}/cuenta/ordenes`}>
              <ShoppingBag size={19} />
              <span>Mis órdenes</span>
              <ChevronRight size={16} />
            </NavLink>
            <Link to={base}>
              <Store size={19} />
              <span>Seguir comprando</span>
              <ChevronRight size={16} />
            </Link>
            <button
              type="button"
              onClick={() => {
                logout();
                window.location.replace(base);
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
