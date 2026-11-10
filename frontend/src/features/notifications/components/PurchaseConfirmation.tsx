import { CircleCheck } from 'lucide-react';
import type { PurchaseConfirmationProps } from '../types';
import { usePurchaseConfirmation } from '../hooks/usePurchaseConfirmation';
/** Confirmación persistente después de verificar el estado pagado de la orden. */
export function PurchaseConfirmation({ orderNumber }: PurchaseConfirmationProps) {
  const { customerName } = usePurchaseConfirmation();
  return (
    <div className="notice success" role="status">
      <CircleCheck size={24} />
      <div>
        <strong>Tu compra fue confirmada, {customerName}.</strong>
        <p>Orden {orderNumber}. Conserva este número para consultar sobre tu compra.</p>
      </div>
    </div>
  );
}
