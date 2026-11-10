import { useState, type FormEvent } from 'react';
import { Button, ErrorNotice, Field } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { storeService } from '../services/storeService';
import type { Store } from '../types';
/** onCreated recibe la tienda después de guardarla en el servidor. */
export interface StoreFormProps {
  onCreated: (store: Store) => void;
}
/** Alta de tienda con validación fiscal y de subdominio. */
export function StoreForm({ onCreated }: StoreFormProps) {
  const [name, setName] = useState('');
  const [taxId, setTaxId] = useState('');
  const [legalName, setLegalName] = useState('');
  const [subdomain, setSubdomain] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const action = useAction();
  const errors = {
    name: !name.trim() ? 'Ingresa el nombre de la tienda.' : '',
    taxId: !/^\d{11}$/.test(taxId) ? 'El RUC debe tener exactamente 11 dígitos.' : '',
    legalName: !legalName.trim() ? 'Ingresa la razón social.' : '',
    subdomain: !/^[a-z0-9]([a-z0-9-]*[a-z0-9])?$/.test(subdomain)
      ? 'Usa letras minúsculas, números o guiones, sin guiones en los extremos.'
      : '',
  };
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (Object.values(errors).some(Boolean)) return;
    const result = await action.run(
      () =>
        storeService.create({ name: name.trim(), taxId, legalName: legalName.trim(), subdomain }),
      'Tienda creada.',
    );
    if (result) onCreated(result);
  }
  return (
    <form noValidate onSubmit={(event) => void submit(event)} className="form-stack">
      <ErrorNotice message={action.error} />
      <Field
        label="Nombre de tienda"
        example="La Bodeguita de María"
        required
        maxLength={150}
        value={name}
        onChange={setName}
        error={submitted ? errors.name : ''}
      />
      <Field
        label="RUC"
        example="20123456789"
        required
        rule="digits"
        inputMode="numeric"
        maxLength={11}
        value={taxId}
        onChange={setTaxId}
        error={submitted ? errors.taxId : ''}
      />
      <Field
        label="Razón social"
        example="Comercial María S.A.C."
        required
        maxLength={200}
        value={legalName}
        onChange={setLegalName}
        error={submitted ? errors.legalName : ''}
      />
      <Field
        label="Subdominio"
        example="bodeguita-maria"
        required
        rule="slug"
        maxLength={63}
        value={subdomain}
        onChange={setSubdomain}
        error={submitted ? errors.subdomain : ''}
      />
      <p className="muted">
        Se creará en soles (PEN) con IGV del 18 %. Puedes modificar el IGV después.
      </p>
      <Button type="submit" pending={action.pending}>
        Crear tienda
      </Button>
    </form>
  );
}
