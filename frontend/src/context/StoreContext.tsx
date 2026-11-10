import { createContext, useCallback, useContext, type ReactNode } from 'react';
import { storeService, type PublicStore } from '@/features/stores';
import { useResource } from '@/core/hooks/useResource';
import { ErrorNotice, Loading } from '@/core/components';

const StoreContext = createContext<PublicStore | null>(null);
/** Busca la tienda usando el subdominio de la URL. */
export function StoreProvider({ subdomain, children }: { subdomain: string; children: ReactNode }) {
  const resource = useResource(useCallback(() => storeService.bySubdomain(subdomain), [subdomain]));
  if (resource.loading) return <Loading />;
  if (!resource.data)
    return (
      <ErrorNotice
        message={resource.error || 'No se encontró la tienda.'}
        retry={resource.reload}
      />
    );
  return <StoreContext.Provider value={resource.data}>{children}</StoreContext.Provider>;
}
/** Permite consultar los datos de la tienda actual. */
export function useStore(): PublicStore {
  const value = useContext(StoreContext);
  if (!value) throw new Error('No se encontró la tienda actual.');
  return value;
}
