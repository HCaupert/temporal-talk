import { Row } from "@/lib/fetchOrders";
import { useToast } from "@/components/ui/use-toast";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { Button } from "@/components/ui/button";

export function IdCell({ row }: { row: Row }) {
  const { toast } = useToast();

  const copyId = () =>
    navigator.clipboard
      .writeText(row.id)
      .then(() => toast({ title: "ID copied", description: row.id }));
  return (
    <TooltipProvider>
      <Tooltip>
        <TooltipTrigger asChild>
          <Button variant="ghost" onClick={copyId}>
            {row.id.substring(0, 6).toUpperCase()}
          </Button>
        </TooltipTrigger>
        <TooltipContent>
          <p>Copy Id</p>
        </TooltipContent>
      </Tooltip>
    </TooltipProvider>
  );
}
