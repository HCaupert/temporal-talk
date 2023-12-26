import { Separator } from "@/components/ui/separator";
import { DialogClose, DialogFooter } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";

const formSchema = z.object({
  trackingNumber: z.string().min(3),
});

type Schema = z.infer<typeof formSchema>;

export function PreparePackageForm({ orderId }: { orderId: string }) {
  const form = useForm<Schema>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      trackingNumber: "",
    },
  });

  async function confirmPreparation(values: Schema) {
    await fetch(
      `http://localhost:8080/api/orders/${orderId}/prepared-shippings`,
      {
        method: "POST",
        headers: {
          "Content-type": "application/json",
        },
        body: JSON.stringify({ id: values.trackingNumber }),
      },
    );
  }

  return (
    <Form {...form}>
      <form
        className="space-y-2 text-muted-foreground flex flex-col"
        onSubmit={form.handleSubmit(confirmPreparation)}
      >
        <Separator className="my-6" />
        <p className="font-semibold text-primary">Preparation de colis</p>
        <FormField
          control={form.control}
          render={({ field }) => (
            <FormItem>
              <FormControl>
                <Input placeholder="Numéro de suivi" {...field} />
              </FormControl>
              <FormDescription>
                Entrer le numéro de suivi et marquer comme préparé.
              </FormDescription>
            </FormItem>
          )}
          name="trackingNumber"
        />

        <DialogFooter className="pt-6">
          <DialogClose asChild>
            <Button variant="outline">Fermer</Button>
          </DialogClose>
          <Button type="submit" disabled={!form.formState.isValid}>
            Marquer comme préparée
          </Button>
        </DialogFooter>
      </form>
    </Form>
  );
}
