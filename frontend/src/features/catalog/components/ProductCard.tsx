import { Link, useLocation } from 'react-router-dom';
import { Plus } from 'lucide-react';
import { Button, ProductImage } from '@/core/components';
import { useSession } from '@/context/SessionContext';
import { useStore } from '@/context/StoreContext';
import { useCart } from '@/features/cart';
import { money } from '@/utils/format';
import type { CatalogProduct } from '../types';
/** Producto del catálogo con su precio actual. */
export interface ProductCardProps {
  product: CatalogProduct;
}
/** Tarjeta con enlace al detalle y acción de compra según permisos y disponibilidad. */
export function ProductCard({ product }: ProductCardProps) {
  const store = useStore();
  const cart = useCart();
  const { user } = useSession();
  const location = useLocation();
  const destination = `/tienda/${store.subdomain}/producto/${product.id}`;
  return (
    <article className="card catalog-card">
      <Link className="catalog-image" to={destination}>
        <ProductImage src={product.imageUrl} name={product.name} />
        {product.offerPrice !== null && <span className="sale-label">En oferta</span>}
      </Link>
      <div className="catalog-card-body">
        <span className={`availability${product.inStock ? '' : ' availability-out-of-stock'}`}>
          {product.inStock ? 'Disponible' : 'Agotado'}
        </span>
        <h3>
          <Link to={destination}>{product.name}</Link>
        </h3>
        <div className="price">
          <strong>{money(product.offerPrice ?? product.basePrice, store.currency)}</strong>
          {product.offerPrice !== null && <del>{money(product.basePrice, store.currency)}</del>}
        </div>
        {!user ? (
          <Link
            className="button secondary"
            to={`/ingresar?next=${encodeURIComponent(location.pathname)}`}
          >
            Inicia sesión para comprar
          </Link>
        ) : user.role === 'CUSTOMER' ? (
          <Button
            variant="secondary"
            disabled={!product.inStock || cart.pending || cart.loading}
            onClick={() => void cart.add(product.id)}
          >
            <Plus size={16} />
            Agregar al carrito
          </Button>
        ) : (
          <Link className="button secondary" to={destination}>
            Ver producto
          </Link>
        )}
      </div>
    </article>
  );
}
