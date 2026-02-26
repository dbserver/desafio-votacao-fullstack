import { z } from "zod";

import UserLoginSchema from "@/domain/User/Schemas/UserLogin.schema";

type TUserLogin = z.infer<typeof UserLoginSchema>;
export default TUserLogin;
