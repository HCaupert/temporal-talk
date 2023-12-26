import { Row } from "@/lib/fetchOrders";
import { cn } from "@/lib/utils";

export function ShippingCell({ row }: { row: Row }) {
  const shippingStatus = row.shippingStatus;
  const isPendingAction = shippingStatus.startsWith("AWAITING");
  const isCompleted = shippingStatus === "SHIPPED";
  const isIdle = shippingStatus === "PENDING" || shippingStatus === "PREPARED";
  return (
    <div
      className={cn(
        "px-2 rounded-full min-h-6 w-fit flex items-center",
        isIdle && "bg-blue-100 text-blue-950",
        isPendingAction && "bg-amber-200 text-amber-950",
        isCompleted && "bg-green-100 text-green-900",
      )}
    >
      {row.shippingStatusLabel}
    </div>
  );
}
