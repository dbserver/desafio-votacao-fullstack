import { z } from "zod";

export const sessaoResponseSchema = z.object({
  id: z.number(),
  pautaId: z.number(),
  pautaTitulo: z.string(),
  inicio: z.string(),
  fim: z.string(),
});

export const criarSessaoRequestSchema = z.object({
  duracaoEmMinutos: z
    .number({ error: "Informe um número" })
    .int("Informe um número inteiro")
    .positive("A duração deve ser maior que zero")
    .optional(),
});
export type SessaoResponse = z.infer<typeof sessaoResponseSchema>;

export type CriarSessaoRequest = z.infer<typeof criarSessaoRequestSchema>;
