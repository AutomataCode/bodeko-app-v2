import { useCallback, useState } from 'react';
import { Link, useOutletContext, useParams } from 'react-router-dom';
import { Button, ErrorNotice, Loading, Modal, PageHeader, Select } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { useResource } from '@/core/hooks/useResource';
import type { Store } from '@/features/stores';
import { labels } from '@/utils/format';
import { orderService } from './services/orderService';
import { OrderDetails } from './components/OrderDetails';
import type { OrderStatus } from './types';
/** Transiciones permitidas del contrato de OrderManagementService. */
const transitions: Record<OrderStatus, OrderStatus[]> = {
  PENDING: ['CANCELLED'],
  PAID: ['SHIPPED', 'CANCELLED'],
  SHIPPED: ['DELIVERED'],
  DELIVERED: [],
  CANCELLED: [],
};
/** Detalle administrativo con confirmación explícita antes de cancelar o reembolsar. */
export function OrderDetailPage() {
  const store = useOutletContext<Store>();
  const { orderId = '' } = useParams();
  const resource = useResource(
    useCallback(() => orderService.detail(store.id, orderId), [store.id, orderId]),
  );
  const action = useAction();
  const [nextStatus, setNextStatus] = useState('');
  const [confirming, setConfirming] = useState(false);
  async function save() {
    const result = await action.run(
      () => orderService.status(store.id, orderId, nextStatus as OrderStatus),
      'Estado de orden actualizado.',
    );
    if (result) {
      setConfirming(false);
      setNextStatus('');
      resource.reload();
    }
  }
  if (resource.loading) return <Loading />;
  if (!resource.data) return <ErrorNotice message={resource.error} retry={resource.reload} />;
  const order = resource.data;
  return (
    <>
      <PageHeader
        title="Detalle de orden"
        actions={<Link to={`/admin/${store.id}/ordenes`}>Volver a órdenes</Link>}
      />
      <section className="card">
        <OrderDetails order={order} currency={store.currency} />
      </section>
      <ErrorNotice message={action.error} />
      {transitions[order.status].length > 0 && (
        <section className="card inline-form">
          <Select
            label="Cambiar estado"
            value={nextStatus}
            onChange={setNextStatus}
            options={[
              { value: '', label: 'Selecciona un estado' },
              ...transitions[order.status].map((value) => ({
                value,
                label: labels[value] || value,
              })),
            ]}
          />
          <Button
            disabled={!nextStatus}
            pending={action.pending}
            onClick={() => setConfirming(true)}
          >
            Actualizar estado
          </Button>
        </section>
      )}
      {confirming && (
        <Modal title="Confirmar cambio de estado" onClose={() => setConfirming(false)}>
          <p>
            {nextStatus === 'CANCELLED'
              ? `Se cancelará esta orden y se restaurará el stock.${order.status === 'PAID' ? ' También se solicitará el reembolso del pago aprobado.' : ''}`
              : `La orden pasará a «${labels[nextStatus]}».`}
          </p>
          <ErrorNotice message={action.error} />
          <Button
            variant={nextStatus === 'CANCELLED' ? 'danger' : 'primary'}
            pending={action.pending}
            onClick={() => void save()}
          >
            Confirmar cambio
          </Button>
        </Modal>
      )}
    </>
  );
}
