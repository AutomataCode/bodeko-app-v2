import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { orderService } from '../services/orderService';
/** Recupera la página de órdenes filtradas; el orden descendente pertenece al backend. */
export function useOrders(storeId: string, status: string, page: number) {
  return useResource(
    useCallback(() => orderService.list(storeId, status, page), [storeId, status, page]),
  );
}
