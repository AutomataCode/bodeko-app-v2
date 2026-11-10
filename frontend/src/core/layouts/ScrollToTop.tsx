import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

/** Inicia cada nueva pantalla arriba; los cambios de filtros conservan el desplazamiento. */
export function ScrollToTop() {
  const { pathname } = useLocation();
  useEffect(() => {
    window.scrollTo({ top: 0, left: 0, behavior: 'instant' });
  }, [pathname]);
  return null;
}
