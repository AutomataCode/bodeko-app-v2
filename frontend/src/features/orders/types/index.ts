/** Estados de órdenes del backend; etiquetas traducidas en la UI. */
export type OrderStatus = 'PENDING' | 'PAID' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
/** Comprobante fiscal elegido al confirmar una compra. */
export type InvoiceType = 'RECEIPT' | 'INVOICE';
/** Línea histórica: el backend congela nombres e importes al crear la orden. */
export interface OrderItem {
  productId: string;
  productName: string;
  quantity: number;
  listUnitPrice: number;
  chargedUnitPrice: number;
  itemSubtotal: number;
}
/** Resumen para listados paginados de administración. */
export interface OrderSummary {
  id: string;
  orderNumber: string;
  invoiceType: InvoiceType;
  customerDocumentType: 'DNI' | 'RUC';
  customerDocumentNumber: string;
  customerBusinessName: string | null;
  total: number;
  status: OrderStatus;
  createdAt: string;
}
/** Detalle completo de una orden con datos tributarios y totales remotos. */
export interface Order extends OrderSummary {
  storeId: string;
  customerBillingAddress: string | null;
  subtotal: number;
  taxAmount: number;
  appliedTaxRate: number;
  items: OrderItem[];
}
