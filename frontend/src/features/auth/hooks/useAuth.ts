import { useAction } from '@/core/hooks/useAction';
import { useSession } from '@/context/SessionContext';
import { authService } from '../services/authService';
import type { Credentials, Registration } from '../types';
/** Inicia sesión o registra al usuario y carga sus datos. */
export function useAuth() {
  const action = useAction();
  const { establish } = useSession();
  return {
    ...action,
    login: (body: Credentials) =>
      action.run(async () => establish((await authService.login(body)).token), 'Sesión iniciada.'),
    register: (body: Registration, owner: boolean) =>
      action.run(
        async () => establish((await authService.register(body, owner)).token),
        'Tu cuenta fue creada correctamente.',
      ),
  };
}
