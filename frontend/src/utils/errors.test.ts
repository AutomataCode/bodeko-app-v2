import { AxiosError, AxiosHeaders } from 'axios';
import { describe, expect, it } from 'vitest';
import { errorMessage } from './errors';

/** Error remoto de prueba sin conectarse a cuentas externas. */
function remoteError(code: string, status = 400) {
  const config = { headers: new AxiosHeaders() };
  return new AxiosError('Technical error', 'ERR_BAD_RESPONSE', config, undefined, {
    status,
    statusText: 'error',
    config,
    headers: {},
    data: { code, message: 'Internal details in English' },
  });
}
describe('Errores visibles en español', () => {
  it('traduce rechazos de pago y conserva la privacidad del servidor', () => {
    expect(errorMessage(remoteError('PAYMENT_DECLINED'))).toContain('rechazado');
    expect(errorMessage(remoteError('CATEGORY_HAS_PRODUCTS'))).toContain('Reasigna');
    expect(errorMessage(remoteError('UNKNOWN', 500))).not.toContain('English');
  });
  it('explica fallos de red y expiración de sesión', () => {
    expect(errorMessage(new AxiosError('Network Error'))).toContain('conectar');
    expect(errorMessage(remoteError('UNKNOWN', 401))).toContain('sesión');
  });
});
