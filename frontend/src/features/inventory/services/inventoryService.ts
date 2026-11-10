import { api } from '@/config';
import type { Inventory } from '../types';
/** Cambios separados de cantidad y umbral; no hay inventarios ficticios en el cliente. */
export const inventoryService = {
  get: async (storeId: string, productId: string) =>
    (await api.get<Inventory>(`/stores/${storeId}/products/${productId}/inventory`)).data,
  quantity: async (storeId: string, productId: string, quantity: number) =>
    (
      await api.patch<Inventory>(`/stores/${storeId}/products/${productId}/inventory/quantity`, {
        quantity,
      })
    ).data,
  threshold: async (storeId: string, productId: string, lowStockThreshold: number) =>
    (
      await api.patch<Inventory>(
        `/stores/${storeId}/products/${productId}/inventory/low-stock-threshold`,
        { lowStockThreshold },
      )
    ).data,
};
