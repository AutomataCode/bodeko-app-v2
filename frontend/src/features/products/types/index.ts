/** Estados de visibilidad aceptados por el backend. */
export type ProductStatus = 'DRAFT' | 'ACTIVE' | 'INACTIVE' | 'ARCHIVED';
/** Producto administrativo con información de publicación e impuestos. */
export interface Product {
  id: string;
  categoryId: string | null;
  name: string;
  description: string | null;
  basePrice: number;
  imageUrl: string | null;
  taxExempt: boolean;
  status: ProductStatus;
  createdAt: string;
}
/** Unidad vendible con SKU, opciones y existencias propias. */
export interface ProductVariant {
  id: string;
  productId: string;
  sku: string;
  options: Record<string, string>;
  basePrice: number;
  quantity: number;
  isDefault: boolean;
  active: boolean;
}
/** Datos editables del producto; estado se modifica con endpoint separado. */
export interface ProductInput {
  name: string;
  description: string;
  basePrice: number;
  imageUrl: string | null;
  categoryId: string | null;
  taxExempt: boolean;
}
