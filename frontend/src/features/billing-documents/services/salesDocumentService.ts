import { api } from '@/config';
import type { BillingProfile, BillingProfileInput, SalesDocument } from '../types';

/** Consulta el perfil de emisor y los documentos de la tienda seleccionada. */
export const salesDocumentService = {
  billingProfile: async (storeId: string) =>
    (await api.get<BillingProfile>(`/stores/${storeId}/billing-profile`)).data,
  updateBillingProfile: async (storeId: string, body: BillingProfileInput) =>
    (await api.put<BillingProfile>(`/stores/${storeId}/billing-profile`, body)).data,
  forStore: async (storeId: string, orderId: string) =>
    (await api.get<SalesDocument>(`/stores/${storeId}/orders/${orderId}/sales-document`)).data,
  forCustomer: async (orderId: string) =>
    (await api.get<SalesDocument>(`/customer/orders/${orderId}/sales-document`)).data,
};
