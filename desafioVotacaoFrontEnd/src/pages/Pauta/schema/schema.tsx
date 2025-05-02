import * as z from "zod";
export const schema = z.object({
  titulo: z.string().min(3, { message: "O Título é obrigatório (mínimo 3 caracteres)" }),
  descricao: z.string().min(10, { message: "A descrição é obrigatória (mínimo 10 caracteres)" }),
});
