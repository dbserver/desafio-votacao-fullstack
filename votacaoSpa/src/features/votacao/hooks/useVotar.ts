import { useState } from 'react'
import { votar } from '../api/votacaoApi'
import type { HttpError, VotePayload } from '../../../types/api'

interface UseVotarResult {
  submitVote: (payload: VotePayload) => Promise<boolean>
  isSubmitting: boolean
  error: HttpError | null
  successMessage: string
}

export function useVotar(): UseVotarResult {
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState<HttpError | null>(null)
  const [successMessage, setSuccessMessage] = useState('')

  const submitVote = async ({ sessaoVotacaoId, cpf, vote }: VotePayload) => {
    setError(null)
    setSuccessMessage('')
    setIsSubmitting(true)

    try {
      await votar({ sessaoVotacaoId, cpf, vote })
      setSuccessMessage('Voto registrado com sucesso.')
      return true
    } catch (requestError) {
      setError(requestError as HttpError)
      return false
    } finally {
      setIsSubmitting(false)
    }
  }

  return {
    submitVote,
    isSubmitting,
    error,
    successMessage,
  }
}
