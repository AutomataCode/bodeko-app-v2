import type { InvoiceType, Order, OrderStatus } from '@/features/orders';

/** Datos mínimos para confirmar una cancelación; no permite editar precios ni estados. */
export interface CancelableOrder {
  id: string;
  orderNumber: string;
  status: OrderStatus;
  total: number;
  currency: string;
}

/** Compra del cliente en cualquier tienda, según la página real enviada por el servidor. */
export interface CustomerOrderSummary extends CancelableOrder {
  storeId: string;
  storeName: string;
  storeSubdomain: string;
  invoiceType: InvoiceType;
  createdAt: string;
}

/** Datos guardados de la compra y de la tienda para continuar el pago. */
export interface CustomerOrderDetail {
  order: Order;
  storeName: string;
  storeSubdomain: string;
  currency: string;
}
