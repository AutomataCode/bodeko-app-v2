import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { getDashboardMetrics } from '../services/dashboardService';
/** Consulta métricas al entrar o actualizar el resumen de una tienda. */
export function useDashboard(storeId: string) {
  return useResource(useCallback(() => getDashboardMetrics(storeId), [storeId]));
}
