import { useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { Empty, ErrorNotice, Loading, PageHeader, Select } from '@/core/components';
import { useProducts } from '@/features/products';
import type { Store } from '@/features/stores';
import { useInventory } from './hooks/useInventory';
import { InventoryEditor } from './components/InventoryEditor';
/** Consulta y actualización de inventario del producto seleccionado. */
export function InventoryPage() {
  const store = useOutletContext<Store>();
  const products = useProducts(store.id);
  const [selected, setSelected] = useState('');
  const inventory = useInventory(store.id, selected);
  return (
    <>
      <PageHeader
        title="Inventario"
        description="Mantén tus existencias al día y anticipa el stock bajo."
      />
      <div className="card">
        <Select
          label="Producto"
          value={selected}
          onChange={setSelected}
          options={[
            { value: '', label: 'Selecciona un producto' },
            ...(products.data || []).map((product) => ({ value: product.id, label: product.name })),
          ]}
          hint="Ejemplo: Café de especialidad 250 g."
        />
      </div>
      <ErrorNotice message={products.error} retry={products.reload} />
      <ErrorNotice message={inventory.error} retry={inventory.reload} />
      {products.loading || inventory.loading ? (
        <Loading />
      ) : inventory.data ? (
        <InventoryEditor
          key={`${selected}-${inventory.data.updatedAt}`}
          storeId={store.id}
          inventory={inventory.data}
          onSaved={inventory.reload}
        />
      ) : (
        <Empty title="Selecciona un producto para gestionar su stock" />
      )}
    </>
  );
}
