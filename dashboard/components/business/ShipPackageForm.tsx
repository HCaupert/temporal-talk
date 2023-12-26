import { Separator } from "@/components/ui/separator";
import { DialogClose, DialogFooter } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";

export function ShipPackageForm({ orderId }: { orderId: string }) {
  async function markAsShipped() {
    await fetch(`http://localhost:8080/api/orders/${orderId}/shippings`, {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
    });
  }

  return (
    <div>
      <Separator className="my-6" />
      <p className="font-semibold text-primary">Envoi du colis</p>
      <p className="text-sm text-muted-foreground">
        Marquer le colis comme envoyé
      </p>
      <DialogFooter className="pt-6">
        <DialogClose asChild>
          <Button variant="outline">Fermer</Button>
        </DialogClose>
        <Button type="submit" onClick={markAsShipped}>
          Marquer comme envoyée
        </Button>
      </DialogFooter>
    </div>
  );
}
