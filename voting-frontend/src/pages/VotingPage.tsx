import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { MainLayout } from "../layouts/MainLayout";
import { Container } from "../components/Container";
import { Card } from "../components/Card";
import { Button } from "../components/Button";
import { LoadingSpinner } from "../components/LoadingSpinner";
import { Toast, useToast } from "../components/Toast";
import { Modal } from "../components/Modal";
import { searchAgendaById } from "../services/agendaService";
import { voteRegister, type VoteEnum } from "../services/vowService";

interface Agenda {
  id: number;
  title: string;
  description: string;
  timer: string;
  createdAt?: string;
  sessionOpen: boolean;
}

export function VotingPage() {
  const { id } = useParams();
  const { toasts, showToast, removeToast } = useToast();

  const [agenda, setAgenda] = useState<Agenda | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [cpf, setCpf] = useState("");
  const [selectedVote, setSelectedVote] = useState<"YES" | "NO" | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [isVoting, setIsVoting] = useState(false);
  const [timeRemaining, setTimeRemaining] = useState(0);

  const formatCPF = (value: string): string => {
    const digits = value.replace(/\D/g, "").slice(0, 11);
    if (digits.length <= 3) return digits;
    if (digits.length <= 6) return `${digits.slice(0, 3)}.${digits.slice(3)}`;
    if (digits.length <= 9) return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6)}`;
    return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6, 9)}-${digits.slice(9)}`;
  };

  const removeCPFFormatting = (value: string): string => {
    return value.replace(/\D/g, "");
  };

  const timeStringToSeconds = (timeString: string): number => {
    const parts = timeString.split(':');
    const hours = parseInt(parts[0], 10) || 0;
    const minutes = parseInt(parts[1], 10) || 0;
    const seconds = parseInt(parts[2], 10) || 0;
    return hours * 3600 + minutes * 60 + seconds;
  };

  useEffect(() => {
    loadAgenda();
  }, [id]);

  useEffect(() => {
    if (!agenda) return;

    const timer = setInterval(() => {
      setTimeRemaining((prev) => (prev > 0 ? prev - 1 : 0));
    }, 1000);

    return () => clearInterval(timer);
  }, [agenda]);

  const loadAgenda = async () => {
    try {
      setIsLoading(true);
      const data = await searchAgendaById(Number(id));
      setAgenda(data);
      const timeInSeconds = timeStringToSeconds(data.timer);
      setTimeRemaining(timeInSeconds);
    } catch (error) {
      console.error("Erro ao carregar pauta:", error);
      showToast("Erro ao carregar pauta", "error");
    } finally {
      setIsLoading(false);
    }
  };

  const handleVoteClick = (voteType: "YES" | "NO") => {
    if (!cpf.trim()) {
      showToast("Por favor, digite seu CPF antes de votar", "warning");
      return;
    }
    setSelectedVote(voteType);
    setShowModal(true);
  };

  const handleConfirmVote = async () => {
    if (!selectedVote) return;

    setIsVoting(true);
    try {
      await voteRegister({
        agendaId: Number(id),
        associatedId: removeCPFFormatting(cpf),
        vote: selectedVote as VoteEnum,
      });
      showToast("✅ Voto registrado com sucesso!", "success");
      setCpf("");
      setSelectedVote(null);
      setShowModal(false);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || "Erro ao registrar voto";
      showToast(`❌ ${errorMessage}`, "error");
      setShowModal(false);
      setSelectedVote(null);
    } finally {
      setIsVoting(false);
    }
  };

  const formatTime = (seconds: number) => {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    return `${hours.toString().padStart(2, "0")}:${minutes.toString().padStart(2, "0")}:${secs.toString().padStart(2, "0")}`;
  };

  const copyVotingLink = () => {
    const link = `${window.location.origin}/pautas/${id}/votar`;
    navigator.clipboard.writeText(link);
    showToast("✅ Link de votação copiado!", "success");
  };

  if (isLoading) {
    return (
      <MainLayout hideNavigation>
        <Container>
          <LoadingSpinner size="lg" message="Carregando pauta..." />
        </Container>
      </MainLayout>
    );
  }

  if (!agenda) {
    return (
      <MainLayout hideNavigation>
        <Container>
          <div className="text-center py-12">
            <p className="text-2xl font-bold text-gray-900 mb-4">
              Pauta não encontrada
            </p>
          </div>
        </Container>
      </MainLayout>
    );
  }

  return (
    <MainLayout hideNavigation>
      <Toast toasts={toasts} removeToast={removeToast} />

      <Container>
        <div className="flex gap-4 mb-8 justify-end">
          <Button
            variant="ghost"
            onClick={copyVotingLink}
          >
            📋 Copiar link de votação
          </Button>
        </div>

        <div className="mb-8">
          <p className="text-blue-600 font-semibold text-sm mb-2">
            🗳️ VOTAÇÃO COMPARTILHADA
          </p>
          <div className="h-1 w-20 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-full" />
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2">
            <Card title={agenda.title}>
              <p className="text-gray-700 text-lg leading-relaxed">
                {agenda.description}
              </p>
            </Card>

            <div className="mt-6">
              <Card title="Registrar Seu Voto">
              <div className="space-y-6">
                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-3">
                    📋 Digite seu CPF
                  </label>
                  <input
                    type="text"
                    placeholder="Ex: 123.456.789-00"
                    value={cpf}
                    onChange={(e) => setCpf(formatCPF(e.target.value))}
                    disabled={isVoting || !agenda.sessionOpen || timeRemaining === 0}
                    maxLength={14}
                    className="w-full px-4 py-3 border-2 border-gray-300 rounded-lg focus:border-blue-500 focus:outline-none transition disabled:bg-gray-100"
                  />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <button
                    onClick={() => handleVoteClick("YES")}
                    disabled={!cpf.trim() || isVoting || !agenda.sessionOpen || timeRemaining === 0}
                    className="p-6 rounded-lg border-3 border-gray-200 hover:border-green-500 hover:bg-green-50 transition font-semibold text-lg disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    <div className="text-4xl mb-2">👍</div>
                    <div className="text-green-600">SIM</div>
                  </button>

                  <button
                    onClick={() => handleVoteClick("NO")}
                    disabled={!cpf.trim() || isVoting || !agenda.sessionOpen || timeRemaining === 0}
                    className="p-6 rounded-lg border-3 border-gray-200 hover:border-red-500 hover:bg-red-50 transition font-semibold text-lg disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    <div className="text-4xl mb-2">👎</div>
                    <div className="text-red-600">NÃO</div>
                  </button>
                </div>

                <div className="p-4 bg-blue-50 rounded-lg border border-blue-200">
                  <p className="text-sm text-blue-800">
                    💡 Seu voto é anônimo e seguro. Você só pode votar uma vez com o mesmo CPF.
                  </p>
                </div>

                {(!agenda.sessionOpen || timeRemaining === 0) && (
                  <div className="p-4 bg-red-50 rounded-lg border border-red-200">
                    <p className="text-sm text-red-800 font-semibold">
                      🔒 Sessão de votação encerrada
                    </p>
                    <p className="text-xs text-red-700 mt-1">
                      Não é mais possível registrar votos. Obrigado pela participação!
                    </p>
                  </div>
                )}
              </div>
            </Card>
            </div>
          </div>

          <div className="lg:col-span-1">
            {agenda.sessionOpen && timeRemaining > 0 ? (
              <Card title="⏱️ Tempo Restante">
                <div className="text-center">
                  <div className="text-5xl font-bold text-blue-600 font-mono mb-3">
                    {formatTime(timeRemaining)}
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2 overflow-hidden">
                    <div
                      className="bg-gradient-to-r from-green-500 to-blue-500 h-full transition-all duration-1000"
                      style={{ width: `${timeRemaining > 0 ? (timeRemaining / 3600) * 100 : 0}%` }}
                    />
                  </div>
                  <p className="text-sm text-gray-600 mt-3">
                    até o encerramento
                  </p>
                </div>
              </Card>
            ) : (
              <Card title="⏱️ Status da Sessão">
                <div className="text-center">
                  <div className="text-6xl mb-4">🔒</div>
                  <p className="text-xl font-bold text-red-600 mb-2">
                    Sessão Encerrada
                  </p>
                  <p className="text-sm text-gray-600">
                    A sessão de votação foi finalizada e não aceita mais votos.
                  </p>
                </div>
              </Card>
            )}
          </div>
        </div>
      </Container>

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
    </MainLayout>
  );
}
