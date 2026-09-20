import { CheckCircle2, MinusCircle, RefreshCw, XCircle } from 'lucide-react';

import { FeedbackMessage } from '@/components/ui/FeedbackMessage';
import { LoadingSpinner } from '@/components/ui/LoadingSpinner';
import { buttonSecondary } from '@/components/ui/styles';
import type { ResultadoVotacao } from '@/types/domain';
import { cn } from '@/utils/cn';

interface PainelResultadosProps {
  resultados: ResultadoVotacao[];
  loading?: boolean;
  error?: string | null;
  onRefresh?: () => void;
}

export function PainelResultados({
  resultados,
  loading = false,
  error = null,
  onRefresh,
}: PainelResultadosProps) {
  return (
    <div className="mx-auto flex max-w-4xl flex-col gap-8">
      <div className="flex flex-col justify-between gap-4 sm:flex-row sm:items-center">
        <div>
          <h2 className="text-2xl font-bold text-neutral-900">Resultados da Votação</h2>
          <p className="mt-1 text-sm text-neutral-500">
            Acompanhe a apuração dos votos e o placar de cada pauta.
          </p>
        </div>

        {onRefresh && (
          <button
            type="button"
            onClick={onRefresh}
            disabled={loading}
            className={cn(buttonSecondary, 'self-start sm:self-auto')}
          >
            <RefreshCw size={16} className={cn(loading && 'animate-spin')} />
            Atualizar
          </button>
        )}
      </div>

      {error && <FeedbackMessage variant="error" message={error} />}

      {loading && resultados.length === 0 ? (
        <LoadingSpinner label="Carregando resultados…" />
      ) : resultados.length === 0 ? (
        <div className="rounded-xl border border-neutral-200 bg-white p-8 text-center text-neutral-500">
          Nenhuma pauta cadastrada até o momento.
        </div>
      ) : (
        <div className="flex flex-col gap-4">
          {resultados.map((res) => {
            const porcentagemSim =
              res.totalVotos > 0 ? Math.round((res.votosSim / res.totalVotos) * 100) : 0;
            const porcentagemNao =
              res.totalVotos > 0 ? Math.round((res.votosNao / res.totalVotos) * 100) : 0;

            const isEmAndamento =
              !res.sessaoEncerrada || res.resultado === 'SESSÃO EM ANDAMENTO';

            return (
              <div
                key={res.pautaId}
                className="flex flex-col gap-4 rounded-xl border border-neutral-200 bg-white p-6 shadow-xs"
              >
                <div className="flex flex-col justify-between gap-2 sm:flex-row sm:items-start">
                  <div>
                    <h3 className="text-lg font-semibold text-neutral-900">{res.tituloPauta}</h3>
                  </div>

                  {/* Badge de Resultado */}
                  <div className="flex items-center gap-2 self-start sm:self-auto">
                    {isEmAndamento ? (
                      <span className="inline-flex items-center gap-1.5 rounded-full bg-blue-50 px-3 py-1 text-xs font-medium text-blue-700">
                        <span className="h-1.5 w-1.5 animate-pulse rounded-full bg-blue-600" />
                        Sessão em andamento
                      </span>
                    ) : res.resultado === 'APROVADA' ? (
                      <span className="inline-flex items-center gap-1.5 rounded-full bg-emerald-50 px-3 py-1 text-xs font-medium text-emerald-700">
                        <CheckCircle2 size={14} />
                        Aprovada
                      </span>
                    ) : res.resultado === 'REPROVADA' ? (
                      <span className="inline-flex items-center gap-1.5 rounded-full bg-red-50 px-3 py-1 text-xs font-medium text-red-700">
                        <XCircle size={14} />
                        Reprovada
                      </span>
                    ) : (
                      <span className="inline-flex items-center gap-1.5 rounded-full bg-amber-50 px-3 py-1 text-xs font-medium text-amber-700">
                        <MinusCircle size={14} />
                        Empate
                      </span>
                    )}
                  </div>
                </div>

                {/* Placar de Votos e Barra Visual */}
                <div className="flex flex-col gap-2">
                  <div className="flex justify-between text-xs font-medium text-neutral-600">
                    <span>
                      Sim: <strong className="text-emerald-600">{res.votosSim}</strong> ({porcentagemSim}%)
                    </span>
                    <span>
                      Total de Votos: <strong>{res.totalVotos}</strong>
                    </span>
                    <span>
                      Não: <strong className="text-red-600">{res.votosNao}</strong> ({porcentagemNao}%)
                    </span>
                  </div>

                  <div className="flex h-3 w-full overflow-hidden rounded-full bg-neutral-100">
                    <div
                      style={{ width: `${porcentagemSim}%` }}
                      className="bg-emerald-500 transition-all duration-300"
                    />
                    <div
                      style={{ width: `${porcentagemNao}%` }}
                      className="bg-red-500 transition-all duration-300"
                    />
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}