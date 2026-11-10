import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useSession } from '@/context/SessionContext';
import type { Role } from '@/core/types';
import { ErrorNotice, Loading } from '@/core/components';
/** Protege la página según la sesión y el rol. El backend también valida los permisos. */
export function ProtectedRoute({ role }: { role: Role }) {
  const session = useSession();
  const location = useLocation();
  if (session.loading) return <Loading />;
  if (session.error && !session.user)
    return <ErrorNotice message={session.error} retry={session.retry} />;
  if (!session.user)
    return (
      <Navigate
        to={`/ingresar?next=${encodeURIComponent(location.pathname + location.search)}`}
        replace
      />
    );
  if (session.user.role !== role)
    return <ErrorNotice message="Tu cuenta no tiene permisos para acceder a esta sección." />;
  return <Outlet />;
}
