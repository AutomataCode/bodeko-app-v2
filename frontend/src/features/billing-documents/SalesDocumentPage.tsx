import { useCallback } from 'react';
import { Link, useOutletContext, useParams } from 'react-router-dom';
import { ArrowLeft, Printer } from 'lucide-react';
import { Button, ErrorNotice, Loading } from '@/core/components';
import { useResource } from '@/core/hooks/useResource';
import { useStore } from '@/context/StoreContext';
import type { PublicStore } from '@/features/stores';
import { salesDocumentService } from './services/salesDocumentService';
import type { SalesDocument } from './types';

/** Pantalla imprimible para guardar el comprobante como PDF desde el navegador. */
function DocumentContent({ store, load }: { store: PublicStore; load: (orderId: string) => Promise<SalesDocument> }) {
  const { orderId = '' } = useParams();
  const resource = useResource(useCallback(() => load(orderId), [load, orderId]));
  if (resource.loading) return <Loading />;
  if (!resource.data) return <ErrorNotice message={resource.error} retry={resource.reload} />;
  const document = resource.data;
  if (document.status === 'VOIDED') {
    return <section className="card document-pending" role="status">
      <h1>Comprobante anulado</h1>
      <p>La orden fue cancelada y su pago se reembolsó. Este comprobante ya no está vigente.</p>
    </section>;
  }
  if (document.status === 'PENDING_CONFIGURATION') {
    return <section className="card document-pending" role="status">
      <h1>Comprobante pendiente</h1>
      <p>El pago está confirmado, pero falta completar el domicilio fiscal del emisor.</p>
      <p>El administrador de {store.name} puede completar esos datos en Configuración de tienda. El comprobante se generará después de guardar.</p>
    </section>;
  }
  const isInvoice = document.invoiceType === 'INVOICE';
  const money = (value: number) => new Intl.NumberFormat('es-PE', {
    style: 'currency', currency: document.currency,
  }).format(value);
  const date = document.issuedAt
    ? new Intl.DateTimeFormat('es-PE', { dateStyle: 'long', timeStyle: 'short' }).format(new Date(document.issuedAt))
    : '';
  return <>
    <div className="document-toolbar screen-only">
      <Link className="back-link" to=".." relative="path"><ArrowLeft size={16} /> Volver a la orden</Link>
      <Button onClick={() => window.print()}><Printer size={16} /> Imprimir o guardar PDF</Button>
    </div>
    <article className="sales-document" aria-label={isInvoice ? 'Factura de venta' : 'Boleta de venta'}>
      <header className="sales-document-header">
        <div><h1>{document.issuerLegalName}</h1><p>{document.issuerFiscalAddress}</p><p>RUC: {document.issuerTaxId}</p></div>
        <div className="document-number-box">
          <strong>{isInvoice ? 'FACTURA' : 'BOLETA DE VENTA'}</strong>
          <span>{document.displayNumber}</span>
          <small>Orden {document.orderNumber}</small>
        </div>
      </header>
      <hr />
      <section className="document-buyer">
        <h2>Datos del cliente</h2>
        <p><strong>{document.buyerName}</strong></p>
        <p>{document.buyerDocumentType}: {document.buyerDocumentNumber}</p>
        {document.buyerAddress && <p>{document.buyerAddress}</p>}
        <p>Fecha de emisión: {date}</p>
      </section>
      <div className="table-wrap document-lines-wrap"><table className="document-lines">
        <thead><tr><th>Cant.</th><th>Descripción</th><th>Precio unitario</th><th>Importe</th></tr></thead>
        <tbody>{document.items.map((item, index) => <tr key={`${item.sku}-${index}`}>
          <td>{item.quantity}</td>
          <td><strong>{item.productName}</strong><small>{item.sku}{Object.keys(item.options).length ? ` · ${Object.entries(item.options).map(([key, value]) => `${key}: ${value}`).join(', ')}` : ''}</small></td>
          <td>{money(item.unitPrice)}</td><td>{money(item.subtotal)}</td>
        </tr>)}</tbody>
      </table></div>
      <section className="document-totals">
        <div><span>Op. gravadas / subtotal</span><strong>{money(document.subtotal)}</strong></div>
        <div><span>IGV ({(document.taxRate * 100).toFixed(0)} %)</span><strong>{money(document.taxAmount)}</strong></div>
        <div className="document-total"><span>Total</span><strong>{money(document.total)}</strong></div>
      </section>
      <footer className="sales-document-footer"><span>Gracias por tu compra en {store.name}.</span><span>Documento generado por el sistema de ventas de la tienda.</span></footer>
    </article>
  </>;
}

/** Comprobante consultado por el cliente dueño de la orden. */
export function CustomerSalesDocumentPage() {
  const store = useStore();
  return <DocumentContent store={store} load={salesDocumentService.forCustomer} />;
}

/** Comprobante consultado por el administrador desde el panel de su tienda. */
export function StoreSalesDocumentPage() {
  const store = useOutletContext<PublicStore>();
  const load = useCallback((orderId: string) => salesDocumentService.forStore(store.id, orderId), [store.id]);
  return <DocumentContent store={store} load={load} />;
}
