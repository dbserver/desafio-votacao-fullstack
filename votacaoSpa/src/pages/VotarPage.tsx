import { Navigate, useNavigate, useParams } from 'react-router-dom'
import VotarForm from '../features/votacao/components/VotarForm'
import { useVotar } from '../features/votacao/hooks/useVotar'
import { useHttpErrorToast } from '../hooks/useHttpErrorToast'
import type { VotePayload } from '../types/api'
import './VotarPage.css'

function VotarPage() {
  const { pautaId, sessaoId } = useParams<{ pautaId?: string; sessaoId?: string }>()
  const navigate = useNavigate()
  const { submitVote, isSubmitting, error, successMessage } = useVotar()

  useHttpErrorToast(error, 'Falha ao registrar voto.')

  if (!pautaId || !sessaoId) {
    return <Navigate to="/pautas" replace />
  }

  const handleVoteSuccess = async (payload: VotePayload) => {
    const success = await submitVote(payload)
    if (success) {
      setTimeout(() => navigate('/pautas'), 1500)
    }
    return success
  }

  return (
    <section>
      <button
        type="button"
        onClick={() => navigate('/pautas')}
        className="back-to-pautas-button"
      >
        Voltar para pautas
      </button>

      <h2>Votacao</h2>
      <p>Registre o voto de um associado para a pauta.</p>

      {sessaoId && <p className="sessao-context">Sessão: {sessaoId}</p>}

      <VotarForm
        sessaoVotacaoId={sessaoId}
        onSubmit={handleVoteSuccess}
        isSubmitting={isSubmitting}
        successMessage={successMessage}
      />
    </section>
  )
}
export default VotarPage
