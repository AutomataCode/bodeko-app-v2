import { Badge } from '@/core/components';
import { dateTime, labels, money } from '@/utils/format';
import type { Order } from '../types';
/** Orden guardada y moneda que se usará para mostrar los importes. */
export interface OrderDetailsProps {
  order: Order;
  currency: string;
}
/** Muestra productos, datos fiscales y totales de la orden. */
export function OrderDetails({ order, currency }: OrderDetailsProps) {
  return (
    <>
      <div className="section-heading">
        <div>
          <h2>{order.orderNumber}</h2>
          <p className="muted">{dateTime(order.createdAt)}</p>
        </div>
        <Badge value={order.status} />
      </div>
      <div className="fiscal-details">
        <div>
          <small>Comprobante</small>
          <strong>{labels[order.invoiceType]}</strong>
        </div>
        <div>
          <small>{order.customerDocumentType}</small>
          <strong>{order.customerDocumentNumber}</strong>
        </div>
        {order.customerBusinessName && (
          <div>
            <small>Razón social</small>
            <strong>{order.customerBusinessName}</strong>
          </div>
        )}
        {order.customerBillingAddress && (
          <div>
            <small>Dirección fiscal</small>
            <strong>{order.customerBillingAddress}</strong>
          </div>
        )}
      </div>
      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Producto</th>
              <th>Cantidad</th>
              <th>Precio de lista</th>
              <th>Precio cobrado</th>
              <th>Subtotal</th>
            </tr>
          </thead>
          <tbody>
            {order.items.map((item) => (
              <tr key={item.productId}>
                <td>{item.productName}</td>
                <td>{item.quantity}</td>
                <td>{money(item.listUnitPrice, currency)}</td>
                <td>{money(item.chargedUnitPrice, currency)}</td>
                <td>{money(item.itemSubtotal, currency)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="order-totals">
        <p>
          <span>Subtotal</span>
          <strong>{money(order.subtotal, currency)}</strong>
        </p>
        <p>
          <span>IGV aplicado ({Math.round(order.appliedTaxRate * 100)} %)</span>
          <strong>{money(order.taxAmount, currency)}</strong>
        </p>
        <p className="total">
          <span>Total</span>
          <strong>{money(order.total, currency)}</strong>
        </p>
      </div>
    </>
  );
}
