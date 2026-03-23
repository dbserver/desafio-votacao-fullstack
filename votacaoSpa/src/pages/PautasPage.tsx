import { useState } from 'react'
import PautasList from '../features/pautas/components/PautasList'
import CreatePautaModal from '../features/pautas/components/CreatePautaModal'
import CreateSessaoModal from '../features/sessoes/components/CreateSessaoModal'
import VotingResultModal from '../features/sessoes/components/VotingResultModal'
import { usePautaSessoes } from '../features/sessoes/hooks/usePautaSessoes'
import { usePautas } from '../features/pautas/hooks/usePautas'
import { useCreateSessao } from '../features/sessoes/hooks/useCreateSessao'
import { useSessaoResultado } from '../features/sessoes/hooks/useSessaoResultado'
import { useToast } from '../components/ui/ToastProvider'
import { useHttpErrorToast } from '../hooks/useHttpErrorToast'
import type { CreatePautaPayload, PautaId } from '../types/api'
import './PautasPage.css'

function PautasPage() {
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [sessaoModalOpen, setSessaoModalOpen] = useState(false)
  const [resultModalOpen, setResultModalOpen] = useState(false)
  const [selectedPautaId, setSelectedPautaId] = useState<PautaId | null>(null)
  const { addToast } = useToast()
  const { pautas, isLoading, isCreating, error, reloadPautas, createPauta } = usePautas()
  const {
    error: sessaoError,
    isExpanded,
    isLoading: isSessaoLoading,
    getSessoes,
    toggle,
    setSessoes,
  } = usePautaSessoes()
  const { isSubmitting: isCreatingSessao, error: createSessaoError, submit: submitSessao } = useCreateSessao()
  const {
    isLoading: isResultadoLoading,
    error: resultadoError,
    resultado,
    loadResultado,
    clearResultado,
  } = useSessaoResultado()

  useHttpErrorToast(error, 'Falha ao carregar pautas.')
  useHttpErrorToast(sessaoError, 'Falha ao carregar sessoes da pauta.')
  useHttpErrorToast(createSessaoError, 'Falha ao criar sessao.')
  useHttpErrorToast(resultadoError, 'Falha ao carregar resultado da sessao.')

  const handleCreatePauta = async (payload: CreatePautaPayload) => {
    const success = await createPauta(payload)

    if (success) {
      addToast({
        type: 'info',
        title: 'Pauta cadastrada',
        message: 'A pauta foi cadastrada com sucesso.',
      })
    }

    return success
  }

  const handleOpenSessaoModal = (pautaId: PautaId) => {
    setSelectedPautaId(pautaId)
    setSessaoModalOpen(true)
  }

  const handleCreateSessao = async (durationInSeconds?: number) => {
    if (!selectedPautaId) return

    const payload = {
      pautaId: String(selectedPautaId),
      ...(typeof durationInSeconds === 'number' ? { durationInSeconds } : {}),
    }

    const result = await submitSessao(payload)

    if (result) {
      addToast({
        type: 'info',
        title: 'Sessão criada',
        message: 'A sessão foi criada com sucesso.',
      })

      const currentSessoes = getSessoes(selectedPautaId)
      const nextSessoes = [
        result,
        ...currentSessoes.filter((sessao) => sessao.id !== result.id),
      ]
      setSessoes(String(selectedPautaId), nextSessoes)
      setSessaoModalOpen(false)
      setSelectedPautaId(null)
    }
  }

  const handleViewResultado = async (sessaoVotacaoId: string) => {
    setResultModalOpen(true)
    await loadResultado(sessaoVotacaoId)
  }

  return (
    <section>
      <div className="pautas-page-header">
        <h2>Pautas</h2>
        <button
          type="button"
          className="pautas-add-button"
          aria-label="Cadastrar nova pauta"
          onClick={() => setIsModalOpen(true)}
        >
          +
        </button>
      </div>

      <p className="pautas-page-description">Consulte as pautas disponiveis para votacao.</p>

      <PautasList
        pautas={pautas}
        isLoading={isLoading}
        onReload={reloadPautas}
        isExpanded={isExpanded}
        isSessaoLoading={isSessaoLoading}
        getSessoes={getSessoes}
        onTogglePauta={toggle}
        onCreateSessao={handleOpenSessaoModal}
        onViewResultado={handleViewResultado}
      />

      <CreatePautaModal
        isOpen={isModalOpen}
        isSubmitting={isCreating}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleCreatePauta}
      />

      <CreateSessaoModal
        isOpen={sessaoModalOpen}
        isSubmitting={isCreatingSessao}
        pautaId={selectedPautaId || ''}
        onClose={() => {
          setSessaoModalOpen(false)
          setSelectedPautaId(null)
        }}
        onSubmit={handleCreateSessao}
      />

      <VotingResultModal
        isOpen={resultModalOpen}
        isLoading={isResultadoLoading}
        resultado={resultado}
        onClose={() => {
          setResultModalOpen(false)
          clearResultado()
        }}
      />
    </section>
  )
}

export default PautasPage
