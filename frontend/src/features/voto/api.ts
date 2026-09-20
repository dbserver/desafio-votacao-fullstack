import { http } from "../../lib/http";
import {
  votoResponseSchema,
  type CriarVotoRequest,
  type VotoResponse,
} from "./schema";

export const listarVotos = async (pautaId: number) => {
  return votoResponseSchema
    .array()
    .parse(await http.get<VotoResponse[]>(`/pauta/${pautaId}/voto`));
};

export const votar = async (pautaId: number, request: CriarVotoRequest) => {
  return votoResponseSchema.parse(
    await http.post<VotoResponse>(`/pauta/${pautaId}/voto`, request),
  );
};
