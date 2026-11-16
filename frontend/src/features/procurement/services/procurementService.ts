import { api } from '@/config';
import { idempotentRequest } from '@/config/idempotency';
import type { PurchaseOrder, Supplier } from '../types';

export const procurementService = {
  suppliers: async (storeId: string) => (await api.get<Supplier[]>(`/stores/${storeId}/procurement/suppliers`)).data,
  createSupplier: async (storeId: string, body: { name: string; contactEmail: string | null; phone: string | null; notes: string | null }) =>
    (await api.post<Supplier>(`/stores/${storeId}/procurement/suppliers`, body)).data,
  orders: async (storeId: string) => (await api.get<PurchaseOrder[]>(`/stores/${storeId}/procurement/orders`)).data,
  createOrder: async (storeId: string, body: { supplierId: string; items: { variantId: string; quantity: number; unitCost: number }[]; notes: string }) =>
    idempotentRequest<PurchaseOrder>('post', `/stores/${storeId}/procurement/orders`, body),
  receive: async (storeId: string, orderId: string, body: { items: { itemId: string; accepted: number; rejected: number }[]; reason: string }) =>
    idempotentRequest<PurchaseOrder>('post', `/stores/${storeId}/procurement/orders/${orderId}/receive`, body),
};
