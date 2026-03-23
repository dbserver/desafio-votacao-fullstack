import { useState } from 'react'
import type { ChangeEvent, FormEvent } from 'react'
import type { VoteChoice, VotePayload } from '../../../types/api'
import { formatCpf, normalizeCpf } from '../utils/cpf'
import './VotarForm.css'

interface VoteFormState {
  cpf: string
  vote: VoteChoice
}

interface VotarFormProps {
  sessaoVotacaoId: string
  onSubmit: (payload: VotePayload) => Promise<boolean>
  isSubmitting: boolean
  successMessage: string
}

const DEFAULT_FORM: VoteFormState = {
  cpf: '',
  vote: 'SIM',
}

function VotarForm({ sessaoVotacaoId, onSubmit, isSubmitting, successMessage }: VotarFormProps) {
  const [formData, setFormData] = useState<VoteFormState>(DEFAULT_FORM)

  const handleChange = (
    event: ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = event.target

    if (name === 'cpf') {
      setFormData((current) => ({
        ...current,
        cpf: formatCpf(value),
      }))
      return
    }

    const field = name as keyof VoteFormState
    setFormData((current) => ({
      ...current,
      [field]: value as VoteFormState[typeof field],
    }))
  }

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const success = await onSubmit({
      sessaoVotacaoId,
      cpf: normalizeCpf(formData.cpf),
      vote: formData.vote,
    })

    if (success) {
      setFormData(DEFAULT_FORM)
    }
  }

  return (
    <form className="vote-form" onSubmit={handleSubmit}>
      <label>
        CPF do associado
        <input
          type="text"
          name="cpf"
          value={formData.cpf}
          onChange={handleChange}
          placeholder="000.000.000-00"
          inputMode="numeric"
          maxLength={14}
          required
        />
      </label>

      <label>
        Voto
        <select name="vote" value={formData.vote} onChange={handleChange}>
          <option value="SIM">SIM</option>
          <option value="NAO">NAO</option>
        </select>
      </label>

      <button type="submit" disabled={isSubmitting} className="action-button vote-button">
        {isSubmitting ? 'Enviando voto...' : 'Confirmar voto'}
      </button>

      {successMessage ? <p className="success-text">{successMessage}</p> : null}
    </form>
  )
}

export default VotarForm
