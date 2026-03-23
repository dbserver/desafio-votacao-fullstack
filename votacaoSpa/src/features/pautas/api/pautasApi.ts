import { requestJson } from '../../../lib/http/client'
import type { CreatePautaPayload, Pauta } from '../../../types/api'

export function listPautas() {
  return requestJson<Pauta[]>('/pautas')
}

export function createPauta(payload: CreatePautaPayload) {
  return requestJson<Pauta>('/pautas', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}
