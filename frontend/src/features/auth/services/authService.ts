import { api } from '@/config';
import type { Id, User } from '@/core/types';
import type { Credentials, Registration, TokenResponse } from '../types';
/** Peticiones de acceso: propietarios en Bodeko y clientes dentro de cada tienda. */
export const authService = {
  login: async (body: Credentials) => (await api.post<TokenResponse>('/auth/login', body)).data,
  registerOwner: async (body: Registration) =>
    (await api.post<TokenResponse>('/auth/register-owner', body)).data,
  storeLogin: async (storeId: Id, body: Credentials) =>
    (await api.post<TokenResponse>(`/auth/stores/${storeId}/login`, body)).data,
  storeRegister: async (storeId: Id, body: Registration) =>
    (await api.post<TokenResponse>(`/auth/stores/${storeId}/register`, body)).data,
  me: async () => (await api.get<User>('/auth/me')).data,
};
