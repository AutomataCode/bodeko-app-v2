import { api } from '@/config';
import type { Offer, OfferInput } from '../types';
/** Historial de ofertas y creación con control de solapamientos en el servidor. */
export const offerService = {
  list: async (storeId: string, productId: string) =>
    (await api.get<Offer[]>(`/stores/${storeId}/products/${productId}/offers`)).data,
  create: async (storeId: string, productId: string, body: OfferInput) =>
    (await api.post<Offer>(`/stores/${storeId}/products/${productId}/offers`, body)).data,
};
