import { api } from "./api";
import type {
  Associado,
  CreateAssociadoRequest,
  PageResponse,
} from "../types/associado";

export async function criarAssociado(
  request: CreateAssociadoRequest,
): Promise<Associado> {
  const response = await api.post<Associado>("/associados", request);

  return response.data;
}

export async function listarAssociados(
  page = 0,
  size = 10,
): Promise<PageResponse<Associado>> {
  const response = await api.get<PageResponse<Associado>>("/associados", {
    params: { page, size },
  });

  return response.data;
}

export async function buscarAssociadoPorId(
  associadoId: number,
): Promise<Associado> {
  const response = await api.get<Associado>(`/associados/${associadoId}`);

  return response.data;
}
