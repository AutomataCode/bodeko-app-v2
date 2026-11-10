/** Oferta temporal serializada con offset ISO 8601. */
export interface Offer {
  productId: string;
  offerPrice: number;
  startsAt: string;
  endsAt: string;
  active: boolean;
  createdAt: string;
}
/** Precio y ventana temporal de una nueva oferta. */
export interface OfferInput {
  offerPrice: number;
  startsAt: string;
  endsAt: string;
}
