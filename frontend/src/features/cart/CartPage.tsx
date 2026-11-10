import { Link } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';
import { Empty, ErrorNotice, Loading, PageHeader } from '@/core/components';
import { useStore } from '@/context/StoreContext';
import { money } from '@/utils/format';
import { useCart } from './hooks/useCart';
import { CartLine } from './components/CartLine';
/** Muestra el carrito guardado y permite continuar con la compra. */
export function CartPage() {
  const store = useStore();
  const state = useCart();
  return (
    <>
      <PageHeader title="Tu carrito" description={`Productos de ${store.name}`} />
      <ErrorNotice message={state.error} retry={state.reload} />
      {state.loading ? (
        <Loading />
      ) : !state.cart?.items.length ? (
        <Empty title="Tu carrito está esperando tus favoritos">
          <Link className="button primary" to={`/tienda/${store.subdomain}`}>
            Explorar productos
          </Link>
        </Empty>
      ) : (
        <div className="checkout-grid">
          <section className="card">
            {state.cart.items.map((item) => (
              <CartLine
                key={`${item.productId}-${item.quantity}`}
                item={item}
                currency={store.currency}
              />
            ))}
          </section>
          <aside className="card summary">
            <h2>Resumen del carrito</h2>
            <p>
              <span>Unidades</span>
              <strong>{state.cart.totalQuantity}</strong>
            </p>
            <p>
              <span>Subtotal de productos</span>
              <strong>{money(state.cart.subtotal, store.currency)}</strong>
            </p>
            <small>El IGV y el total definitivo se calculan al confirmar la orden.</small>
            <Link className="button primary" to={`/tienda/${store.subdomain}/checkout`}>
              Continuar al checkout <ArrowRight size={16} />
            </Link>
            <Link to={`/tienda/${store.subdomain}`}>Seguir comprando</Link>
          </aside>
        </div>
      )}
    </>
  );
}
