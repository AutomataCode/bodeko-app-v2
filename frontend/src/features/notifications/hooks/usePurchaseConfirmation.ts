import { useSession } from '@/context/SessionContext';
/** Obtiene el nombre del cliente para confirmar su compra. */
export function usePurchaseConfirmation() {
  const { user } = useSession();
  return { customerName: user?.fullName || 'Cliente' };
}
