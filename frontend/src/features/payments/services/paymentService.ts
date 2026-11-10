import { api } from '@/config';
import type { Payment } from '../types';
/** Envía solo el token: importe y correo se obtienen del pedido en el servidor. */
export const paymentService = {
  pay: async (storeId: string, orderId: string, sourceId: string) =>
    (
      await api.post<Payment>(`/customer/stores/${storeId}/orders/${orderId}/payments`, {
        sourceId,
      })
    ).data,
};
