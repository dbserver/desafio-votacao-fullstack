import { z } from "zod";

import UserSignupSchema from "@/domain/User/Schemas/UserSignup.schema";

type TUserSignup = z.infer<typeof UserSignupSchema>;
export default TUserSignup;
