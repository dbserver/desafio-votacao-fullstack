import { useState } from 'react'
import type { FormEvent } from 'react'
import type { CreatePautaPayload } from '../../../types/api'
import './CreatePautaModal.css'

interface CreatePautaModalProps {
  isOpen: boolean
  isSubmitting: boolean
  onClose: () => void
  onSubmit: (payload: CreatePautaPayload) => Promise<boolean>
}

function CreatePautaModal({
  isOpen,
  isSubmitting,
  onClose,
  onSubmit,
}: CreatePautaModalProps) {
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')

  if (!isOpen) {
    return null
  }

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const success = await onSubmit({
      name: name.trim(),
      description: description.trim(),
    })

    if (!success) {
      return
    }

    setName('')
    setDescription('')
    onClose()
  }

  return (
    <div className="create-pauta-overlay" role="dialog" aria-modal="true" aria-label="Cadastrar nova pauta">
      <div className="create-pauta-modal">
        <div className="create-pauta-header">
          <h3>Nova pauta</h3>
          <button type="button" className="create-pauta-close" onClick={onClose} aria-label="Fechar cadastro">
            x
          </button>
        </div>

        <form onSubmit={handleSubmit} className="create-pauta-form">
          <label>
            Name
            <input
              type="text"
              value={name}
              onChange={(event) => setName(event.target.value)}
              minLength={3}
              required
            />
          </label>

          <label>
            Description
            <textarea
              value={description}
              onChange={(event) => setDescription(event.target.value)}
              rows={4}
              minLength={5}
              required
            />
          </label>

          <button type="submit" className="action-button" disabled={isSubmitting}>
            {isSubmitting ? 'Salvando...' : 'Salvar pauta'}
          </button>
        </form>
      </div>
    </div>
  )
}

export default CreatePautaModal
