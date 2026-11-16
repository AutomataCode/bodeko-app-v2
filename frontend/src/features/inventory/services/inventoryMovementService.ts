import { api } from '@/config';
import type { InventoryMovementFilters, InventoryMovementPage } from '../types/movement';

/** Consulta movimientos paginados; la tienda se valida también en el backend. */
export const inventoryMovementService = {
  async list(storeId: string, filters: InventoryMovementFilters): Promise<InventoryMovementPage> {
    const params = Object.fromEntries(Object.entries(filters).filter(([, value]) => value !== ''));
    return (await api.get<InventoryMovementPage>(`/stores/${storeId}/inventory/movements`, { params })).data;
  },
};
