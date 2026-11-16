import { useCallback, useMemo, useState, type FormEvent } from 'react';
import { useOutletContext } from 'react-router-dom';
import { FilterX, Search, Trash2 } from 'lucide-react';
import { toast } from 'sonner';
import { Button, Empty, ErrorNotice, Loading, PageHeader, Pagination } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { useResource } from '@/core/hooks/useResource';
import { useProducts } from '@/features/products';
import { productVariantService } from '@/features/products/services/productVariantService';
import type { ProductVariant } from '@/features/products/types';
import type { Store } from '@/features/stores';
import { inventoryShrinkageService } from './services/inventoryShrinkageService';
import type { InventoryMovementFilters } from './types/movement';

const INITIAL_FILTERS: InventoryMovementFilters = { search: '', type: '', actor: '', reference: '', from: '', to: '', page: 0, size: 20 };
const MAX_QUANTITY = 10000;

/** Panel por tienda para registrar daños/pérdidas y revisar el historial auditado. */
export function InventoryShrinkagesPage() {
  const store = useOutletContext<Store>();
  const products = useProducts(store.id);
  const [filters, setFilters] = useState(INITIAL_FILTERS);
  const [productId, setProductId] = useState('');
  const [variantId, setVariantId] = useState('');
  const [type, setType] = useState<'DAMAGE' | 'LOSS'>('DAMAGE');
  const [quantity, setQuantity] = useState('1');
  const [reason, setReason] = useState('');
  const action = useAction();
  const variantResource = useResource(useCallback(() => productVariantService.list(store.id, productId), [store.id, productId]), !!productId);
  const list = useResource(useCallback(() => inventoryShrinkageService.list(store.id, filters), [store.id, filters]));
  const variantsWithStock = useMemo(() => (variantResource.data || []).filter((variant) => variant.quantity > 0), [variantResource.data]);
  const selectedVariant = variantsWithStock.find((variant) => variant.id === variantId);

  const changeProduct = (value: string) => {
    setProductId(value);
    setVariantId('');
  };
  const updateFilter = (field: keyof InventoryMovementFilters, value: string) => setFilters((current) => ({ ...current, [field]: value, page: 0 }));
  const resetFilters = () => setFilters(INITIAL_FILTERS);

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const parsedQuantity = Number(quantity);
    if (!productId || !variantId || !reason.trim() || !Number.isInteger(parsedQuantity) || parsedQuantity < 1 || parsedQuantity > MAX_QUANTITY) {
      toast.error('Completa los campos y verifica que la cantidad sea un entero entre 1 y 10 000.');
      return;
    }
    if (!selectedVariant || parsedQuantity > selectedVariant.quantity) {
      toast.error('La cantidad no puede superar las existencias disponibles de esta variante.');
      return;
    }
    const result = await action.run(
      () => inventoryShrinkageService.register(store.id, { productId, variantId, quantity: parsedQuantity, type, reason: reason.trim() }),
      'Merma registrada y descontada del inventario.',
    );
    if (result !== undefined) {
      setReason('');
      setQuantity('1');
      void variantResource.reload();
      void list.reload();
    }
  }

  return <>
    <PageHeader title="Mermas" description={`Registra productos defectuosos o pérdidas de ${store.name}. Cada registro descuenta el stock y queda en el kardex.`} />
    <form className="card shrinkage-form" onSubmit={(event) => void submit(event)}>
      <h2><Trash2 size={19} /> Registrar merma</h2>
      <label>Producto<select required value={productId} onChange={(event) => changeProduct(event.target.value)}>
        <option value="">Selecciona un producto</option>{(products.data || []).map((product) => <option key={product.id} value={product.id}>{product.name}</option>)}
      </select><small>Ejemplo: Café de especialidad 250 g.</small></label>
      <label>Variante / SKU<select required value={variantId} onChange={(event) => setVariantId(event.target.value)} disabled={!productId || variantResource.loading}>
        <option value="">{variantResource.loading ? 'Cargando variantes…' : 'Selecciona una variante'}</option>{variantsWithStock.map((variant: ProductVariant) => <option key={variant.id} value={variant.id}>{variant.sku} · {Object.values(variant.options).join(' / ') || 'Presentación estándar'} · Stock: {variant.quantity}{variant.active ? '' : ' · Desactivada'}</option>)}
      </select><small>Elige la presentación que se dañó o se perdió.</small></label>
      <label>Tipo de merma<select value={type} onChange={(event) => setType(event.target.value as 'DAMAGE' | 'LOSS')}>
        <option value="DAMAGE">Producto defectuoso o malogrado</option><option value="LOSS">Pérdida o extravío</option>
      </select><small>Ejemplo: envase roto o producto vencido.</small></label>
      <label>Cantidad<input type="number" inputMode="numeric" min="1" max={Math.min(MAX_QUANTITY, selectedVariant?.quantity ?? MAX_QUANTITY)} step="1" value={quantity} onChange={(event) => setQuantity(event.target.value)} required />
        <small>{selectedVariant ? `Disponible: ${selectedVariant.quantity} unidades. Ejemplo: 2.` : 'Ejemplo: 2 unidades.'}</small></label>
      <label className="shrinkage-reason">Motivo / observación<textarea value={reason} onChange={(event) => setReason(event.target.value.slice(0, 500))} maxLength={500} rows={3} placeholder="Ejemplo: 2 unidades con el envase roto durante el almacenamiento." required />
        <small>{reason.length}/500 caracteres. Describe qué ocurrió.</small></label>
      <div className="shrinkage-submit"><Button pending={action.pending} disabled={!productId || !variantId}>Registrar y descontar stock</Button></div>
    </form>
    <ErrorNotice message={products.error || variantResource.error} retry={products.error ? products.reload : variantResource.reload} />

    <PageHeader title="Historial de mermas" description="Consulta quién registró cada merma, el motivo y el saldo resultante." />
    <section className="card movement-filters" aria-label="Filtros del historial de mermas">
      <label>Producto o SKU<input value={filters.search} placeholder="Ejemplo: CAF-250" onChange={(event) => updateFilter('search', event.target.value)} /></label>
      <label>Tipo<select value={filters.type} onChange={(event) => updateFilter('type', event.target.value)}><option value="">Daños y pérdidas</option><option value="DAMAGE">Defectuoso o dañado</option><option value="LOSS">Pérdida o extravío</option></select></label>
      <label>Responsable<input value={filters.actor} placeholder="Ejemplo: admin@tienda.pe" onChange={(event) => updateFilter('actor', event.target.value)} /></label>
      <label>Fecha inicial<input type="date" value={filters.from.slice(0, 10)} onChange={(event) => updateFilter('from', event.target.value ? `${event.target.value}T00:00:00-05:00` : '')} /></label>
      <label>Fecha final<input type="date" value={filters.to.slice(0, 10)} onChange={(event) => updateFilter('to', event.target.value ? `${event.target.value}T23:59:59-05:00` : '')} /></label>
      <label>Motivo<input value={filters.reference} placeholder="Ejemplo: envase roto" onChange={(event) => updateFilter('reference', event.target.value)} /></label>
      <div className="movement-filter-actions"><Button variant="secondary" onClick={resetFilters}><FilterX size={16} /> Limpiar filtros</Button><span className="muted"><Search size={15} /> {list.data?.totalElements ?? 0} registros</span></div>
    </section>
    <ErrorNotice message={list.error} retry={list.reload} />
    {list.loading ? <Loading /> : list.data?.content.length ? <>
      <div className="table-wrap movement-table-wrap"><table><thead><tr><th>Fecha y hora</th><th>Producto / variante</th><th>Tipo</th><th>Unidades</th><th>Saldo disponible</th><th>Motivo</th><th>Responsable</th></tr></thead>
        <tbody>{list.data.content.map((movement) => <tr key={movement.id}>
          <td>{new Intl.DateTimeFormat('es-PE', { dateStyle: 'short', timeStyle: 'short' }).format(new Date(movement.createdAt))}</td>
          <td><strong>{movement.sku}</strong><small>{movement.productName} · {movement.variantOptions}</small></td>
          <td><span className={`movement-badge movement-${movement.movementType.toLowerCase()}`}>{movement.movementType === 'DAMAGE' ? 'Daño' : 'Pérdida'}</span></td>
          <td className="movement-negative">−{movement.units}</td><td>{movement.quantityBefore} → {movement.quantityAfter}</td><td>{movement.reason || '—'}</td><td>{movement.actorName || 'Administrador'}<small>{movement.actorEmail || ''}</small></td>
        </tr>)}</tbody></table></div>
      <Pagination page={list.data.page} totalPages={list.data.totalPages} onChange={(page) => setFilters((current) => ({ ...current, page }))} />
    </> : <Empty title="No hay mermas para estos filtros" />}
  </>;
}
