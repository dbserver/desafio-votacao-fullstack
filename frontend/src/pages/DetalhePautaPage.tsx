import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { FormularioVoto } from '../components/FormularioVoto';
import { ResultadoVotacao } from '../components/ResultadoVotacao';
import { abrirSessao, buscarSessaoPorPauta } from '../services/sessaoService';
import { buscarPauta } from '../services/pautaService';
import { buscarResultado } from '../services/votoService';
import type { PautaResponse, ResultadoResponse, SessaoResponse } from '../types/api';
import { formatDateTime } from '../utils/format';

function SkeletonBlock() {
  return (
    <div className="rounded-3xl border border-slate-200 bg-white p-6 shadow-soft">
      <div className="h-5 w-28 animate-pulse rounded-full bg-slate-200" />
      <div className="mt-4 h-8 w-2/3 animate-pulse rounded-full bg-slate-200" />
      <div className="mt-3 space-y-2">
        <div className="h-4 animate-pulse rounded-full bg-slate-100" />
        <div className="h-4 w-5/6 animate-pulse rounded-full bg-slate-100" />
        <div className="h-4 w-2/3 animate-pulse rounded-full bg-slate-100" />
      </div>
    </div>
  );
}

function ResultadoSkeleton() {
  return (
    <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-soft">
      <div className="space-y-4">
        <div className="h-5 w-32 animate-pulse rounded-full bg-slate-200" />
        <div className="grid gap-3 sm:grid-cols-3">
          <div className="h-24 animate-pulse rounded-2xl bg-slate-100" />
          <div className="h-24 animate-pulse rounded-2xl bg-slate-100" />
          <div className="h-24 animate-pulse rounded-2xl bg-slate-100" />
        </div>
        <div className="h-5 w-full animate-pulse rounded-full bg-slate-100" />
      </div>
    </section>
  );
}

function formatarTempoRestante(totalSegundos: number): string {
  const segundosTotais = Math.max(0, Math.floor(totalSegundos));
  const horas = Math.floor(segundosTotais / 3600);
  const minutos = Math.floor((segundosTotais % 3600) / 60);
  const segundos = segundosTotais % 60;

  if (horas > 0) {
    return `${horas}:${String(minutos).padStart(2, '0')}:${String(segundos).padStart(2, '0')}`;
  }

  return `${String(minutos).padStart(2, '0')}:${String(segundos).padStart(2, '0')}`;
}

