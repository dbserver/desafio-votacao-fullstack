import { api } from "./api";
import type { CreateVotoRequest, Voto } from "../types/voto";

export async function registrarVoto(
  pautaId: number,
  request: CreateVotoRequest,
): Promise<Voto> {
  const response = await api.post<Voto>(`/votos/${pautaId}`, request);

  return response.data;
}
