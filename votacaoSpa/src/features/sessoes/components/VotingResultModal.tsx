import './VotingResultModal.css'
import type { VotingResultDTO } from '../../../types/api'

interface VotingResultModalProps {
  isOpen: boolean
  isLoading: boolean
  resultado: VotingResultDTO | null
  onClose: () => void
}

function VotingResultModal({ isOpen, isLoading, resultado, onClose }: VotingResultModalProps) {
  if (!isOpen) {
    return null
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content result-modal" onClick={(event) => event.stopPropagation()}>
        <div className="modal-header">
          <h2>Resultado da votação</h2>
          <button
            type="button"
            className="modal-close-btn"
            onClick={onClose}
            aria-label="Fechar modal"
          >
            x
          </button>
        </div>

        {isLoading ? (
          <p className="result-loading">Carregando resultado...</p>
        ) : resultado ? (
          <div className="result-grid">
            <div className="result-item">
              <span>Total de votos</span>
              <strong>{resultado.totalVotes}</strong>
            </div>
            <div className="result-item result-item--yes">
              <span>Votos SIM</span>
              <strong>{resultado.votesYes}</strong>
            </div>
            <div className="result-item result-item--no">
              <span>Votos NAO</span>
              <strong>{resultado.votesNo}</strong>
            </div>
            <div className="result-item result-item--final">
              <span>Resultado final</span>
              <strong>{resultado.result}</strong>
            </div>
          </div>
        ) : (
          <p className="result-loading">Resultado indisponivel para esta sessao.</p>
        )}
      </div>
    </div>
  )
}

export default VotingResultModal
