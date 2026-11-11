import { useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { Pencil, Plus } from 'lucide-react';
import {
  Badge,
  Button,
  Empty,
  ErrorNotice,
  Field,
  Loading,
  Modal,
  PageHeader,
  ProductImage,
  Select,
} from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import type { Store } from '@/features/stores';
import { money } from '@/utils/format';
import { useProducts } from './hooks/useProducts';
import { ProductForm } from './components/ProductForm';
import { productService } from './services/productService';
import type { Product, ProductStatus } from './types';
/** Administración del ciclo borrador/activo/archivado sin destruir historial. */
export function ProductsPage() {
  const store = useOutletContext<Store>();
  const resource = useProducts(store.id);
  const action = useAction();
  const [editor, setEditor] = useState<Product | 'new' | null>(null);
  const [search, setSearch] = useState('');
  const [status, setStatus] = useState('');
  const products = resource.data?.filter(
    (item) =>
      item.name.toLocaleLowerCase().includes(search.toLocaleLowerCase()) &&
      (!status || item.status === status),
  );
  return (
    <>
      <PageHeader
        title="Productos"
        description="Un catálogo cuidado, una mejor experiencia de compra."
        actions={
          <Button onClick={() => setEditor('new')}>
            <Plus size={16} />
            Agregar producto
          </Button>
        }
      />
      <div className="card filters">
        <Field
          label="Buscar producto"
          example="Café"
          value={search}
          maxLength={200}
          onChange={setSearch}
        />
        <Select
          label="Estado"
          value={status}
          onChange={setStatus}
          options={[
            { value: '', label: 'Todos los estados' },
            { value: 'ACTIVE', label: 'Activos' },
            { value: 'INACTIVE', label: 'Inactivos' },
            { value: 'DRAFT', label: 'Borradores' },
            { value: 'ARCHIVED', label: 'Archivados' },
          ]}
        />
      </div>
      <ErrorNotice message={resource.error} retry={resource.reload} />
      <ErrorNotice message={action.error} />
      {resource.loading ? (
        <Loading />
      ) : !products?.length ? (
        <Empty title="No hay productos para mostrar">
          <p>Agrega un producto o cambia los filtros.</p>
        </Empty>
      ) : (
        <div className="card table-wrap">
          <table>
            <thead>
              <tr>
                <th>Producto</th>
                <th>Precio base</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {products.map((product) => (
                <tr key={product.id}>
                  <td>
                    <div className="product-cell">
                      <ProductImage src={product.imageUrl} name={product.name} />
                      <div>
                        <strong>{product.name}</strong>
                        <small>{product.taxExempt ? 'Exonerado de IGV' : 'Afecto a IGV'}</small>
                      </div>
                    </div>
                  </td>
                  <td>{money(product.basePrice, store.currency)}</td>
                  <td>
                    <Badge value={product.status} />
                  </td>
                  <td>
                    <div className="actions">
                      <Button
                        variant="secondary"
                        onClick={() => setEditor(product)}
                        aria-label={`Editar ${product.name}`}
                      >
                        <Pencil size={16} />
                      </Button>
                      <select
                        aria-label={`Estado de ${product.name}`}
                        value={product.status}
                        disabled={action.pending}
                        onChange={(event) => {
                          void action
                            .run(
                              () =>
                                productService.status(
                                  store.id,
                                  product.id,
                                  event.target.value as ProductStatus,
                                ),
                              'Estado actualizado.',
                            )
                            .then((result) => {
                              if (result) resource.reload();
                            });
                        }}
                      >
                        <option value="DRAFT">Borrador</option>
                        <option value="ACTIVE">Activo</option>
                        <option value="INACTIVE">Inactivo</option>
                        <option value="ARCHIVED">Archivado</option>
                      </select>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      {editor && (
        <Modal
          title={editor === 'new' ? 'Agregar producto' : 'Editar producto'}
          onClose={() => setEditor(null)}
        >
          <ProductForm
            storeId={store.id}
            product={editor === 'new' ? undefined : editor}
            onSaved={() => {
              setEditor(null);
              resource.reload();
            }}
          />
        </Modal>
      )}
    </>
  );
}
