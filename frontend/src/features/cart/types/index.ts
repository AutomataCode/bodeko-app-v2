/** Producto del carrito con los precios calculados por el servidor. */
export interface CartItem {
  productId: string;
  name: string;
  imageUrl: string | null;
  quantity: number;
  listUnitPrice: number;
  chargedUnitPrice: number;
  itemSubtotal: number;
}
/** Carrito abierto por cliente y tienda; id es null cuando aún está vacío. */
export interface Cart {
  id: string | null;
  storeId: string;
  items: CartItem[];
  totalQuantity: number;
  subtotal: number;
}
