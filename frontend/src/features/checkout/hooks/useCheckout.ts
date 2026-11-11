import { useCart } from '@/context/CartContext';
import { useAction } from '@/core/hooks/useAction';
import { checkoutService } from '../services/checkoutService';
import type { CheckoutInput } from '../types';
/** Coordina confirmación de la orden y manejo de errores de stock/datos fiscales. */
export function useCheckout(storeId: string) {
  const action = useAction();
  const { cart } = useCart();
  return {
    ...action,
    create: (body: CheckoutInput) =>
      action.run(
        () => checkoutService.create(storeId, body, cart?.id || ''),
        'Orden creada. Continúa con el pago.',
      ),
  };
}
