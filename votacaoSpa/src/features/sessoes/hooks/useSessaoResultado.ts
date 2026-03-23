import { useCallback, useState } from 'react'
import { getSessaoResultado } from '../api/sessoesApi'
import type { HttpError, VotingResultDTO } from '../../../types/api'

interface UseSessaoResultadoResult {
  isLoading: boolean
  error: HttpError | null
  resultado: VotingResultDTO | null
  loadResultado: (sessaoVotacaoId: string) => Promise<void>
  clearResultado: () => void
}

export function useSessaoResultado(): UseSessaoResultadoResult {
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<HttpError | null>(null)
  const [resultado, setResultado] = useState<VotingResultDTO | null>(null)

  const loadResultado = useCallback(async (sessaoVotacaoId: string) => {
    setIsLoading(true)
    setError(null)

    try {
      const response = await getSessaoResultado(sessaoVotacaoId)
      setResultado(response)
    } catch (requestError) {
      setResultado(null)
      setError(requestError as HttpError)
    } finally {
      setIsLoading(false)
    }
  }, [])

  const clearResultado = useCallback(() => {
    setResultado(null)
    setError(null)
  }, [])

  return {
    isLoading,
    error,
    resultado,
    loadResultado,
    clearResultado,
  }
}
