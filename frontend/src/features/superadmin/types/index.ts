/** Cuenta que se puede administrar desde el panel de plataforma. */
export interface ManagedAccount {
  id: string;
  fullName: string;
  email: string;
  phoneNumber: string | null;
  role: 'ADMIN' | 'CUSTOMER';
  active: boolean;
  createdAt: string;
  stores: ManagedStore[];
}
/** Tienda asociada a un vendedor o cliente. */
export interface ManagedStore { id: string; name: string; subdomain: string }
/** Conteos para el resumen inicial. */
export interface PlatformUserSummary { vendors: number; activeVendors: number; customers: number; activeCustomers: number }
/** Tienda disponible para aplicar filtros. */
export interface PlatformStoreOption { id: string; name: string; subdomain: string }
/** Filtros avanzados y paginación enviados al backend. */
export interface AccountFilters {
  search: string;
  active: string;
  storeId: string;
  from: string;
  to: string;
  page: number;
}
