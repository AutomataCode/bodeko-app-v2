import type { OrderStatus } from '@/features/orders';
/** Resultado real del endpoint de pagos; no se determina éxito en el navegador. */
export interface Payment {
  id: string;
  orderId: string;
  orderNumber: string;
  provider: string;
  transactionId: string;
  amount: number;
  status: 'PENDING' | 'APPROVED' | 'FAILED' | 'REFUNDED';
  orderStatus: OrderStatus;
  message: string | null;
}
/** Interfaz mínima del SDK externo usada para tokenización; nunca expone tarjeta o CVV. */
export interface CulqiInstance {
  token?: { id: string };
  error?: unknown;
  culqi: () => void;
  open: () => void;
  close: () => void;
}
/** Constructor público del Checkout Custom documentado por Culqi. */
export type CulqiConstructor = new (
  publicKey: string,
  config: Record<string, unknown>,
) => CulqiInstance;
