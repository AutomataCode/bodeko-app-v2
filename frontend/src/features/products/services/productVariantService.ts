import { api } from '@/config';
import type { ProductVariant } from '../types';

/** Administra las variantes vendibles y únicas de un producto. */
export const productVariantService = {
  list: async (storeId: string, productId: string) =>
    (await api.get<ProductVariant[]>(`/stores/${storeId}/products/${productId}/variants`)).data,
  create: async (
    storeId: string,
    productId: string,
    body: { sku: string; options: Record<string, string>; basePrice: number },
  ) =>
    (
      await api.post<ProductVariant>(
        `/stores/${storeId}/products/${productId}/variants`,
        body,
      )
    ).data,
  update: async (
    storeId: string,
    productId: string,
    variantId: string,
    body: { sku: string; options: Record<string, string>; basePrice: number },
  ) => (await api.put<ProductVariant>(
    `/stores/${storeId}/products/${productId}/variants/${variantId}`,
    body,
  )).data,
  changeStatus: async (storeId: string, productId: string, variantId: string, active: boolean) =>
    (await api.patch<ProductVariant>(
      `/stores/${storeId}/products/${productId}/variants/${variantId}/status`,
      { active },
    )).data,
};
