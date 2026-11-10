import { Link } from 'react-router-dom';
import { ArrowRight, CreditCard, ReceiptText, Store, X } from 'lucide-react';
import { Badge, Button } from '@/core/components';
import { dateTime, labels, money } from '@/utils/format';
import type { CustomerOrderSummary } from '../types';

/** Compra del cliente y función para abrir la confirmación de cancelación. */
export interface CustomerOrderCardProps {
  order: CustomerOrderSummary;
  onCancel: () => void;
}

/** Tarjeta del historial con acciones disponibles según el estado actual de la compra. */
export function CustomerOrderCard({ order, onCancel }: CustomerOrderCardProps) {
  const canCancel = order.status === 'PENDING' || order.status === 'PAID';
  return (
    <article className="customer-order-card">
      <header className="customer-order-card-header">
        <div>
          <span className="order-label">NÚMERO DE ORDEN</span>
          <Link className="order-number" to={`/cuenta/ordenes/${order.id}`}>
            {order.orderNumber}
          </Link>
          <time dateTime={order.createdAt}>{dateTime(order.createdAt)}</time>
        </div>
        <Badge value={order.status} />
      </header>
      <div className="customer-order-card-body">
        <div className="order-store">
          <span className="order-store-icon">
            <Store size={23} />
          </span>
          <div>
            <Link to={`/tienda/${order.storeSubdomain}`}>{order.storeName}</Link>
            <p>
              <ReceiptText size={14} />
              {labels[order.invoiceType]}
            </p>
          </div>
        </div>
        <div className="customer-order-amount">
          <small>Total de tu compra</small>
          <strong>{money(order.total, order.currency)}</strong>
        </div>
      </div>
      <footer className="customer-order-card-footer">
        <Link className="order-detail-link" to={`/cuenta/ordenes/${order.id}`}>
          Ver detalle de la orden <ArrowRight size={15} />
        </Link>
        <div className="actions">
          {canCancel && (
            <Button className="cancel-order-button" variant="secondary" onClick={onCancel}>
              <X size={15} />
              Cancelar orden
            </Button>
          )}
          {order.status === 'PENDING' && (
            <Link
              className="button primary"
              to={`/tienda/${order.storeSubdomain}/pago/${order.id}`}
            >
              <CreditCard size={15} />
              Pagar ahora
            </Link>
          )}
        </div>
      </footer>
    </article>
  );
}
