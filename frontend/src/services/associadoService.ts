import { api } from "./api";
import type { Associado, CreateAssociadoRequest } from "../types/associado";

export async function criarAssociado(
  request: CreateAssociadoRequest,
): Promise<Associado> {
  const response = await api.post<Associado>("/associados", request);

  return response.data;
}

export async function listarAssociados(): Promise<Associado[]> {
  const response = await api.get<Associado[]>("/associados");

  return response.data;
}

export async function buscarAssociadoPorId(
  associadoId: number,
): Promise<Associado> {
  const response = await api.get<Associado>(`/associados/${associadoId}`);

  return response.data;
}
