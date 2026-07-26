import { api } from "./api";
import type { CreatePautaRequest, Pauta } from "../types/pauta";

export async function listarPautas(): Promise<Pauta[]> {
  const response = await api.get<Pauta[]>("/pautas");
  return response.data;
}

export async function criarPauta(request: CreatePautaRequest): Promise<Pauta> {
  const response = await api.post<Pauta>("/pautas", request);
  return response.data;
}
