import { z } from "zod";

export default z.object({
  total: z.number(),
  pages: z.number(),
});
