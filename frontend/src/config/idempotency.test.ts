import { beforeEach, describe, expect, it, vi } from 'vitest';
vi.mock('@/config', () => ({ api: { request: vi.fn() } }));
import { api } from '@/config';
import { idempotentRequest } from './idempotency';

/** Comprueba reintentos y separación de compras, no solo la forma de la petición. */
describe('Claves de operaciones', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });
  it('reutiliza la clave después de perder la respuesta de red', async () => {
    vi.mocked(api.request)
      .mockRejectedValueOnce(new Error('red'))
      .mockResolvedValueOnce({ data: { id: 'orden' } });
    await expect(idempotentRequest('post', '/qa/retry', {})).rejects.toThrow();
    await idempotentRequest('post', '/qa/retry', {});
    expect(vi.mocked(api.request).mock.calls[0]?.[0].headers).toEqual(
      vi.mocked(api.request).mock.calls[1]?.[0].headers,
    );
  });
  it('una compra nueva con otro carrito recibe otra clave', async () => {
    vi.mocked(api.request).mockResolvedValue({ data: {} });
    await idempotentRequest('post', '/qa/cart', {}, 'cart-one');
    await idempotentRequest('post', '/qa/cart', {}, 'cart-two');
    expect(vi.mocked(api.request).mock.calls[0]?.[0].headers).not.toEqual(
      vi.mocked(api.request).mock.calls[1]?.[0].headers,
    );
  });
  it('permite otra clave tras un rechazo definitivo de datos', async () => {
    vi.mocked(api.request)
      .mockRejectedValueOnce({ response: { status: 400, data: { code: 'VALIDATION_ERROR' } } })
      .mockResolvedValueOnce({ data: {} });
    await expect(idempotentRequest('post', '/qa/invalid', {})).rejects.toBeDefined();
    await idempotentRequest('post', '/qa/invalid', {});
    expect(vi.mocked(api.request).mock.calls[0]?.[0].headers).not.toEqual(
      vi.mocked(api.request).mock.calls[1]?.[0].headers,
    );
  });
  it('conserva la clave cuando el pago requiere revisión', async () => {
    vi.mocked(api.request)
      .mockRejectedValueOnce({ response: { status: 409, data: { code: 'OPERATION_UNCERTAIN' } } })
      .mockResolvedValueOnce({ data: {} });
    await expect(idempotentRequest('post', '/qa/uncertain', {})).rejects.toBeDefined();
    await idempotentRequest('post', '/qa/uncertain', {});
    expect(vi.mocked(api.request).mock.calls[0]?.[0].headers).toEqual(
      vi.mocked(api.request).mock.calls[1]?.[0].headers,
    );
  });
});
