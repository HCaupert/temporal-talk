import { OrderListElement } from "@/lib/models";

//Funky reflexion
export type Row = Awaited<ReturnType<typeof fetchOrders>>[number];

const shippingStatusLabels = {
  PENDING: "En attente du paiement",
  AWAITING_PREPARATION: "À préparer",
  PREPARED: "Prête pour expédition",
  AWAITING_SHIPPING: "À envoyer",
  SHIPPED: "Envoyée",
} as const;

export async function fetchOrders() {
  const response = await fetch("http://localhost:8080/api/orders");
  const json = (await response.json()) as OrderListElement[];
  const rows = json.map((element) => ({
    ...element,
    date: new Date(element.date),
    shippingStatusLabel: shippingStatusLabels[element.shippingStatus],
  }));
  return rows;
}
