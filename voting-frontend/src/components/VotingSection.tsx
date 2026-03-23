import { useState } from "react";
import { Card } from "./Card";
import { Modal } from "./Modal";
import { LoadingSpinner } from "./LoadingSpinner";
import { Button } from "./Button";

interface VotingSectionProps {
  sessionOpen: boolean;
  onVote: (cpf: string, voteType: "YES" | "NO") => Promise<void>;
  isLoading?: boolean;
  agendaId?: number;
  onShowToast?: (message: string, type: "success" | "error" | "info" | "warning") => void;
}

export function VotingSection({ 
  sessionOpen, 
  onVote,
  isLoading = false,
  agendaId,
  onShowToast
}: VotingSectionProps) {
  const [cpf, setCpf] = useState("");
  const [selectedVote, setSelectedVote] = useState<"YES" | "NO" | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [isVoting, setIsVoting] = useState(false);

  const handleVoteClick = (voteType: "YES" | "NO") => {
    if (!cpf.trim()) {
      alert("Por favor, digite seu CPF antes de votar");
      return;
    }
    setSelectedVote(voteType);
    setShowModal(true);
  };

  const handleConfirmVote = async () => {
    if (!selectedVote) return;
    
    setIsVoting(true);
    try {
      await onVote(cpf, selectedVote);
      setCpf("");
      setSelectedVote(null);
      setShowModal(false);
    } catch (error) {
      console.error("Erro ao votar:", error);
    } finally {
      setIsVoting(false);
    }
  };

  const copyVotingLink = () => {
    if (!agendaId) return;
    const link = `${window.location.origin}/pautas/${agendaId}/votar`;
    navigator.clipboard.writeText(link);
    onShowToast?.("✅ Link de votação copiado!", "success");
  };

  if (!sessionOpen) {
    return (
      <Card title="Votação" subtitle="A sessão de votação não foi iniciada">
        <div className="p-8 text-center bg-gray-50 rounded-lg border-2 border-dashed border-gray-300">
          <p className="text-gray-600 text-lg mb-2">🔒 Votação indisponível</p>
          <p className="text-gray-500">
            A sessão de votação precisa ser aberta primeiro.
          </p>
        </div>
      </Card>
    );
  }

  if (isLoading) {
    return <LoadingSpinner message="Carregando informações de votação..." />;
  }

  return (
    <>
      {/* Link Compartilhável */}
      {agendaId && (
        <Card className="mb-6 bg-gradient-to-r from-blue-50 to-indigo-50 border-2 border-blue-200">
          <div className="flex items-center justify-between gap-4">
            <div>
              <p className="text-sm font-semibold text-gray-700 mb-1">
                🔗 Link de Votação Compartilhável
              </p>
              <p className="text-xs text-gray-600">
                Compartilhe este link para que outras pessoas votem nesta pauta
              </p>
            </div>
            <Button
              variant="primary"
              size="md"
              onClick={copyVotingLink}
              className="whitespace-nowrap"
            >
              📋 Copiar Link
            </Button>
          </div>
        </Card>
      )}

      <Card title="Registrar Voto" subtitle="Escolha seu posicionamento">
        <div className="space-y-6">
          {/* CPF Input */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-3">
              📋 Digite seu CPF
            </label>
            <input
              type="text"
              placeholder="Ex: 123.456.789-00"
              value={cpf}
              onChange={(e) => setCpf(e.target.value)}
              disabled={isVoting}
              maxLength={14}
              className="w-full px-4 py-3 border-2 border-gray-300 rounded-lg focus:border-blue-500 focus:outline-none transition disabled:bg-gray-100 disabled:cursor-not-allowed"
            />
            <p className="text-xs text-gray-500 mt-2">
              Seu CPF é necessário para registrar o voto
            </p>
          </div>

          {/* Vote Buttons */}
          <div>
            <p className="text-sm font-semibold text-gray-700 mb-4">
              👥 Qual é o seu voto?
            </p>
            <div className="grid grid-cols-2 gap-4">
              <button
                onClick={() => handleVoteClick("YES")}
                disabled={!cpf.trim() || isVoting}
                className="p-6 rounded-lg border-3 border-gray-200 hover:border-green-500 hover:bg-green-50 transition font-semibold text-lg disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <div className="text-4xl mb-2">👍</div>
                <div className="text-green-600">SIM</div>
              </button>

              <button
                onClick={() => handleVoteClick("NO")}
                disabled={!cpf.trim() || isVoting}
                className="p-6 rounded-lg border-3 border-gray-200 hover:border-red-500 hover:bg-red-50 transition font-semibold text-lg disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <div className="text-4xl mb-2">👎</div>
                <div className="text-red-600">NÃO</div>
              </button>
            </div>
          </div>

          {/* Info Box */}
          <div className="p-4 bg-blue-50 rounded-lg border border-blue-200">
            <p className="text-sm text-blue-800">
              💡 Seu voto é anônimo e seguro. Você só pode votar uma vez com o mesmo CPF.
            </p>
          </div>
        </div>
      </Card>

      <Modal
        isOpen={showModal}
        title={selectedVote === "YES" ? "Confirmar voto SIM" : "Confirmar voto NÃO"}
        message={`Tem certeza que deseja votar ${selectedVote === "YES" ? "SIM" : "NÃO"}? Esta ação não pode ser desfeita.`}
        onConfirm={handleConfirmVote}
        onCancel={() => {
          setShowModal(false);
          setSelectedVote(null);
        }}
        confirmText="Confirmar voto"
        cancelText="Cancelar"
        isLoading={isVoting}
      >
        <div className="p-4 bg-gray-100 rounded-lg text-center">
          <p className="text-sm text-gray-600">CPF: <span className="font-semibold">{cpf}</span></p>
        </div>
      </Modal>
    </>
  );
}
