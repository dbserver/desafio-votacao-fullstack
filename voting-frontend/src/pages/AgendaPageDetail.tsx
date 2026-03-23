import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { MainLayout } from "../layouts/MainLayout";
import { Container } from "../components/Container";
import { searchAgendaById } from "../services/agendaService";
import { openSession } from "../services/sessionService";
import { voteRegister, type VoteEnum } from "../services/vowService";
import { type VoteResult, getVowsResults } from "../services/voteResultService";
import { Toast, useToast } from "../components/Toast";
import { LoadingSpinner } from "../components/LoadingSpinner";
import { Tabs } from "../components/Tabs";
import { AgendaInfoSection } from "../components/AgendaInfoSection";
import { AgendaStatusSection } from "../components/AgendaStatusSection";
import { VotingSection } from "../components/VotingSection";
import { ResultsSection } from "../components/ResultsSection";

export function AgendaPageDetail() {
  const { id } = useParams();
  const { toasts, showToast, removeToast } = useToast();

  const [agenda, setAgenda] = useState<any>(null);
  const [voteResult, setVoteResult] = useState<VoteResult | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isResultLoading, setIsResultLoading] = useState(false);
  const [sessionOpen, setSessionOpen] = useState(false);
  const [createdSession, setCreatedSession] = useState<boolean | false>(false);

  const [activeTab, setActiveTab] = useState("info");

  useEffect(() => {
    loadAgenda();
  }, [id]);

  const loadAgenda = async () => {
    try {
      setIsLoading(true);
      const data = await searchAgendaById(Number(id));
      setAgenda(data);
      setSessionOpen(data.sessionOpen || false);
      setCreatedSession(!!data.createdSession);
    } catch (error) {
      console.error("Erro ao carregar pauta:", error);
      showToast("Erro ao carregar informações da pauta", "error");
    } finally {
      setIsLoading(false);
    }
  };

  const handleOpenSession = async (durationMinutes: number) => {
    try {
      await openSession(Number(id), durationMinutes);
      setSessionOpen(true);
      setCreatedSession(true);
      showToast("✅ Sessão de votação aberta com sucesso!", "success");
      setActiveTab("voting");
    } catch (error) {
      console.error("Erro ao abrir sessão:", error);
      showToast("❌ Erro ao abrir a sessão de votação", "error");
    }
  };

  const handleVote = async (cpf: string, voteType: "YES" | "NO") => {
    try {
      await voteRegister({
        agendaId: Number(id),
        associatedId: cpf,
        vote: voteType as VoteEnum,
      });
      showToast(`✅ Voto registrado com sucesso!`, "success");
      setTimeout(() => {
        setActiveTab("results");
      }, 1500);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || "Erro ao registrar voto";
      showToast(`❌ ${errorMessage}`, "error");
    }
  };

  const fetchVoteResults = async () => {
    try {
      setIsResultLoading(true);
      const data = await getVowsResults(Number(id));
      setVoteResult(data);
      showToast("✅ Resultados carregados com sucesso!", "success");
    } catch (error) {
      console.error("Erro ao carregar resultados:", error);
      showToast("❌ Erro ao carregar resultados", "error");
    } finally {
      setIsResultLoading(false);
    }
  };

  const tabs = [
    { id: "info", label: "📋 Informações", disabled: false },
    { id: "status", label: "🔔 Status", disabled: false },
    { id: "voting", label: "🗳️ Votação", disabled: !sessionOpen },
    { id: "results", label: "📊 Resultados", disabled: false },
  ];

  if (isLoading) {
    return (
      <MainLayout>
        <Container>
          <LoadingSpinner size="lg" message="Carregando pauta..." />
        </Container>
      </MainLayout>
    );
  }

  if (!agenda) {
    return (
      <MainLayout>
        <Container>
          <div className="text-center py-12">
            <p className="text-2xl font-bold text-gray-900 mb-4">
              Pauta não encontrada
            </p>
            <p className="text-gray-600">
              A pauta que você está procurando não existe ou foi removida.
            </p>
          </div>
        </Container>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <Toast toasts={toasts} removeToast={removeToast} />

      <Container>
        <div className="mb-8">
          <div className="mb-6">
            <p className="text-blue-600 font-semibold text-sm">PAUTA Nº {agenda.id}</p>
          </div>
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            {agenda.titulo}
          </h1>
          <div className="h-1 w-20 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-full" />
        </div>

        <Tabs tabs={tabs} activeTab={activeTab} onTabChange={setActiveTab}>
          {activeTab === "info" && <AgendaInfoSection agenda={agenda} />}

          {activeTab === "status" && (
            <AgendaStatusSection
              agendaId={agenda.id}
              sessionOpen={sessionOpen}
              createdSession={createdSession}
              onOpenSession={handleOpenSession}
              onOpenSessionSuccess={() => {
                setSessionOpen(true);
                setCreatedSession(true);
              }}
            />
          )}

          {activeTab === "voting" && (
            <VotingSection
              sessionOpen={sessionOpen}
              onVote={handleVote}
              isLoading={isLoading}
              agendaId={agenda.id}
              onShowToast={showToast}
            />
          )}

          {activeTab === "results" && (
            <ResultsSection
              voteResult={voteResult}
              isLoading={isResultLoading}
              onRefresh={fetchVoteResults}
            />
          )}
        </Tabs>
      </Container>
    </MainLayout>
  );
}