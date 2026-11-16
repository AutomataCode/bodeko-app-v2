import { useCallback, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { ArrowLeft, ShieldCheck } from 'lucide-react';
import { Button, ErrorNotice, Field, Loading, ProductImage, Select } from '@/core/components';
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
  const [selectedVariantId, setSelectedVariantId] = useState('');
  const resource = useResource(
    useCallback(() => catalogService.detail(store.id, productId), [store.id, productId]),
  );
  if (resource.loading) return <Loading />;
  if (!resource.data) return <ErrorNotice message={resource.error} retry={resource.reload} />;
  const product = resource.data;
  const variants = product.variants ?? [];
  const selectedVariant = variants.find((variant) => variant.id === selectedVariantId)
    ?? variants.find((variant) => variant.quantity > 0)
    ?? variants[0];
  const displayedPrice = selectedVariant?.offerPrice ?? selectedVariant?.basePrice ?? product.offerPrice ?? product.basePrice;
  const listPrice = selectedVariant?.basePrice ?? product.basePrice;
  const quantityValid = isInteger(quantity, 1) && (!selectedVariant || Number(quantity) <= selectedVariant.quantity);
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
            <strong>{money(displayedPrice, store.currency)}</strong>
            {displayedPrice < listPrice && <del>{money(listPrice, store.currency)}</del>}
          </div>
          <span className={`badge ${(selectedVariant?.quantity ?? (product.inStock ? 1 : 0)) > 0 ? 'badge-active' : ''}`}>
            {(selectedVariant?.quantity ?? (product.inStock ? 1 : 0)) > 0 ? 'En stock' : 'Agotado'}
          </span>
          <p className="description">
            {product.description || 'Este producto todavía no tiene descripción.'}
          </p>
          {variants.length > 0 && (
            <Select
              label="Presentación, talla o color"
              value={selectedVariant?.id ?? ''}
              onChange={setSelectedVariantId}
              options={variants.map((variant) => ({
                value: variant.id,
                label: `${Object.entries(variant.options).map(([key, value]) => `${key}: ${value}`).join(' · ') || 'Presentación estándar'} · SKU ${variant.sku}${variant.quantity > 0 ? '' : ' · Agotado'}`,
              }))}
            />
          )}
          <Field
            label="Cantidad"
            example="2"
            rule="digits"
            inputMode="numeric"
            maxLength={10}
            value={quantity}
            onChange={setQuantity}
            error={!isInteger(quantity, 1) ? 'Ingresa un entero mayor que cero.' : !quantityValid ? 'La cantidad supera las existencias disponibles.' : ''}
          />
          {!user ? (
            <Link
              className="button primary"
              to={`/tienda/${store.subdomain}/ingresar?next=${encodeURIComponent(`/tienda/${store.subdomain}/producto/${product.id}`)}`}
            >
              Inicia sesión para comprar
            </Link>
          ) : (
            user.role === 'CUSTOMER' && (
              <Button
                pending={cart.pending}
                disabled={!selectedVariant || selectedVariant.quantity < 1 || !quantityValid || cart.loading}
                onClick={() => selectedVariant && void cart.add(product.id, selectedVariant.id, Number(quantity))}
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
