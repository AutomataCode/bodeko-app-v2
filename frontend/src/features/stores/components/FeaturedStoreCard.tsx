import { Link } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';
import type { TopSellingStore } from '../types';

/** Tienda real y posición que ocupa en el listado de ventas. */
interface FeaturedStoreCardProps {
  store: TopSellingStore;
  position: number;
}

/** Tarjeta de tienda. La fotografía decorativa no representa su local real. */
export function FeaturedStoreCard({ store, position }: FeaturedStoreCardProps) {
  return (
    <article className={`landing-store${position === 0 ? ' landing-store-featured' : ''}`}>
      <div className={`landing-store-cover landing-cover-${position % 3}`} aria-hidden="true">
        <span className="landing-rank">{String(position + 1).padStart(2, '0')} / Más vendidas</span>
        <span className="landing-cover-caption">BODEKO / DESCUBRE</span>
      </div>
      <div className="landing-store-info">
        <p className="landing-sales">
          {store.salesCount} {store.salesCount === 1 ? 'compra' : 'compras'}
        </p>
        <h3>{store.name}</h3>
        <p className="landing-store-slug">{store.subdomain}</p>
        <Link to={`/tienda/${encodeURIComponent(store.subdomain)}`} className="landing-store-link">
          Explorar tienda <ArrowRight size={17} aria-hidden="true" />
          <span className="sr-only">: {store.name}</span>
        </Link>
      </div>
    </article>
  );
}
