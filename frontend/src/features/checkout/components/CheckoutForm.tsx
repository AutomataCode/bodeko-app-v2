import { useState, type FormEvent } from 'react';
import { Button, ErrorNotice, Field, Select } from '@/core/components';
import { isDocument } from '@/utils/validation';
import type { Order, InvoiceType } from '@/features/orders';
import { useCheckout } from '../hooks/useCheckout';
/** onCreated recibe la orden creada para continuar con el pago. */
export interface CheckoutFormProps {
  storeId: string;
  onCreated: (order: Order) => void;
}
/** Datos fiscales con DNI para boleta y RUC/razón social para factura. */
export function CheckoutForm({ storeId, onCreated }: CheckoutFormProps) {
  const [invoiceType, setInvoiceType] = useState<InvoiceType>('RECEIPT');
  const [document, setDocument] = useState('');
  const [businessName, setBusinessName] = useState('');
  const [address, setAddress] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const checkout = useCheckout(storeId);
  const invoice = invoiceType === 'INVOICE';
  const documentError = !isDocument(document, invoice ? 'RUC' : 'DNI')
    ? `Ingresa ${invoice ? 'un RUC de 11' : 'un DNI de 8'} dígitos.`
    : '';
  const businessError =
    invoice && !businessName.trim() ? 'Ingresa la razón social de la empresa.' : '';
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (documentError || businessError) return;
    const result = await checkout.create({
      invoiceType,
      documentType: invoice ? 'RUC' : 'DNI',
      documentNumber: document,
      businessName: invoice ? businessName.trim() : null,
      billingAddress: address.trim() || null,
    });
    if (result) onCreated(result);
  }
  return (
    <form noValidate className="card form-stack" onSubmit={(event) => void submit(event)}>
      <h2>Datos del comprobante</h2>
      <ErrorNotice message={checkout.error} />
      <Select
        label="Tipo de comprobante"
        value={invoiceType}
        onChange={(value) => {
          setInvoiceType(value as InvoiceType);
          setDocument('');
        }}
        options={[
          { value: 'RECEIPT', label: 'Boleta de venta' },
          { value: 'INVOICE', label: 'Factura' },
        ]}
        hint="Ejemplo: Factura si compras a nombre de una empresa."
      />
      <Field
        label={invoice ? 'RUC' : 'DNI'}
        example={invoice ? '20123456789' : '12345678'}
        rule="digits"
        inputMode="numeric"
        maxLength={invoice ? 11 : 8}
        value={document}
        onChange={setDocument}
        required
        error={submitted ? documentError : ''}
      />
      {invoice && (
        <Field
          label="Razón social"
          example="Comercial María S.A.C."
          maxLength={200}
          value={businessName}
          onChange={setBusinessName}
          required
          error={submitted ? businessError : ''}
        />
      )}
      <Field
        label="Dirección fiscal (opcional)"
        example="Av. Arequipa 123, Lima"
        maxLength={300}
        value={address}
        onChange={setAddress}
      />
      <p className="muted">
        Al confirmar se crea tu orden y se reserva el stock. Los precios y el IGV quedarán fijados
        en el comprobante.
      </p>
      <Button type="submit" pending={checkout.pending}>
        Confirmar orden y continuar
      </Button>
    </form>
  );
}
