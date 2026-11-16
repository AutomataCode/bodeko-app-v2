/** Producto activo del catálogo con su oferta y disponibilidad actuales. */
export interface CatalogProduct {
  id: string;
  categoryId: string | null;
  name: string;
  description: string | null;
  imageUrl: string | null;
  basePrice: number;
  offerPrice: number | null;
  inStock: boolean;
  variants?: CatalogVariant[];
}
/** Variante concreta disponible para selección y compra. */
export interface CatalogVariant {
  id: string;
  sku: string;
  options: Record<string, string>;
  basePrice: number;
  offerPrice: number | null;
  quantity: number;
}
/** Filtros y página enviados al catálogo público. */
export interface CatalogFilters {
  search: string;
  categoryId: string;
  inStock: boolean;
  page: number;
}
