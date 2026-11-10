import { BadgeCheck, Mail, Phone, UserRound } from 'lucide-react';
import { useSession } from '@/context/SessionContext';

/** Muestra los datos guardados de la cuenta, sin permitir editarlos. */
export function AccountProfilePage() {
  const { user } = useSession();
  return (
    <>
      <header className="account-page-heading">
        <div>
          <span className="eyebrow">MI CUENTA</span>
          <h1>Mis datos personales</h1>
          <p>Información asociada a tu cuenta de Bodeko.</p>
        </div>
      </header>
      <section className="account-section-card account-profile">
        <h2>Datos de tu cuenta</h2>
        <dl>
          <div>
            <dt>
              <UserRound size={16} />
              Nombre completo
            </dt>
            <dd>{user?.fullName}</dd>
          </div>
          <div>
            <dt>
              <Mail size={16} />
              Correo electrónico
            </dt>
            <dd>{user?.email}</dd>
          </div>
          <div>
            <dt>
              <Phone size={16} />
              Teléfono / celular
            </dt>
            <dd>{user?.phoneNumber || 'No registrado'}</dd>
          </div>
          <div>
            <dt>
              <BadgeCheck size={16} />
              Tipo de cuenta
            </dt>
            <dd>Cliente</dd>
          </div>
        </dl>
        <p className="muted">
          Estos datos corresponden a tu registro. Puedes consultar y gestionar tus compras desde
          “Mis órdenes”.
        </p>
      </section>
    </>
  );
}
