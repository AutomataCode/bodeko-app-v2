import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { customerOrderService } from '../services/customerOrderService';

/** Historial remoto con filtros y paginación; descarta respuestas de filtros anteriores. */
export function useCustomerOrders(status: string, page: number) {
  return useResource(useCallback(() => customerOrderService.list(status, page), [status, page]));
}
