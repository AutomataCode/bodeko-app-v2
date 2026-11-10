import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import { ErrorBoundary } from '@/core/components/ErrorBoundary';
import '@/assets/styles.css';

/** Punto de entrada de la SPA; StrictMode detecta efectos sin limpieza en desarrollo. */
const root = document.getElementById('root');
if (!root) throw new Error('No se encontró el elemento raíz de la aplicación.');
createRoot(root).render(
  <StrictMode>
    <ErrorBoundary>
      <App />
    </ErrorBoundary>
  </StrictMode>,
);
