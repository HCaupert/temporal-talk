import { OrderInfo } from "@/lib/models";

export async function fetchImage(search: string) {
  const safeSearch = encodeURIComponent(search);
  const response = await fetch(
    `https://api.bing.microsoft.com/v7.0/images/search?q=${safeSearch}`,
    {
      headers: {
        "Ocp-Apim-Subscription-Key": process.env.NEXT_PUBLIC_BING_KEY as string,
      },
    },
  );
  const json = await response.json();
  return json.value[0].contentUrl;
}

export async function fetchOrder(id: string) {
  const response = await fetch(`http://localhost:8080/api/orders/${id}`);
  return (await response.json()) as OrderInfo;
}
