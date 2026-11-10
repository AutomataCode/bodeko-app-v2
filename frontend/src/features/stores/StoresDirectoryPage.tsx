import { Link } from 'react-router-dom';
import { ArrowRight, Store } from 'lucide-react';
import { Empty, ErrorNotice, Loading } from '@/core/components';
import { useStores } from './hooks/useStores';

/** Directorio completo, independiente del ranking de ventas de la portada. */
export function StoresDirectoryPage() {
  const resource = useStores();
  return (
    <section>
      <div className="page-heading">
        <div>
          <span className="eyebrow">DIRECTORIO BODEKO</span>
          <h1>Todas las tiendas</h1>
          <p>Descubre cada comercio y explora sus productos.</p>
        </div>
      </div>
      <ErrorNotice message={resource.error} retry={resource.reload} />
      {resource.loading ? (
        <Loading />
      ) : resource.error ? null : resource.data?.length === 0 ? (
        <Empty title="Aún no hay tiendas publicadas">
          <p>Los comercios aparecerán aquí cuando se registren.</p>
        </Empty>
      ) : (
        <div className="store-grid">
          {resource.data?.map((store, index) => (
            <Link
              className="card store-card"
              key={store.id}
              to={`/tienda/${encodeURIComponent(store.subdomain)}`}
            >
              <div className={`store-symbol tone-${index % 3}`}>
                <Store size={32} />
              </div>
              <h2>{store.name}</h2>
              <p className="muted">{store.subdomain}</p>
              <span className="store-link">
                Visitar tienda <ArrowRight size={16} />
              </span>
            </Link>
          ))}
        </div>
      )}
    </section>
  );
}
