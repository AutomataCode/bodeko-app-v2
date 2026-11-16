import { useCallback } from 'react';
import { BadgeCheck, Mail, Phone, UserRound } from 'lucide-react';
import { ErrorNotice, Loading } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { useResource } from '@/core/hooks/useResource';
import { customerPreferenceService } from './services/customerPreferenceService';
import { useSession } from '@/context/SessionContext';
import { useStore } from '@/context/StoreContext';

/** Muestra los datos guardados de la cuenta, sin permitir editarlos. */
export function AccountProfilePage() {
  const { user } = useSession();
  const store = useStore();
  const preference = useResource(useCallback(() => customerPreferenceService.marketing(), []));
  const action = useAction();
  async function updateMarketing(enabled: boolean) {
    const result = await action.run(() => customerPreferenceService.updateMarketing(enabled), enabled ? 'Preferencia guardada.' : 'Dejaste de recibir recordatorios promocionales.');
    if (result) preference.reload();
  }
  return (
    <>
      <header className="account-page-heading">
        <div>
          <span className="eyebrow">MI CUENTA</span>
          <h1>Mis datos personales</h1>
          <p>Información asociada a tu cuenta en {store.name}.</p>
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
            <dd>Cliente de {store.name}</dd>
          </div>
        </dl>
        <section className="marketing-preference">
          <h3>Preferencias de correo</h3>
          <ErrorNotice message={preference.error} retry={preference.reload} />
          {preference.loading ? <Loading /> : preference.data && <label><input type="checkbox" checked={preference.data.enabled} disabled={action.pending} onChange={(event) => void updateMarketing(event.target.checked)} /> Acepto recibir novedades y recordatorios de mi carrito por correo. Puedes cambiar esta preferencia cuando quieras.</label>}
        </section>
        <p className="muted">
          Estos datos corresponden a tu registro. Puedes consultar y gestionar tus compras desde
          “Mis órdenes”.
        </p>
      </section>
    </>
  );
}
