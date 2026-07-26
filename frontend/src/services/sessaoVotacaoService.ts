import { api } from "./api";
import type {
  CreateSessaoVotacaoRequest,
  SessaoVotacao,
} from "../types/sessao";
import axios from "axios";

export async function abrirSessao(
  pautaId: number,
  request: CreateSessaoVotacaoRequest,
): Promise<SessaoVotacao> {
  const response =
    request.duracao === undefined
      ? await api.post<SessaoVotacao>(`/sessoes/${pautaId}`)
      : await api.post<SessaoVotacao>(`/sessoes/${pautaId}`, {
          ...request,
          duracao: request.duracao * 60,
        });

  return response.data;
}

export async function buscarSessaoPorPauta(
  pautaId: number,
): Promise<SessaoVotacao | null> {
  try {
    const response = await api.get<SessaoVotacao>(`/sessoes/${pautaId}`);

    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response?.status === 404) {
      return null;
    }

    throw error;
  }
}
