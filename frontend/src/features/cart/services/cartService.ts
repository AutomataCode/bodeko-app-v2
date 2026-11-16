import { api } from '@/config';
import type { Cart } from '../types';
/** El carrito vive en PostgreSQL para recuperarlo al volver a iniciar sesión. */
export const cartService = {
  get: async (storeId: string) => (await api.get<Cart>(`/customer/stores/${storeId}/cart`)).data,
  add: async (storeId: string, productId: string, variantId: string, quantity: number) =>
    (await api.post<Cart>(`/customer/stores/${storeId}/cart/items`, { productId, variantId, quantity })).data,
  update: async (storeId: string, variantId: string, quantity: number) =>
    (await api.patch<Cart>(`/customer/stores/${storeId}/cart/items/${variantId}`, { quantity }))
      .data,
  remove: async (storeId: string, variantId: string) =>
    (await api.delete<Cart>(`/customer/stores/${storeId}/cart/items/${variantId}`)).data,
};
