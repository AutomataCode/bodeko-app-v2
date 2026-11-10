import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { productService } from '../services/productService';
/** Recupera el catálogo completo del propietario, incluidos borradores y archivados. */
export function useProducts(storeId: string) {
  return useResource(useCallback(() => productService.list(storeId), [storeId]));
}
