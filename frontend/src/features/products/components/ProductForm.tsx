import { useState, type FormEvent } from 'react';
import { Button, ErrorNotice, Field, Loading, Select } from '@/core/components';
import { useCategories } from '@/features/categories';
import { useAction } from '@/core/hooks/useAction';
import { isHttpUrl, isMoney } from '@/utils/validation';
import { productService } from '../services/productService';
import type { Product } from '../types';
/** Datos del editor. Sin product se crea un producto en borrador. */
export interface ProductFormProps {
  storeId: string;
  product?: Product;
  onSaved: () => void;
}
/** Editor completo con validaciones numéricas y selección de categoría real. */
export function ProductForm({ storeId, product, onSaved }: ProductFormProps) {
  const categories = useCategories(storeId);
  const action = useAction();
  const [name, setName] = useState(product?.name || '');
  const [description, setDescription] = useState(product?.description || '');
  const [price, setPrice] = useState(product?.basePrice.toString() || '');
  const [imageUrl, setImageUrl] = useState(product?.imageUrl || '');
  const [categoryId, setCategoryId] = useState(product?.categoryId || '');
  const [taxExempt, setTaxExempt] = useState(product?.taxExempt || false);
  const [submitted, setSubmitted] = useState(false);
  const errors = {
    name: !name.trim() ? 'Ingresa el nombre del producto.' : '',
    price:
      !isMoney(price) || Number(price) < 0.01 || Number(price) > 100000
        ? 'Ingresa un precio entre 0.01 y 100000.00 con hasta dos decimales.'
        : '',
    image:
      imageUrl && !isHttpUrl(imageUrl) ? 'Ingresa una URL que empiece con https:// o http://.' : '',
  };
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (Object.values(errors).some(Boolean)) return;
    const result = await action.run(
      () =>
        productService.save(
          storeId,
          {
            name: name.trim(),
            description: description.trim(),
            basePrice: Number(price),
            imageUrl: imageUrl || null,
            categoryId: categoryId || null,
            taxExempt,
          },
          product?.id,
        ),
      'Producto guardado.',
    );
    if (result) onSaved();
  }
  return (
    <form noValidate className="form-stack" onSubmit={(event) => void submit(event)}>
      <ErrorNotice message={action.error} />
      <Field
        label="Nombre del producto"
        example="Café de especialidad 250 g"
        required
        maxLength={200}
        value={name}
        onChange={setName}
        error={submitted ? errors.name : ''}
      />
      <Field
        label="Descripción (opcional)"
        example="Café tostado de origen peruano"
        maxLength={5000}
        value={description}
        onChange={setDescription}
      />
      <Field
        label="Precio base (S/)"
        example="25.90"
        rule="money"
        inputMode="decimal"
        value={price}
        onChange={setPrice}
        required
        error={submitted ? errors.price : ''}
      />
      <Field
        label="URL de imagen (opcional)"
        example="https://mitienda.com/cafe.jpg"
        maxLength={500}
        type="url"
        value={imageUrl}
        onChange={setImageUrl}
        error={submitted ? errors.image : ''}
      />
      <ErrorNotice message={categories.error} retry={categories.reload} />
      {categories.loading ? (
        <Loading />
      ) : (
        <Select
          label="Categoría"
          value={categoryId}
          onChange={setCategoryId}
          options={[
            { value: '', label: 'Sin categoría' },
            ...(categories.data || []).map((item) => ({ value: item.id, label: item.name })),
          ]}
          hint="Ejemplo: Abarrotes."
        />
      )}
      <label className="checkbox">
        <input
          type="checkbox"
          checked={taxExempt}
          onChange={(event) => setTaxExempt(event.target.checked)}
        />
        Producto exonerado de IGV
      </label>
      <Button
        type="submit"
        pending={action.pending}
        disabled={categories.loading || !!categories.error}
      >
        {product ? 'Guardar cambios' : 'Guardar como borrador'}
      </Button>
    </form>
  );
}
