import { useState } from 'react'
import './CreateSessaoModal.css'
import type { PautaId } from '../../../types/api'

interface CreateSessaoModalProps {
  isOpen: boolean
  isSubmitting: boolean
  pautaId: PautaId
  onClose: () => void
  onSubmit: (durationInSeconds?: number) => Promise<void>
}

function CreateSessaoModal({
  isOpen,
  isSubmitting,
  pautaId,
  onClose,
  onSubmit,
}: CreateSessaoModalProps) {
  const [durationMinutes, setDurationMinutes] = useState('')
  const [durationSeconds, setDurationSeconds] = useState('')

  const normalizeNumberInput = (value: string) => value.replace(/\D/g, '')

  const resetDuration = () => {
    setDurationMinutes('')
    setDurationSeconds('')
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    const minutesRaw = durationMinutes.trim()
    const secondsRaw = durationSeconds.trim()

    if (!minutesRaw && !secondsRaw) {
      await onSubmit(undefined)
      resetDuration()
      return
    }

    const minutes = minutesRaw ? parseInt(minutesRaw, 10) : 0
    const seconds = secondsRaw ? parseInt(secondsRaw, 10) : 0

    if ((Number.isNaN(minutes) || minutes < 0) || (Number.isNaN(seconds) || seconds < 0)) {
      return
    }

    const durationInSeconds = (minutes * 60) + seconds
    if (durationInSeconds <= 0) {
      return
    }

    await onSubmit(durationInSeconds)
    resetDuration()
  }

  const handleClose = () => {
    resetDuration()
    onClose()
  }

  if (!isOpen) {
    return null
  }

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Criar Nova Sessão</h2>
          <button
            type="button"
            className="modal-close-btn"
            onClick={handleClose}
            aria-label="Fechar modal"
          >
            ✕
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="duration-minutes">Duração:</label>
            <div className="duration-fields">
              <input
                id="duration-minutes"
                type="text"
                inputMode="numeric"
                pattern="[0-9]*"
                value={durationMinutes}
                onChange={(e) => setDurationMinutes(normalizeNumberInput(e.target.value))}
                placeholder="Min"
                disabled={isSubmitting}
                aria-label="Minutos"
              />
              <input
                id="duration-seconds"
                type="text"
                inputMode="numeric"
                pattern="[0-9]*"
                value={durationSeconds}
                onChange={(e) => setDurationSeconds(normalizeNumberInput(e.target.value))}
                placeholder="Seg"
                disabled={isSubmitting}
                aria-label="Segundos"
              />
            </div>
            <small className="duration-help">Se não informado, a sessão usa 1 minuto por padrão.</small>
          </div>

          <div className="modal-actions">
            <button
              type="button"
              className="btn-cancel"
              onClick={handleClose}
              disabled={isSubmitting}
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="btn-submit"
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Criando...' : 'Criar Sessão'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default CreateSessaoModal
