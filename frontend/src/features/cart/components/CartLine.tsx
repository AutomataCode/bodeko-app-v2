import { useState } from 'react';
import { Trash2 } from 'lucide-react';
import { Button, Field, ProductImage } from '@/core/components';
import { useCart } from '../hooks/useCart';
import type { CartItem } from '../types';
import { money } from '@/utils/format';
import { isInteger } from '@/utils/validation';
/** Producto del carrito y moneda de la tienda. */
export interface CartLineProps {
  item: CartItem;
  currency: string;
}
/** Cantidad editable con guardado explícito; evita envíos parciales al escribir. */
export function CartLine({ item, currency }: CartLineProps) {
  const cart = useCart();
  const [quantity, setQuantity] = useState(String(item.quantity));
  return (
    <article className="cart-line">
      <ProductImage src={item.imageUrl} name={item.name} />
      <div className="cart-line-content">
        <h3>{item.name}</h3>
        <p>
          {money(item.chargedUnitPrice, currency)} por unidad{' '}
          {item.chargedUnitPrice < item.listUnitPrice && (
            <del>{money(item.listUnitPrice, currency)}</del>
          )}
        </p>
        <form
          className="inline-form"
          onSubmit={(event) => {
            event.preventDefault();
            if (isInteger(quantity, 1)) void cart.update(item.productId, Number(quantity));
          }}
        >
          <Field
            label="Cantidad"
            example="2"
            rule="digits"
            inputMode="numeric"
            maxLength={10}
            value={quantity}
            onChange={setQuantity}
            required
            error={!isInteger(quantity, 1) ? 'Ingresa un entero mayor que cero.' : ''}
          />
          <Button
            type="submit"
            variant="secondary"
            pending={cart.pending}
            disabled={!isInteger(quantity, 1) || Number(quantity) === item.quantity}
          >
            Actualizar
          </Button>
        </form>
      </div>
      <div className="cart-line-total">
        <strong>{money(item.itemSubtotal, currency)}</strong>
        <Button
          variant="secondary"
          aria-label={`Quitar ${item.name}`}
          disabled={cart.pending}
          onClick={() => void cart.remove(item.productId)}
        >
          <Trash2 size={16} />
          Quitar
        </Button>
      </div>
    </article>
  );
}
