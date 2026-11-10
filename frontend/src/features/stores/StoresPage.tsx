import { Link } from 'react-router-dom';
import { useState } from 'react';
import { ArrowRight, ArrowUpRight, MapPin, Search, Store } from 'lucide-react';
import { Empty, ErrorNotice, Loading } from '@/core/components';
import { useSession } from '@/context/SessionContext';
import { FeaturedStoreCard } from './components/FeaturedStoreCard';
import { useTopSellingStores } from './hooks/useTopSellingStores';
import './landing.css';

/** Presenta Bodeko y las tiendas con más compras confirmadas desde la API. */
export function StoresPage() {
  const resource = useTopSellingStores();
  const { user } = useSession();
  const [search, setSearch] = useState('');
  const stores = resource.data ?? [];
  const normalizedSearch = search.trim().toLocaleLowerCase('es');
  const visibleStores = stores.filter((store) =>
    `${store.name} ${store.subdomain}`.toLocaleLowerCase('es').includes(normalizedSearch),
  );
  const accountDestination =
    user?.role === 'ADMIN' ? '/admin' : user ? '/cuenta/ordenes' : '/registro';

  return (
    <div className="bodeko-landing" id="inicio">
      <section className="landing-hero" aria-labelledby="landing-title">
        <div>
          <span className="landing-eyebrow">Un lugar para descubrir</span>
          <h1 id="landing-title">
            Detrás de cada tienda hay algo <em>especial.</em>
          </h1>
          <p className="landing-hero-copy">
            Encuentra negocios con personalidad, descubre lo que ofrecen y explora sus productos
            desde un solo lugar.
          </p>
          <div className="landing-hero-actions">
            <a href="#tiendas" className="button primary">
              Descubrir tiendas <ArrowRight size={17} aria-hidden="true" />
            </a>
            <span className="landing-caption">
              <i aria-hidden="true" />
              Comercios independientes
            </span>
          </div>
        </div>
        <div className="landing-art" aria-hidden="true">
          <div className="landing-art-main">
            <div className="landing-art-image" />
            <span className="landing-art-kicker">TIENDAS · HISTORIAS · IDEAS</span>
            <div className="landing-art-text">
              Encuentra tu
              <br />
              próxima favorita.<small>DESCUBRE BODEKO</small>
            </div>
          </div>
          <div className="landing-art-badge">
            <Store size={23} />
            Comercios independientes
          </div>
          <div className="landing-art-stamp">
            EXPLORA
            <br />
            LO LOCAL
            <ArrowUpRight size={22} />
          </div>
        </div>
      </section>

      <section id="tiendas" className="landing-section" aria-labelledby="top-stores-title">
        <div className="landing-section-heading">
          <div>
            <span className="landing-eyebrow">Las más elegidas</span>
            <h2 id="top-stores-title">
              Tiendas que destacan<span>.</span>
            </h2>
            <p>Los comercios con más compras confirmadas en Bodeko.</p>
          </div>
          <Link to="/tiendas" className="landing-directory-link">
            Ver todas las tiendas <ArrowRight size={16} aria-hidden="true" />
          </Link>
        </div>
        <div className="landing-toolbar">
          <div className="landing-ranking-label">
            Top 3 en ventas <span>Por cantidad de órdenes · Todo el historial</span>
          </div>
          <div className="landing-search">
            <label htmlFor="featured-store-search">Buscar entre las destacadas</label>
            <div>
              <Search size={17} aria-hidden="true" />
              <input
                id="featured-store-search"
                type="search"
                maxLength={100}
                aria-describedby="featured-store-example"
                placeholder="Ejemplo: Bodeko Market"
                value={search}
                onChange={(event) => setSearch(event.target.value)}
              />
            </div>
            <small id="featured-store-example">Ejemplo: Bodeko Market</small>
          </div>
        </div>
        <ErrorNotice message={resource.error} retry={resource.reload} />
        <div aria-busy={resource.loading}>
          {resource.loading ? (
            <Loading />
          ) : resource.error ? null : stores.length === 0 ? (
            <Empty title="Las primeras ventas están por llegar">
              <p>Cuando una tienda reciba compras confirmadas, podrá aparecer aquí.</p>
              <Link to="/tiendas" className="button secondary">
                Explorar todas las tiendas
              </Link>
            </Empty>
          ) : visibleStores.length === 0 ? (
            <Empty title="No encontramos tiendas destacadas">
              <p>Prueba con otro nombre o explora el directorio completo.</p>
              <button type="button" className="button secondary" onClick={() => setSearch('')}>
                Limpiar búsqueda
              </button>
            </Empty>
          ) : (
            <div className="landing-store-grid">
              {visibleStores.map((store) => (
                <FeaturedStoreCard
                  key={store.id}
                  store={store}
                  position={stores.findIndex((ranked) => ranked.id === store.id)}
                />
              ))}
            </div>
          )}
        </div>
        <p className="landing-mid-note">
          <MapPin size={14} aria-hidden="true" />
          Un espacio para descubrir negocios, a tu manera. Fotografías referenciales.
        </p>
      </section>

      <section id="como-funciona" className="landing-how" aria-labelledby="how-title">
        <div>
          <span className="landing-eyebrow">Así de sencillo</span>
          <h2 id="how-title">Tu próxima compra empieza aquí.</h2>
        </div>
        <div>
          <span className="landing-step-number">01 / DESCUBRE</span>
          <h3>Encuentra una tienda</h3>
          <p>Explora el directorio y encuentra negocios por su nombre.</p>
        </div>
        <div>
          <span className="landing-step-number">02 / EXPLORA</span>
          <h3>Mira sus productos</h3>
          <p>Entra al catálogo de cada comercio y conoce lo que ofrece.</p>
        </div>
        <div>
          <span className="landing-step-number">03 / ELIGE</span>
          <h3>Arma tu carrito</h3>
          <p>Inicia sesión, elige tus favoritos y continúa con tu compra.</p>
        </div>
      </section>
      <section className="landing-cta" aria-labelledby="sell-title">
        <div>
          <span className="landing-eyebrow">Para quienes venden</span>
          <h2 id="sell-title">
            Tu tienda también merece
            <br />
            su propio espacio.
          </h2>
          <p>
            Presenta tus productos y haz que más personas conozcan tu negocio. Bodeko reúne
            comercios, cada uno con su personalidad.
          </p>
        </div>
        <Link className="button" to={accountDestination}>
          {user ? 'Ir a mi cuenta' : 'Crear mi cuenta'}
          <ArrowUpRight size={17} aria-hidden="true" />
        </Link>
      </section>
    </div>
  );
}
