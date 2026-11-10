import { createContext, useCallback, useContext, useEffect, useState, type ReactNode } from 'react';
import { SESSION_KEY } from '@/config';
import { authService } from '@/features/auth/services/authService';
import type { User } from '@/core/types';
import { errorMessage } from '@/utils/errors';

/** Consulta /auth/me para comprobar la sesión y el rol del usuario. */
interface SessionState {
  user: User | null;
  loading: boolean;
  error: string;
  establish: (token: string) => Promise<User>;
  logout: () => void;
  retry: () => void;
}
const SessionContext = createContext<SessionState | null>(null);
/** Recupera la sesión al cargar la página y la limpia cuando vence. */
export function SessionProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [revision, setRevision] = useState(0);
  const logout = useCallback(() => {
    sessionStorage.removeItem(SESSION_KEY);
    setUser(null);
    setError('');
  }, []);
  useEffect(() => {
    let active = true;
    const expire = () => {
      logout();
    };
    window.addEventListener('bodeko:unauthorized', expire);
    if (!sessionStorage.getItem(SESSION_KEY)) {
      setLoading(false);
      return () => window.removeEventListener('bodeko:unauthorized', expire);
    }
    setLoading(true);
    setError('');
    void authService
      .me()
      .then((result) => {
        if (active) setUser(result);
      })
      .catch((cause: unknown) => {
        if (active && sessionStorage.getItem(SESSION_KEY)) setError(errorMessage(cause));
      })
      .finally(() => {
        if (active) setLoading(false);
      });
    return () => {
      active = false;
      window.removeEventListener('bodeko:unauthorized', expire);
    };
  }, [logout, revision]);
  async function establish(token: string) {
    sessionStorage.setItem(SESSION_KEY, token);
    try {
      const currentUser = await authService.me();
      setUser(currentUser);
      setError('');
      return currentUser;
    } catch (cause) {
      logout();
      throw cause;
    }
  }
  return (
    <SessionContext.Provider
      value={{
        user,
        loading,
        error,
        establish,
        logout,
        retry: () => setRevision((value) => value + 1),
      }}
    >
      {children}
    </SessionContext.Provider>
  );
}
/** Permite consultar la sesión desde los componentes y las rutas protegidas. */
export function useSession(): SessionState {
  const value = useContext(SessionContext);
  if (!value) throw new Error('No se encontró el proveedor de sesión.');
  return value;
}
