import {
  useEffect,
  useId,
  useRef,
  useState,
  type ButtonHTMLAttributes,
  type InputHTMLAttributes,
  type ReactNode,
} from 'react';
import { AlertCircle, ImageOff, LoaderCircle, PackageOpen, X } from 'lucide-react';
import { permits, type CharacterRule } from '@/utils/validation';
import type { Option } from '@/core/types';
import { labels } from '@/utils/format';

/** Opciones del botón. pending bloquea el botón mientras se procesa la acción. */
export interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  pending?: boolean;
  variant?: 'primary' | 'secondary' | 'danger';
}
/** Botón reutilizable para toda la aplicación. */
export function Button({ pending, variant = 'primary', children, ...props }: ButtonProps) {
  return (
    <button
      {...props}
      className={`button ${variant} ${props.className || ''}`}
      disabled={props.disabled || pending}
      aria-busy={pending}
    >
      {pending && <LoaderCircle size={16} className="spin" />}
      {children}
    </button>
  );
}
/** Datos del campo: etiqueta, ejemplo, valor y mensaje de error. */
export interface FieldProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'onChange'> {
  label: string;
  example: string;
  error?: string;
  rule?: CharacterRule;
  onChange: (value: string) => void;
}
/** El ejemplo permanece visible aunque el usuario ya haya escrito. */
export function Field({ label, example, error, rule, onChange, ...props }: FieldProps) {
  const id = useId();
  return (
    <div className="field">
      <label htmlFor={id}>
        {label}
        {props.required && <span aria-hidden="true"> *</span>}
      </label>
      <input
        {...props}
        id={id}
        placeholder={props.placeholder || example}
        aria-invalid={!!error}
        aria-describedby={`${id}-help`}
        onChange={(event) => {
          if (permits(event.target.value, rule)) onChange(event.target.value);
        }}
      />
      <small id={`${id}-help`} className={error ? 'field-error' : ''}>
        {error || `Ejemplo: ${example}`}
      </small>
    </div>
  );
}
/** Selector con etiqueta explícita y orientación de uso. */
export interface SelectProps {
  label: string;
  value: string;
  options: Option[];
  onChange: (value: string) => void;
  hint?: string;
  required?: boolean;
}
/** Selector nativo, operable con teclado y lectores de pantalla. */
export function Select({
  label,
  value,
  options,
  onChange,
  hint = 'Selecciona una opción.',
  required,
}: SelectProps) {
  const id = useId();
  return (
    <div className="field">
      <label htmlFor={id}>{label}</label>
      <select
        id={id}
        value={value}
        required={required}
        onChange={(event) => onChange(event.target.value)}
        aria-describedby={`${id}-help`}
      >
        {options.map((item) => (
          <option key={item.value} value={item.value}>
            {item.label}
          </option>
        ))}
      </select>
      <small id={`${id}-help`}>{hint}</small>
    </div>
  );
}
/** Muestra el error hasta que se resuelva o cambie la pantalla. */
export function ErrorNotice({ message, retry }: { message: string; retry?: () => void }) {
  if (!message) return null;
  return (
    <div role="alert" className="notice error">
      <AlertCircle size={20} />
      <span>{message}</span>
      {retry && (
        <Button variant="secondary" onClick={retry}>
          Reintentar
        </Button>
      )}
    </div>
  );
}
/** Indicador accesible para cualquier consulta pendiente. */
export function Loading() {
  return (
    <div className="loading" role="status">
      <LoaderCircle className="spin" /> Cargando información…
    </div>
  );
}
/** Indica que no hay resultados para mostrar. */
export function Empty({ title, children }: { title: string; children?: ReactNode }) {
  return (
    <div className="empty">
      <PackageOpen size={38} />
      <h3>{title}</h3>
      {children}
    </div>
  );
}
/** Muestra el título de la página y sus botones de acción. */
export function PageHeader({
  title,
  description,
  actions,
}: {
  title: string;
  description?: string;
  actions?: ReactNode;
}) {
  return (
    <header className="page-heading">
      <div>
        <h1>{title}</h1>
        {description && <p>{description}</p>}
      </div>
      {actions}
    </header>
  );
}
/** Muestra el estado en español con su color correspondiente. */
export function Badge({ value }: { value: string }) {
  return <span className={`badge badge-${value.toLowerCase()}`}>{labels[value] || value}</span>;
}
/** Muestra la imagen o un icono si falta o no se puede cargar. */
export function ProductImage({ src, name }: { src: string | null; name: string }) {
  const [broken, setBroken] = useState(false);
  useEffect(() => {
    setBroken(false);
  }, [src]);
  return src && /^https?:\/\//i.test(src) && !broken ? (
    <img
      className="product-image"
      src={src}
      alt={name}
      loading="lazy"
      decoding="async"
      onError={() => setBroken(true)}
    />
  ) : (
    <div className="image-fallback" aria-label={`Sin imagen de ${name}`}>
      <ImageOff size={36} />
    </div>
  );
}
/** Permite cambiar de página. La primera página tiene el índice 0. */
export function Pagination({
  page,
  totalPages,
  onChange,
}: {
  page: number;
  totalPages: number;
  onChange: (page: number) => void;
}) {
  if (totalPages <= 1) return null;
  return (
    <nav className="pagination" aria-label="Paginación">
      <Button variant="secondary" disabled={page === 0} onClick={() => onChange(page - 1)}>
        Anterior
      </Button>
      <span>
        Página {page + 1} de {totalPages}
      </span>
      <Button
        variant="secondary"
        disabled={page >= totalPages - 1}
        onClick={() => onChange(page + 1)}
      >
        Siguiente
      </Button>
    </nav>
  );
}
/** Abre un diálogo y permite cerrarlo con la tecla Escape. */
export function Modal({
  title,
  onClose,
  children,
}: {
  title: string;
  onClose: () => void;
  children: ReactNode;
}) {
  const dialog = useRef<HTMLDialogElement>(null);
  const titleId = useId();
  useEffect(() => {
    const element = dialog.current;
    element?.showModal();
    return () => element?.close();
  }, []);
  return (
    <dialog
      ref={dialog}
      className="modal"
      aria-labelledby={titleId}
      onCancel={(event) => {
        event.preventDefault();
        onClose();
      }}
    >
      <div className="modal-title">
        <h2 id={titleId}>{title}</h2>
        <Button variant="secondary" aria-label="Cerrar diálogo" onClick={onClose}>
          <X size={18} />
        </Button>
      </div>
      {children}
    </dialog>
  );
}
