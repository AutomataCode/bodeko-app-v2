import { api } from '@/config';
export interface LowStockVariant { productId: string; variantId: string; productName: string; sku: string; options: Record<string, string>; quantity: number; threshold: number; suggestedQuantity: number; updatedAt: string; }
export const lowStockService = { list: async (storeId: string) => (await api.get<LowStockVariant[]>(`/stores/${storeId}/inventory/low-stock`)).data };
