import { beforeEach, describe, expect, it, vi } from 'vitest';
vi.mock('@/config', () => ({ api: { get: vi.fn(), post: vi.fn() } }));
import { api } from '@/config';
import { customerOrderService } from './customerOrderService';

describe('Contrato del área de cliente', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(api.get).mockResolvedValue({ data: {} });
    vi.mocked(api.post).mockResolvedValue({ data: {} });
  });
  it('no transmite identificadores de cliente para obtener el historial', async () => {
    await customerOrderService.list('PENDING', 2);
    expect(api.get).toHaveBeenCalledWith('/customer/orders', {
      params: { status: 'PENDING', page: 2, size: 6 },
    });
  });
  it('solo solicita cancelar, sin permitir estados arbitrarios en el payload', async () => {
    await customerOrderService.cancel('order');
    expect(api.post).toHaveBeenCalledWith('/customer/orders/order/cancel');
  });
});
