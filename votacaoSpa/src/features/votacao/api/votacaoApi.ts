import { requestJson } from '../../../lib/http/client'
import type { VotePayload } from '../../../types/api'

export function votar(payload: VotePayload) {
  const requestBody: VotePayload = {
    sessaoVotacaoId: payload.sessaoVotacaoId,
    cpf: payload.cpf,
    vote: payload.vote,
  }

  return requestJson<unknown>('/votos', {
    method: 'POST',
    body: JSON.stringify(requestBody),
  })
}
