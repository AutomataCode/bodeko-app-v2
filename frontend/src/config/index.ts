import axios from 'axios';
/** Marca del proyecto; el nombre del Excel corresponde únicamente a su plantilla. */
export const APP_NAME = 'Bodeko';
/** Clave de la sesión del propietario en Bodeko; la API valida siempre el JWT. */
export const SESSION_KEY = 'bodeko.token';
/** Cada tienda tiene sus propios clientes, así que cada una guarda su propia sesión. */
export function storeSessionKey(subdomain: string) {
  return `${SESSION_KEY}.tienda.${subdomain.toLowerCase()}`;
}
/** La sesión activa depende de la página: dentro de /tienda/:subdominio se usa la del cliente. */
export function currentSessionKey() {
  const match = /^\/tienda\/([^/]+)/.exec(window.location.pathname);
  return match?.[1] ? storeSessionKey(decodeURIComponent(match[1])) : SESSION_KEY;
}
/** Configura la conexión y el tiempo de espera de las peticiones a la API. */
export const api = axios.create({
  baseURL: `${(import.meta.env.VITE_API_URL || '').replace(/\/$/, '')}/api`,
  timeout: 35000,
});
api.interceptors.request.use((config) => {
  const token = sessionStorage.getItem(currentSessionKey());
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
      !error.config?.url?.endsWith('/login')
    ) {
      sessionStorage.removeItem(currentSessionKey());
      window.dispatchEvent(new Event('bodeko:unauthorized'));
    }
    return Promise.reject(error);
  },
);
