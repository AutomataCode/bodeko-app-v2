import { useAction } from '@/core/hooks/useAction';
import { paymentService } from '../services/paymentService';
/** Ejecuta el cobro; no reintenta automáticamente operaciones financieras. */
export function usePayment(storeId: string, orderId: string) {
  const action = useAction();
  return {
    ...action,
    pay: (token: string) => action.run(() => paymentService.pay(storeId, orderId, token)),
  };
}
