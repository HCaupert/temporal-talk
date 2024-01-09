import { Order } from "@/lib/models";

export async function fetchOrder(id: string) {
  const response = await fetch(`http://localhost:8080/api/orders/${id}`);
  return (await response.json()) as Order;
}
