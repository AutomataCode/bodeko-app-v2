/** Identificador UUID recibido de la API. */
export type Id = string;
/** Resultados de una página y datos para navegar entre páginas. */
export interface Page<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
/** Roles exactos de Spring Security. */
export type Role = 'ADMIN' | 'CUSTOMER';
/** Usuario autenticado; nunca contiene contraseña. */
export interface User {
  id: Id;
  fullName: string;
  email: string;
  role: Role;
  phoneNumber: string | null;
}
/** Opciones de los selectores de los formularios. */
export interface Option {
  value: string;
  label: string;
}
