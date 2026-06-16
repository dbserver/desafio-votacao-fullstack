import { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';
import { ModalNovaPauta } from '../components/ModalNovaPauta';
import { listarPautas } from '../services/pautaService';
import { buscarSessaoPorPauta } from '../services/sessaoService';
import type { PautaResponse } from '../types/api';

const TAMANHO_PAGINA = 6;

type StatusSessao = 'aberta' | 'encerrada' | 'sem_sessao' | 'indisponivel';

function StatusSessaoBadge({ status }: { status: StatusSessao }) {
  const variantClasses: Record<StatusSessao, string> = {
    aberta: 'bg-emerald-50 text-emerald-700',
    encerrada: 'bg-rose-50 text-rose-700',
    sem_sessao: 'bg-slate-100 text-slate-700',
    indisponivel: 'bg-amber-50 text-amber-700',
  };

  const labels: Record<StatusSessao, string> = {
    aberta: 'Aberta',
    encerrada: 'Encerrada',
    sem_sessao: 'Sem sessão',
    indisponivel: 'Indisponível',
  };

  return (
    <span className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold ${variantClasses[status]}`}>
      {labels[status]}
    </span>
  );
}

function SkeletonItem() {
  return (
    <tr className="align-top">
      <td className="px-6 py-5">
        <div className="h-5 w-40 animate-pulse rounded-full bg-slate-200" />
      </td>
      <td className="px-6 py-5">
        <div className="space-y-2">
          <div className="h-4 w-full animate-pulse rounded-full bg-slate-100" />
          <div className="h-4 w-5/6 animate-pulse rounded-full bg-slate-100" />
        </div>
      </td>
      <td className="px-6 py-5">
        <div className="h-6 w-24 animate-pulse rounded-full bg-slate-100" />
      </td>
      <td className="px-6 py-5 text-right">
        <div className="ml-auto h-10 w-10 animate-pulse rounded-full bg-slate-100" />
      </td>
    </tr>
  );
}

function LupaIcon() {
  return (
    <svg viewBox="0 0 24 24" fill="none" aria-hidden="true" className="h-5 w-5">
      <circle cx="11" cy="11" r="6.5" stroke="currentColor" strokeWidth="1.8" />
      <path
        d="M16.2 16.2L20 20"
        stroke="currentColor"
        strokeWidth="1.8"
        strokeLinecap="round"
      />
    </svg>
  );
}

export function PautasPage() {
  const [pautas, setPautas] = useState<PautaResponse[]>([]);
  const [statusSessaoPorPauta, setStatusSessaoPorPauta] = useState<Record<string, StatusSessao>>({});
  const [paginaAtual, setPaginaAtual] = useState(0);
  const [totalPaginas, setTotalPaginas] = useState(0);
  const [totalElementos, setTotalElementos] = useState(0);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);
  const [modalAberto, setModalAberto] = useState(false);
  const loadIdRef = useRef(0);

  async function buscarStatusSessao(pautaId: string): Promise<StatusSessao> {
    try {
      const sessao = await buscarSessaoPorPauta(pautaId);

      if (!sessao) {
        return 'sem_sessao';
      }

      return sessao.encerrada ? 'encerrada' : 'aberta';
    } catch {
      return 'indisponivel';
    }
  }

  async function carregarPautas(pagina = 0) {
    const loadId = loadIdRef.current + 1;
    loadIdRef.current = loadId;

    setCarregando(true);
    setErro(null);
    setStatusSessaoPorPauta({});

    try {
      const data = await listarPautas(pagina, TAMANHO_PAGINA);
      const statusEntries = await Promise.all(
        data.content.map(async (pauta) => [pauta.id, await buscarStatusSessao(pauta.id)] as const),
      );

      if (loadId !== loadIdRef.current) {
        return;
      }

      const statusMap = statusEntries.reduce<Record<string, StatusSessao>>((acc, [pautaId, status]) => {
        acc[pautaId] = status;
        return acc;
      }, {});

      setPautas(data.content);
      setStatusSessaoPorPauta(statusMap);
      setPaginaAtual(data.number);
      setTotalPaginas(data.totalPages);
      setTotalElementos(data.totalElements);
    } catch (error) {
      if (loadId !== loadIdRef.current) {
        return;
      }

      const message = error instanceof Error && error.message ? error.message : 'Não foi possível carregar as pautas.';
      setErro(message);
    } finally {
      if (loadId === loadIdRef.current) {
        setCarregando(false);
      }
    }
  }

  useEffect(() => {
    void carregarPautas(0);
  }, []);

  function handlePautaCriada(_novaPauta: PautaResponse) {
    void carregarPautas(0);
  }

  const paginaExibida = totalPaginas > 0 ? paginaAtual + 1 : 0;

  return (
    <div className="space-y-8">
      <section className="rounded-[2rem] border border-slate-200 bg-white p-6 shadow-soft sm:p-8">
        <div className="flex flex-col gap-6 md:flex-row md:items-end md:justify-between">
          <div className="max-w-2xl">
            <span className="inline-flex rounded-full bg-indigo-50 px-3 py-1 text-xs font-semibold uppercase tracking-wide text-indigo-700">
              Sistema de votação cooperativa
            </span>
            <h1 className="mt-4 text-3xl font-semibold tracking-tight text-slate-900 sm:text-4xl">
              Pautas cadastradas
            </h1>
            <p className="mt-3 text-sm leading-6 text-slate-600 sm:text-base">
              Acompanhe as pautas disponíveis em uma lista paginada, abra novas sessões e veja a apuração dos votos em tempo real.
            </p>
          </div>

          <button
            type="button"
            onClick={() => setModalAberto(true)}
            className="inline-flex items-center justify-center rounded-full bg-indigo-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-indigo-700"
          >
            Nova Pauta
          </button>
        </div>
      </section>

      <section className="space-y-5">
        {erro ? (
          <div className="rounded-3xl border border-red-200 bg-red-50 px-6 py-5 text-sm text-red-700 shadow-soft">
            <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
              <span>{erro}</span>
              <button
                type="button"
                onClick={() => void carregarPautas(paginaAtual)}
                className="inline-flex items-center justify-center rounded-full border border-red-200 bg-white px-4 py-2 text-sm font-semibold text-red-700 transition hover:bg-red-50"
              >
                Tentar novamente
              </button>
            </div>
          </div>
        ) : null}

        {!erro && carregando ? (
          <div className="overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-soft">
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-slate-200">
                <thead className="bg-slate-50">
                  <tr>
                    <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                      Título
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                      Descrição
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                      Status da sessão
                    </th>
                    <th className="px-6 py-4 text-right text-xs font-semibold uppercase tracking-wide text-slate-500">
                      Ações
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 bg-white">
                  {Array.from({ length: TAMANHO_PAGINA }).map((_, index) => (
                    <SkeletonItem key={index} />
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        ) : null}

        {!carregando && !erro && pautas.length === 0 ? (
          <div className="rounded-3xl border border-dashed border-slate-300 bg-white px-6 py-12 text-center shadow-soft">
            <h2 className="text-xl font-semibold text-slate-900">Nenhuma pauta cadastrada</h2>
            <p className="mx-auto mt-3 max-w-lg text-sm leading-6 text-slate-600">
              Quando houver pautas disponíveis, elas aparecerão aqui. Use o botão &ldquo;Nova Pauta&rdquo; para criar a primeira.
            </p>
          </div>
        ) : null}

        {!carregando && !erro && pautas.length > 0 ? (
          <div className="space-y-5">
            <div className="overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-soft">
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-slate-200">
                <thead className="bg-slate-50">
                  <tr>
                    <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                      Título
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                      Descrição
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                      Status da sessão
                    </th>
                    <th className="px-6 py-4 text-right text-xs font-semibold uppercase tracking-wide text-slate-500">
                      Ações
                    </th>
                  </tr>
                </thead>
                  <tbody className="divide-y divide-slate-100 bg-white">
                    {pautas.map((pauta) => {
                      const descricao = pauta.descricao?.trim() ? pauta.descricao : 'Sem descrição cadastrada.';

                      return (
                        <tr key={pauta.id} className="align-top transition hover:bg-slate-50/80">
                          <td className="px-6 py-5">
                            <div className="max-w-sm">
                              <p className="font-semibold text-slate-900">{pauta.titulo}</p>
                              <p className="mt-1 text-xs uppercase tracking-wide text-slate-500">Pauta</p>
                            </div>
                          </td>
                          <td className="px-6 py-5 text-sm leading-6 text-slate-600">
                            {descricao}
                          </td>
                          <td className="px-6 py-5">
                            <StatusSessaoBadge status={statusSessaoPorPauta[pauta.id] ?? 'indisponivel'} />
                          </td>
                          <td className="px-6 py-5 text-right">
                            <Link
                              to={`/pautas/${pauta.id}`}
                              aria-label={`Ver detalhes da pauta ${pauta.titulo}`}
                              className="inline-flex h-10 w-10 items-center justify-center rounded-full border border-indigo-200 bg-white text-indigo-700 transition hover:border-indigo-300 hover:bg-indigo-50 hover:text-indigo-800"
                            >
                              <LupaIcon />
                            </Link>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </div>

            <div className="flex flex-col gap-4 rounded-3xl border border-slate-200 bg-white px-5 py-4 shadow-soft sm:flex-row sm:items-center sm:justify-between">
              <p className="text-sm text-slate-600">
                {totalElementos}{' '}
                {totalElementos === 1 ? 'pauta cadastrada' : 'pautas cadastradas'}
              </p>

              <div className="flex flex-wrap items-center gap-3">
                <button
                  type="button"
                  onClick={() => void carregarPautas(paginaAtual - 1)}
                  disabled={carregando || paginaAtual === 0}
                  className="inline-flex items-center justify-center rounded-full border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-60"
                >
                  Anterior
                </button>

                <span className="text-sm font-medium text-slate-600">
                  Página {paginaExibida} de {totalPaginas}
                </span>

                <button
                  type="button"
                  onClick={() => void carregarPautas(paginaAtual + 1)}
                  disabled={carregando || paginaAtual >= totalPaginas - 1}
                  className="inline-flex items-center justify-center rounded-full border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-60"
                >
                  Próxima
                </button>
              </div>
            </div>
          </div>
        ) : null}
      </section>

      <ModalNovaPauta
        isOpen={modalAberto}
        onClose={() => setModalAberto(false)}
        onCreated={handlePautaCriada}
      />
    </div>
  );
}
