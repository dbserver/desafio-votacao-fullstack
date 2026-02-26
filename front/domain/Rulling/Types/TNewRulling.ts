import { z } from "zod";

import NewRullingSchema from "@/domain/Rulling/Schemas/NewRulling.schema";

type TNewRulling = z.infer<typeof NewRullingSchema>;

export default TNewRulling;
