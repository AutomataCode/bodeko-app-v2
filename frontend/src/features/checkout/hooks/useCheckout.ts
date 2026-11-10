import { useAction } from '@/core/hooks/useAction';
import { checkoutService } from '../services/checkoutService';
import type { CheckoutInput } from '../types';
/** Coordina confirmación de la orden y manejo de errores de stock/datos fiscales. */
export function useCheckout(storeId: string) {
  const action = useAction();
  return {
    ...action,
    create: (body: CheckoutInput) =>
      action.run(
        () => checkoutService.create(storeId, body),
        'Orden creada. Continúa con el pago.',
      ),
  };
}
