import * as z from "zod";
export const schema = z.object({
  nome: z.string().min(3, { message: "O Nome é obrigatório (mínimo 3 caracteres)" }),
  cpf: z.string().min(11, { message: "O CPF é obrigatório  ( 11 caracteres)" }),
});
