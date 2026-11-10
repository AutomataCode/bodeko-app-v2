import { useState, type FormEvent } from 'react';
import { Button, ErrorNotice, Field } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { categoryService } from '../services/categoryService';
import type { Category } from '../types';
/** Datos del editor. Sin category se crea una categoría nueva. */
export interface CategoryFormProps {
  storeId: string;
  category?: Category;
  onSaved: () => void;
}
/** Editor de nombre con límite de 100 caracteres definido en PostgreSQL. */
export function CategoryForm({ storeId, category, onSaved }: CategoryFormProps) {
  const [name, setName] = useState(category?.name || '');
  const [submitted, setSubmitted] = useState(false);
  const action = useAction();
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (!name.trim()) return;
    const result = await action.run(
      () => categoryService.save(storeId, name.trim(), category?.id),
      'Categoría guardada.',
    );
    if (result) onSaved();
  }
  return (
    <form noValidate className="form-stack" onSubmit={(event) => void submit(event)}>
      <ErrorNotice message={action.error} />
      <Field
        label="Nombre de categoría"
        example="Abarrotes"
        value={name}
        onChange={setName}
        maxLength={100}
        required
        error={submitted && !name.trim() ? 'Ingresa un nombre de categoría.' : ''}
      />
      <Button type="submit" pending={action.pending}>
        Guardar categoría
      </Button>
    </form>
  );
}
