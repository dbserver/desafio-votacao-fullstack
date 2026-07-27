import { api } from "./api";
import type { ResultadoVotacao } from "../types/resultado";

export async function buscarResultado(
  pautaId: number,
): Promise<ResultadoVotacao> {
  const response = await api.get<ResultadoVotacao>(`/resultados/${pautaId}`);

  return response.data;
}
