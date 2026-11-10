import { useState } from 'react';
import { Pencil, Plus, Trash2 } from 'lucide-react';
import { useOutletContext } from 'react-router-dom';
import { Button, Empty, ErrorNotice, Loading, Modal, PageHeader } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import type { Store } from '@/features/stores';
import { useCategories } from './hooks/useCategories';
import { categoryService } from './services/categoryService';
import { CategoryForm } from './components/CategoryForm';
import type { Category } from './types';
/** Permite crear, editar y eliminar categorías de la tienda. */
export function CategoriesPage() {
  const store = useOutletContext<Store>();
  const resource = useCategories(store.id);
  const action = useAction();
  const [editor, setEditor] = useState<Category | 'new' | null>(null);
  const [deleting, setDeleting] = useState<Category | null>(null);
  return (
    <>
      <PageHeader
        title="Categorías"
        description="Organiza tu catálogo para facilitar cada compra."
        actions={
          <Button onClick={() => setEditor('new')}>
            <Plus size={16} />
            Agregar categoría
          </Button>
        }
      />
      <ErrorNotice message={resource.error} retry={resource.reload} />
      <ErrorNotice message={action.error} />
      {resource.loading ? (
        <Loading />
      ) : !resource.data?.length ? (
        <Empty title="Tu catálogo empieza con una categoría" />
      ) : (
        <div className="card table-wrap">
          <table>
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {resource.data.map((category) => (
                <tr key={category.id}>
                  <td>{category.name}</td>
                  <td className="actions">
                    <Button
                      variant="secondary"
                      aria-label={`Editar ${category.name}`}
                      onClick={() => setEditor(category)}
                    >
                      <Pencil size={16} />
                    </Button>
                    <Button
                      variant="secondary"
                      aria-label={`Eliminar ${category.name}`}
                      onClick={() => setDeleting(category)}
                    >
                      <Trash2 size={16} />
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      {editor && (
        <Modal
          title={editor === 'new' ? 'Nueva categoría' : 'Editar categoría'}
          onClose={() => setEditor(null)}
        >
          <CategoryForm
            storeId={store.id}
            category={editor === 'new' ? undefined : editor}
            onSaved={() => {
              setEditor(null);
              resource.reload();
            }}
          />
        </Modal>
      )}
      {deleting && (
        <Modal title="Eliminar categoría" onClose={() => setDeleting(null)}>
          <p>
            ¿Eliminar «{deleting.name}»? Si tiene productos asociados, primero tendrás que
            reasignarlos.
          </p>
          <Button
            variant="danger"
            pending={action.pending}
            onClick={() => {
              void action
                .run(() => categoryService.remove(store.id, deleting.id), 'Categoría eliminada.')
                .then((done) => {
                  if (done) {
                    setDeleting(null);
                    resource.reload();
                  }
                });
            }}
          >
            Eliminar categoría
          </Button>
        </Modal>
      )}
    </>
  );
}
