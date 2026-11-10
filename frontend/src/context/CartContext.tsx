import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useRef,
  useState,
  type ReactNode,
} from 'react';
import { toast } from 'sonner';
import { useSession } from './SessionContext';
import { useStore } from './StoreContext';
import { cartService } from '@/features/cart/services/cartService';
import type { Cart } from '@/features/cart/types';
import { errorMessage } from '@/utils/errors';

/** Comparte el carrito entre las páginas de la tienda actual. */
interface CartState {
  cart: Cart | null;
  loading: boolean;
  pending: boolean;
  error: string;
  reload: () => void;
  add: (id: string, quantity?: number) => Promise<boolean>;
  update: (id: string, quantity: number) => Promise<boolean>;
  remove: (id: string) => Promise<boolean>;
}
const CartContext = createContext<CartState | null>(null);
/** Carga el carrito e ignora respuestas de una cuenta o tienda anterior. */
export function CartProvider({ children }: { children: ReactNode }) {
  const { user } = useSession();
  const store = useStore();
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState(false);
  const [pending, setPending] = useState(false);
  const [error, setError] = useState('');
  const [revision, setRevision] = useState(0);
  const generation = useRef(0);
  const busy = useRef(false);
  const reload = useCallback(() => setRevision((value) => value + 1), []);
  useEffect(() => {
    const current = ++generation.current;
    setCart(null);
    setError('');
    setLoading(user?.role === 'CUSTOMER');
    if (user?.role !== 'CUSTOMER') return;
    void cartService
      .get(store.id)
      .then((result) => {
        if (generation.current === current) setCart(result);
      })
      .catch((cause: unknown) => {
        if (generation.current === current) setError(errorMessage(cause));
      })
      .finally(() => {
        if (generation.current === current) setLoading(false);
      });
    return () => {
      generation.current++;
    };
  }, [user?.id, user?.role, store.id, revision]);
  async function mutate(operation: () => Promise<Cart>, success: string) {
    if (busy.current) return false;
    if (user?.role !== 'CUSTOMER') {
      toast.error('Inicia sesión con una cuenta de cliente para comprar.');
      return false;
    }
    const current = generation.current;
    busy.current = true;
    setPending(true);
    setError('');
    try {
      const result = await operation();
      if (generation.current !== current) return false;
      setCart(result);
      toast.success(success);
      return true;
    } catch (cause) {
      if (generation.current === current) {
        const message = errorMessage(cause);
        setError(message);
        toast.error(message);
      }
      return false;
    } finally {
      busy.current = false;
      setPending(false);
    }
  }
  return (
    <CartContext.Provider
      value={{
        cart,
        loading,
        pending,
        error,
        reload,
        add: (id, quantity = 1) =>
          mutate(() => cartService.add(store.id, id, quantity), 'Producto agregado al carrito.'),
        update: (id, quantity) =>
          mutate(() => cartService.update(store.id, id, quantity), 'Cantidad actualizada.'),
        remove: (id) =>
          mutate(() => cartService.remove(store.id, id), 'Producto eliminado del carrito.'),
      }}
    >
      {children}
    </CartContext.Provider>
  );
}
/** Permite consultar y modificar el carrito de la tienda actual. */
export function useCart(): CartState {
  const value = useContext(CartContext);
  if (!value) throw new Error('No se encontró el proveedor de carrito.');
  return value;
}
