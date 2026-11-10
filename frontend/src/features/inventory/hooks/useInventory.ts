import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { inventoryService } from '../services/inventoryService';
/** Consulta stock del producto seleccionado, sin disparar una petición por cada fila. */
export function useInventory(storeId: string, productId: string) {
  return useResource(
    useCallback(() => inventoryService.get(storeId, productId), [storeId, productId]),
    !!productId,
  );
}
