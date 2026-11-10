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
