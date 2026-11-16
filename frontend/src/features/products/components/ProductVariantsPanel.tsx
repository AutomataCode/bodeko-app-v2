import { useCallback, useEffect, useState, type FormEvent } from 'react';
import { Button, ErrorNotice, Field, Loading } from '@/core/components';
import { useResource } from '@/core/hooks/useResource';
import { useAction } from '@/core/hooks/useAction';
import { inventoryService } from '@/features/inventory/services/inventoryService';
import { isInteger, isMoney } from '@/utils/validation';
import { productVariantService } from '../services/productVariantService';
import type { ProductVariant } from '../types';

/** Administra SKU, opciones, precio y existencias de las variantes de un producto. */
export function ProductVariantsPanel({ storeId, productId }: { storeId: string; productId: string }) {
  const [sku, setSku] = useState('');
  const [optionsText, setOptionsText] = useState('');
  const [price, setPrice] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const action = useAction();
  const resource = useResource(
    useCallback(() => productVariantService.list(storeId, productId), [storeId, productId]),
  );
  const parsedOptions = optionsText.split(',').map((part) => part.trim()).filter(Boolean);
  const options = Object.fromEntries(parsedOptions.map((part) => {
    const [key = '', ...value] = part.split('=');
    return [key.trim(), value.join('=').trim()];
  }));
  const optionsValid = parsedOptions.length > 0 && parsedOptions.length <= 8
    && parsedOptions.every((part) => part.includes('='))
    && Object.keys(options).length === parsedOptions.length
    && Object.entries(options).every(([key, value]) => key && value && key.length <= 40 && value.length <= 80)
    && new Set(parsedOptions.map((part) => (part.split('=')[0] ?? '').trim().toLocaleLowerCase())).size === parsedOptions.length;
  async function create(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (!sku.trim() || !optionsValid || !isMoney(price) || Number(price) < 0.01 || Number(price) > 100000) return;
    const result = await action.run(
      () => productVariantService.create(storeId, productId, { sku: sku.trim(), options, basePrice: Number(price) }),
      'Variante agregada.',
    );
    if (result) {
      setSku('');
      setOptionsText('');
      setPrice('');
      setSubmitted(false);
      resource.reload();
    }
  }
  return (
    <section className="card form-stack" aria-labelledby="variant-heading">
      <h3 id="variant-heading">Variantes y existencias</h3>
      <p className="muted">Cada combinación tiene SKU, precio y stock propios. Ejemplo de opciones: Talla=M, Color=Negro.</p>
      <ErrorNotice message={resource.error || action.error} retry={resource.reload} />
      {resource.loading ? <Loading /> : (resource.data ?? []).map((variant) => (
        <VariantStockRow key={variant.id} storeId={storeId} productId={productId} variant={variant}
          onSaved={resource.reload} />
      ))}
      <form className="form-stack" noValidate onSubmit={(event) => void create(event)}>
        <h4>Agregar variante</h4>
        <Field label="SKU" example="POLO-NEG-M" value={sku} maxLength={80}
          onChange={(value) => setSku(value.replace(/[^A-Za-z0-9._-]/g, ''))}
          error={submitted && !sku.trim() ? 'Ingresa un SKU.' : ''} required />
        <Field label="Opciones" example="Talla=M, Color=Negro" value={optionsText} maxLength={500}
          onChange={setOptionsText} error={submitted && !optionsValid ? 'Usa pares distintos con formato Nombre=Valor.' : ''} required />
        <Field label="Precio de esta variante" example="29.90" rule="money" inputMode="decimal"
          value={price} onChange={setPrice} error={submitted && (!isMoney(price) || Number(price) < 0.01) ? 'Ingresa un precio válido.' : ''} required />
        <Button type="submit" pending={action.pending}>Agregar variante</Button>
      </form>
    </section>
  );
}

