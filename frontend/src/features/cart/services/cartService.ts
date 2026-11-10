import { api } from '@/config';
import type { Cart } from '../types';
/** El carrito vive en PostgreSQL para recuperarlo al volver a iniciar sesión. */
export const cartService = {
  get: async (storeId: string) => (await api.get<Cart>(`/customer/stores/${storeId}/cart`)).data,
  add: async (storeId: string, productId: string, quantity: number) =>
    (await api.post<Cart>(`/customer/stores/${storeId}/cart/items`, { productId, quantity })).data,
  update: async (storeId: string, productId: string, quantity: number) =>
    (await api.patch<Cart>(`/customer/stores/${storeId}/cart/items/${productId}`, { quantity }))
      .data,
  remove: async (storeId: string, productId: string) =>
    (await api.delete<Cart>(`/customer/stores/${storeId}/cart/items/${productId}`)).data,
};
