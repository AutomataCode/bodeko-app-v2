import { useCallback, useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { FilterX, Search } from 'lucide-react';
import { Button, Empty, ErrorNotice, Loading, PageHeader, Pagination } from '@/core/components';
import { useResource } from '@/core/hooks/useResource';
import type { Store } from '@/features/stores';
import { inventoryMovementService } from './services/inventoryMovementService';
import type { InventoryMovementFilters } from './types/movement';

const INITIAL_FILTERS: InventoryMovementFilters = {
  search: '', type: '', actor: '', reference: '', from: '', to: '', page: 0, size: 20,
};
const MOVEMENT_LABELS: Record<string, string> = {
  RESERVATION: 'Reserva', SALE: 'Venta concretada', RESERVATION_RELEASE: 'Reserva liberada',
  STOCK_ENTRY: 'Entrada al inventario', ADJUSTMENT: 'Ajuste', RETURN: 'Devolución',
  DAMAGE: 'Merma o daño', LOSS: 'Pérdida',
};

/** Historial de existencias de la tienda seleccionada en el panel. */
export function InventoryMovementsPage() {
  const store = useOutletContext<Store>();
  const [filters, setFilters] = useState(INITIAL_FILTERS);
  const resource = useResource(useCallback(
    () => inventoryMovementService.list(store.id, filters), [store.id, filters],
  ));
  const updateFilter = (field: keyof InventoryMovementFilters, value: string) =>
    setFilters((current) => ({ ...current, [field]: value, page: 0 }));
  const resetFilters = () => setFilters(INITIAL_FILTERS);
  const page = resource.data;
  return (
    <>
      <PageHeader title="Movimientos de inventario" description={`Kardex de ${store.name}: reservas, ventas, entradas y ajustes de stock.`} />
      <section className="card movement-filters" aria-label="Filtros del kardex">
        <label>Producto o SKU<input value={filters.search} placeholder="Ejemplo: CAF-250" onChange={(event) => updateFilter('search', event.target.value)} /></label>
        <label>Tipo de movimiento<select value={filters.type} onChange={(event) => updateFilter('type', event.target.value)}>
          <option value="">Todos los movimientos</option>
          {Object.entries(MOVEMENT_LABELS).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
        </select></label>
        <label>Usuario responsable<input value={filters.actor} placeholder="Ejemplo: admin@tienda.pe" onChange={(event) => updateFilter('actor', event.target.value)} /></label>
        <label>Fecha inicial<input type="date" value={filters.from} onChange={(event) => updateFilter('from', event.target.value ? `${event.target.value}T00:00:00-05:00` : '')} /></label>
        <label>Fecha final<input type="date" value={filters.to.slice(0, 10)} onChange={(event) => updateFilter('to', event.target.value ? `${event.target.value}T23:59:59-05:00` : '')} /></label>
        <label>Pedido o motivo<input value={filters.reference} placeholder="Ejemplo: PED-20261113" onChange={(event) => updateFilter('reference', event.target.value)} /></label>
        <div className="movement-filter-actions">
          <Button variant="secondary" onClick={resetFilters}><FilterX size={16} /> Limpiar filtros</Button>
          <span className="muted"><Search size={15} /> {page?.totalElements ?? 0} movimientos</span>
        </div>
      </section>
      <ErrorNotice message={resource.error} retry={resource.reload} />
      {resource.loading ? <Loading /> : page?.content.length ? (
        <>
          <div className="table-wrap movement-table-wrap">
            <table>
              <thead><tr><th>Fecha y hora</th><th>Producto / variante</th><th>Movimiento</th><th>Cant.</th><th>Saldo disponible</th><th>Documento / referencia</th><th>Motivo y responsable</th></tr></thead>
              <tbody>{page.content.map((movement) => (
                <tr key={movement.id}>
                  <td>{new Intl.DateTimeFormat('es-PE', { dateStyle: 'short', timeStyle: 'short' }).format(new Date(movement.createdAt))}</td>
                  <td><strong>{movement.sku}</strong><small>{movement.productName} · {movement.variantOptions}</small></td>
                  <td><span className={`movement-badge movement-${movement.movementType.toLowerCase()}`}>{MOVEMENT_LABELS[movement.movementType] || movement.movementType}</span></td>
                  <td className={movement.quantityChange < 0 ? 'movement-negative' : movement.quantityChange > 0 ? 'movement-positive' : ''}>{movement.movementType === 'SALE' ? movement.units : movement.quantityChange > 0 ? `+${movement.quantityChange}` : movement.quantityChange}</td>
                  <td>{movement.quantityBefore} → {movement.quantityAfter}</td>
                  <td>{movement.referenceNumber || '—'}</td>
                  <td>{movement.reason || '—'}<small>{movement.actorName || 'Sistema'}{movement.actorEmail ? ` · ${movement.actorEmail}` : ''}</small></td>
                </tr>
              ))}</tbody>
            </table>
          </div>
          <Pagination page={page.page} totalPages={page.totalPages} onChange={(next) => setFilters((current) => ({ ...current, page: next }))} />
        </>
      ) : <Empty title="No hay movimientos para estos filtros" />}
    </>
  );
}
