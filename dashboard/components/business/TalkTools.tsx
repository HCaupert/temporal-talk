"use client";

import { Card, CardContent } from "@/components/ui/card";
import { ReactNode } from "react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { DialogClose } from "@/components/ui/dialog";
import { Separator } from "@/components/ui/separator";
import { Order } from "@/lib/models";

export default function TalkTools({
  children,
  className,
}: {
  children: ReactNode;
  className: string;
}) {
  return (
    <Card className={cn("fixed w-56", className)}>
      <CardContent className="flex flex-col text-muted-foreground pt-6 gap-2">
        <div className="flex items-center gap-2">
          <Separator className="w-auto grow" />
          Simuler
          <Separator className="w-auto grow" />
        </div>
        {children}
      </CardContent>
    </Card>
  );
}

export function HomeTools() {
  function simulateOrder() {
    return fetch("http://localhost:8080/api/orders", { method: "POST" });
  }

  return (
    <TalkTools className="right-10 bottom-10">
      <Button
        onClick={simulateOrder}
        variant="ghost"
        className="active:bg-primary"
      >
        Commande
      </Button>
    </TalkTools>
  );
}

export function PaymentTools({ order }: { order: Order }) {
  function simulatePayment() {
    return fetch(
      `http://localhost:8080/api/psps/payment-confirmations/${order.id}`,
      {
        method: "POST"
      }
    );
  }

  if (order.payment.status !== "PENDING") return <></>;

  return (
    <TalkTools className="-right-5 translate-x-[100%]">
      <DialogClose asChild>
        <Button
          onClick={simulatePayment}
          variant="ghost"
          className="active:bg-primary"
        >
          Paiement
        </Button>
      </DialogClose>
    </TalkTools>
  );
}
