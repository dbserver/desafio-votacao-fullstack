import { useEffect, useState } from 'react';
import { useResultadoWebSocket } from '../hooks/useResultadoWebSocket';
import type { Resultado } from '../types';

interface ResultadoVotacaoProps {
  sessaoId: string;
  resultadoInicial: Resultado;
}

function formatPercent(value: number): string {
  return `${value}%`;
}

export function ResultadoVotacao({ sessaoId, resultadoInicial }: ResultadoVotacaoProps) {
  const [resultado, setResultado] = useState<Resultado>(resultadoInicial);

  useEffect(() => {
    setResultado(resultadoInicial);
  }, [resultadoInicial]);

  useResultadoWebSocket(sessaoId, setResultado);

  const total = resultado.total;
  const simPercent = Math.round((resultado.sim / Math.max(total, 1)) * 100);
  const naoPercent = Math.round((resultado.nao / Math.max(total, 1)) * 100);
  const aoVivo = !resultado.encerrada;
  const statusClasses = aoVivo ? 'bg-emerald-50 text-emerald-700' : 'bg-rose-50 text-rose-700';

  return (
    <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-soft">
      <div className="flex flex-wrap items-start justify-between gap-4">
        <div>
          <p className="text-sm font-medium text-indigo-700">Resultado</p>
          <h3 className="mt-1 text-xl font-semibold text-slate-900">Apuração da sessão</h3>
        </div>

        <div className={`rounded-full px-3 py-1 text-xs font-semibold ${statusClasses}`}>
          {aoVivo ? 'Ao vivo' : 'Encerrada'}
        </div>
      </div>

      <div className="mt-5 grid gap-3 sm:grid-cols-3">
        <div className="rounded-2xl bg-indigo-50 p-4">
          <p className="text-sm text-slate-600">Sim</p>
          <p className="mt-2 text-2xl font-semibold text-slate-900">{resultado.sim}</p>
        </div>
        <div className="rounded-2xl bg-rose-50 p-4">
          <p className="text-sm text-slate-600">Não</p>
          <p className="mt-2 text-2xl font-semibold text-slate-900">{resultado.nao}</p>
        </div>
        <div className="rounded-2xl bg-slate-100 p-4">
          <p className="text-sm text-slate-600">Total</p>
          <p className="mt-2 text-2xl font-semibold text-slate-900">{resultado.total}</p>
        </div>
      </div>

      <div className="mt-6">
        <div className="mb-2 flex items-center justify-between text-sm text-slate-600">
          <span>Distribuição dos votos</span>
          <span>
            {formatPercent(simPercent)} sim / {formatPercent(naoPercent)} não
          </span>
        </div>

        <div className="h-4 overflow-hidden rounded-full bg-slate-200">
          <div className="flex h-full w-full">
            <div
              className="h-full bg-indigo-600 transition-all duration-500"
              style={{ width: `${simPercent}%` }}
              aria-hidden="true"
            />
            <div
              className="h-full bg-slate-400 transition-all duration-500"
              style={{ width: `${naoPercent}%` }}
              aria-hidden="true"
            />
          </div>
        </div>
      </div>

      {!aoVivo ? null : (
        <p className="mt-4 text-sm font-medium text-emerald-700">· Atualizando em tempo real</p>
      )}
    </section>
  );
}
