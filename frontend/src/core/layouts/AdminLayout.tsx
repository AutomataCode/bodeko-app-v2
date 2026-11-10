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
