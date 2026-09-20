import { somenteDigitos } from "../../lib/format";
import { http } from "../../lib/http";
import {
  associadoResponseSchema,
  type AssociadoResponse,
  type CriarAssociadoRequest,
} from "./schema";

export const listarAssociados = async () => {
  return associadoResponseSchema
    .array()
    .parse(await http.get<AssociadoResponse[]>("/associado"));
};

export const criarAssociado = async (request: CriarAssociadoRequest) => {
  return associadoResponseSchema.parse(
    await http.post<AssociadoResponse>("/associado", request),
  );
};

export const buscarAssociadoPorCpf = async (cpf: string) => {
  return associadoResponseSchema.parse(
    await http.get<AssociadoResponse>(`/associado/cpf/${somenteDigitos(cpf)}`),
  );
};
