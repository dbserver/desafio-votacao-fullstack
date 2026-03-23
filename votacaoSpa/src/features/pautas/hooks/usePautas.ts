import { useCallback, useEffect, useState } from 'react'
import { createPauta as createPautaRequest, listPautas } from '../api/pautasApi'
import type { CreatePautaPayload, HttpError, Pauta } from '../../../types/api'

interface UsePautasResult {
  pautas: Pauta[]
  isLoading: boolean
  isCreating: boolean
  error: HttpError | null
  reloadPautas: () => Promise<void>
  createPauta: (payload: CreatePautaPayload) => Promise<boolean>
}

export function usePautas(): UsePautasResult {
  const [pautas, setPautas] = useState<Pauta[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [isCreating, setIsCreating] = useState(false)
  const [error, setError] = useState<HttpError | null>(null)

  const loadPautas = useCallback(async () => {
    setIsLoading(true)
    setError(null)

    try {
      const result = await listPautas()
      setPautas(Array.isArray(result) ? result : [])
    } catch (requestError) {
      setError(requestError as HttpError)
    } finally {
      setIsLoading(false)
    }
  }, [])

  const createPauta = useCallback(
    async (payload: CreatePautaPayload) => {
      setIsCreating(true)
      setError(null)

      try {
        await createPautaRequest(payload)
        await loadPautas()
        return true
      } catch (requestError) {
        setError(requestError as HttpError)
        return false
      } finally {
        setIsCreating(false)
      }
    },
    [loadPautas],
  )

  useEffect(() => {
    loadPautas()
  }, [loadPautas])

  return {
    pautas,
    isLoading,
    isCreating,
    error,
    reloadPautas: loadPautas,
    createPauta,
  }
}
