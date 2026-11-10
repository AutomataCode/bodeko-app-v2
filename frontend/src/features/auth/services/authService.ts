import { api } from '@/config';
import type { User } from '@/core/types';
import type { Credentials, Registration, TokenResponse } from '../types';
/** Peticiones para iniciar sesión, registrarse y consultar la cuenta. */
export const authService = {
  login: async (body: Credentials) => (await api.post<TokenResponse>('/auth/login', body)).data,
  register: async (body: Registration, owner: boolean) =>
    (
      await api.post<TokenResponse>(
        owner ? '/auth/register-owner' : '/auth/register-customer',
        body,
      )
    ).data,
  me: async () => (await api.get<User>('/auth/me')).data,
};
