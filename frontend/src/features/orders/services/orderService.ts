import { idempotentRequest } from '@/config/idempotency';
import { api } from '@/config';
import type { Page } from '@/core/types';
import type { Order, OrderStatus, OrderSummary } from '../types';
/** Gestión de órdenes del propietario; el servidor verifica cada transición. */
export const orderService = {
  list: async (storeId: string, status: string, page: number) =>
    (
      await api.get<Page<OrderSummary>>(`/stores/${storeId}/orders`, {
        params: { status: status || undefined, page, size: 15 },
      })
    ).data,
  detail: async (storeId: string, orderId: string) =>
    (await api.get<Order>(`/stores/${storeId}/orders/${orderId}`)).data,
  status: async (storeId: string, orderId: string, status: OrderStatus) =>
    idempotentRequest<Order>('patch', `/stores/${storeId}/orders/${orderId}/status`, { status }),
};
