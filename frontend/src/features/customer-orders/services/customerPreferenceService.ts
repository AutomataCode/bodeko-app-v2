import { api } from '@/config';
export const customerPreferenceService = {
  marketing: async () => (await api.get<{ enabled: boolean }>('/customer/preferences/marketing')).data,
  updateMarketing: async (enabled: boolean) => (await api.patch<{ enabled: boolean }>('/customer/preferences/marketing', { enabled })).data,
};
