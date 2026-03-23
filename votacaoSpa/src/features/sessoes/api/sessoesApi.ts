import { requestJson } from '../../../lib/http/client'
import type {
  SessaoVotacaoRequestDTO,
  SessaoVotacaoResponseDTO,
  VotingResultDTO,
} from '../../../types/api'

interface SessaoApiResponse {
  id: string
  pautaId: string
  createdAt: string
  finalSessionDateTime: string
  isActive?: boolean
  active?: boolean
}

function normalizeSessao(item: SessaoApiResponse): SessaoVotacaoResponseDTO {
  return {
    id: item.id,
    pautaId: item.pautaId,
    createdAt: item.createdAt,
    finalSessionDateTime: item.finalSessionDateTime,
    isActive: Boolean(item.isActive ?? item.active),
  }
}

export async function listSessoesByPauta(
  pautaId: string,
): Promise<SessaoVotacaoResponseDTO[]> {
  const response = await requestJson<SessaoApiResponse | SessaoApiResponse[]>(
    `/sessoes/pauta/${pautaId}`,
    {
      method: 'GET',
    },
  )

  const rows = Array.isArray(response) ? response : [response]

  return rows.map(normalizeSessao)
}

export async function getSessaoResultado(
  sessaoVotacaoId: string,
): Promise<VotingResultDTO> {
  return requestJson<VotingResultDTO>(`/sessoes/${sessaoVotacaoId}/results`, {
    method: 'GET',
  })
}

export async function createSessao(
  payload: SessaoVotacaoRequestDTO,
): Promise<SessaoVotacaoResponseDTO> {
  const response = await requestJson<SessaoApiResponse>(
    '/sessoes',
    {
      method: 'POST',
      body: JSON.stringify(payload),
    },
  )

  return normalizeSessao(response)
}
