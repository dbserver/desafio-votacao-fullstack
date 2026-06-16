import { useEffect, useState, type FormEvent } from 'react';
import type { PautaResponse } from '../types/api';
import { criarPauta } from '../services/pautaService';

interface ModalNovaPautaProps {
  isOpen: boolean;
  onClose: () => void;
  onCreated: (pauta: PautaResponse) => void;
}

export function ModalNovaPauta({ isOpen, onClose, onCreated }: ModalNovaPautaProps) {
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [erro, setErro] = useState<string | null>(null);
  const [enviando, setEnviando] = useState(false);

  useEffect(() => {
    if (!isOpen) {
      return;
    }

    setTitulo('');
    setDescricao('');
    setErro(null);
    setEnviando(false);
  }, [isOpen]);

  useEffect(() => {
    if (!isOpen) {
      return undefined;
    }

    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        onClose();
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [isOpen, onClose]);

  if (!isOpen) {
    return null;
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const tituloLimpo = titulo.trim();
    if (!tituloLimpo) {
      setErro('Título é obrigatório.');
      return;
    }

    setEnviando(true);
    setErro(null);

    try {
      const pauta = await criarPauta({
        titulo: tituloLimpo,
        descricao: descricao.trim() || undefined,
      });
      onCreated(pauta);
      onClose();
    } catch (error) {
      const message = error instanceof Error && error.message ? error.message : 'Não foi possível criar a pauta.';
      setErro(message);
    } finally {
      setEnviando(false);
    }
  }

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/50 px-4 py-6 backdrop-blur-sm"
      onClick={onClose}
      role="presentation"
    >
      <div
        className="w-full max-w-xl rounded-3xl border border-slate-200 bg-white p-6 shadow-2xl"
        onClick={(event) => event.stopPropagation()}
        role="dialog"
        aria-modal="true"
        aria-labelledby="nova-pauta-title"
      >
        <div className="flex items-start justify-between gap-4">
          <div>
            <p className="text-sm font-medium text-indigo-700">Nova pauta</p>
            <h2 id="nova-pauta-title" className="mt-1 text-2xl font-semibold text-slate-900">
              Criar pauta
            </h2>
          </div>
          <button
            type="button"
            onClick={onClose}
            className="rounded-full p-2 text-slate-400 transition hover:bg-slate-100 hover:text-slate-600"
            aria-label="Fechar modal"
          >
            <span aria-hidden="true">×</span>
          </button>
        </div>

        <form className="mt-6 space-y-5" onSubmit={handleSubmit}>
          <label className="block">
            <span className="mb-2 block text-sm font-medium text-slate-700">Título *</span>
            <input
              type="text"
              value={titulo}
              onChange={(event) => {
                setTitulo(event.target.value);
                if (erro) {
                  setErro(null);
                }
              }}
              className="w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
              placeholder="Ex.: Aprovação de orçamento"
              autoFocus
            />
          </label>

          <label className="block">
            <span className="mb-2 block text-sm font-medium text-slate-700">Descrição</span>
            <textarea
              value={descricao}
              onChange={(event) => setDescricao(event.target.value)}
              rows={4}
              className="w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
              placeholder="Detalhe o objetivo da pauta, se desejar."
            />
          </label>

          {erro ? (
            <p className="rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
              {erro}
            </p>
          ) : null}

          <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
            <button
              type="button"
              onClick={onClose}
              className="inline-flex items-center justify-center rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={enviando}
              className="inline-flex items-center justify-center rounded-full bg-indigo-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-70"
            >
              {enviando ? 'Criando...' : 'Confirmar'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
