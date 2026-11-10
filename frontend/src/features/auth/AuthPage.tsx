import { Store } from 'lucide-react';
import { AuthForm } from './components/AuthForm';
import { APP_NAME } from '@/config';
/** Vista pública de acceso y registro, centrada y adaptable a dispositivos móviles. */
export function AuthPage({ register = false }: { register?: boolean }) {
  return (
    <div className="auth-layout">
      <div className="auth-copy">
        <span className="eyebrow">COMERCIO LOCAL, CONECTADO</span>
        <h1>
          Una cuenta.
          <br />
          Muchas posibilidades.
        </h1>
        <p>Descubre tiendas, encuentra lo que necesitas y lleva tu negocio al siguiente nivel.</p>
        <div className="auth-mark">
          <Store size={72} />
          <span>{APP_NAME}</span>
        </div>
      </div>
      <section className="card auth-card">
        <h2>{register ? 'Crea tu cuenta' : 'Te damos la bienvenida'}</h2>
        <p className="muted">
          {register
            ? 'Empieza a comprar o a gestionar tu tienda.'
            : 'Ingresa para continuar con tu cuenta.'}
        </p>
        <AuthForm key={register ? 'register' : 'login'} register={register} />
      </section>
    </div>
  );
}
