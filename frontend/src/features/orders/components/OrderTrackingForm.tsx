import { useState, type FormEvent } from 'react';
import { toast } from 'sonner';
import { Button, Field } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { orderService } from '../services/orderService';
import type { Order } from '../types';

/** Formulario administrativo para guardar transportista y referencia del paquete. */
export function OrderTrackingForm({ storeId, order, onSaved }: { storeId: string; order: Order; onSaved: () => void }) {
  const [carrier, setCarrier] = useState(order.trackingCarrier || '');
  const [trackingNumber, setTrackingNumber] = useState(order.trackingNumber || '');
  const [trackingUrl, setTrackingUrl] = useState(order.trackingUrl || '');
  const [estimated, setEstimated] = useState(order.estimatedDeliveryAt?.slice(0, 10) || '');
  const action = useAction();
  async function save(event: FormEvent) {
    event.preventDefault();
    if (!carrier.trim() || !trackingNumber.trim() || (trackingUrl && !/^https?:\/\//i.test(trackingUrl))) { toast.error('Completa transportista, número y revisa la URL.'); return; }
    const result = await action.run(() => orderService.tracking(storeId, order.id, { carrier: carrier.trim(), trackingNumber: trackingNumber.trim(), trackingUrl: trackingUrl.trim(), estimatedDeliveryAt: estimated ? `${estimated}T23:59:59-05:00` : null }), 'Seguimiento guardado para el cliente.');
    if (result) onSaved();
  }
  return <form className="card order-tracking-form" onSubmit={(event) => void save(event)}><h2>Datos de seguimiento del envío</h2>
    <Field label="Transportista" example="Courier local" value={carrier} onChange={setCarrier} maxLength={100} required />
    <Field label="Número de seguimiento" example="ENV-2026-00125" value={trackingNumber} onChange={setTrackingNumber} maxLength={120} required />
    <Field label="Enlace de seguimiento" example="https://courier.pe/seguimiento/ENV-00125" type="url" value={trackingUrl} onChange={setTrackingUrl} maxLength={500} />
    <Field label="Fecha estimada de entrega" example="2026-11-20" type="date" value={estimated} onChange={setEstimated} />
    <Button type="submit" pending={action.pending}>Guardar seguimiento</Button>
  </form>;
}
