import { ApiError, request } from './api';
import type { AbrirSessaoRequest, SessaoResponse } from '../types/api';

export async function abrirSessao(pautaId: string, duracaoMinutos?: number): Promise<SessaoResponse> {
  const body: AbrirSessaoRequest | undefined =
    typeof duracaoMinutos === 'number' ? { duracaoMinutos } : undefined;

  return request<SessaoResponse>(
    {
      method: 'POST',
      url: `/pautas/${pautaId}/sessao`,
      data: body,
    },
    'Não foi possível abrir a sessão.',
  );
}

export async function buscarSessaoPorPauta(pautaId: string): Promise<SessaoResponse | null> {
  try {
    return await request<SessaoResponse>(
      {
        method: 'GET',
        url: `/pautas/${pautaId}/sessao`,
      },
      'Não foi possível verificar a sessão.',
    );
  } catch (error) {
    if (error instanceof ApiError && error.status === 404) {
      return null;
    }

    throw error;
  }
}
