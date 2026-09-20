import { z } from "zod";

export const pautaResponseSchema = z.object({
  id: z.number(),
  titulo: z.string(),
  descricao: z.string(),
  criadaEm: z.string(),
});

export const criarPautaRequestSchema = z.object({
  titulo: z
    .string()
    .trim()
    .min(1, "Título é obrigatório")
    .max(255, "Título deve ter no máximo 255 caracteres"),
  descricao: z.string().trim().min(1, "Descrição é obrigatória"),
});

export type PautaResponse = z.infer<typeof pautaResponseSchema>;

export type CriarPautaRequest = z.infer<typeof criarPautaRequestSchema>;
