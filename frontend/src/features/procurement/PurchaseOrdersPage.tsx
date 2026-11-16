import { useCallback, useMemo, useState, type FormEvent } from 'react';
import { useOutletContext } from 'react-router-dom';
import { ClipboardList, Plus, Truck } from 'lucide-react';
import { toast } from 'sonner';
import { Button, Empty, ErrorNotice, Field, Loading, PageHeader, Select } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { useResource } from '@/core/hooks/useResource';
import { useProducts } from '@/features/products';
import { productVariantService } from '@/features/products/services/productVariantService';
import type { ProductVariant } from '@/features/products/types';
import type { Store } from '@/features/stores';
import { money } from '@/utils/format';
import { procurementService } from './services/procurementService';
import type { PurchaseOrder, PurchaseOrderItem, Supplier } from './types';

interface DraftLine { variantId: string; productName: string; sku: string; options: string; quantity: number; unitCost: number; }
const STATUS_LABELS = { ORDERED: 'Solicitada', PARTIALLY_RECEIVED: 'Recepción parcial', RECEIVED: 'Recibida', CANCELLED: 'Cancelada' };

/** Permite crear proveedores y órdenes de compra y registrar entregas parciales. */
export function PurchaseOrdersPage() {
  const store = useOutletContext<Store>();
  const products = useProducts(store.id);
  const suppliers = useResource(useCallback(() => procurementService.suppliers(store.id), [store.id]));
  const orders = useResource(useCallback(() => procurementService.orders(store.id), [store.id]));
  const supplierAction = useAction();
  const orderAction = useAction();
  const receiveAction = useAction();
  const [supplierName, setSupplierName] = useState('');
  const [supplierEmail, setSupplierEmail] = useState('');
  const [supplierPhone, setSupplierPhone] = useState('');
  const [supplierNotes, setSupplierNotes] = useState('');
  const [supplierId, setSupplierId] = useState('');
  const [productId, setProductId] = useState('');
  const [variantId, setVariantId] = useState('');
  const [quantity, setQuantity] = useState('1');
  const [unitCost, setUnitCost] = useState('0.00');
  const [notes, setNotes] = useState('');
  const [lines, setLines] = useState<DraftLine[]>([]);
  const [receivingId, setReceivingId] = useState('');
  const [receiptCounts, setReceiptCounts] = useState<Record<string, { accepted: string; rejected: string }>>({});
  const [receiptReason, setReceiptReason] = useState('');
  const variants = useResource(useCallback(() => productVariantService.list(store.id, productId), [store.id, productId]), !!productId);
  const sellableVariants = useMemo(() => (variants.data || []).filter((item) => item.active), [variants.data]);
  const selectedVariant = sellableVariants.find((item) => item.id === variantId);

  async function createSupplier(event: FormEvent) {
    event.preventDefault();
    const result = await supplierAction.run(() => procurementService.createSupplier(store.id, { name: supplierName.trim(), contactEmail: supplierEmail.trim() || null, phone: supplierPhone.trim() || null, notes: supplierNotes.trim() || null }), 'Proveedor guardado.');
    if (result) { setSupplierName(''); setSupplierEmail(''); setSupplierPhone(''); setSupplierNotes(''); setSupplierId(result.id); suppliers.reload(); }
  }
  function addLine() {
    const selectedProduct = products.data?.find((product) => product.id === productId);
    if (!selectedVariant || !selectedProduct || !Number.isInteger(Number(quantity)) || Number(quantity) < 1 || Number(quantity) > 10000 || !Number.isFinite(Number(unitCost)) || Number(unitCost) < 0) {
      toast.error('Revisa la variante, la cantidad y el costo unitario.'); return;
    }
    if (lines.some((line) => line.variantId === selectedVariant.id)) { toast.error('Esa variante ya está en la orden.'); return; }
    setLines((current) => [...current, { variantId: selectedVariant.id, productName: selectedProduct.name, sku: selectedVariant.sku, options: Object.values(selectedVariant.options).join(' / ') || 'Presentación estándar', quantity: Number(quantity), unitCost: Number(unitCost) }]);
    setVariantId(''); setQuantity('1'); setUnitCost('0.00');
  }
  async function createOrder(event: FormEvent) {
    event.preventDefault();
    if (!supplierId || lines.length === 0) { toast.error('Selecciona un proveedor y agrega al menos una variante.'); return; }
    const result = await orderAction.run(() => procurementService.createOrder(store.id, { supplierId, notes: notes.trim(), items: lines.map(({ variantId, quantity, unitCost }) => ({ variantId, quantity, unitCost })) }), 'Orden de compra creada.');
    if (result) { setLines([]); setNotes(''); orders.reload(); }
  }
  function beginReceive(order: PurchaseOrder) {
    setReceivingId(order.id); setReceiptReason('');
    setReceiptCounts(Object.fromEntries(order.items.filter((item) => item.quantityPending > 0).map((item) => [item.id, { accepted: String(item.quantityPending), rejected: '0' }])));
  }
  async function receive(event: FormEvent, order: PurchaseOrder) {
    event.preventDefault();
    const receivedItems = order.items.filter((item) => item.quantityPending > 0).map((item) => ({ itemId: item.id, accepted: Number(receiptCounts[item.id]?.accepted || 0), rejected: Number(receiptCounts[item.id]?.rejected || 0) })).filter((item) => item.accepted + item.rejected > 0);
    if (!receivedItems.length) { toast.error('Ingresa al menos una cantidad aceptada o rechazada.'); return; }
    if (!receiptReason.trim() || receivedItems.some((item) => !Number.isInteger(item.accepted) || !Number.isInteger(item.rejected) || item.accepted < 0 || item.rejected < 0 || item.accepted + item.rejected > (order.items.find((line) => line.id === item.itemId)?.quantityPending ?? 0))) { toast.error('Ingresa un motivo y cantidades válidas que no superen lo pendiente.'); return; }
    const result = await receiveAction.run(() => procurementService.receive(store.id, order.id, { items: receivedItems, reason: receiptReason.trim() }), 'Recepción registrada; las unidades aceptadas ya están en inventario.');
    if (result) { setReceivingId(''); setReceiptCounts({}); orders.reload(); }
  }

  return <>
    <PageHeader title="Compras a proveedores" description="Registra órdenes internas y añade al inventario solo las unidades que recibiste y aceptaste." />
    <div className="procurement-grid">
      <form className="card procurement-form" onSubmit={(event) => void createSupplier(event)}>
        <h2>Proveedores</h2>
        <Field label="Nombre del proveedor" example="Distribuidora Central" value={supplierName} onChange={setSupplierName} maxLength={120} required />
        <Field label="Correo de contacto" example="ventas@proveedor.pe" type="email" value={supplierEmail} onChange={setSupplierEmail} maxLength={255} />
        <Field label="Teléfono" example="+51987654321" value={supplierPhone} onChange={setSupplierPhone} maxLength={30} />
        <Field label="Notas" example="Entrega los martes" value={supplierNotes} onChange={setSupplierNotes} maxLength={500} />
        <Button type="submit" pending={supplierAction.pending}><Plus size={16} /> Guardar proveedor</Button>
        {suppliers.data?.length ? <Select label="Proveedor para la orden" value={supplierId} onChange={setSupplierId} options={[{ value: '', label: 'Selecciona proveedor' }, ...suppliers.data.map((supplier: Supplier) => ({ value: supplier.id, label: supplier.name }))]} /> : <p className="muted">Registra un proveedor para crear tu primera orden.</p>}
      </form>

      <form className="card procurement-form" onSubmit={(event) => void createOrder(event)}>
        <h2>Nueva orden de compra</h2>
        <Select label="Proveedor" value={supplierId} onChange={setSupplierId} options={[{ value: '', label: 'Selecciona proveedor' }, ...(suppliers.data || []).map((supplier: Supplier) => ({ value: supplier.id, label: supplier.name }))]} required />
        <Select label="Producto" value={productId} onChange={(value) => { setProductId(value); setVariantId(''); }} options={[{ value: '', label: 'Selecciona producto' }, ...(products.data || []).map((product) => ({ value: product.id, label: product.name }))]} required />
        <Select label="Variante / SKU" value={variantId} onChange={setVariantId} options={[{ value: '', label: 'Selecciona variante' }, ...sellableVariants.map((variant: ProductVariant) => ({ value: variant.id, label: `${variant.sku} · ${Object.values(variant.options).join(' / ') || 'Estándar'}` }))]} required />
        <Field label="Cantidad solicitada" example="10" type="number" min="1" max="10000" step="1" value={quantity} onChange={setQuantity} required />
        <Field label={`Costo unitario (${store.currency})`} example="12.50" type="number" min="0" max="9999999999" step="0.01" value={unitCost} onChange={setUnitCost} required />
        <Button type="button" variant="secondary" onClick={addLine}><Plus size={16} /> Agregar variante</Button>
        {lines.length > 0 && <div className="procurement-draft-lines"><strong>Artículos de la orden</strong>{lines.map((line) => <div key={line.variantId}><span>{line.productName} · {line.options} · {line.sku} × {line.quantity}</span><button type="button" onClick={() => setLines((items) => items.filter((item) => item.variantId !== line.variantId))}>Quitar</button></div>)}</div>}
        <Field label="Notas" example="Entrega acordada para el viernes" value={notes} onChange={setNotes} maxLength={500} />
        <Button type="submit" pending={orderAction.pending} disabled={!supplierId || !lines.length}><ClipboardList size={16} /> Crear orden</Button>
        <small>Registro de abastecimiento interno; no es una factura de compra.</small>
      </form>
    </div>
    <ErrorNotice message={suppliers.error || products.error || variants.error || orders.error} retry={orders.reload} />
    <PageHeader title="Órdenes registradas" description="Puedes recibir una orden en varias entregas y rechazar unidades dañadas en tránsito." />
    {orders.loading ? <Loading /> : orders.data?.length ? <div className="procurement-orders">{orders.data.map((order: PurchaseOrder) => <section className="card procurement-order" key={order.id}>
      <header><div><strong>{order.orderNumber}</strong><small>{order.supplierName} · {new Intl.DateTimeFormat('es-PE', { dateStyle: 'medium' }).format(new Date(order.createdAt))}</small></div><span className={`badge badge-${order.status.toLowerCase()}`}>{STATUS_LABELS[order.status]}</span></header>
      <div className="table-wrap"><table><thead><tr><th>Variante</th><th>Solicitadas</th><th>Recibidas</th><th>Rechazadas</th><th>Pendientes</th><th>Costo unitario</th></tr></thead><tbody>{order.items.map((item: PurchaseOrderItem) => <tr key={item.id}><td>{item.productName}<small>{item.sku} · {item.variantOptions}</small></td><td>{item.quantityOrdered}</td><td>{item.quantityReceived}</td><td>{item.quantityRejected}</td><td>{item.quantityPending}</td><td>{money(item.unitCost, store.currency)}</td></tr>)}</tbody></table></div>
      {order.status !== 'RECEIVED' && order.status !== 'CANCELLED' && (receivingId === order.id ? <form className="procurement-receive" onSubmit={(event) => void receive(event, order)}>
        {order.items.filter((item) => item.quantityPending > 0).map((item) => <div className="procurement-receive-row" key={item.id}><strong>{item.sku} · Pendientes: {item.quantityPending}</strong><label>Aceptadas<input type="number" min="0" max={item.quantityPending} value={receiptCounts[item.id]?.accepted || '0'} onChange={(event) => setReceiptCounts((current) => ({ ...current, [item.id]: { accepted: event.target.value, rejected: current[item.id]?.rejected || '0' } }))} /></label><label>Rechazadas<input type="number" min="0" max={item.quantityPending} value={receiptCounts[item.id]?.rejected || '0'} onChange={(event) => setReceiptCounts((current) => ({ ...current, [item.id]: { accepted: current[item.id]?.accepted || '0', rejected: event.target.value } }))} /></label></div>)}
        <Field label="Motivo de recepción" example="Llegaron 2 cajas; 1 unidad con envase roto" value={receiptReason} onChange={setReceiptReason} maxLength={300} required />
        <div className="actions"><Button type="button" variant="secondary" onClick={() => setReceivingId('')}>Cerrar</Button><Button type="submit" pending={receiveAction.pending}><Truck size={16} /> Confirmar recepción</Button></div>
      </form> : <Button variant="secondary" onClick={() => beginReceive(order)}><Truck size={16} /> Registrar recepción</Button>)}
    </section>)}</div> : <Empty title="Todavía no hay órdenes de compra" />}
  </>;
}
