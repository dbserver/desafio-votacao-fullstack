import { z } from "zod";

export const opcaoVotoSchema = z.enum(["SIM", "NAO"]);

export const votoResponseSchema = z.object({
  id: z.number(),
  sessaoId: z.number(),
  associadoId: z.number(),
  opcao: opcaoVotoSchema,
  criadoEm: z.string(),
});

export const criarVotoRequestSchema = z.object({
  associadoId: z.number({ error: "Informe o associado" }),
  opcao: opcaoVotoSchema,
});

export type VotoResponse = z.infer<typeof votoResponseSchema>;

export type OpcaoVoto = z.infer<typeof opcaoVotoSchema>;

export type CriarVotoRequest = z.infer<typeof criarVotoRequestSchema>;
