import { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Search } from 'lucide-react';
import {
  Button,
  Empty,
  ErrorNotice,
  Field,
  Loading,
  PageHeader,
  Pagination,
  Select,
} from '@/core/components';
import { useStore } from '@/context/StoreContext';
import { useCategories } from '@/features/categories';
import { useCatalog } from './hooks/useCatalog';
import { ProductCard } from './components/ProductCard';
/** Muestra el catálogo y guarda los filtros en la URL. */
export function CatalogPage() {
  const store = useStore();
  const [query, setQuery] = useSearchParams();
  const [draftSearch, setDraftSearch] = useState(query.get('buscar') || '');
  const search = query.get('buscar') || '';
  const categoryId = query.get('categoria') || '';
  const inStock = query.get('stock') === 'si';
  const page = Math.max(0, Math.floor(Number(query.get('pagina')) || 0));
  const resource = useCatalog(store.id, { search, categoryId, inStock, page });
  const categories = useCategories(store.id, true);
  function filter(key: string, value: string) {
    const next = new URLSearchParams(query);
    value ? next.set(key, value) : next.delete(key);
    if (key !== 'pagina') next.delete('pagina');
    setQuery(next);
  }
  return (
    <>
      <PageHeader title={store.name} description="Descubre productos elegidos para tu día a día." />
      <div className="card filters">
        <form
          className="search-form"
          onSubmit={(event) => {
            event.preventDefault();
            filter('buscar', draftSearch.trim());
          }}
        >
          <Field
            label="Buscar en la tienda"
            example="Café"
            maxLength={200}
            value={draftSearch}
            onChange={setDraftSearch}
          />
          <Button type="submit" variant="secondary" aria-label="Buscar productos">
            <Search size={18} />
          </Button>
        </form>
        <Select
          label="Categoría"
          value={categoryId}
          onChange={(value) => filter('categoria', value)}
          options={[
            { value: '', label: 'Todas las categorías' },
            ...(categories.data || []).map((item) => ({ value: item.id, label: item.name })),
          ]}
        />
        <label className="checkbox">
          <input
            type="checkbox"
            checked={inStock}
            onChange={(event) => filter('stock', event.target.checked ? 'si' : '')}
          />
          Solo en stock
        </label>
      </div>
      <ErrorNotice message={categories.error} retry={categories.reload} />
      <ErrorNotice message={resource.error} retry={resource.reload} />
      {resource.loading ? (
        <Loading />
      ) : resource.data?.content.length === 0 ? (
        <Empty title="No encontramos productos">
          <p>Prueba otra búsqueda o cambia los filtros.</p>
        </Empty>
      ) : (
        <>
          <p className="muted">{resource.data?.totalElements ?? 0} productos</p>
          <div className="catalog-grid">
            {resource.data?.content.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
          <Pagination
            page={page}
            totalPages={resource.data?.totalPages || 0}
            onChange={(value) => filter('pagina', String(value))}
          />
        </>
      )}
    </>
  );
}
