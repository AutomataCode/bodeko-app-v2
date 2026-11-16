import type { InvoiceType } from '@/features/orders';

/** Datos de una boleta o factura generada en la simulación del proyecto. */
export interface SalesDocument {
  id: string;
  orderId: string;
  orderNumber: string;
  invoiceType: InvoiceType;
  status: 'ISSUED' | 'PENDING_CONFIGURATION' | 'VOIDED';
  series: string | null;
  documentNumber: number | null;
  displayNumber: string | null;
  issuerTaxId: string | null;
  issuerLegalName: string | null;
  issuerFiscalAddress: string | null;
  buyerName: string;
  buyerDocumentType: 'DNI' | 'RUC';
  buyerDocumentNumber: string;
  buyerAddress: string | null;
  currency: string;
  subtotal: number;
  taxAmount: number;
  taxRate: number;
  total: number;
  issuedAt: string | null;
  items: SalesDocumentLine[];
}

/** Producto y precio congelados al confirmar la orden. */
export interface SalesDocumentLine {
  productName: string;
  sku: string;
  options: Record<string, string>;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

/** Datos fiscales que pueden compartir varias tiendas del mismo titular. */
export interface BillingProfile {
  issuerId: string | null;
  taxId: string;
  legalName: string;
  fiscalAddress: string;
  linkedStores: number;
}

/** Cambios admitidos en el emisor fiscal compartido. */
export interface BillingProfileInput {
  taxId: string;
  legalName: string;
  fiscalAddress: string;
}
