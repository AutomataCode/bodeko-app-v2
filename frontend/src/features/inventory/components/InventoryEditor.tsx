import { useState } from 'react';
import { Button, ErrorNotice, Field } from '@/core/components';
import { useAction } from '@/core/hooks/useAction';
import { isInteger } from '@/utils/validation';
import { dateTime } from '@/utils/format';
import { inventoryService } from '../services/inventoryService';
import type { Inventory } from '../types';
/** Inventario consultado y función para actualizarlo después de guardar. */
export interface InventoryEditorProps {
  storeId: string;
  inventory: Inventory;
  onSaved: () => void;
}
/** Permite guardar la cantidad y el umbral por separado. */
export function InventoryEditor({ storeId, inventory, onSaved }: InventoryEditorProps) {
  const [quantity, setQuantity] = useState(String(inventory.quantity));
  const [threshold, setThreshold] = useState(String(inventory.lowStockThreshold));
  const action = useAction();
  return (
    <section className="card form-stack">
      <div className="section-heading">
        <h2>Stock disponible</h2>
        <span className={`badge ${inventory.lowStock ? 'badge-pending' : 'badge-active'}`}>
          {inventory.lowStock ? 'Stock bajo' : 'Stock saludable'}
        </span>
      </div>
      <p className="muted">Última actualización: {dateTime(inventory.updatedAt)}</p>
      <ErrorNotice message={action.error} />
      <form
        className="inline-form"
        onSubmit={(event) => {
          event.preventDefault();
          if (isInteger(quantity))
            void action
              .run(
                () => inventoryService.quantity(storeId, inventory.productId, Number(quantity)),
                'Cantidad actualizada.',
              )
              .then((result) => {
                if (result) onSaved();
              });
        }}
      >
        <Field
          label="Cantidad de unidades"
          example="20"
          rule="digits"
          inputMode="numeric"
          maxLength={10}
          value={quantity}
          onChange={setQuantity}
          required
          error={!isInteger(quantity) ? 'Ingresa un entero entre 0 y 2147483647.' : ''}
        />
        <Button type="submit" pending={action.pending} disabled={!isInteger(quantity)}>
          Guardar cantidad
        </Button>
      </form>
      <form
        className="inline-form"
        onSubmit={(event) => {
          event.preventDefault();
          if (isInteger(threshold))
            void action
              .run(
                () => inventoryService.threshold(storeId, inventory.productId, Number(threshold)),
                'Umbral actualizado.',
              )
              .then((result) => {
                if (result) onSaved();
              });
        }}
      >
        <Field
          label="Umbral de stock bajo"
          example="5"
          rule="digits"
          inputMode="numeric"
          maxLength={10}
          value={threshold}
          onChange={setThreshold}
          required
          error={!isInteger(threshold) ? 'Ingresa un entero no negativo.' : ''}
        />
        <Button
          type="submit"
          variant="secondary"
          pending={action.pending}
          disabled={!isInteger(threshold)}
        >
          Guardar umbral
        </Button>
      </form>
    </section>
  );
}
