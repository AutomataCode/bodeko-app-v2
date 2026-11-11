import { idempotentRequest } from '@/config/idempotency';
import { api } from '@/config';
import type { Page } from '@/core/types';
import type { CustomerOrderDetail, CustomerOrderSummary } from '../types';

/** La API obtiene el cliente del JWT: ninguna operación acepta un customerId del navegador. */
export const customerOrderService = {
  list: async (status: string, page: number) =>
    (
      await api.get<Page<CustomerOrderSummary>>('/customer/orders', {
        params: { status: status || undefined, page, size: 6 },
      })
    ).data,
  detail: async (orderId: string) =>
    (await api.get<CustomerOrderDetail>(`/customer/orders/${orderId}`)).data,
  cancel: async (orderId: string) =>
    idempotentRequest<CustomerOrderDetail>('post', `/customer/orders/${orderId}/cancel`, {}),
};
