import { useCallback, useEffect, useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { toast } from 'sonner';
import { Button, ErrorNotice, Field, PageHeader } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { storeService } from './services/storeService';
import { salesDocumentService } from '@/features/billing-documents/services/salesDocumentService';
import { useResource } from '@/core/hooks/useResource';
import type { Store } from './types';
/** Configuración fiscal: muestra porcentaje entero y envía la fracción esperada por Spring. */
export function StoreSettingsPage() {
  const store = useOutletContext<Store>();
  const [percentage, setPercentage] = useState(String(Math.round(store.taxRate * 100)));
  const [savedRate, setSavedRate] = useState(store.taxRate);
  const action = useAction();
  const profile = useResource(useCallback(() => salesDocumentService.billingProfile(store.id), [store.id]));
  const [issuerTaxId, setIssuerTaxId] = useState(store.taxId);
  const [issuerLegalName, setIssuerLegalName] = useState(store.legalName);
  const [issuerAddress, setIssuerAddress] = useState('');
  const saveProfile = useAction();
  useEffect(() => {
    if (!profile.data) return;
    setIssuerTaxId(profile.data.taxId);
    setIssuerLegalName(profile.data.legalName);
    setIssuerAddress(profile.data.fiscalAddress);
  }, [profile.data]);
  const valid = /^\d{1,3}$/.test(percentage) && Number(percentage) <= 100;
  return (
    <>
      <PageHeader
        title="Configuración de tienda"
        description="Información fiscal y preferencias de tu negocio."
      />
      <ErrorNotice message={profile.error} retry={profile.reload} />
      <div className="two-columns">
        <section className="card form-stack">
          <h2>Datos de la tienda</h2>
          <dl className="data-list">
            <dt>Nombre comercial</dt>
            <dd>{store.name}</dd>
            <dt>RUC</dt>
            <dd>{store.taxId}</dd>
            <dt>Razón social</dt>
            <dd>{store.legalName}</dd>
            <dt>Subdominio</dt>
            <dd>{store.subdomain}</dd>
            <dt>Moneda</dt>
            <dd>{store.currency}</dd>
          </dl>
          <Button
            variant="secondary"
            onClick={() => {
              void navigator.clipboard
                .writeText(`${window.location.origin}/tienda/${store.subdomain}`)
                .then(() => toast.success('Enlace de tienda copiado.'))
                .catch(() =>
                  toast.error(
                    'No se pudo copiar el enlace. Cópialo desde la barra de direcciones de tu tienda.',
                  ),
                );
            }}
          >
            Copiar enlace de tienda
          </Button>
        </section>
        <form
          className="card form-stack"
          onSubmit={(event) => {
            event.preventDefault();
            if (valid)
              void action
                .run(() => storeService.tax(store.id, Number(percentage) / 100), 'IGV actualizado.')
                .then((result) => {
                  if (result) setSavedRate(result.taxRate);
                });
          }}
        >
          <h2>Impuesto general a las ventas</h2>
          <p className="muted">
            IGV actual: {Math.round(savedRate * 100)} %. El cambio se aplicará a las nuevas órdenes.
          </p>
          <ErrorNotice message={action.error} />
          <Field
            label="IGV (%)"
            example="18"
            rule="digits"
            inputMode="numeric"
            maxLength={3}
            value={percentage}
            onChange={setPercentage}
            required
            error={!valid ? 'Ingresa un porcentaje entero entre 0 y 100.' : ''}
          />
          <Button type="submit" disabled={!valid} pending={action.pending}>
            Guardar IGV
          </Button>
        </form>
      </div>
      <form className="card form-stack billing-profile-form" noValidate onSubmit={(event) => {
        event.preventDefault();
        if (!/^\d{11}$/.test(issuerTaxId) || !issuerLegalName.trim() || !issuerAddress.trim()) return;
        void saveProfile.run(() => salesDocumentService.updateBillingProfile(store.id, {
          taxId: issuerTaxId, legalName: issuerLegalName.trim(), fiscalAddress: issuerAddress.trim(),
        }), 'Perfil fiscal guardado; se procesaron los comprobantes pendientes.').then((result) => {
          if (result) profile.reload();
        });
      }}>
        <h2>Emisor de boletas y facturas</h2>
        <p className="muted">La numeración es simulada para el proyecto y no se envía a SUNAT. Si varias tiendas usan el mismo RUC, comparten este perfil fiscal.</p>
        {(profile.data?.linkedStores ?? 1) > 1 && <p className="billing-shared-note">Este perfil se comparte con {profile.data?.linkedStores} tiendas. Los cambios se aplicarán a todas.</p>}
        <ErrorNotice message={saveProfile.error} />
        <div className="billing-profile-grid">
          <Field label="RUC del emisor" example="20123456789" rule="digits" inputMode="numeric" maxLength={11} value={issuerTaxId} onChange={setIssuerTaxId} required error={issuerTaxId && !/^\d{11}$/.test(issuerTaxId) ? 'Ingresa exactamente 11 dígitos.' : ''} />
          <Field label="Razón social" example="Comercial Bodeko S.A.C." maxLength={200} value={issuerLegalName} onChange={setIssuerLegalName} required />
          <Field label="Domicilio fiscal" example="Av. Ejemplo 123, Lima" maxLength={300} value={issuerAddress} onChange={setIssuerAddress} required />
        </div>
        <Button type="submit" pending={saveProfile.pending} disabled={!/^\d{11}$/.test(issuerTaxId) || !issuerLegalName.trim() || !issuerAddress.trim()}>
          Guardar datos del emisor
        </Button>
      </form>
    </>
  );
}
