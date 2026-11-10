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
