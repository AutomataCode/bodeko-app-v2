/** Moneda del comercio, con formato peruano y dos decimales. */
export const money = (value: number, currency = 'PEN'): string =>
  new Intl.NumberFormat('es-PE', { style: 'currency', currency }).format(value);
/** Fecha de la API mostrada en la zona horaria del usuario. */
export const dateTime = (value: string): string =>
  new Intl.DateTimeFormat('es-PE', { dateStyle: 'medium', timeStyle: 'short' }).format(
    new Date(value),
  );
/** Traduce estados internos sin alterar sus valores de transporte. */
export const labels: Record<string, string> = {
  ACTIVE: 'Activo',
  INACTIVE: 'Inactivo',
  DRAFT: 'Borrador',
  ARCHIVED: 'Archivado',
  PENDING: 'Pendiente',
  PAID: 'Pagada',
  SHIPPED: 'Enviada',
  DELIVERED: 'Entregada',
  CANCELLED: 'Cancelada',
  APPROVED: 'Aprobado',
  FAILED: 'Rechazado',
  REFUNDED: 'Reembolsado',
  RECEIPT: 'Boleta',
  INVOICE: 'Factura',
};
