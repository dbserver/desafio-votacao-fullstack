import { http } from "../../lib/http";
import {
  sessaoResponseSchema,
  type CriarSessaoRequest,
  type SessaoResponse,
} from "./schema";

export const buscarSessao = async (pautaId: number) => {
  return sessaoResponseSchema.parse(
    await http.get<SessaoResponse>(`/pauta/${pautaId}/sessao`),
  );
};

export const abrirSessao = async (
  pautaId: number,
  request: CriarSessaoRequest,
) => {
  return sessaoResponseSchema.parse(
    await http.post<SessaoResponse>(`/pauta/${pautaId}/sessao`, request),
  );
};
