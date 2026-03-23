import api from "./api";
import { VotoRequest, VotoResponse, ResultadoVotacao } from "../types";

export const registrarVoto = (pautaId: number, voto: VotoRequest) =>
  api.post<VotoResponse>(`/pautas/${pautaId}/votos`, voto);

export const buscarResultado = (pautaId: number) =>
  api.get<ResultadoVotacao>(`/pautas/${pautaId}/votos/resultado`);
