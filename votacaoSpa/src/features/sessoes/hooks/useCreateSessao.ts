import { useCallback, useState } from 'react'
import { createSessao } from '../api/sessoesApi'
import type { SessaoVotacaoRequestDTO, SessaoVotacaoResponseDTO } from '../../../types/api'
import type { HttpError } from '../../../types/api'

interface UseCreateSessaoResult {
  isSubmitting: boolean
  error: HttpError | null
  submit: (payload: SessaoVotacaoRequestDTO) => Promise<SessaoVotacaoResponseDTO | null>
}

export function useCreateSessao(): UseCreateSessaoResult {
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState<HttpError | null>(null)

  const submit = useCallback(async (payload: SessaoVotacaoRequestDTO) => {
    setIsSubmitting(true)
    setError(null)

    try {
      const result = await createSessao(payload)
      return result
    } catch (err) {
      setError(err as HttpError)
      return null
    } finally {
      setIsSubmitting(false)
    }
  }, [])

  return { isSubmitting, error, submit }
}
