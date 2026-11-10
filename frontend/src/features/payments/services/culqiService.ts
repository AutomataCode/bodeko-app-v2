import type { CulqiConstructor } from '../types';
import { UserFacingError } from '@/utils/errors';

/** Carga única del SDK oficial; se elimina el script fallido para permitir reintentos. */
let sdkPromise: Promise<CulqiConstructor> | undefined;
/** Carga el formulario oficial de Culqi con un tiempo de espera limitado. */
export function loadCulqi(): Promise<CulqiConstructor> {
  const sdkWindow = window as Window & { CulqiCheckout?: CulqiConstructor };
  if (sdkWindow.CulqiCheckout) return Promise.resolve(sdkWindow.CulqiCheckout);
  if (sdkPromise) return sdkPromise;
  sdkPromise = new Promise<CulqiConstructor>((resolve, reject) => {
    const script = document.createElement('script');
    script.src = 'https://js.culqi.com/checkout-js';
    script.async = true;
    const fail = () => {
      clearTimeout(timer);
      script.remove();
      sdkPromise = undefined;
      reject(
        new UserFacingError(
          'No se pudo cargar el formulario de Culqi. Revisa tu conexión e inténtalo nuevamente.',
        ),
      );
    };
    const timer = window.setTimeout(fail, 15000);
    script.onload = () => {
      clearTimeout(timer);
      if (sdkWindow.CulqiCheckout) resolve(sdkWindow.CulqiCheckout);
      else fail();
    };
    script.onerror = fail;
    document.head.appendChild(script);
  });
  return sdkPromise;
}
