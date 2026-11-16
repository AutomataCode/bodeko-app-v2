import { api } from '@/config';
import type { Page } from '@/core/types';
import type { AccountFilters, ManagedAccount, PlatformStoreOption, PlatformUserSummary } from '../types';

/** Consulta y cambia estados de cuentas desde rutas privadas de superadmin. */
export const superadminService = {
  summary: async () => (await api.get<PlatformUserSummary>('/super-admin/summary')).data,
  stores: async () => (await api.get<PlatformStoreOption[]>('/super-admin/stores')).data,
  list: async (kind: 'vendors' | 'customers', filters: AccountFilters) =>
    (await api.get<Page<ManagedAccount>>(`/super-admin/${kind}`, {
      params: {
        search: filters.search || undefined,
        active: filters.active === '' ? undefined : filters.active === 'true',
        storeId: filters.storeId || undefined,
        from: filters.from || undefined,
        to: filters.to || undefined,
        page: filters.page,
        size: 20,
      },
    })).data,
  changeStatus: async (kind: 'vendors' | 'customers', id: string, active: boolean, reason: string) =>
    (await api.patch<ManagedAccount>(`/super-admin/${kind}/${id}/status`, { active, reason })).data,
};
