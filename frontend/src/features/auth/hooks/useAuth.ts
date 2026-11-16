import { useAction } from '@/core/hooks/useAction';
import { useSession } from '@/context/SessionContext';
import type { Id } from '@/core/types';
import { authService } from '../services/authService';
import type { Credentials, Registration } from '../types';
/** Inicia sesión o registra la cuenta. Con storeId, la cuenta es de cliente en esa tienda. */
export function useAuth(storeId?: Id) {
  const action = useAction();
  const { establish } = useSession();
  return {
    ...action,
    login: (body: Credentials) =>
      action.run(async () => {
        const result = storeId
          ? await authService.storeLogin(storeId, body)
          : await authService.login(body);
        return establish(result.token);
      }, 'Sesión iniciada.'),
    register: (body: Registration) =>
      action.run(async () => {
        const result = storeId
          ? await authService.storeRegister(storeId, body)
          : await authService.registerOwner(body);
        return establish(result.token);
      }, 'Tu cuenta fue creada correctamente.'),
  };
}
