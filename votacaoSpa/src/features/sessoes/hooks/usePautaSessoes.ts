import { useCallback, useState } from 'react'
import { listSessoesByPauta } from '../api/sessoesApi'
import type { HttpError, PautaId, SessaoVotacaoResponseDTO } from '../../../types/api'

interface UsePautaSessoesResult {
  error: HttpError | null
  isExpanded: (pautaId: PautaId) => boolean
  isLoading: (pautaId: PautaId) => boolean
  getSessoes: (pautaId: PautaId) => SessaoVotacaoResponseDTO[]
  toggle: (pautaId: PautaId) => Promise<void>
  setSessoes: (pautaId: string, sessoes: SessaoVotacaoResponseDTO[]) => void
}

export function usePautaSessoes(): UsePautaSessoesResult {
  const [error, setError] = useState<HttpError | null>(null)
  const [expandedMap, setExpandedMap] = useState<Record<string, boolean>>({})
  const [loadingMap, setLoadingMap] = useState<Record<string, boolean>>({})
  const [sessoesByPauta, setSessoesByPauta] = useState<
    Record<string, SessaoVotacaoResponseDTO[]>
  >({})

  const toggle = useCallback(async (pautaId: PautaId) => {
    const id = String(pautaId)
    const currentlyExpanded = Boolean(expandedMap[id])

    setExpandedMap((current) => {
      const nextExpanded = !current[id]
      return { ...current, [id]: nextExpanded }
    })

    if (currentlyExpanded || loadingMap[id]) {
      return
    }

    setLoadingMap((current) => ({ ...current, [id]: true }))
    setError(null)

    try {
      const sessoes = await listSessoesByPauta(id)
      setSessoesByPauta((current) => ({ ...current, [id]: sessoes }))
    } catch (requestError) {
      setError(requestError as HttpError)
      setSessoesByPauta((current) => ({ ...current, [id]: [] }))
    } finally {
      setLoadingMap((current) => ({ ...current, [id]: false }))
    }
  }, [expandedMap, loadingMap])

  const isExpanded = useCallback(
    (pautaId: PautaId) => Boolean(expandedMap[String(pautaId)]),
    [expandedMap],
  )

  const isLoading = useCallback(
    (pautaId: PautaId) => Boolean(loadingMap[String(pautaId)]),
    [loadingMap],
  )

  const getSessoes = useCallback(
    (pautaId: PautaId) => sessoesByPauta[String(pautaId)] || [],
    [sessoesByPauta],
  )

  const setSessoes = useCallback(
    (pautaId: string, sessoes: SessaoVotacaoResponseDTO[]) => {
      setSessoesByPauta((current) => ({ ...current, [pautaId]: sessoes }))
    },
    [],
  )

  return {
    error,
    isExpanded,
    isLoading,
    getSessoes,
    toggle,
    setSessoes,
  }
}
