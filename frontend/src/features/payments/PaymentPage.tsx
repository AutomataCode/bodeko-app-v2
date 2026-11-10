import { useCallback } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, ErrorNotice, Loading, PageHeader } from '@/core/components';
import { useResource } from '@/core/hooks/useResource';
import { useStore } from '@/context/StoreContext';
import { useCart } from '@/features/cart';
import { OrderDetails } from '@/features/orders';
import { PurchaseConfirmation } from '@/features/notifications';
import { checkoutService } from '@/features/checkout/services/checkoutService';
import { PaymentForm } from './components/PaymentForm';
/** Orden recuperada del servidor, incluso al recargar la página de pago. */
export function PaymentPage() {
  const { orderId = '' } = useParams();
  const store = useStore();
  const cart = useCart();
  const resource = useResource(
    useCallback(() => checkoutService.get(store.id, orderId), [store.id, orderId]),
  );
  if (resource.loading) return <Loading />;
  if (!resource.data) return <ErrorNotice message={resource.error} retry={resource.reload} />;
  const order = resource.data;
  return (
    <>
      <PageHeader
        title={order.status === 'PENDING' ? 'Revisa tu orden y paga' : 'Estado de tu compra'}
        actions={
          <div className="actions">
            <Link className="button secondary" to={`/cuenta/ordenes/${order.id}`}>
              Ver en mis órdenes
            </Link>
            <Button variant="secondary" onClick={resource.reload}>
              Actualizar estado
            </Button>
          </div>
        }
      />
      {['PAID', 'SHIPPED', 'DELIVERED'].includes(order.status) && (
        <PurchaseConfirmation orderNumber={order.orderNumber} />
      )}
      <div className="checkout-grid">
        <section className="card">
          <OrderDetails order={order} currency={store.currency} />
        </section>
        {order.status === 'PENDING' ? (
          <PaymentForm
            key={order.id}
            storeId={store.id}
            storeName={store.name}
            currency={store.currency}
            order={order}
            onPaid={() => {
              cart.reload();
              resource.reload();
            }}
          />
        ) : (
          <aside className="card summary">
            <h2>{order.status === 'CANCELLED' ? 'Orden cancelada' : 'Gracias por tu compra'}</h2>
            <p>Conserva el número de tu orden para cualquier consulta.</p>
            <Link className="button secondary" to="/cuenta/ordenes">
              Ver mis órdenes
            </Link>
            <Link className="button primary" to={`/tienda/${store.subdomain}`}>
              Volver a la tienda
            </Link>
          </aside>
        )}
      </div>
    </>
  );
}
