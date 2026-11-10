import { useRef, useState } from 'react';
import { toast } from 'sonner';
import { errorMessage } from '@/utils/errors';

/** Ejecuta una acción, evita envíos repetidos y muestra el resultado con Sonner. */
export function useAction() {
  const [pending, setPending] = useState(false);
  const [error, setError] = useState('');
  const busy = useRef(false);
  async function run<T>(operation: () => Promise<T>, success?: string): Promise<T | undefined> {
    if (busy.current) return undefined;
    busy.current = true;
    setPending(true);
    setError('');
    try {
      const result = await operation();
      if (success) toast.success(success);
      return result;
    } catch (cause) {
      const message = errorMessage(cause);
      setError(message);
      toast.error(message);
      return undefined;
    } finally {
      busy.current = false;
      setPending(false);
    }
  }
  return { run, pending, error };
}
