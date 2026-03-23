import './PautasList.css'
import SessaoCard from '../../sessoes/components/SessaoCard'
import type { Pauta, PautaId, SessaoVotacaoResponseDTO } from '../../../types/api'

interface PautasListProps {
  pautas: Pauta[]
  isLoading: boolean
  onReload: () => void | Promise<void>
  isExpanded: (pautaId: PautaId) => boolean
  isSessaoLoading: (pautaId: PautaId) => boolean
  getSessoes: (pautaId: PautaId) => SessaoVotacaoResponseDTO[]
  onTogglePauta: (pautaId: PautaId) => Promise<void>
  onCreateSessao: (pautaId: PautaId) => void
  onViewResultado: (sessaoVotacaoId: string) => void
}

function PautasList({
  pautas,
  isLoading,
  onReload,
  isExpanded,
  isSessaoLoading,
  getSessoes,
  onTogglePauta,
  onCreateSessao,
  onViewResultado,
}: PautasListProps) {
  if (isLoading) {
    return <p className="state-text">Carregando pautas...</p>
  }

  if (pautas.length === 0) {
    return (
      <div className="state-box">
        <p>Nenhuma pauta cadastrada no momento.</p>
        <button type="button" className="action-button" onClick={onReload}>
          Tentar novamente
        </button>
      </div>
    )
  }

  return (
    <ul className="pauta-list">
      {pautas.map((pauta) => (
        <li key={pauta.id} className="pauta-card">
          <button
            type="button"
            className="pauta-toggle"
            onClick={() => onTogglePauta(pauta.id)}
            aria-expanded={isExpanded(pauta.id)}
          >
            <div className="pauta-toggle-main">
              <h3>{`Pauta - ${pauta.name}`}</h3>
              <p>{pauta.description || 'Sem descricao informada.'}</p>
            </div>

            <span className="pauta-toggle-icon" aria-hidden="true">
              {isExpanded(pauta.id) ? '-' : '+'}
            </span>
          </button>

          {isExpanded(pauta.id) ? (
            <section className="sessoes-container">
              <div className="sessoes-header">
                <h4>Sessões</h4>
                <button
                  type="button"
                  className="btn-create-sessao"
                  onClick={() => onCreateSessao(pauta.id)}
                >
                  + Nova Sessão
                </button>
              </div>

              {isSessaoLoading(pauta.id) ? (
                <p className="sessao-state">Carregando sessoes...</p>
              ) : getSessoes(pauta.id).length === 0 ? (
                <p className="sessao-state">Nenhuma sessao para esta pauta.</p>
              ) : (
                <ul className="sessao-list">
                  {getSessoes(pauta.id).map((sessao) => (
                    <li key={sessao.id} className="sessao-list-item">
                      <SessaoCard sessao={sessao} onViewResultado={onViewResultado} />
                    </li>
                  ))}
                </ul>
              )}
            </section>
          ) : null}
        </li>
      ))}
    </ul>
  )
}

export default PautasList
