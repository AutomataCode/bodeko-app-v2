import { useState, type FormEvent } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { Button, ErrorNotice, Field, Select } from '@/core/components';
import { isDocument, isEmail, type DocumentType } from '@/utils/validation';
import { useAuth } from '../hooks/useAuth';

/** register indica si se muestra el registro o el inicio de sesión. */
export interface AuthFormProps {
  register: boolean;
}
/** Formulario compartido de inicio de sesión y registro. */
export function AuthForm({ register }: AuthFormProps) {
  const navigate = useNavigate();
  const [query] = useSearchParams();
  const auth = useAuth();
  const [owner, setOwner] = useState(false);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirm, setConfirm] = useState('');
  const [phone, setPhone] = useState('');
  const [documentType, setDocumentType] = useState('');
  const [documentNumber, setDocumentNumber] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const errors = {
    name:
      register && name.trim().length < 2 ? 'Ingresa tu nombre completo (mínimo 2 caracteres).' : '',
    email: !isEmail(email) ? 'Ingresa un correo válido.' : '',
    password:
      password.length < (register ? 8 : 1)
        ? register
          ? 'Usa al menos 8 caracteres.'
          : 'Ingresa tu contraseña.'
        : register && new TextEncoder().encode(password).length > 72
          ? 'La contraseña es demasiado larga. Usa una más corta.'
          : '',
    confirm: register && confirm !== password ? 'Las contraseñas no coinciden.' : '',
    phone:
      register && ((owner && !phone) || (phone && !/^\+[1-9]\d{7,14}$/.test(phone)))
        ? 'Usa el código de país: +51987654321.'
        : '',
    document:
      register && !owner && documentType && !isDocument(documentNumber, documentType)
        ? 'Revisa la longitud y el formato del documento.'
        : '',
  };
  async function submit(event: FormEvent) {
    event.preventDefault();
    setSubmitted(true);
    if (Object.values(errors).some(Boolean)) return;
    const body = {
      email: email.trim().toLowerCase(),
      password,
      fullName: name.trim(),
      ...(phone ? { phoneNumber: phone } : {}),
      ...(!owner && documentType
        ? { documentType: documentType as DocumentType, documentNumber }
        : {}),
    };
    const result = register ? await auth.register(body, owner) : await auth.login(body);
    if (result) {
      const next = query.get('next');
      navigate(
        next?.startsWith('/') && !next.startsWith('//')
          ? next
          : result.role === 'ADMIN'
            ? '/admin'
            : '/',
        { replace: true },
      );
    }
  }
  return (
    <form onSubmit={(event) => void submit(event)} noValidate className="form-stack">
      <ErrorNotice message={auth.error} />
      {register && (
        <>
          <Select
            label="Tipo de cuenta"
            value={owner ? 'owner' : 'customer'}
            onChange={(value) => setOwner(value === 'owner')}
            options={[
              { value: 'customer', label: 'Cliente: quiero comprar' },
              { value: 'owner', label: 'Propietario: quiero vender' },
            ]}
            hint="Ejemplo: Cliente para comprar en las tiendas."
          />
          <Field
            label="Nombre completo"
            example="María García López"
            value={name}
            onChange={setName}
            rule="name"
            maxLength={150}
            autoComplete="name"
            required
            error={submitted ? errors.name : ''}
          />
        </>
      )}
      <Field
        label="Correo electrónico"
        example="maria@correo.com"
        type="email"
        rule="email"
        value={email}
        onChange={setEmail}
        maxLength={255}
        autoComplete="email"
        required
        error={submitted ? errors.email : ''}
      />
      <Field
        label="Contraseña"
        example={
          register ? 'Una frase segura de al menos 8 caracteres' : 'No compartas tu contraseña con nadie'
        }
        type="password"
        value={password}
        onChange={setPassword}
        autoComplete={register ? 'new-password' : 'current-password'}
        required
        error={submitted ? errors.password : ''}
      />
      {register && (
        <>
          <Field
            label="Confirmar contraseña"
            example="Repite exactamente tu contraseña"
            type="password"
            value={confirm}
            onChange={setConfirm}
            autoComplete="new-password"
            required
            error={submitted ? errors.confirm : ''}
          />
          <Field
            label={`Teléfono${owner ? '' : ' (opcional)'}`}
            example="+51987654321"
            type="tel"
            rule="phone"
            value={phone}
            onChange={setPhone}
            maxLength={16}
            autoComplete="tel"
            required={owner}
            error={submitted ? errors.phone : ''}
          />
          {!owner && (
            <>
              <Select
                label="Documento (opcional)"
                value={documentType}
                onChange={(value) => {
                  setDocumentType(value);
                  setDocumentNumber('');
                }}
                options={[
                  { value: '', label: 'No agregar documento' },
                  { value: 'DNI', label: 'DNI' },
                  { value: 'RUC', label: 'RUC' },
                  { value: 'CE', label: 'Carné de extranjería' },
                  { value: 'PASSPORT', label: 'Pasaporte' },
                ]}
                hint="Ejemplo: DNI para un documento peruano."
              />
              {documentType && (
                <Field
                  label="Número de documento"
                  example={
                    documentType === 'DNI'
                      ? '12345678'
                      : documentType === 'RUC'
                        ? '20123456789'
                        : 'AB123456'
                  }
                  rule={['DNI', 'RUC'].includes(documentType) ? 'digits' : 'document'}
                  maxLength={documentType === 'DNI' ? 8 : documentType === 'RUC' ? 11 : 20}
                  value={documentNumber}
                  onChange={setDocumentNumber}
                  required
                  error={submitted ? errors.document : ''}
                />
              )}
            </>
          )}
        </>
      )}
      <Button type="submit" pending={auth.pending}>
        {register ? 'Crear cuenta' : 'Iniciar sesión'}
      </Button>
      <p className="muted">
        {register ? '¿Ya tienes cuenta? ' : '¿Aún no tienes cuenta? '}
        <Link
          to={`${register ? '/ingresar' : '/registro'}${query.toString() ? `?${query.toString()}` : ''}`}
        >
          {register ? 'Inicia sesión' : 'Regístrate'}
        </Link>
      </p>
    </form>
  );
}
