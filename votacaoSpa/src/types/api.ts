export type PautaId = string | number

export interface Pauta {
  id: PautaId
  name?: string
  description?: string
  titulo?: string
  descricao?: string
}

export interface CreatePautaPayload {
  name: string
  description: string
}

export interface SessaoVotacaoRequestDTO {
  pautaId: string
  durationInSeconds?: number
}

export interface SessaoVotacaoResponseDTO {
  id: string
  pautaId: string
  createdAt: string
  finalSessionDateTime: string
  isActive: boolean
}

export interface VotingResultDTO {
  sessaoVotacaoId: string
  totalVotes: number
  votesYes: number
  votesNo: number
  result: string
}

export type VoteChoice = 'SIM' | 'NAO'

export interface VotePayload {
  sessaoVotacaoId: string
  cpf: string
  vote: VoteChoice
}

export interface HttpError extends Error {
  status?: number
  payload?: unknown
}

export interface ApiErrorResponse {
  message?: string
}
