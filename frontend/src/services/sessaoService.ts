import api from "./api";
import { SessaoVotacao } from "../types";

export const abrirSessao = (pautaId: number, duracaoMinutos?: number) =>
  api.post<SessaoVotacao>("/sessoes", { pautaId, duracaoMinutos });

export const buscarSessao = (pautaId: number) =>
  api.get<SessaoVotacao>(`/sessoes/pauta/${pautaId}`);
