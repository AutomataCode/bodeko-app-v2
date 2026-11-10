/** El backend envía el correo al aprobar el pago; no se solicita desde el navegador. */
export const notificationPolicy = {
  deliveryOwnedBy: 'backend',
  automaticOnApprovedPayment: true,
} as const;
