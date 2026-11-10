import type { InvoiceType } from '@/features/orders';
/** Datos fiscales enviados al confirmar la orden; nunca incluye precios del cliente. */
export interface CheckoutInput {
  invoiceType: InvoiceType;
  documentType: 'DNI' | 'RUC';
  documentNumber: string;
  businessName: string | null;
  billingAddress: string | null;
}
