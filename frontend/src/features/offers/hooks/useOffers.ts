import { useCallback } from 'react';
import { useResource } from '@/core/hooks/useResource';
import { offerService } from '../services/offerService';
/** Recupera ofertas únicamente cuando hay un producto seleccionado. */
export function useOffers(storeId: string, productId: string) {
  return useResource(
    useCallback(() => offerService.list(storeId, productId), [storeId, productId]),
    !!productId,
  );
}
