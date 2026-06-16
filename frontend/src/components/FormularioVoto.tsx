import { useState } from 'react';
import { registrarVoto } from '../services/votoService';
import type { OpcaoVoto } from '../types/api';

interface FormularioVotoProps {
  sessaoId: string;
  onVotoRegistrado: () => void;
}

export function FormularioVoto({ sessaoId, onVotoRegistrado }: FormularioVotoProps) {
  const [associadoId, setAssociadoId] = useState('');
  const [enviando, setEnviando] = useState(false);
  const [votoRegistrado, setVotoRegistrado] = useState(false);
  const [mensagem, setMensagem] = useState<string | null>(null);
  const [erro, setErro] = useState<string | null>(null);

  async function registrar(opcao: OpcaoVoto) {
    const associadoLimpo = associadoId.trim();
    if (!associadoLimpo) {
      setErro('CPF do associado é obrigatório.');
      return;
    }

    setEnviando(true);
    setErro(null);
    setMensagem(null);

    try {
      await registrarVoto(sessaoId, associadoLimpo, opcao);
      setMensagem('Voto registrado!');
      setVotoRegistrado(true);
      onVotoRegistrado();
    } catch (error) {
      const message = error instanceof Error && error.message ? error.message : 'Não foi possível registrar o voto.';
      setErro(message);
    } finally {
      setEnviando(false);
    }
  }

  function handleChangeAssociadoId(value: string) {
    setAssociadoId(value);
    if (erro) {
      setErro(null);
    }
  }

  const desabilitado = enviando || votoRegistrado;

  return (
    <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-soft">
      <div className="flex items-start justify-between gap-4">
        <div>
          <p className="text-sm font-medium text-indigo-700">Votação aberta</p>
          <h3 className="mt-1 text-xl font-semibold text-slate-900">Registrar voto</h3>
        </div>
        {votoRegistrado ? (
          <span className="rounded-full bg-emerald-50 px-3 py-1 text-xs font-semibold text-emerald-700">
            Concluído
          </span>
        ) : null}
      </div>

      <div className="mt-5">
        <label className="block">
          <span className="mb-2 block text-sm font-medium text-slate-700">CPF do Associado *</span>
          <input
            type="text"
            value={associadoId}
            onChange={(event) => handleChangeAssociadoId(event.target.value)}
            disabled={desabilitado}
            className="w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100 disabled:cursor-not-allowed disabled:bg-slate-50"
            placeholder="00000000000"
          />
        </label>
        <p className="mt-2 text-xs text-slate-500">
          O voto é registrado imediatamente ao clicar em uma das opções.
        </p>
      </div>

      {mensagem ? (
        <div className="mt-4 rounded-2xl border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm font-medium text-emerald-700">
          {mensagem}
        </div>
      ) : null}

      {erro ? (
        <div className="mt-4 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {erro}
        </div>
      ) : null}

      <div className="mt-5 grid gap-3 sm:grid-cols-2">
        <button
          type="button"
          onClick={() => void registrar('SIM')}
          disabled={desabilitado}
          className="inline-flex min-h-16 items-center justify-center rounded-2xl bg-emerald-600 px-5 py-4 text-base font-semibold text-white transition hover:bg-emerald-700 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {enviando ? 'Registrando...' : 'Sim'}
        </button>
        <button
          type="button"
          onClick={() => void registrar('NAO')}
          disabled={desabilitado}
          className="inline-flex min-h-16 items-center justify-center rounded-2xl bg-rose-600 px-5 py-4 text-base font-semibold text-white transition hover:bg-rose-700 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {enviando ? 'Registrando...' : 'Não'}
        </button>
      </div>
    </section>
  );
}
