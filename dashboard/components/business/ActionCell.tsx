"use client";

import { Row } from "@/lib/fetchOrders";
import { Button } from "@/components/ui/button";
import { EyeIcon, HomeIcon, LucideIcon, PackageIcon, TruckIcon, UserIcon } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from "@/components/ui/dialog";
import { fetchOrder } from "@/lib/fetchOrder";
import { ReactElement, useEffect, useState } from "react";
import { Order, ShippingStatus } from "@/lib/models";
import { Skeleton } from "@/components/ui/skeleton";
import { Progress } from "@/components/ui/progress";
import { cn } from "@/lib/utils";
import { PreparePackageForm } from "@/components/business/PreparePackageForm";
import { ShipPackageForm } from "@/components/business/ShipPackageForm";
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip";
import Image from "next/image";
import { PaymentTools } from "@/components/business/TalkTools";

const shippingPercentages: Record<ShippingStatus, number> = {
  PENDING: 10,
  AWAITING_PREPARATION: 30,
  PREPARED: 50,
  AWAITING_SHIPPING: 70,
  SHIPPED: 100
};

const shippingStatuses: Record<ShippingStatus, ReactElement> = {
  PENDING: <span className="text-blue-700">En attente du paiement</span>,
  AWAITING_PREPARATION: (
    <span className="text-amber-600">En attente de préparation</span>
  ),
  PREPARED: <span className="text-blue-700">Colis prêt</span>,
  AWAITING_SHIPPING: (
    <span className="text-amber-600">En attente d&apos;envoi</span>
  ),
  SHIPPED: <span className="text-green-600">Expédié</span>
};

function Order({ row }: { row: Row }) {
  const [order, setOrder] = useState<Order>();
  useEffect(() => {
    fetchOrder(row.id).then(setOrder);
  }, []);

  if (!order) {
    return <Skeleton />;
  }

  const { payment: { status: paymentStatus }, shipping: { status: shippingStatus, trackingNumber } } = order;
  const { shipping, article } = order;
  return (
    <div className="flex flex-col gap-2 static">
      <PaymentTools order={order} />
      <div className="flex gap-2 my-6">
        <Image
          src={article.image}
          alt="image"
          priority
          width={160}
          height={160}
          className="rounded-lg object-cover h-40 w-40"
        />
        <div className="grow justify-between flex flex-col">
          <div className="flex justify-between">
            <h3 className="font-bold">{article.name}</h3>
            <p className="text-muted-foreground text-right ml-2">
              {new Date(order.creationDate).toLocaleDateString("fr-FR")}
            </p>
          </div>
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <p
                  className={cn(
                    "ml-auto text-end w-fit font-semibold cursor-default underline hover:no-underline",
                    paymentStatus === "PENDING" && "text-amber-600",
                    paymentStatus === "AUTHORIZED" && "text-blue-700",
                    paymentStatus === "CAPTURED" && "text-green-600"
                  )}
                >
                  {article.price / 100} €
                </p>
              </TooltipTrigger>
              <TooltipContent>
                {paymentStatus === "PENDING" && <p>Paiement en attente</p>}
                {paymentStatus === "AUTHORIZED" && <p>Paiement autorisé</p>}
                {paymentStatus === "CAPTURED" && <p>Paiement capturé</p>}
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
          <div className="h-8" />
          <p className="flex gap-2 items-center text-muted-foreground">
            <UserIcon className="size-5 text-primary" />
            {shipping.receiver.firstName} {shipping.receiver.lastName}
          </p>
          <p className="flex gap-2 items-center text-muted-foreground">
            <HomeIcon className="size-5 text-primary" />
            {shipping.address.street}, {shipping.address.city} -{" "}
            {shipping.address.iso3CountryCode}
          </p>
        </div>
      </div>
      <p>
        Livraison{" "}
        <span className="italic">{shipping.method.toLowerCase()}</span>{" "}
        - {shippingStatuses[shippingStatus]}
      </p>
      {trackingNumber && (
        <p>
          Numéro de suivi : <span className="italic">{trackingNumber}</span>
        </p>
      )}
      <Progress value={shippingPercentages[shippingStatus]} />
      {shippingStatus === "AWAITING_PREPARATION" && (
        <PreparePackageForm orderId={order.id} />
      )}
      {shippingStatus === "AWAITING_SHIPPING" && (
        <ShipPackageForm orderId={order.id} />
      )}
    </div>
  );
}

function ConfirmButtonDialog({
                               text,
                               Icon,
                               element
                             }: {
  text: string;
  Icon: LucideIcon;
  element: Row;
}) {
  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="ghost" className="gap-2 w-full">
          <Icon />
          {text}
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            Commande {element.id.substring(0, 6).toUpperCase()}
          </DialogTitle>
          <DialogDescription className="text-pretty">
            Récapitulatif
          </DialogDescription>
        </DialogHeader>
        <Order row={element} />
      </DialogContent>
    </Dialog>
  );
}

export default function ActionCell({ element }: { element: Row }) {
  const shippingStatus = element.shippingStatus;
  if (shippingStatus === "AWAITING_PREPARATION")
    return (
      <ConfirmButtonDialog
        Icon={PackageIcon}
        text="Préparer"
        element={element}
      />
    );
  if (shippingStatus === "AWAITING_SHIPPING")
    return (
      <ConfirmButtonDialog Icon={TruckIcon} text="Envoyer" element={element} />
    );
  return <ConfirmButtonDialog Icon={EyeIcon} text="Voir" element={element} />;
}
