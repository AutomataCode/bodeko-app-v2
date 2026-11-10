import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { storeService } from '../services/storeService';
/** Recupera tiendas públicas o únicamente las del propietario autenticado. */
export function useStores(owned = false) {
  return useResource(
    useCallback(() => (owned ? storeService.owned() : storeService.publicList()), [owned]),
  );
}
