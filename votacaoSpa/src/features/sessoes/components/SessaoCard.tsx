import { useNavigate } from 'react-router-dom'
import './SessaoCard.css'
import type { SessaoVotacaoResponseDTO } from '../../../types/api'

interface SessaoCardProps {
  sessao: SessaoVotacaoResponseDTO
  onViewResultado: (sessaoVotacaoId: string) => void
}

function formatDateTime(dateIso: string) {
  return new Intl.DateTimeFormat('pt-BR', {
    dateStyle: 'short',
    timeStyle: 'short',
  }).format(new Date(dateIso))
}

function SessaoCard({ sessao, onViewResultado }: SessaoCardProps) {
  const navigate = useNavigate()
  const finalDateTime = new Date(sessao.finalSessionDateTime)
  const now = new Date()
  const isClosed = finalDateTime < now
  const statusClass = isClosed ? 'sessao-card--closed' : 'sessao-card--active'

  const handleClick = (e: React.MouseEvent) => {
    e.preventDefault()
    e.stopPropagation()
    if (isClosed) {
      return
    }

    const path = `/votar/${encodeURIComponent(String(sessao.pautaId))}/${encodeURIComponent(String(sessao.id))}`
    navigate(path)
  }

  return (
    <div className={`sessao-card ${statusClass}`}>
      <button
        type="button"
        className={`sessao-card-main ${!isClosed ? 'sessao-card-main--clickable' : ''}`}
        onClick={handleClick}
      >
        <p>
          <strong>ID:</strong> {sessao.id}
        </p>
        <p>
          <strong>Abertura:</strong> {formatDateTime(sessao.createdAt)}
        </p>
        <p>
          <strong>Encerramento:</strong> {formatDateTime(sessao.finalSessionDateTime)}
        </p>
      </button>

      {isClosed ? (
        <button
          type="button"
          className="sessao-result-button"
          onClick={() => onViewResultado(sessao.id)}
        >
          Ver resultado
        </button>
      ) : (
        <p className="sessao-open-hint">Clique no card para votar</p>
      )}
    </div>
  )
}

export default SessaoCard
