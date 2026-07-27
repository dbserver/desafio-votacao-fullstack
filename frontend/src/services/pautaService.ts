import { api } from "./api";
import type { CreatePautaRequest, PageResponse, Pauta } from "../types/pauta";

export async function listarPautas(
  page = 0,
  size = 10,
): Promise<PageResponse<Pauta>> {
  const response = await api.get<PageResponse<Pauta>>("/pautas", {
    params: { page, size },
  });

  return response.data;
}

export async function criarPauta(request: CreatePautaRequest): Promise<Pauta> {
  const response = await api.post<Pauta>("/pautas", request);

  return response.data;
}
