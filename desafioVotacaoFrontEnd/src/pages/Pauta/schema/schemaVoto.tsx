import * as z from "zod";
export const schemaVoto = z.object({
  pautaId: z.number().optional(),
  cpf: z.string().min(11, { message: "O CPF é obrigatório com os 11 caracteres" }),
  opcao: z.string().optional(),
});
