import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { catalogService } from '../services/catalogService';
import type { CatalogFilters } from '../types';
/** Carga los productos según los filtros y la página elegida. */
export function useCatalog(storeId: string, filters: CatalogFilters) {
  const { search, categoryId, inStock, page } = filters;
  return useResource(
    useCallback(
      () => catalogService.search(storeId, { search, categoryId, inStock, page }),
      [storeId, search, categoryId, inStock, page],
    ),
  );
}
