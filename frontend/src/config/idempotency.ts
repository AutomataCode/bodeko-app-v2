import { api } from '@/config';

/** Mantiene solo huellas y claves opacas en la pestaña, nunca el cuerpo con el token. */
const keys = new Map<string, string>();
/** Algunos navegadores restringen el almacenamiento; se conserva al menos la clave en memoria. */
function stored(key: string): string | null {
  try {
    return sessionStorage.getItem(key);
  } catch {
    return null;
  }
}
function save(key: string, value: string | null): void {
  try {
    if (value) sessionStorage.setItem(key, value);
    else sessionStorage.removeItem(key);
  } catch {
    /* La solicitud conserva su clave en memoria. */
  }
}
/** Una petición repetida conserva la clave tras timeout; un 4xx definitivo permite un intento nuevo. */
export async function idempotentRequest<T>(
  method: 'post' | 'patch',
  path: string,
  body: unknown,
  context = '',
): Promise<T> {
  const bytes = new TextEncoder().encode(JSON.stringify([method, path, body, context]));
  const digest = await crypto.subtle.digest('SHA-256', bytes);
  const identity = `bodeko:request:${Array.from(new Uint8Array(digest), (value) => value.toString(16).padStart(2, '0')).join('')}`;
  let key = keys.get(identity) || stored(identity);
  if (!key) {
    key = crypto.randomUUID();
    keys.set(identity, key);
    save(identity, key);
  }
  try {
    return (
      await api.request<T>({ method, url: path, data: body, headers: { 'Idempotency-Key': key } })
    ).data;
  } catch (cause: unknown) {
    const error = cause as { response?: { status: number; data?: { code?: string } } };
    const code = error.response?.data?.code;
    if (
      error.response &&
      error.response.status < 500 &&
      !['OPERATION_IN_PROGRESS', 'OPERATION_UNCERTAIN', 'CONCURRENT_UPDATE'].includes(code || '')
    ) {
      keys.delete(identity);
      save(identity, null);
    }
    throw cause;
  }
}
