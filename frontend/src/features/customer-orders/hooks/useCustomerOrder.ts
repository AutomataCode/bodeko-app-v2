import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { customerOrderService } from '../services/customerOrderService';

/** Recupera el detalle directamente desde la URL, incluida una recarga del navegador. */
export function useCustomerOrder(orderId: string) {
  return useResource(useCallback(() => customerOrderService.detail(orderId), [orderId]));
}
