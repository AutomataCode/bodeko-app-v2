import { idempotentRequest } from '@/config/idempotency';
import { api } from '@/config';
import type { Order } from '@/features/orders';
import type { CheckoutInput } from '../types';
/** Crea la orden, fija los precios y descuenta stock en una sola operación del servidor. */
export const checkoutService = {
  create: async (storeId: string, body: CheckoutInput, cartId = '') =>
    idempotentRequest<Order>('post', `/customer/stores/${storeId}/orders`, body, cartId),
  get: async (storeId: string, orderId: string) =>
    (await api.get<Order>(`/customer/stores/${storeId}/orders/${orderId}`)).data,
};
