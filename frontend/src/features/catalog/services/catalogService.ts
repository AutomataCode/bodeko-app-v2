import { api } from '@/config';
import type { Page } from '@/core/types';
import type { CatalogFilters, CatalogProduct } from '../types';
/** Catálogo real con búsqueda, filtros y paginación en el servidor. */
export const catalogService = {
  search: async (storeId: string, filters: CatalogFilters) =>
    (
      await api.get<Page<CatalogProduct>>(`/public/stores/${storeId}/products`, {
        params: {
          search: filters.search || undefined,
          categoryId: filters.categoryId || undefined,
          inStock: filters.inStock || undefined,
          page: filters.page,
          size: 12,
        },
      })
    ).data,
  detail: async (storeId: string, productId: string) =>
    (await api.get<CatalogProduct>(`/public/stores/${storeId}/products/${productId}`)).data,
};
