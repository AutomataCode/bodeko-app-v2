import { api } from '@/config';
import type { Page } from '@/core/types';
import type { Product } from '@/features/products';
import type { DashboardMetrics } from '../types';
/** Consulta el total de productos y órdenes de la tienda. */
export async function getDashboardMetrics(storeId: string): Promise<DashboardMetrics> {
  const [products, pending, paid] = await Promise.all([
    api.get<Product[]>(`/stores/${storeId}/products`),
    api.get<Page<unknown>>(`/stores/${storeId}/orders`, { params: { status: 'PENDING', size: 1 } }),
    api.get<Page<unknown>>(`/stores/${storeId}/orders`, { params: { status: 'PAID', size: 1 } }),
  ]);
  return {
    products: products.data.length,
    activeProducts: products.data.filter((item) => item.status === 'ACTIVE').length,
    pendingOrders: pending.data.totalElements,
    paidOrders: paid.data.totalElements,
  };
}
