import { Button } from "./Button";
import { Card } from "./Card";
import { LoadingSpinner } from "./LoadingSpinner";
import { StatusBadge } from "./StatusBadge";

interface VoteResult {
  totalYes: number;
  totalNo: number;
}

interface ResultsSectionProps {
  voteResult: VoteResult | null;
  isLoading: boolean;
  onRefresh: () => Promise<void>;
}

export function ResultsSection({ 
  voteResult, 
  isLoading,
  onRefresh 
}: ResultsSectionProps) {
  const totalVotes = voteResult ? voteResult.totalYes + voteResult.totalNo : 0;
  const yesPercentage = totalVotes > 0 ? (voteResult!.totalYes / totalVotes) * 100 : 0;
  const noPercentage = totalVotes > 0 ? (voteResult!.totalNo / totalVotes) * 100 : 0;

  return (
    <div className="space-y-6">
      {isLoading && (
        <LoadingSpinner message="Carregando resultados..." />
      )}

      {!voteResult && !isLoading && (
        <Card>
          <div className="p-8 text-center">
            <p className="text-gray-600 text-lg mb-4">
              📊 Nenhum resultado disponível
            </p>
            <p className="text-gray-500 mb-6">
              Clique no botão abaixo para carregar os resultados da votação
            </p>
            <Button
              variant="primary"
              size="lg"
              onClick={onRefresh}
              loading={isLoading}
            >
              Carregar Resultados
            </Button>
          </div>
        </Card>
      )}

      {voteResult && (
        <>
          {/* Summary Cards */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <Card className="text-center">
              <p className="text-gray-500 text-sm mb-2">Total de Votos</p>
              <p className="text-4xl font-bold text-blue-600">{totalVotes}</p>
            </Card>

            <Card className="text-center bg-green-50">
              <p className="text-gray-500 text-sm mb-2">Votos SIM</p>
              <p className="text-4xl font-bold text-green-600">
                {voteResult.totalYes}
              </p>
              <p className="text-sm text-green-600 mt-2">
                {yesPercentage.toFixed(1)}%
              </p>
            </Card>

            <Card className="text-center bg-red-50">
              <p className="text-gray-500 text-sm mb-2">Votos NÃO</p>
              <p className="text-4xl font-bold text-red-600">
                {voteResult.totalNo}
              </p>
              <p className="text-sm text-red-600 mt-2">
                {noPercentage.toFixed(1)}%
              </p>
            </Card>
          </div>

          {/* Progress Bars */}
          <Card title="Distribuição de Votos" subtitle="Visualização do resultado">
            <div className="space-y-8">
              {/* SIM */}
              <div>
                <div className="flex justify-between items-center mb-3">
                  <div className="flex items-center gap-2">
                    <span className="text-2xl">👍</span>
                    <span className="font-semibold text-gray-900">Votos SIM</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-2xl font-bold text-green-600">
                      {voteResult.totalYes}
                    </span>
                    <StatusBadge
                      status="success"
                      label={`${yesPercentage.toFixed(1)}%`}
                    />
                  </div>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-4 overflow-hidden">
                  <div
                    className="bg-green-500 h-full rounded-full transition-all duration-500"
                    style={{ width: `${yesPercentage}%` }}
                  />
                </div>
              </div>

              {/* NÃO */}
              <div>
                <div className="flex justify-between items-center mb-3">
                  <div className="flex items-center gap-2">
                    <span className="text-2xl">👎</span>
                    <span className="font-semibold text-gray-900">Votos NÃO</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-2xl font-bold text-red-600">
                      {voteResult.totalNo}
                    </span>
                    <StatusBadge
                      status="error"
                      label={`${noPercentage.toFixed(1)}%`}
                    />
                  </div>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-4 overflow-hidden">
                  <div
                    className="bg-red-500 h-full rounded-full transition-all duration-500"
                    style={{ width: `${noPercentage}%` }}
                  />
                </div>
              </div>

              {/* Resultado Final */}
              <div className="pt-6 border-t border-gray-200 text-center">
                <p className="text-sm text-gray-600 mb-3">Resultado</p>
                <div className="inline-block">
                  <StatusBadge
                    status={voteResult.totalYes > voteResult.totalNo ? "success" : "error"}
                    label={
                      voteResult.totalYes > voteResult.totalNo
                        ? "✓ APROVADO"
                        : voteResult.totalNo > voteResult.totalYes
                        ? "✕ REPROVADO"
                        : "⚠ EMPATE"
                    }
                    className="text-base px-6 py-3"
                  />
                </div>
              </div>
            </div>
          </Card>

          {/* Refresh Button */}
          <div className="flex justify-center">
            <Button
              variant="secondary"
              onClick={onRefresh}
              loading={isLoading}
            >
              🔄 Atualizar Resultados
            </Button>
          </div>
        </>
      )}
    </div>
  );
}
