import { api } from '@/config';
import type { Category } from '../types';
/** Operaciones reales de categorías con alcance de tienda. */
export const categoryService = {
  list: async (storeId: string, publicRead = false) =>
    (await api.get<Category[]>(`${publicRead ? '/public' : ''}/stores/${storeId}/categories`)).data,
  save: async (storeId: string, name: string, id?: string) =>
    id
      ? (await api.put<Category>(`/stores/${storeId}/categories/${id}`, { name })).data
      : (await api.post<Category>(`/stores/${storeId}/categories`, { name })).data,
  remove: async (storeId: string, id: string) => {
    await api.delete(`/stores/${storeId}/categories/${id}`);
    return true;
  },
};
