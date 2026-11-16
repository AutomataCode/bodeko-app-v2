import { Store } from 'lucide-react';
import { AuthForm } from './components/AuthForm';
import { APP_NAME } from '@/config';
/** Vista pública de acceso y registro, centrada y adaptable a dispositivos móviles. */
export function AuthPage({ register = false }: { register?: boolean }) {
  return (
    <div className="auth-layout">
      <div className="auth-copy">
        <span className="eyebrow">PARA PROPIETARIOS</span>
        <h1>
          Tu negocio.
          <br />
          Tu propia tienda.
        </h1>
        <p>
          Crea tu tienda en minutos. Tus clientes se registran directamente en ella, con tu nombre y
          tu marca.
        </p>
        <div className="auth-mark">
          <Store size={72} />
          <span>{APP_NAME}</span>
        </div>
      </div>
      <section className="card auth-card">
        <h2>{register ? 'Crea tu cuenta' : 'Te damos la bienvenida'}</h2>
        <p className="muted">
          {register
            ? 'Registra tu cuenta de propietario para crear y gestionar tu tienda.'
            : 'Ingresa para gestionar tu negocio.'}
        </p>
        <AuthForm key={register ? 'register' : 'login'} register={register} />
      </section>
    </div>
  );
}
