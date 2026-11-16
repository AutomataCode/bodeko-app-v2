import { useStore } from '@/context/StoreContext';
import { AuthForm } from './components/AuthForm';
import { storeInitials } from '@/core/layouts/StoreLayout';

/** Acceso y registro de clientes con la identidad de la tienda, sin marca de la plataforma. */
export function StoreAuthPage({ register = false }: { register?: boolean }) {
  const store = useStore();
  return (
    <div className="auth-layout">
      <div className="auth-copy">
        <span className="eyebrow">{store.name.toUpperCase()}</span>
        <h1>{register ? 'Únete a nuestra tienda.' : 'Qué bueno verte de nuevo.'}</h1>
        <p>
          {register
            ? `Crea tu cuenta en ${store.name} para comprar, pagar en línea y seguir tus pedidos.`
            : `Ingresa con tu cuenta de ${store.name} para continuar con tu compra.`}
        </p>
        <div className="auth-mark">
          <span className="store-monogram store-monogram-lg" aria-hidden="true">
            {storeInitials(store.name)}
          </span>
          <span>{store.name}</span>
        </div>
      </div>
      <section className="card auth-card">
        <h2>{register ? 'Crea tu cuenta' : 'Iniciar sesión'}</h2>
        <p className="muted">
          {register
            ? `Tu cuenta es exclusiva de ${store.name}.`
            : `Usa el correo con el que te registraste en ${store.name}.`}
        </p>
        <AuthForm key={register ? 'register' : 'login'} register={register} store={store} />
      </section>
    </div>
  );
}
