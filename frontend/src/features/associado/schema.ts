import { z } from "zod";
import { somenteDigitos } from "../../lib/cpf";

export const associadoResponseSchema = z.object({
  id: z.number(),
  nome: z.string(),
  cpf: z.string(),
});

export const criarAssociadoRequestSchema = z.object({
  nome: z
    .string()
    .trim()
    .min(1, "Nome é obrigatório")
    .max(255, "Nome deve ter no máximo 255 caracteres"),
  cpf: z
    .string()
    .trim()
    .refine(
      (cpf) => somenteDigitos(cpf).length === 11,
      "CPF deve ter 11 dígitos",
    ),
});

export type AssociadoResponse = z.infer<typeof associadoResponseSchema>;

export type CriarAssociadoRequest = z.infer<typeof criarAssociadoRequestSchema>;
