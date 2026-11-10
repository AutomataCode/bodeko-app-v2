import { AlertCircle } from 'lucide-react';
import { Button, ErrorNotice, Modal } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { money } from '@/utils/format';
import { customerOrderService } from '../services/customerOrderService';
import type { CancelableOrder } from '../types';

/** Orden que se va a cancelar y función para actualizar la pantalla. */
export interface CancelOrderDialogProps {
  order: CancelableOrder;
  onClose: () => void;
  onCancelled: () => void;
}

/** Confirmación explícita; evita cerrar mientras la transacción de cancelación está pendiente. */
export function CancelOrderDialog({ order, onClose, onCancelled }: CancelOrderDialogProps) {
  const action = useAction();
  const paid = order.status === 'PAID';
  async function confirm() {
    const result = await action.run(
      () => customerOrderService.cancel(order.id),
      'Orden cancelada correctamente.',
    );
    if (result) onCancelled();
  }
  return (
    <Modal
      title="Cancelar mi orden"
      onClose={() => {
        if (!action.pending) onClose();
      }}
    >
      <div className="cancel-order-intro">
        <span>
          <AlertCircle size={24} />
        </span>
        <div>
          <h3>{order.orderNumber}</h3>
          <p>Importe: {money(order.total, order.currency)}</p>
        </div>
      </div>
      <p>
        {paid
          ? 'Al confirmar se cancelará tu compra y se solicitará la devolución del pago. Si la pasarela no acepta la devolución, la orden conservará su estado.'
          : 'Al confirmar se cancelará tu compra y los productos volverán a estar disponibles en la tienda. Esta acción no se puede deshacer.'}
      </p>
      <ErrorNotice message={action.error} />
      <div className="dialog-actions">
        <Button variant="secondary" disabled={action.pending} onClick={onClose}>
          Conservar orden
        </Button>
        <Button variant="danger" pending={action.pending} onClick={() => void confirm()}>
          Sí, cancelar orden
        </Button>
      </div>
    </Modal>
  );
}
