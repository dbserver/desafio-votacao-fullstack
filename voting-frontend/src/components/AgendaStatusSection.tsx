import { useState } from "react";
import { Button } from "./Button";
import { Card } from "./Card";
import { StatusBadge } from "./StatusBadge";

interface AgendaStatusSectionProps {
  agendaId?: number;
  sessionOpen: boolean;
  createdSession: boolean;
  onOpenSession: (durationMinutes: number) => Promise<void>;
  onOpenSessionSuccess: () => void;
}

export function AgendaStatusSection({
  sessionOpen,
  onOpenSession,
  createdSession,
  onOpenSessionSuccess,
}: AgendaStatusSectionProps) {
  const [showModal, setShowModal] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [durationMinutes, setDurationMinutes] = useState<string>("5");
  const [error, setError] = useState<string>("");

  const handleOpenSession = async () => {
    setError("");
    
    if (!durationMinutes || Number(durationMinutes) <= 0) {
      setError("Por favor, informe uma duração válida (maior que 0)");
      return;
    }

    setIsLoading(true);
    try {
      await onOpenSession(Number(durationMinutes));
      onOpenSessionSuccess();
      setShowModal(false);
      setDurationMinutes("5");
    } catch (error) {
      console.error("Erro ao abrir sessão:", error);
      setError("Erro ao abrir a sessão. Tente novamente.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <div className="space-y-6">
        <Card title="Status da Sessão" subtitle="Gerenciamento da sessão de votação">
          <div className="space-y-6">
            {
              createdSession && (
                <div className="flex items-center justify-between p-6 bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg border border-blue-200">
                  <div>
                    <p className="text-gray-600 mb-2">Estado atual da sessão</p>
                    <p className="text-2xl font-bold text-gray-900">
                      {sessionOpen ? "🟢 Aberta" : "🔴 Fechada"}
                    </p>
                  </div>
                  <StatusBadge
                    status={sessionOpen ? "open" : "closed"}
                    label={sessionOpen ? "Sessão Aberta" : "Sessão Fechada"}
                  />
                </div>
              )
            }

            {!createdSession && (
              <div className="p-6 bg-yellow-50 rounded-lg border border-yellow-200">
                <h4 className="font-semibold text-yellow-900 mb-2">
                  ⚠️ Sessão de votação ainda não foi iniciada
                </h4>
                <p className="text-yellow-800 text-sm mb-4">
                  Para iniciar o processo de votação, clique no botão abaixo para abrir a sessão.
                </p>
                <Button
                  variant="success"
                  size="lg"
                  onClick={() => setShowModal(true)}
                  className="w-full md:w-auto"
                >
                  🔓 Abrir Sessão de Votação
                </Button>
              </div>
            )}

            {sessionOpen && (
              <div className="p-6 bg-green-50 rounded-lg border border-green-200">
                <h4 className="font-semibold text-green-900 mb-2">
                  ✓ Sessão de votação ativa!
                </h4>
                <p className="text-green-800 text-sm">
                  A votação já começou. Os eleitores podem registrar seus votos.
                </p>
              </div>
            )}
          </div>
        </Card>
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-black/50 bg-opacity-30 backdrop-blur-sm flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
            <h2 className="text-2xl font-bold text-gray-900 mb-2">
              Abrir Sessão de Votação
            </h2>
            <p className="text-gray-600 mb-6">
              Informe por quantos minutos a sessão ficará aberta para votação.
            </p>

            <div className="mb-6">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Duração da Sessão (minutos)
              </label>
              <input
                type="number"
                min="1"
                value={durationMinutes}
                onChange={(e) => {
                  setDurationMinutes(e.target.value);
                  setError("");
                }}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Ex: 5, 10, 30"
              />
              <p className="text-xs text-gray-500 mt-1">
                Mínimo 1 minuto
              </p>
            </div>

            {error && (
              <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded text-red-700 text-sm">
                {error}
              </div>
            )}

            <div className="flex gap-3">
              <Button
                variant="secondary"
                onClick={() => {
                  setShowModal(false);
                  setError("");
                  setDurationMinutes("5");
                }}
                disabled={isLoading}
                className="flex-1"
              >
                Cancelar
              </Button>
              <Button
                variant="success"
                onClick={handleOpenSession}
                loading={isLoading}
                className="flex-1"
              >
                Abrir Sessão
              </Button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
