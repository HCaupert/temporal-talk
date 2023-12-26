"use client";

import { Row } from "@/lib/fetchOrders";
import { DataTable } from "@/components/ui/data-table";
import { ColumnDef } from "@tanstack/react-table";
import ActionCell from "@/components/business/ActionCell";
import { IdCell } from "@/components/business/IdCell";
import { ShippingCell } from "@/components/business/ShippingCell";

const columnDefs: ColumnDef<Row>[] = [
  {
    cell: ({ row }) => <IdCell row={row.original} />,
    header: "Order n°",
  },
  {
    header: "Status",
    cell: ({ row }) => <ShippingCell row={row.original} />,
  },
  {
    header: "Date",
    cell: ({
      row: {
        original: { date },
      },
    }) => (
      <p className="text-center">
        {date.toLocaleDateString("fr-FR")}
        <br />
        {date.toLocaleTimeString("fr-FR")}
      </p>
    ),
  },
  {
    header: "Opérations",
    id: "actions",
    cell: ({ row: { original } }) => <ActionCell element={original} />,
  },
];

export default function ShippingTable({ rows }: { rows: Row[] }) {
  return (
    <DataTable
      columns={columnDefs}
      data={rows.filter((value, index, array) => index < 6)}
      getRowId={(r) => r.id + r.shippingStatus}
    />
  );
}
