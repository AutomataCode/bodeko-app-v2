import { useState } from 'react';
import { Link, useOutletContext } from 'react-router-dom';
import {
  Badge,
  Empty,
  ErrorNotice,
  Loading,
  PageHeader,
  Pagination,
  Select,
} from '@/core/components';
import type { Store } from '@/features/stores';
import { dateTime, labels, money } from '@/utils/format';
import { useOrders } from './hooks/useOrders';
/** Listado de ventas con paginación y filtro por estados reales de la API. */
export function OrdersPage() {
  const store = useOutletContext<Store>();
  const [status, setStatus] = useState('');
  const [page, setPage] = useState(0);
  const resource = useOrders(store.id, status, page);
  return (
    <>
      <PageHeader
        title="Órdenes"
        description="Acompaña cada venta desde su recepción hasta la entrega."
      />
      <div className="card filters">
        <Select
          label="Estado de la orden"
          value={status}
          onChange={(value) => {
            setStatus(value);
            setPage(0);
          }}
          options={[
            { value: '', label: 'Todos los estados' },
            ...['PENDING', 'PAID', 'SHIPPED', 'DELIVERED', 'CANCELLED'].map((value) => ({
              value,
              label: labels[value] || value,
            })),
          ]}
        />
      </div>
      <ErrorNotice message={resource.error} retry={resource.reload} />
      {resource.loading ? (
        <Loading />
      ) : !resource.data?.content.length ? (
        <Empty title="No hay órdenes con este estado" />
      ) : (
        <>
          <div className="card table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Orden</th>
                  <th>Fecha</th>
                  <th>Documento</th>
                  <th>Estado</th>
                  <th>Total</th>
                </tr>
              </thead>
              <tbody>
                {resource.data.content.map((order) => (
                  <tr key={order.id}>
                    <td>
                      <Link to={`/admin/${store.id}/ordenes/${order.id}`}>{order.orderNumber}</Link>
                    </td>
                    <td>{dateTime(order.createdAt)}</td>
                    <td>{order.customerDocumentNumber}</td>
                    <td>
                      <Badge value={order.status} />
                    </td>
                    <td>{money(order.total, store.currency)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <Pagination page={page} totalPages={resource.data.totalPages} onChange={setPage} />
        </>
      )}
    </>
  );
}
