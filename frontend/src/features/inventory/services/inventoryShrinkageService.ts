import { api } from '@/config';
import type { InventoryMovementFilters, InventoryMovementPage } from '../types/movement';

/** Consulta el historial de pérdidas y registra nuevas mermas por variante. */
export const inventoryShrinkageService = {
  async list(storeId: string, filters: InventoryMovementFilters): Promise<InventoryMovementPage> {
    const params = Object.fromEntries(Object.entries(filters).filter(([, value]) => value !== ''));
    return (await api.get<InventoryMovementPage>(`/stores/${storeId}/inventory/shrinkages`, { params })).data;
  },
  async register(storeId: string, body: { productId: string; variantId: string; quantity: number; type: 'DAMAGE' | 'LOSS'; reason: string }) {
    return (await api.post(`/stores/${storeId}/inventory/shrinkages`, body)).data;
  },
};
