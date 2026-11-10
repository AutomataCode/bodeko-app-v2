import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { categoryService } from '../services/categoryService';
/** Carga categorías según el permiso requerido por la pantalla. */
export function useCategories(storeId: string, publicRead = false) {
  return useResource(
    useCallback(() => categoryService.list(storeId, publicRead), [storeId, publicRead]),
  );
}
