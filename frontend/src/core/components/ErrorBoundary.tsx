import { Component, type ErrorInfo, type ReactNode } from 'react';
/** Contenido que se mostrará si no ocurre un error de pantalla. */
interface ErrorBoundaryProps {
  children: ReactNode;
}
/** Indica si ocurrió un error al mostrar la pantalla. */
interface ErrorBoundaryState {
  failed: boolean;
}
/** Muestra un mensaje de recuperación si falla la pantalla. */
export class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  state: ErrorBoundaryState = { failed: false };
  static getDerivedStateFromError(): ErrorBoundaryState {
    return { failed: true };
  }
  componentDidCatch(_error: Error, _info: ErrorInfo) {
    /* El mensaje de recuperación evita mostrar detalles internos. */
  }
  render() {
    return this.state.failed ? (
      <main className="main-container">
        <h1>No pudimos mostrar esta página</h1>
        <p>Recarga para volver a intentarlo.</p>
        <button className="button primary" onClick={() => window.location.reload()}>
          Recargar página
        </button>
      </main>
    ) : (
      this.props.children
    );
  }
}