export function DetalhePautaPage() {
  const { pautaId } = useParams();
  const [pauta, setPauta] = useState<PautaResponse | null>(null);
  const [sessao, setSessao] = useState<SessaoResponse | null>(null);
  const [resultado, setResultado] = useState<ResultadoResponse | null>(null);
  const [carregando, setCarregando] = useState(true);
  const [erroPagina, setErroPagina] = useState<string | null>(null);
  const [erroSessao, setErroSessao] = useState<string | null>(null);
  const [erroResultado, setErroResultado] = useState<string | null>(null);
  const [carregandoResultado, setCarregandoResultado] = useState(false);
  const [duracaoMinutos, setDuracaoMinutos] = useState('1');
  const [abrindoSessao, setAbrindoSessao] = useState(false);
  const [tempoRestanteSegundos, setTempoRestanteSegundos] = useState<number | null>(null);

  async function carregarResultado(sessaoId: string) {
    setCarregandoResultado(true);
    setErroResultado(null);

    try {
      const data = await buscarResultado(sessaoId);
      setResultado(data);
    } catch (error) {
      const message = error instanceof Error && error.message ? error.message : 'Não foi possível carregar o resultado.';
      setErroResultado(message);
    } finally {
      setCarregandoResultado(false);
    }
  }

  async function carregarSessao() {
    if (!pautaId) {
      return;
    }

    setErroSessao(null);

    try {
      const sessaoAtual = await buscarSessaoPorPauta(pautaId);
      setSessao(sessaoAtual);

      if (sessaoAtual) {
        await carregarResultado(sessaoAtual.id);
      } else {
        setResultado(null);
        setErroResultado(null);
      }
    } catch (error) {
      const message = error instanceof Error && error.message ? error.message : 'Não foi possível verificar a sessão.';
      setErroSessao(message);
    }
  }

  async function carregarDetalhe() {
    if (!pautaId) {
      setErroPagina('Pauta não informada.');
      setCarregando(false);
      return;
    }

    setCarregando(true);
    setPauta(null);
    setSessao(null);
    setResultado(null);
    setErroPagina(null);
    setErroSessao(null);
    setErroResultado(null);

    try {
      const pautaData = await buscarPauta(pautaId);
      setPauta(pautaData);
      await carregarSessao();
    } catch (error) {
      const message = error instanceof Error && error.message ? error.message : 'Não foi possível carregar a pauta.';
      setErroPagina(message);
    } finally {
      setCarregando(false);
    }
  }

  useEffect(() => {
    void carregarDetalhe();
  }, [pautaId]);

  async function abrirNovaSessao() {
    if (!pautaId) {
      return;
    }

    const parsedDuration = Number.parseInt(duracaoMinutos, 10);
    const duracao = Number.isFinite(parsedDuration) && parsedDuration > 0 ? parsedDuration : 1;

    setAbrindoSessao(true);
    setErroSessao(null);

    try {
      const sessaoCriada = await abrirSessao(pautaId, duracao);
      setSessao(sessaoCriada);
      await carregarResultado(sessaoCriada.id);
    } catch (error) {
      const message = error instanceof Error && error.message ? error.message : 'Não foi possível abrir a sessão.';
      setErroSessao(message);
    } finally {
      setAbrindoSessao(false);
    }
  }

  const sessaoEncerrada = Boolean(sessao?.encerrada);
  const sessaoAberta = Boolean(sessao && !sessao.encerrada);

  useEffect(() => {
    if (!sessao || sessaoEncerrada) {
      setTempoRestanteSegundos(null);
      return undefined;
    }

    const atualizarTempoRestante = () => {
      const expiraEm = new Date(sessao.expiraEm).getTime();
      const restante = Math.max(0, Math.ceil((expiraEm - Date.now()) / 1000));
      setTempoRestanteSegundos(restante);
    };

    atualizarTempoRestante();
    const intervalo = window.setInterval(atualizarTempoRestante, 1000);

    return () => window.clearInterval(intervalo);
  }, [sessao?.id, sessao?.encerrada, sessao?.expiraEm, sessaoEncerrada]);

  useEffect(() => {
    if (!sessao || sessaoEncerrada) {
      return undefined;
    }

    const expiraEm = new Date(sessao.expiraEm).getTime();
    const delay = expiraEm - Date.now();

    if (delay <= 0) {
      void carregarSessao();
      return undefined;
    }

    const timer = window.setTimeout(() => {
      void carregarSessao();
    }, delay + 250);

    return () => window.clearTimeout(timer);
  }, [sessao?.id, sessao?.encerrada, sessao?.expiraEm, sessaoAberta, sessaoEncerrada]);

  function renderResultado() {
    if (carregandoResultado && !resultado) {
      return <ResultadoSkeleton />;
    }

    if (erroResultado && !resultado) {
      return (
        <section className="rounded-3xl border border-red-200 bg-red-50 p-6 text-sm text-red-700 shadow-soft">
          {erroResultado}
        </section>
      );
    }

    if (!sessao || !resultado) {
      return null;
    }

    return <ResultadoVotacao sessaoId={sessao.id} resultadoInicial={resultado} />;
  }

  if (carregando && !pauta) {
    return (
      <div className="space-y-6">
        <SkeletonBlock />
        <div className="grid gap-6 xl:grid-cols-2">
          <SkeletonBlock />
          <SkeletonBlock />
        </div>
      </div>
    );
  }

  if (erroPagina) {
    return (
      <div className="rounded-3xl border border-red-200 bg-red-50 p-6 text-red-700 shadow-soft">
        <h1 className="text-xl font-semibold">Não foi possível carregar a pauta</h1>
        <p className="mt-3 text-sm">{erroPagina}</p>
        <div className="mt-6">
          <Link
            to="/pautas"
            className="inline-flex items-center justify-center rounded-full bg-white px-4 py-2 text-sm font-semibold text-red-700 transition hover:bg-red-100"
          >
            Voltar para pautas
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <section className="rounded-[2rem] border border-slate-200 bg-white p-6 shadow-soft sm:p-8">
        <div className="flex flex-col gap-5">
          <Link
            to="/pautas"
            className="inline-flex w-fit items-center rounded-full border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
          >
            ← Voltar para pautas
          </Link>

          <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
            <div className="max-w-3xl">
              <span className="inline-flex rounded-full bg-indigo-50 px-3 py-1 text-xs font-semibold uppercase tracking-wide text-indigo-700">
                Detalhe da pauta
              </span>
              <h1 className="mt-4 text-3xl font-semibold tracking-tight text-slate-900 sm:text-4xl">
                {pauta?.titulo}
              </h1>
              <p className="mt-4 max-w-3xl text-sm leading-7 text-slate-600 sm:text-base">
                {pauta?.descricao?.trim() ? pauta.descricao : 'Esta pauta não possui descrição informada.'}
              </p>
            </div>
          </div>

          <div className="grid gap-3 text-sm text-slate-600 sm:grid-cols-2 lg:grid-cols-4">
            <div className="rounded-2xl bg-slate-50 px-4 py-3">
              <p className="font-medium text-slate-500">Criada em</p>
              <p className="mt-1 text-slate-900">{formatDateTime(pauta?.criadoEm)}</p>
            </div>
            <div className="rounded-2xl bg-slate-50 px-4 py-3">
              <p className="font-medium text-slate-500">Sessão</p>
              <div className="mt-2 flex flex-wrap items-center gap-2">
                {sessao ? (
                  <span
                    className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold ${sessaoEncerrada ? 'bg-rose-50 text-rose-700' : 'bg-emerald-50 text-emerald-700'
                      }`}
                  >
                    {sessaoEncerrada ? 'Encerrada' : 'Aberta'}
                  </span>
                ) : null}
              </div>
            </div>
            <div className="rounded-2xl bg-slate-50 px-4 py-3">
              <p className="font-medium text-slate-500">Expira em</p>
              <p className="mt-1 text-slate-900">{formatDateTime(sessao?.expiraEm)}</p>
            </div>
            <div className="rounded-2xl bg-slate-50 px-4 py-3">
              <p className="font-medium text-slate-500">Tempo restante</p>
              <p className="mt-1 text-slate-900">
                {sessaoEncerrada
                  ? 'Sessão encerrada'
                  : tempoRestanteSegundos !== null
                    ? formatarTempoRestante(tempoRestanteSegundos)
                    : 'Não informado'}
              </p>
              <p className="mt-1 text-xs text-slate-500">
                {sessaoEncerrada
                  ? 'A sessão foi encerrada automaticamente ao atingir o tempo limite.'
                  : 'Contagem regressiva até o encerramento da sessão.'}
              </p>
            </div>
          </div>
        </div>
      </section>

      {!sessao ? (
        <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-soft">
          <div className="flex flex-col gap-5 lg:flex-row lg:items-end lg:justify-between">
            <div className="max-w-2xl">
              <p className="text-sm font-medium text-indigo-700">Sessão de votação</p>
              <h2 className="mt-1 text-xl font-semibold text-slate-900">Abrir sessão de votação</h2>
              <p className="mt-3 text-sm leading-6 text-slate-600">
                Informe a duração em minutos e abra a sessão para que os associados possam votar.
              </p>
            </div>

            <div className="flex w-full flex-col gap-3 lg:max-w-sm lg:flex-row">
              <label className="flex-1">
                <span className="mb-2 block text-sm font-medium text-slate-700">Duração em minutos</span>
                <input
                  type="number"
                  min="1"
                  value={duracaoMinutos}
                  onChange={(event) => setDuracaoMinutos(event.target.value)}
                  className="w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                />
              </label>
              <div className="flex items-end">
                <button
                  type="button"
                  onClick={() => void abrirNovaSessao()}
                  disabled={abrindoSessao}
                  className="inline-flex w-full items-center justify-center rounded-full bg-indigo-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-70"
                >
                  {abrindoSessao ? 'Abrindo...' : 'Abrir Sessão de Votação'}
                </button>
              </div>
            </div>
          </div>

          {erroSessao ? (
            <p className="mt-4 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
              {erroSessao}
            </p>
          ) : null}

          <p className="mt-4 text-sm text-slate-500">
            Depois de abrir a sessão, o formulário de votação será exibido imediatamente e o encerramento ocorre automaticamente ao fim do tempo definido.
          </p>
        </section>
      ) : null}

      {sessao && sessaoAberta ? (
        <div className="grid gap-6 xl:grid-cols-[1.15fr_0.85fr]">
          <FormularioVoto
            sessaoId={sessao.id}
            onVotoRegistrado={() => {
              if (sessao) {
                void carregarResultado(sessao.id);
              }
            }}
          />

          {renderResultado()}
        </div>
      ) : null}

      {sessao && !sessaoAberta ? (
        renderResultado()
      ) : null}

      {sessao && erroSessao ? (
        <p className="rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {erroSessao}
        </p>
      ) : null}
    </div>
  );
}
