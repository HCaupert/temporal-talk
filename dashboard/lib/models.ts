export type PaymentStatus = "PENDING" | "AUTHORIZED" | "CAPTURED";

export type ShippingStatus =
  | "PENDING"
  | "AWAITING_PREPARATION"
  | "PREPARED"
  | "AWAITING_SHIPPING"
  | "SHIPPED";

export interface OrderListElement {
  id: string;
  date: string;
  shippingStatus: ShippingStatus;
}

export interface Order {
  id: string;
  payment: Payment;
  creationDate: string;
  article: Article;
  shipping: Shipping;
}

export interface Article {
  id: string;
  image: string;
  name: string;
  price: number;
}

export interface Shipping {
  status: ShippingStatus;
  trackingNumber?: string;
  method: string;
  address: Address;
  receiver: Receiver;
}

export interface Address {
  street: string;
  city: string;
  iso3CountryCode: string;
}

export interface Receiver {
  firstName: string;
  lastName: string;
}

export interface Payment {
  id: string;
  status: PaymentStatus;
}
