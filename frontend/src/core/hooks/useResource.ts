import { useCallback, useEffect, useRef, useState } from 'react';
import { errorMessage } from '@/utils/errors';

/** Carga datos y descarta respuestas anteriores. Recibe una función estable de carga. */
export function useResource<T>(loader: () => Promise<T>, enabled = true) {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(enabled);
  const [error, setError] = useState('');
  const [version, setVersion] = useState(0);
  const generation = useRef(0);
  const reload = useCallback(() => setVersion((value) => value + 1), []);
  useEffect(() => {
    const current = ++generation.current;
    setData(null);
    setError('');
    setLoading(enabled);
    if (!enabled) return;
    void loader()
      .then((result) => {
        if (generation.current === current) setData(result);
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
  }, [loader, enabled, version]);
  return { data, loading, error, reload };
}
