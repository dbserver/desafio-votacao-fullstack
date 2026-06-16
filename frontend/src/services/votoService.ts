import { request } from './api';
import type { OpcaoVoto, RegistrarVotoRequest, ResultadoResponse, VotoResponse } from '../types/api';

export async function registrarVoto(
  sessaoId: string,
  associadoId: string,
  opcao: OpcaoVoto,
): Promise<VotoResponse> {
  const body: RegistrarVotoRequest = {
    associadoId,
    opcao,
  };

  return request<VotoResponse>(
    {
      method: 'POST',
      url: `/sessoes/${sessaoId}/votos`,
      data: body,
    },
    'Não foi possível registrar o voto.',
  );
}

export async function buscarResultado(sessaoId: string): Promise<ResultadoResponse> {
  return request<ResultadoResponse>(
    {
      method: 'GET',
      url: `/sessoes/${sessaoId}/resultado`,
    },
    'Não foi possível carregar o resultado.',
  );
}
