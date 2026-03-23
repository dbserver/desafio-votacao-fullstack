import type { ApiErrorResponse, HttpError } from '../../types/api'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'

async function parseResponse(response: Response): Promise<unknown> {
  const contentType = response.headers.get('content-type') || ''
  const isJson = contentType.includes('application/json')

  if (response.status === 204) {
    return null
  }

  if (isJson) {
    return response.json()
  }

  return response.text()
}

function createHttpError(status: number, message: string, payload: unknown): HttpError {
  const error = new Error(message) as HttpError
  error.status = status
  error.payload = payload
  return error
}

export async function requestJson<T>(
  path: string,
  options: RequestInit = {},
): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      Accept: 'application/json',
      ...(options.body ? { 'Content-Type': 'application/json' } : {}),
      ...options.headers,
    },
    ...options,
  })

  const payload = await parseResponse(response)

  if (!response.ok) {
    const apiPayload = payload as ApiErrorResponse | string | null
    const message =
      (apiPayload && typeof apiPayload !== 'string' && apiPayload.message) ||
      (typeof apiPayload === 'string' && apiPayload) ||
      `Erro HTTP ${response.status}`

    throw createHttpError(response.status, message, payload)
  }

  return payload as T
}
