import { NativeConnection, Worker } from "@temporalio/worker";
import * as activities from "./activities";


const connection = await NativeConnection.connect({
  address: "eu-west-2.aws.api.temporal.io:7233",
  apiKey: process.env.TEMPORAL_API_KEY,
  tls: true
});

const worker = await Worker.create({
  connection,
  namespace: "hugo.nxuww",
  taskQueue: "shipping",
  activities
});

await worker.run();
