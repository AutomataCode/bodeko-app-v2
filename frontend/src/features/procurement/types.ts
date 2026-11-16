export interface Supplier { id: string; name: string; contactEmail: string | null; phone: string | null; notes: string | null; }
export type PurchaseOrderStatus = 'ORDERED' | 'PARTIALLY_RECEIVED' | 'RECEIVED' | 'CANCELLED';
export interface PurchaseOrderItem { id: string; variantId: string; productName: string; sku: string; variantOptions: string; quantityOrdered: number; quantityReceived: number; quantityRejected: number; quantityPending: number; unitCost: number; }
export interface PurchaseOrder { id: string; orderNumber: string; supplierId: string; supplierName: string; status: PurchaseOrderStatus; notes: string | null; createdAt: string; items: PurchaseOrderItem[]; }
