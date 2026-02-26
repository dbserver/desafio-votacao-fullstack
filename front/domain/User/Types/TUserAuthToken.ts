import { z } from "zod";

import UserAuthTokenSchema from "@/domain/User/Schemas/UserAuthToken.schema";

type TUserAuthToken = z.infer<typeof UserAuthTokenSchema>;
export default TUserAuthToken;
