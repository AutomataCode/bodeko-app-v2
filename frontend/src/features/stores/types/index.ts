import type { Id } from '@/core/types';
/** Información pública de una tienda disponible para visitantes. */
export interface PublicStore {
  id: Id;
  name: string;
  subdomain: string;
  currency: string;
  taxRate: number;
}
/** Tienda destacada por cantidad histórica de órdenes vendidas. */
export interface TopSellingStore extends PublicStore {
  salesCount: number;
}
/** Información fiscal privada visible únicamente al propietario. */
export interface Store extends PublicStore {
  taxId: string;
  legalName: string;
  createdAt: string;
}
/** Datos obligatorios al crear una tienda. */
export interface StoreInput {
  name: string;
  taxId: string;
  legalName: string;
  subdomain: string;
}
