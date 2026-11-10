import { api } from '@/config';
import type { PublicStore, Store, StoreInput, TopSellingStore } from '../types';
/** Descubrimiento público separado de las operaciones administrativas. */
export const storeService = {
  publicList: async () => (await api.get<PublicStore[]>('/public/stores')).data,
  topSelling: async () => (await api.get<TopSellingStore[]>('/public/stores/top-selling')).data,
  bySubdomain: async (subdomain: string) =>
    (await api.get<PublicStore>(`/public/stores/by-subdomain/${encodeURIComponent(subdomain)}`))
      .data,
  owned: async () => (await api.get<Store[]>('/stores')).data,
  create: async (body: StoreInput) => (await api.post<Store>('/stores', body)).data,
  tax: async (id: string, taxRate: number) =>
    (await api.patch<Store>(`/stores/${id}/tax-rate`, { taxRate })).data,
};
