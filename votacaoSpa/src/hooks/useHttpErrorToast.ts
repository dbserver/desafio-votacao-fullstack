import { useEffect, useRef } from 'react'
import { useToast } from '../components/ui/ToastProvider'
import type { HttpError } from '../types/api'

function extractApiMessage(error: HttpError): string | null {
  const payload = error.payload

  if (!payload || typeof payload !== 'object') {
    return null
  }

  const maybeMessage = (payload as { message?: unknown }).message
  if (typeof maybeMessage === 'string' && maybeMessage.trim()) {
    return maybeMessage
  }

  const maybeError = (payload as { error?: unknown }).error
  if (typeof maybeError === 'string' && maybeError.trim()) {
    return maybeError
  }

  return null
}

function resolveHttpMessage(
  error: HttpError | null,
  fallbackMessage: string,
): string | null {
  if (!error) {
    return null
  }

  const apiMessage = extractApiMessage(error)

  if (apiMessage) {
    return apiMessage
  }

  if (error.message) {
    return error.message
  }

  if (error.status === 404) {
    return 'Recurso nao encontrado (404). Verifique o endpoint da API.'
  }

  if (error.status === 409) {
    return 'Conflito de dados (409). Atualize os dados e tente novamente.'
  }

  return fallbackMessage
}

export function useHttpErrorToast(error: HttpError | null, fallbackMessage: string) {
  const { addToast } = useToast()
  const previousRef = useRef<string | null>(null)

  useEffect(() => {
    if (!error) {
      return
    }

    const message = resolveHttpMessage(error, fallbackMessage)

    if (!message || previousRef.current === message) {
      return
    }

    previousRef.current = message

    addToast({
      type: 'error',
      title: 'Erro',
      message,
      durationMs: 5500,
    })
  }, [addToast, error, fallbackMessage])
}
