import { Link, useNavigate } from 'react-router-dom';
import { Empty, ErrorNotice, Loading, PageHeader } from '@/core/components';
import { useCart } from '@/features/cart';
import { useStore } from '@/context/StoreContext';
import { money } from '@/utils/format';
import { CheckoutForm } from './components/CheckoutForm';
/** Muestra el resumen y crea la orden antes de iniciar el pago. */
export function CheckoutPage() {
  const store = useStore();
  const cart = useCart();
  const navigate = useNavigate();
  return (
    <>
      <PageHeader
        title="Finaliza tu compra"
        description="1. Revisa tu carrito   /   2. Confirma tus datos   /   3. Realiza el pago"
      />
      <ErrorNotice message={cart.error} retry={cart.reload} />
      {cart.loading ? (
        <Loading />
      ) : !cart.cart?.items.length ? (
        <Empty title="Necesitas productos para continuar">
          <Link to={`/tienda/${store.subdomain}`}>Volver al catálogo</Link>
        </Empty>
      ) : (
        <div className="checkout-grid">
          <CheckoutForm
            storeId={store.id}
            onCreated={(order) => {
              cart.reload();
              navigate(`/tienda/${store.subdomain}/pago/${order.id}`, { replace: true });
            }}
          />
          <aside className="card summary">
            <h2>Tu compra</h2>
            {cart.cart.items.map((item) => (
              <p key={item.productId}>
                <span>
                  {item.quantity} × {item.name}
                </span>
                <strong>{money(item.itemSubtotal, store.currency)}</strong>
              </p>
            ))}
            <hr />
            <p>
              <span>Subtotal</span>
              <strong>{money(cart.cart.subtotal, store.currency)}</strong>
            </p>
            <small>
              El IGV aplica según cada producto. Verás el desglose definitivo antes de pagar.
            </small>
            <Link to={`/tienda/${store.subdomain}/carrito`}>Editar carrito</Link>
          </aside>
        </div>
      )}
    </>
  );
}
