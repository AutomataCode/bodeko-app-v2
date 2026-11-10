import { useState, type FormEvent } from 'react';
import { Button, ErrorNotice, Field } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { isMoney } from '@/utils/validation';
import { offerService } from '../services/offerService';
/** Producto y tienda a los que pertenece la oferta. */
export interface OfferFormProps {
  storeId: string;
  productId: string;
  onSaved: () => void;
}
/** Convierte fechas locales a ISO con zona para evitar promociones desplazadas. */
export function OfferForm({ storeId, productId, onSaved }: OfferFormProps) {
  const [price, setPrice] = useState('');
  const [start, setStart] = useState('');
  const [end, setEnd] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const action = useAction();
  const validStart = Number.isFinite(Date.parse(start));
  const validEnd = Number.isFinite(Date.parse(end)) && Date.parse(end) > Date.parse(start);
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (!isMoney(price) || !validStart || !validEnd) return;
    const result = await action.run(
      () =>
        offerService.create(storeId, productId, {
          offerPrice: Number(price),
          startsAt: new Date(start).toISOString(),
          endsAt: new Date(end).toISOString(),
        }),
      'Oferta creada.',
    );
    if (result) {
      setPrice('');
      setStart('');
      setEnd('');
      setSubmitted(false);
      onSaved();
    }
  }
  return (
    <form noValidate className="card form-stack" onSubmit={(event) => void submit(event)}>
      <h2>Nueva oferta</h2>
      <ErrorNotice message={action.error} />
      <Field
        label="Precio de oferta (S/)"
        example="19.90"
        rule="money"
        inputMode="decimal"
        required
        value={price}
        onChange={setPrice}
        error={submitted && !isMoney(price) ? 'Ingresa un precio válido no negativo.' : ''}
      />
      <Field
        label="Fecha y hora de inicio"
        example="20/11/2026, 09:00"
        type="datetime-local"
        required
        value={start}
        onChange={setStart}
        error={submitted && !validStart ? 'Selecciona una fecha de inicio.' : ''}
      />
      <Field
        label="Fecha y hora de fin"
        example="30/11/2026, 18:00"
        type="datetime-local"
        required
        value={end}
        onChange={setEnd}
        error={submitted && !validEnd ? 'La fecha final debe ser posterior al inicio.' : ''}
      />
      <Button type="submit" pending={action.pending}>
        Crear oferta
      </Button>
    </form>
  );
}
