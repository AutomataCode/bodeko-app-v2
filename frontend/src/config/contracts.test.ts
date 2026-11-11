import { beforeEach, describe, expect, it, vi } from 'vitest';
vi.mock('@/config', () => ({
  api: {
    get: vi.fn(),
    request: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    patch: vi.fn(),
    delete: vi.fn(),
  },
}));
import { api } from '@/config';
import { catalogService } from '@/features/catalog/services/catalogService';
import { cartService } from '@/features/cart/services/cartService';
import { checkoutService } from '@/features/checkout/services/checkoutService';
import { paymentService } from '@/features/payments/services/paymentService';
import { inventoryService } from '@/features/inventory/services/inventoryService';

describe('Contratos de integración con Spring', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(api.request).mockResolvedValue({ data: {} });
    for (const method of [api.get, api.post, api.patch])
      vi.mocked(method).mockResolvedValue({ data: {} });
  });
  it('envía filtros al catálogo público sin exigir un token de cliente', async () => {
    await catalogService.search('store', {
      search: 'café',
      categoryId: '',
      inStock: true,
      page: 2,
    });
    expect(api.get).toHaveBeenCalledWith('/public/stores/store/products', {
      params: { search: 'café', categoryId: undefined, inStock: true, page: 2, size: 12 },
    });
  });
  it('persiste cambios de cantidad mediante el endpoint del cliente', async () => {
    await cartService.update('store', 'product', 3);
    expect(api.patch).toHaveBeenCalledWith('/customer/stores/store/cart/items/product', {
      quantity: 3,
    });
  });
  it('no envía importes desde el navegador al cobrar una orden', async () => {
    await paymentService.pay('store', 'order', 'tkn_test_valid');
    expect(api.request).toHaveBeenCalledWith(
      expect.objectContaining({
        method: 'post',
        url: '/customer/stores/store/orders/order/payments',
        data: { sourceId: 'tkn_test_valid' },
        headers: { 'Idempotency-Key': expect.any(String) },
      }),
    );
  });
  it('recupera una orden por la ruta protegida del cliente', async () => {
    await checkoutService.get('store', 'order');
    expect(api.get).toHaveBeenCalledWith('/customer/stores/store/orders/order');
  });
  it('mantiene cantidad y umbral como operaciones separadas', async () => {
    await inventoryService.threshold('store', 'product', 5);
    expect(api.patch).toHaveBeenCalledWith(
      '/stores/store/products/product/inventory/low-stock-threshold',
      { lowStockThreshold: 5 },
    );
  });
});
