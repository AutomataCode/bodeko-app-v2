import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { ArrowLeft, CreditCard, RefreshCw, Store, X } from 'lucide-react';
import { Button, ErrorNotice, Loading } from '@/core/components';
import { OrderDetails } from '@/features/orders';
import { useCustomerOrder } from './hooks/useCustomerOrder';
import { CancelOrderDialog } from './components/CancelOrderDialog';

/** Muestra los datos guardados de la orden y las acciones permitidas al cliente. */
export function CustomerOrderDetailPage() {
  const { orderId = '' } = useParams();
  const resource = useCustomerOrder(orderId);
  const [cancelling, setCancelling] = useState(false);
  if (resource.loading) return <Loading />;
  if (!resource.data)
    return (
      <>
        <Link className="back-link" to="/cuenta/ordenes">
          <ArrowLeft size={15} />
          Mis órdenes
        </Link>
        <ErrorNotice message={resource.error} retry={resource.reload} />
      </>
    );
  const { order, storeName, storeSubdomain, currency } = resource.data;
  const canCancel = order.status === 'PENDING' || order.status === 'PAID';
  return (
    <>
      <Link className="back-link" to="/cuenta/ordenes">
        <ArrowLeft size={15} />
        Volver a mis órdenes
      </Link>
      <header className="account-page-heading">
        <div>
          <span className="eyebrow">DETALLE DE COMPRA</span>
          <h1>Tu orden, en detalle</h1>
          <p>Consulta tus productos, comprobante e importe final.</p>
        </div>
        <Button variant="secondary" onClick={resource.reload}>
          <RefreshCw size={15} />
          Actualizar
        </Button>
      </header>
      <section className="account-section-card customer-order-detail">
        <div className="order-detail-store">
          <Store size={20} />
          <Link to={`/tienda/${storeSubdomain}`}>{storeName}</Link>
        </div>
        <OrderDetails order={order} currency={currency} />
      </section>
      <section className="account-section-card customer-order-management">
        <div>
          <h2>{order.status === 'CANCELLED' ? 'Orden cancelada' : 'Gestiona tu compra'}</h2>
          <p>
            {order.status === 'PENDING'
              ? 'Tu orden está pendiente de pago. Puedes continuar con el pago o cancelarla.'
              : order.status === 'PAID'
                ? 'Tu pago está confirmado. Puedes cancelar la orden antes de que sea enviada.'
                : order.status === 'CANCELLED'
                  ? 'Esta compra ya no admite cambios. Puedes seguir explorando los productos de la tienda.'
                  : 'Tu orden ya no admite cancelaciones desde la cuenta. Conserva su número para consultar a la tienda.'}
          </p>
        </div>
        <div className="actions">
          {canCancel && (
            <Button
              variant="secondary"
              className="cancel-order-button"
              onClick={() => setCancelling(true)}
            >
              <X size={15} />
              Cancelar orden
            </Button>
          )}
          {order.status === 'PENDING' && (
            <Link className="button primary" to={`/tienda/${storeSubdomain}/pago/${order.id}`}>
              <CreditCard size={15} />
              Pagar ahora
            </Link>
          )}
        </div>
      </section>
      {cancelling && (
        <CancelOrderDialog
          order={{
            id: order.id,
            orderNumber: order.orderNumber,
            status: order.status,
            total: order.total,
            currency,
          }}
          onClose={() => setCancelling(false)}
          onCancelled={() => {
            setCancelling(false);
            resource.reload();
          }}
        />
      )}
    </>
  );
}
