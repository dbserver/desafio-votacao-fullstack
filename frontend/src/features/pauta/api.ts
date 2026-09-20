import { http } from "../../lib/http";
import {
  pautaResponseSchema,
  type CriarPautaRequest,
  type PautaResponse,
} from "./schema";

export const listarPautas = async () => {
  return pautaResponseSchema
    .array()
    .parse(await http.get<PautaResponse[]>("/pauta"));
};

export const criarPauta = async (request: CriarPautaRequest) => {
  return pautaResponseSchema.parse(
    await http.post<PautaResponse>("/pauta", request),
  );
};
