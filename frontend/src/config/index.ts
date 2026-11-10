import axios from 'axios';
/** Marca del proyecto; el nombre del Excel corresponde únicamente a su plantilla. */
export const APP_NAME = 'Bodeko';
/** Clave de almacenamiento de la sesión; la API valida siempre el JWT. */
export const SESSION_KEY = 'bodeko.token';
/** Configura la conexión y el tiempo de espera de las peticiones a la API. */
export const api = axios.create({
  baseURL: `${(import.meta.env.VITE_API_URL || '').replace(/\/$/, '')}/api`,
  timeout: 35000,
});
api.interceptors.request.use((config) => {
  const token = sessionStorage.getItem(SESSION_KEY);
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});
api.interceptors.response.use(
  (response) => response,
  (error: unknown) => {
    // Un 401 del login no invalida otra sesión; los recursos protegidos sí.
    if (
      axios.isAxiosError(error) &&
      error.response?.status === 401 &&
      !error.config?.url?.includes('/auth/login')
    ) {
      sessionStorage.removeItem(SESSION_KEY);
      window.dispatchEvent(new Event('bodeko:unauthorized'));
    }
    return Promise.reject(error);
  },
);
