import { z } from "zod";

const RullingSchema = z.object({
  active: z.boolean(),
  author: z.uuid(),
  createdAt: z.string(),
  description: z.string().optional(),
  expiration: z.string(),
  id: z.uuid(),
  title: z.string(),
  voteNo: z.number(),
  voteTotal: z.number(),
  voteYes: z.number(),
});

type Rulling = z.infer<typeof RullingSchema>;

export { RullingSchema, type Rulling };
