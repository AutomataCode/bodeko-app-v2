/** Stock actual y punto de alerta calculado por el backend. */
export interface Inventory {
  productId: string;
  variantId: string;
  sku: string;
  quantity: number;
  lowStockThreshold: number;
  lowStock: boolean;
  updatedAt: string;
}
