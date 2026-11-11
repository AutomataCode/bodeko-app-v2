/** Oferta temporal serializada con offset ISO 8601. */
export interface Offer {
  productId: string;
  offerPrice: number;
  discountPercentage: number | null;
  startsAt: string;
  endsAt: string;
  active: boolean;
  createdAt: string;
}
/** Porcentaje y ventana temporal de una nueva oferta. */
export interface OfferInput {
  discountPercentage: number;
  startsAt: string;
  endsAt: string;
}
