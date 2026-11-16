/** Movimiento auditado de inventario con responsable, saldo y referencia. */
export interface InventoryMovement {
  id: string;
  productId: string;
  variantId: string;
  productName: string;
  sku: string;
  variantOptions: string;
  movementType: 'RESERVATION' | 'SALE' | 'RESERVATION_RELEASE' | 'STOCK_ENTRY' | 'ADJUSTMENT' | 'RETURN' | 'DAMAGE' | 'LOSS';
  units: number;
  quantityChange: number;
  quantityBefore: number;
  quantityAfter: number;
  referenceType: string | null;
  referenceId: string | null;
  referenceNumber: string | null;
  reason: string | null;
  actorId: string | null;
  actorName: string | null;
  actorEmail: string | null;
  actorRole: string | null;
  createdAt: string;
}

/** Página de resultados en el formato de paginación del backend. */
export interface InventoryMovementPage {
  content: InventoryMovement[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

/** Filtros opcionales para el kardex de una tienda. */
export interface InventoryMovementFilters {
  search: string;
  type: string;
  actor: string;
  reference: string;
  from: string;
  to: string;
  page: number;
  size: number;
}
