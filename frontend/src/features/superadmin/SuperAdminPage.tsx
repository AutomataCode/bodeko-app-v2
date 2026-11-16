import { useCallback, useState } from 'react';
import { RefreshCw, Search, ShieldCheck, Store, UserRound, Users } from 'lucide-react';
import { Badge, Button, Empty, ErrorNotice, Field, Loading, Modal, PageHeader, Pagination, Select } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { useResource } from '@/core/hooks/useResource';
import { dateTime } from '@/utils/format';
import { superadminService } from './services/superadminService';
import type { AccountFilters, ManagedAccount } from './types';

const initialFilters: AccountFilters = { search: '', active: '', storeId: '', from: '', to: '', page: 0 };
type AccountKind = 'vendors' | 'customers';

/** Dashboard de la plataforma con filtros y acciones reversibles sobre cuentas. */
export function SuperAdminPage() {
  const [kind, setKind] = useState<AccountKind>('vendors');
  const [filters, setFilters] = useState(initialFilters);
  const [selected, setSelected] = useState<ManagedAccount | null>(null);
  const [reason, setReason] = useState('');
  const summary = useResource(useCallback(() => superadminService.summary(), []));
  const stores = useResource(useCallback(() => superadminService.stores(), []));
  const accounts = useResource(useCallback(() => superadminService.list(kind, filters), [kind, filters]));
  const action = useAction();
  function updateFilter(key: Exclude<keyof AccountFilters, 'page'>, value: string) {
    setFilters((current) => ({ ...current, [key]: value, page: 0 }));
  }
  async function changeStatus() {
    if (!selected || reason.trim().length < 3) return;
    const active = !selected.active;
    const result = await action.run(() => superadminService.changeStatus(kind, selected.id, active, reason.trim()),
      active ? 'Cuenta reactivada.' : 'Cuenta suspendida.');
    if (result) {
      setSelected(null);
      setReason('');
      accounts.reload();
      summary.reload();
    }
  }
  return (
    <>
      <PageHeader title="Gestión de usuarios" description="Administra vendedores y clientes de todas las tiendas."
        actions={<Button variant="secondary" onClick={() => { summary.reload(); stores.reload(); accounts.reload(); }}><RefreshCw size={16} />Actualizar</Button>} />
      <ErrorNotice message={summary.error || stores.error || accounts.error} retry={() => { summary.reload(); stores.reload(); accounts.reload(); }} />
      {summary.loading ? <Loading /> : summary.data && (
        <div className="metrics-grid superadmin-metrics">
          <article className="metric-card"><Users size={20} /><span>Vendedores</span><strong>{summary.data.vendors}</strong><small>{summary.data.activeVendors} activos</small></article>
          <article className="metric-card"><Store size={20} /><span>Clientes</span><strong>{summary.data.customers}</strong><small>{summary.data.activeCustomers} activos</small></article>
          <article className="metric-card"><ShieldCheck size={20} /><span>Vendedores suspendidos</span><strong>{summary.data.vendors - summary.data.activeVendors}</strong><small>Cuentas de propietario</small></article>
          <article className="metric-card"><UserRound size={20} /><span>Clientes suspendidos</span><strong>{summary.data.customers - summary.data.activeCustomers}</strong><small>Cuentas por tienda</small></article>
        </div>
      )}
      <div className="superadmin-tabs" role="tablist" aria-label="Tipo de cuenta">
        <button type="button" role="tab" aria-selected={kind === 'vendors'} className={kind === 'vendors' ? 'active' : ''}
          onClick={() => { setKind('vendors'); setFilters(initialFilters); }}>Vendedores</button>
        <button type="button" role="tab" aria-selected={kind === 'customers'} className={kind === 'customers' ? 'active' : ''}
          onClick={() => { setKind('customers'); setFilters(initialFilters); }}>Clientes</button>
      </div>
      <section className="card superadmin-filters" aria-label="Filtros de usuarios">
        <Field label="Buscar por nombre o correo" example="ana@correo.com" value={filters.search}
          maxLength={255} onChange={(value) => updateFilter('search', value)} />
        <Select label="Estado de la cuenta" value={filters.active} onChange={(value) => updateFilter('active', value)}
          options={[{ value: '', label: 'Todos los estados' }, { value: 'true', label: 'Activos' }, { value: 'false', label: 'Suspendidos' }]} />
        <Select label="Tienda" value={filters.storeId} onChange={(value) => updateFilter('storeId', value)}
          options={[{ value: '', label: 'Todas las tiendas' }, ...(stores.data ?? []).map((store) => ({ value: store.id, label: store.name }))]} />
        <Field label="Registrado desde" example="2026-01-01" type="date" value={filters.from} onChange={(value) => updateFilter('from', value)} />
        <Field label="Registrado hasta" example="2026-12-31" type="date" value={filters.to} onChange={(value) => updateFilter('to', value)} />
        <Button variant="secondary" onClick={() => setFilters(initialFilters)}><Search size={16} />Limpiar filtros</Button>
      </section>
      {accounts.loading ? <Loading /> : !accounts.data?.content.length ? <Empty title="No hay cuentas con estos filtros" /> : (
        <>
          <div className="card table-wrap superadmin-table-wrap"><table>
            <thead><tr><th>Cuenta</th><th>Correo</th><th>Tienda(s)</th><th>Registro</th><th>Estado</th><th>Acción</th></tr></thead>
            <tbody>{accounts.data.content.map((account) => (
              <tr key={account.id}>
                <td><strong>{account.fullName}</strong><small>{account.role === 'ADMIN' ? 'Vendedor' : 'Cliente'}</small></td>
                <td>{account.email}</td>
                <td>{account.stores.length ? account.stores.map((store) => store.name).join(', ') : 'Sin tiendas'}</td>
                <td>{dateTime(account.createdAt)}</td>
                <td><Badge value={account.active ? 'ACTIVE' : 'INACTIVE'} /></td>
                <td><Button variant={account.active ? 'danger' : 'secondary'} onClick={() => { setSelected(account); setReason(''); }}>
                  {account.active ? 'Dar de baja' : 'Reactivar'}
                </Button></td>
              </tr>
            ))}</tbody>
          </table></div>
          <Pagination page={filters.page} totalPages={accounts.data.totalPages} onChange={(page) => setFilters((current) => ({ ...current, page }))} />
        </>
      )}
      {selected && <Modal title={selected.active ? 'Suspender cuenta' : 'Reactivar cuenta'} onClose={() => setSelected(null)}>
        <div className="form-stack"><p>{selected.fullName} · {selected.email}</p>
          <Field label="Motivo del cambio" example="Solicitud validada por soporte" value={reason} maxLength={500}
            onChange={setReason} required />
          <ErrorNotice message={reason.trim().length > 0 && reason.trim().length < 3 ? 'Escribe un motivo de al menos 3 caracteres.' : action.error} />
          <div className="actions"><Button variant="secondary" onClick={() => setSelected(null)}>Volver</Button>
            <Button variant={selected.active ? 'danger' : 'primary'} pending={action.pending}
              disabled={reason.trim().length < 3} onClick={() => void changeStatus()}>
              {selected.active ? 'Confirmar baja' : 'Confirmar reactivación'}
            </Button></div>
        </div>
      </Modal>}
    </>
  );
}
