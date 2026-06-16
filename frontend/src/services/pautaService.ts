import { request } from './api';
import type { CriarPautaRequest, PautaPageResponse, PautaResponse } from '../types/api';

export async function listarPautas(page = 0, size = 6): Promise<PautaPageResponse> {
  return request<PautaPageResponse>(
    {
      method: 'GET',
      url: '/pautas',
      params: { page, size },
    },
    'Não foi possível carregar as pautas.',
  );
}

export async function buscarPauta(id: string): Promise<PautaResponse> {
  return request<PautaResponse>({ method: 'GET', url: `/pautas/${id}` }, 'Não foi possível carregar a pauta.');
}

export async function criarPauta(payload: CriarPautaRequest): Promise<PautaResponse> {
  return request<PautaResponse>(
    {
      method: 'POST',
      url: '/pautas',
      data: payload,
    },
    'Não foi possível criar a pauta.',
  );
}
