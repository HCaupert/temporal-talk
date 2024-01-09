"use client";

import ShippingTable from "@/components/business/ShippingTable";
import { fetchOrders } from "@/lib/fetchOrders";
import { useQuery } from "@tanstack/react-query";
import { HomeTools } from "@/components/business/TalkTools";

export default function Home() {
  const query = useQuery({
    queryKey: ["orders"],
    queryFn: fetchOrders,
    refetchInterval: 1000,
  });

  return (
    <main className="flex min-h-dvh flex-col items-center container">
      <HomeTools />
      <h1 className="self-start my-10 text-2xl">Vido Game Logistics</h1>
      {query.data && <ShippingTable rows={query.data} />}
    </main>
  );
}
