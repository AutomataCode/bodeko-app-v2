import { api } from '@/config';
import type { Product, ProductInput, ProductStatus } from '../types';
/** Productos aislados por tienda; nunca elimina historial de ventas. */
export const productService = {
  list: async (storeId: string) => (await api.get<Product[]>(`/stores/${storeId}/products`)).data,
  save: async (storeId: string, body: ProductInput, id?: string) =>
    id
      ? (await api.put<Product>(`/stores/${storeId}/products/${id}`, body)).data
      : (await api.post<Product>(`/stores/${storeId}/products`, body)).data,
  status: async (storeId: string, id: string, status: ProductStatus) =>
    (await api.patch<Product>(`/stores/${storeId}/products/${id}/status`, { status })).data,
};
