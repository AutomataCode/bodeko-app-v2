import axios from 'axios';

/** Error local deliberadamente redactado para el usuario, siempre en español. */
export class UserFacingError extends Error {}

/** Traducciones estables por código; no se muestran mensajes técnicos del servidor. */
const messages: Record<string, string> = {
  BAD_CREDENTIALS: 'El correo o la contraseña son incorrectos.',
  USER_EMAIL_EXIST: 'Este correo ya está registrado.',
  UNAUTHORIZED: 'Inicia sesión para continuar.',
  AUTHENTICATION_REQUIRED: 'Inicia sesión para continuar.',
  ACCOUNT_DISABLED: 'Tu cuenta está deshabilitada. Contacta al administrador.',
  ACCESS_DENIED: 'No tienes permiso para realizar esta acción.',
  CATEGORY_NAME_ALREADY_IN_USE: 'Ya existe una categoría con ese nombre.',
  CATEGORY_HAS_PRODUCTS: 'Reasigna los productos antes de eliminar esta categoría.',
  STORE_TAX_ID_ALREADY_IN_USE: 'Este RUC ya está registrado.',
  STORE_SUBDOMAIN_ALREADY_IN_USE: 'Ese subdominio ya está en uso.',
  INVENTORY_INSUFFICIENT_STOCK: 'No hay suficiente stock para completar la compra.',
  ORDER_EMPTY_CART: 'Agrega productos al carrito antes de continuar.',
  ORDER_INVALID_STATUS_TRANSITION: 'Ese cambio de estado no está permitido.',
  ORDER_INVOICE_REQUIRES_RUC: 'Para una factura necesitas un RUC de 11 dígitos.',
  ORDER_INVOICE_REQUIRES_BUSINESS_NAME: 'Ingresa la razón social para emitir la factura.',
  ORDER_RECEIPT_REQUIRES_DNI: 'Para una boleta necesitas un DNI de 8 dígitos.',
  ORDER_INVALID_DOCUMENT_NUMBER: 'Revisa el número de documento.',
  ORDER_PRODUCT_NOT_AVAILABLE: 'Un producto ya no está disponible. Revisa tu carrito.',
  CART_PRODUCT_NOT_AVAILABLE: 'El producto no está disponible.',
  OFFER_INVALID_DATE_RANGE: 'La fecha final debe ser posterior a la fecha inicial.',
  OFFER_DATE_RANGE_OVERLAP: 'La oferta se superpone con otra oferta existente.',
  PAYMENT_ORDER_NOT_PENDING: 'Esta orden ya no está pendiente de pago.',
  PAYMENT_INVALID_AMOUNT: 'El total a pagar debe ser mayor que cero.',
  PAYMENT_DECLINED: 'El pago fue rechazado. Revisa tu medio de pago o intenta con otro.',
  PAYMENT_REFUND_REJECTED: 'No se pudo procesar la devolución. Contacta al administrador.',
  PAYMENT_GATEWAY_INVALID_REQUEST:
    'La pasarela no aceptó los datos del pago. Genera un nuevo token e inténtalo nuevamente.',
  PAYMENT_GATEWAY_UNAVAILABLE: 'La pasarela de pagos no está disponible. Inténtalo más tarde.',
  VALIDATION_ERROR: 'Revisa los campos del formulario.',
  INVALID_REQUEST: 'Los datos enviados no son válidos.',
  INVALID_PARAMETER_TYPE: 'Revisa los filtros o identificadores enviados.',
  DATA_INTEGRITY_VIOLATION: 'No se pudo guardar: hay datos duplicados o registros relacionados.',
  INTERNAL_SERVER_ERROR: 'Ocurrió un error en el servidor. Inténtalo más tarde.',
};
/** Normaliza errores desconocidos y de Axios a un mensaje seguro en español. */
export function errorMessage(error: unknown): string {
  if (axios.isAxiosError<{ code?: string }>(error)) {
    if (!error.response)
      return 'No se pudo conectar con el servidor. Revisa tu conexión y que el backend esté disponible.';
    const code = error.response.data?.code;
    if (code && messages[code]) return messages[code];
    if (error.response.status === 401)
      return 'Tu sesión no es válida o expiró. Vuelve a iniciar sesión.';
    if (error.response.status === 403) return 'No tienes permiso para realizar esta acción.';
    if (error.response.status === 404) return 'No se encontró el recurso solicitado.';
    if (error.response.status === 409)
      return 'Los datos entran en conflicto con un registro existente. Actualiza la página e inténtalo de nuevo.';
    return 'No se pudo completar la solicitud. Inténtalo de nuevo.';
  }
  return error instanceof UserFacingError
    ? error.message
    : 'Ocurrió un error inesperado. Inténtalo nuevamente.';
}
