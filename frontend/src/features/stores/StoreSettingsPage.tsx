import { useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { toast } from 'sonner';
import { Button, ErrorNotice, Field, PageHeader } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { storeService } from './services/storeService';
import type { Store } from './types';
/** Configuración fiscal: muestra porcentaje entero y envía la fracción esperada por Spring. */
export function StoreSettingsPage() {
  const store = useOutletContext<Store>();
  const [percentage, setPercentage] = useState(String(Math.round(store.taxRate * 100)));
  const [savedRate, setSavedRate] = useState(store.taxRate);
  const action = useAction();
  const valid = /^\d{1,3}$/.test(percentage) && Number(percentage) <= 100;
  return (
    <>
      <PageHeader
        title="Configuración de tienda"
        description="Información fiscal y preferencias de tu negocio."
      />
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
    </>
  );
}
