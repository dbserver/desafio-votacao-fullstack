import { z } from "zod";

const UserSchema = z.object({
  id: z.uuid(),
  name: z.string(),
  email: z.email(),
});

type User = z.infer<typeof UserSchema>;

export { UserSchema, type User };