function VariantStockRow({ storeId, productId, variant, onSaved }: {
  storeId: string; productId: string; variant: ProductVariant; onSaved: () => void;
}) {
  const [quantity, setQuantity] = useState(String(variant.quantity));
  const [editing, setEditing] = useState(false);
  const [sku, setSku] = useState(variant.sku);
  const [optionsText, setOptionsText] = useState(Object.entries(variant.options).map(([key, value]) => `${key}=${value}`).join(', '));
  const [price, setPrice] = useState(String(variant.basePrice));
  const [movementType, setMovementType] = useState('STOCK_ENTRY');
  const [stockReason, setStockReason] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const action = useAction();
  const editAction = useAction();
  const statusAction = useAction();
  useEffect(() => {
    setQuantity(String(variant.quantity));
    setSku(variant.sku);
    setOptionsText(Object.entries(variant.options).map(([key, value]) => `${key}=${value}`).join(', '));
    setPrice(String(variant.basePrice));
  }, [variant]);
  const options = Object.entries(variant.options).map(([key, value]) => `${key}: ${value}`).join(' · ');
  const parsedOptions = optionsText.split(',').map((part) => part.trim()).filter(Boolean);
  const updatedOptions = Object.fromEntries(parsedOptions.map((part) => {
    const [key = '', ...value] = part.split('=');
    return [key.trim(), value.join('=').trim()];
  }));
  const validOptions = parsedOptions.length > 0 && parsedOptions.length <= 8 && parsedOptions.every((part) => part.includes('='))
    && Object.keys(updatedOptions).length === parsedOptions.length
    && Object.entries(updatedOptions).every(([key, value]) => key.length > 0 && key.length <= 40 && value.length > 0 && value.length <= 80)
    && new Set(parsedOptions.map((part) => (part.split('=')[0] ?? '').trim().toLocaleLowerCase())).size === parsedOptions.length;
  return (
    <div className="variant-stock-row">
      <div className="variant-stock-heading">
        <div><strong>{options || 'Presentación estándar'} · {variant.sku}</strong>
          <small> {variant.basePrice.toFixed(2)} · {variant.active ? 'Activa' : 'Pausada'}</small></div>
        <Button type="button" variant="secondary" pending={statusAction.pending}
          onClick={() => void statusAction.run(
            () => productVariantService.changeStatus(storeId, productId, variant.id, !variant.active),
            variant.active ? 'Variante pausada.' : 'Variante activada.',
          ).then((result) => { if (result) onSaved(); })}>
          {variant.active ? 'Pausar venta' : 'Activar venta'}
        </Button>
      </div>
      <Button type="button" variant="secondary" onClick={() => setEditing((value) => !value)}>
        {editing ? 'Cerrar edición' : 'Editar variante'}
      </Button>
      {editing && <form className="form-stack" noValidate onSubmit={(event) => {
        event.preventDefault();
        setSubmitted(true);
        if (!sku.trim() || !validOptions || !isMoney(price) || Number(price) < 0.01 || Number(price) > 100000) return;
        void editAction.run(() => productVariantService.update(storeId, productId, variant.id, {
          sku: sku.trim(), options: updatedOptions, basePrice: Number(price),
        }), 'Variante actualizada.').then((result) => { if (result) { setEditing(false); setSubmitted(false); onSaved(); } });
      }}>
        <Field label="SKU" example="POLO-NEG-M" value={sku} maxLength={80}
          onChange={(value) => setSku(value.replace(/[^A-Za-z0-9._-]/g, ''))}
          error={submitted && !sku.trim() ? 'Ingresa un SKU válido.' : ''} required />
        <Field label="Opciones" example="Talla=M, Color=Negro" value={optionsText} maxLength={500}
          onChange={setOptionsText} error={submitted && !validOptions ? 'Revisa el formato y usa nombres distintos.' : ''} required />
        <Field label="Precio de esta variante" example="29.90" rule="money" inputMode="decimal" value={price}
          onChange={setPrice} error={submitted && (!isMoney(price) || Number(price) < 0.01 || Number(price) > 100000) ? 'Ingresa un precio válido.' : ''} required />
        <ErrorNotice message={editAction.error} />
        <Button type="submit" pending={editAction.pending}>Guardar variante</Button>
      </form>}
      <ErrorNotice message={statusAction.error} />
      <form className="inline-form" onSubmit={(event) => {
      event.preventDefault();
      if (!isInteger(quantity, 0)) return;
      void action.run(
        () => inventoryService.variantQuantity(storeId, productId, variant.id, Number(quantity), movementType, stockReason),
        'Existencias actualizadas.',
      ).then((result) => { if (result) onSaved(); });
    }}>
      <Field label="Stock disponible" example="12" rule="digits" inputMode="numeric" value={quantity}
        onChange={setQuantity} error={!isInteger(quantity, 0) ? 'Ingresa cero o un entero positivo.' : ''} />
      <label className="field"><span>Tipo de movimiento</span><select value={movementType} onChange={(event) => setMovementType(event.target.value)}>
        <option value="STOCK_ENTRY">Entrada al inventario</option><option value="ADJUSTMENT">Ajuste de conteo</option>
        <option value="RETURN">Devolución</option><option value="DAMAGE">Merma o daño</option><option value="LOSS">Pérdida</option>
      </select></label>
      <Field label="Motivo" example="Reposición de mercadería" value={stockReason} maxLength={500} onChange={setStockReason} />
      <Button type="submit" variant="secondary" pending={action.pending}>Guardar stock</Button>
      <ErrorNotice message={action.error} />
      </form>
    </div>
  );
}
