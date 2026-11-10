import { useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { Empty, ErrorNotice, Loading, PageHeader, Select } from '@/core/components';
import { useProducts } from '@/features/products';
import type { Store } from '@/features/stores';
import { dateTime, money } from '@/utils/format';
import { useOffers } from './hooks/useOffers';
import { OfferForm } from './components/OfferForm';
/** Configuración e historial de promociones por producto. */
export function OffersPage() {
  const store = useOutletContext<Store>();
  const products = useProducts(store.id);
  const [selected, setSelected] = useState('');
  const offers = useOffers(store.id, selected);
  return (
    <>
      <PageHeader title="Ofertas" description="Dale a cada promoción su momento." />
      <div className="card">
        <Select
          label="Producto"
          value={selected}
          onChange={setSelected}
          options={[
            { value: '', label: 'Selecciona un producto' },
            ...(products.data || []).map((product) => ({ value: product.id, label: product.name })),
          ]}
          hint="Ejemplo: Café de especialidad 250 g."
        />
      </div>
      <ErrorNotice message={products.error} retry={products.reload} />
      <ErrorNotice message={offers.error} retry={offers.reload} />
      {products.loading || offers.loading ? (
        <Loading />
      ) : selected ? (
        <div className="two-columns">
          <OfferForm
            key={selected}
            storeId={store.id}
            productId={selected}
            onSaved={offers.reload}
          />
          <section className="card">
            <h2>Historial de ofertas</h2>
            {offers.data?.length ? (
              offers.data.map((offer) => (
                <article className="offer-row" key={`${offer.startsAt}-${offer.endsAt}`}>
                  <strong>{money(offer.offerPrice, store.currency)}</strong>
                  <span className={`badge ${offer.active ? 'badge-active' : ''}`}>
                    {offer.active ? 'Vigente' : 'Fuera de vigencia'}
                  </span>
                  <p>
                    {dateTime(offer.startsAt)} — {dateTime(offer.endsAt)}
                  </p>
                </article>
              ))
            ) : (
              <Empty title="Este producto aún no tiene ofertas" />
            )}
          </section>
        </div>
      ) : (
        <Empty title="Selecciona un producto para ver sus ofertas" />
      )}
    </>
  );
}
