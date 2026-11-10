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
}
/** Filtros y página enviados al catálogo público. */
export interface CatalogFilters {
  search: string;
  categoryId: string;
  inStock: boolean;
  page: number;
}
