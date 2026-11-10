import { useCallback, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { ArrowLeft, ShieldCheck } from 'lucide-react';
import { Button, ErrorNotice, Field, Loading, ProductImage } from '@/core/components';
import { useResource } from '@/core/hooks/useResource';
import { useSession } from '@/context/SessionContext';
import { useStore } from '@/context/StoreContext';
import { useCart } from '@/features/cart';
import { money } from '@/utils/format';
import { isInteger } from '@/utils/validation';
import { catalogService } from './services/catalogService';
/** Consulta el detalle de un producto usando su URL. */
export function ProductDetailPage() {
  const { productId = '' } = useParams();
  const store = useStore();
  const cart = useCart();
  const { user } = useSession();
  const [quantity, setQuantity] = useState('1');
  const resource = useResource(
    useCallback(() => catalogService.detail(store.id, productId), [store.id, productId]),
  );
  if (resource.loading) return <Loading />;
  if (!resource.data) return <ErrorNotice message={resource.error} retry={resource.reload} />;
  const product = resource.data;
  return (
    <>
      <Link className="back-link" to={`/tienda/${store.subdomain}`}>
        <ArrowLeft size={16} />
        Volver al catálogo
      </Link>
      <div className="product-detail">
        <div className="detail-image">
          <ProductImage src={product.imageUrl} name={product.name} />
        </div>
        <section className="card detail-content">
          <span className="eyebrow">{store.name}</span>
          <h1>{product.name}</h1>
          <div className="price">
            <strong>{money(product.offerPrice ?? product.basePrice, store.currency)}</strong>
            {product.offerPrice !== null && <del>{money(product.basePrice, store.currency)}</del>}
          </div>
          <span className={`badge ${product.inStock ? 'badge-active' : ''}`}>
            {product.inStock ? 'En stock' : 'Agotado'}
          </span>
          <p className="description">
            {product.description || 'Este producto todavía no tiene descripción.'}
          </p>
          <Field
            label="Cantidad"
            example="2"
            rule="digits"
            inputMode="numeric"
            maxLength={10}
            value={quantity}
            onChange={setQuantity}
            error={!isInteger(quantity, 1) ? 'Ingresa un entero mayor que cero.' : ''}
          />
          {!user ? (
            <Link
              className="button primary"
              to={`/ingresar?next=${encodeURIComponent(`/tienda/${store.subdomain}/producto/${product.id}`)}`}
            >
              Inicia sesión para comprar
            </Link>
          ) : (
            user.role === 'CUSTOMER' && (
              <Button
                pending={cart.pending}
                disabled={!product.inStock || !isInteger(quantity, 1) || cart.loading}
                onClick={() => void cart.add(product.id, Number(quantity))}
              >
                Agregar al carrito
              </Button>
            )
          )}
          <ErrorNotice message={cart.error} />
          <p className="trust">
            <ShieldCheck size={18} />
            Los importes finales se confirman en tu orden.
          </p>
        </section>
      </div>
    </>
  );
}
