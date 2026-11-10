import type { DocumentType } from '@/utils/validation';
/** Datos de autenticación; contraseña transmitida únicamente al servidor por HTTPS. */
export interface Credentials {
  email: string;
  password: string;
}
/** Registro de cliente o propietario; confirmación solo existe en la vista. */
export interface Registration extends Credentials {
  fullName: string;
  phoneNumber?: string;
  documentType?: DocumentType;
  documentNumber?: string;
}
/** Token recibido al iniciar sesión o crear una cuenta. */
export interface TokenResponse {
  token: string;
}
