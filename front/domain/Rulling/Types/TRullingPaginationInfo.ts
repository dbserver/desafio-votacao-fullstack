import { z } from "zod";

import RullingPaginationInfoSchema from "@/domain/Rulling/Schemas/RullingPaginationInfo.schema";

type TRullingPaginationInfo = z.infer<typeof RullingPaginationInfoSchema>;
export default TRullingPaginationInfo;
