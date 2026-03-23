import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, expect, it, vi } from 'vitest'
import VotarForm from './VotarForm'

describe('VotarForm', () => {
  it('should mask cpf input and submit normalized payload', async () => {
    const onSubmit = vi.fn(async () => true)
    const user = userEvent.setup()

    render(
      <VotarForm
        sessaoVotacaoId="sessao-123"
        onSubmit={onSubmit}
        isSubmitting={false}
        successMessage=""
      />,
    )

    const cpfInput = screen.getByLabelText('CPF do associado')
    await user.type(cpfInput, '12345678901')
    expect(cpfInput).toHaveValue('123.456.789-01')

    await user.selectOptions(screen.getByLabelText('Voto'), 'NAO')
    await user.click(screen.getByRole('button', { name: 'Confirmar voto' }))

    expect(onSubmit).toHaveBeenCalledWith({
      sessaoVotacaoId: 'sessao-123',
      cpf: '12345678901',
      vote: 'NAO',
    })
  })
})
