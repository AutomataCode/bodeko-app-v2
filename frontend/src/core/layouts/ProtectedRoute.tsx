import { Navigate, Outlet, useLocation, useParams } from 'react-router-dom';
import { useSession } from '@/context/SessionContext';
import type { Role } from '@/core/types';
import { ErrorNotice, Loading } from '@/core/components';
/** Protege la página según la sesión y el rol. El backend también valida los permisos. */
export function ProtectedRoute({ role }: { role: Role }) {
  const session = useSession();
  const location = useLocation();
  // Dentro de una tienda se inicia sesión con la cuenta de cliente de esa tienda.
  const { subdomain } = useParams();
  const loginPath = subdomain ? `/tienda/${subdomain}/ingresar` : '/ingresar';
  if (session.loading) return <Loading />;
  if (session.error && !session.user)
    return <ErrorNotice message={session.error} retry={session.retry} />;
  if (!session.user)
    return (
      <Navigate
        to={`${loginPath}?next=${encodeURIComponent(location.pathname + location.search)}`}
        replace
      />
    );
  if (session.user.role !== role)
    return <ErrorNotice message="Tu cuenta no tiene permisos para acceder a esta sección." />;
  return <Outlet />;
}
