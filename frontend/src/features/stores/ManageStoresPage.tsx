import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Plus, Store as StoreIcon } from 'lucide-react';
import { Button, Empty, ErrorNotice, Loading, Modal, PageHeader } from '@/core/components';
import { useStores } from './hooks/useStores';
import { StoreForm } from './components/StoreForm';
/** Selector de tiendas del propietario; una cuenta puede administrar varios comercios. */
export function ManageStoresPage() {
  const resource = useStores(true);
  const [creating, setCreating] = useState(false);
  const navigate = useNavigate();
  return (
    <>
      <PageHeader
        title="Tus tiendas"
        description="Cada negocio tiene su propio espacio. Elige uno para comenzar."
        actions={
          <Button onClick={() => setCreating(true)}>
            <Plus size={16} />
            Crear tienda
          </Button>
        }
      />
      <ErrorNotice message={resource.error} retry={resource.reload} />
      {resource.loading ? (
        <Loading />
      ) : !resource.data?.length ? (
        <Empty title="Haz espacio para tu próximo negocio">
          <p>Crea tu primera tienda para agregar productos y recibir órdenes.</p>
          <Button onClick={() => setCreating(true)}>Crear mi primera tienda</Button>
        </Empty>
      ) : (
        <div className="store-grid">
          {resource.data.map((store) => (
            <Link key={store.id} to={`/admin/${store.id}`} className="card store-card">
              <StoreIcon size={32} />
              <h2>{store.name}</h2>
              <p className="muted">{store.subdomain}</p>
              <span className="store-link">Administrar tienda</span>
            </Link>
          ))}
        </div>
      )}
      {creating && (
        <Modal title="Crea tu tienda" onClose={() => setCreating(false)}>
          <StoreForm onCreated={(store) => navigate(`/admin/${store.id}`)} />
        </Modal>
      )}
    </>
  );
}
