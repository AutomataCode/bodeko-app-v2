import { useEffect, useRef, useState, type FormEvent } from 'react';
import { CreditCard, LockKeyhole } from 'lucide-react';
import { toast } from 'sonner';
import { Button, ErrorNotice, Field } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { useSession } from '@/context/SessionContext';
import type { Order } from '@/features/orders';
import { usePayment } from '../hooks/usePayment';
import { loadCulqi } from '../services/culqiService';
import type { CulqiInstance, Payment } from '../types';
import { UserFacingError } from '@/utils/errors';
import { APP_NAME } from '@/config';

/** Datos de la orden que se pagará y función que recibe el resultado. */
export interface PaymentFormProps {
  storeId: string;
  storeName: string;
  currency: string;
  order: Order;
  onPaid: (payment: Payment) => void;
}
/** Recibe un token de prueba y envía el pago al backend. */
export function PaymentForm({ storeId, storeName, currency, order, onPaid }: PaymentFormProps) {
  const [sourceId, setSourceId] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const payment = usePayment(storeId, order.id);
  const sdkAction = useAction();
  const { user } = useSession();
  const instance = useRef<CulqiInstance | null>(null);
  const mounted = useRef(true);
  const externalCheckout = import.meta.env.VITE_PAYMENT_MODE === 'culqi';
  const validToken = /^[a-zA-Z0-9_-]{1,100}$/.test(sourceId);
  useEffect(() => {
    mounted.current = true;
    return () => {
      mounted.current = false;
      if (instance.current) {
        instance.current.culqi = () => {};
        instance.current.close();
      }
    };
  }, []);
  async function openCheckout() {
    await sdkAction.run(async () => {
      const publicKey = import.meta.env.VITE_CULQI_PUBLIC_KEY || '';
      if (!publicKey.startsWith('pk_test_'))
        throw new UserFacingError(
          'El pago de prueba no está configurado. Contacta al administrador.',
        );
      instance.current?.close();
      setSourceId('');
      const Checkout = await loadCulqi();
      if (!mounted.current) return false;
      const checkout = new Checkout(publicKey, {
        settings: { title: storeName, currency, amount: Math.round(order.total * 100) },
        client: { email: user?.email },
        options: {
          lang: 'es',
          modal: true,
          installments: false,
          paymentMethods: {
            tarjeta: true,
            yape: false,
            billetera: false,
            bancaMovil: false,
            agente: false,
            cuotealo: false,
          },
          paymentMethodsSort: ['tarjeta'],
        },
        appearance: {
          theme: 'default',
          defaultStyle: { buttonBackground: '#008060', buttonTextColor: '#ffffff' },
        },
      });
      checkout.culqi = () => {
        if (checkout.token?.id) {
          setSourceId(checkout.token.id);
          checkout.close();
          toast.success('Medio de pago listo. Confirma el pago para continuar.');
        } else {
          toast.error(
            'No se pudo validar el medio de pago. Revisa los datos e inténtalo nuevamente.',
          );
        }
      };
      instance.current = checkout;
      checkout.open();
      return true;
    });
  }
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (!validToken || order.total <= 0) return;
    const result = await payment.pay(sourceId);
    setSourceId('');
    if (result) {
      if (result.status === 'APPROVED') {
        toast.success('Pago aprobado.');
        onPaid(result);
      } else {
        toast.error(
          'El pago no fue aprobado. Consulta el estado de tu orden antes de volver a intentarlo.',
        );
      }
    }
  }
  return (
    <form noValidate className="card form-stack" onSubmit={(event) => void submit(event)}>
      <h2>
        <CreditCard size={22} /> Realizar pago
      </h2>
      <ErrorNotice message={payment.error || sdkAction.error} />
      {externalCheckout ? (
        <>
          <p>Ingresa tu medio de pago en el formulario seguro de Culqi.</p>
          <Button
            type="button"
            variant="secondary"
            pending={sdkAction.pending}
            disabled={payment.pending || order.total <= 0}
            onClick={() => void openCheckout()}
          >
            Elegir medio de pago
          </Button>
          {sourceId && (
            <p role="status" className="notice success">
              Medio de pago listo para confirmar.
            </p>
          )}
        </>
      ) : (
        <>
          <p className="muted">
            Pago de prueba: utiliza el token de pago proporcionado para la demostración.
          </p>
          <Field
            label="Token de pago de prueba"
            example="tkn_test_ok"
            rule="token"
            maxLength={100}
            value={sourceId}
            onChange={setSourceId}
            required
            error={
              submitted && !validToken ? 'Ingresa un token válido de hasta 100 caracteres.' : ''
            }
          />
        </>
      )}
      {order.total <= 0 && (
        <ErrorNotice message="La orden debe tener un importe mayor que cero para pagar." />
      )}
      <Button
        type="submit"
        pending={payment.pending}
        disabled={order.total <= 0 || (externalCheckout && !sourceId)}
      >
        Confirmar pago
      </Button>
      <p className="trust">
        <LockKeyhole size={16} />
        Los datos de tu tarjeta no se guardan en {APP_NAME}.
      </p>
    </form>
  );
}
