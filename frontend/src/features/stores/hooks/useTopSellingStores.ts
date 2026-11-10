import { useResource } from '@/core/hooks/useResource';
import { storeService } from '../services/storeService';

/** Carga las tres tiendas más vendidas y permite reintentar si falla la API. */
export function useTopSellingStores() {
  return useResource(storeService.topSelling);
}
