import { Link } from 'react-router-dom';
import type { PautaResponse } from '../types/api';

interface PautaCardProps {
  pauta: PautaResponse;
}

export function PautaCard({ pauta }: PautaCardProps) {
  const descricao = pauta.descricao?.trim() ? pauta.descricao : 'Sem descrição cadastrada.';

  return (
    <article className="flex h-full flex-col rounded-3xl border border-slate-200 bg-white p-6 shadow-soft">
      <div className="flex-1">
        <div className="mb-3 inline-flex items-center rounded-full bg-indigo-50 px-3 py-1 text-xs font-medium text-indigo-700">
          Pauta
        </div>
        <h3 className="text-xl font-semibold text-slate-900">{pauta.titulo}</h3>
        <p className="mt-3 text-sm leading-6 text-slate-600">{descricao}</p>
      </div>

      <div className="mt-6 flex items-center justify-end">
        <Link
          to={`/pautas/${pauta.id}`}
          className="inline-flex items-center justify-center rounded-full border border-indigo-200 bg-white px-4 py-2 text-sm font-semibold text-indigo-700 transition hover:border-indigo-300 hover:bg-indigo-50 hover:text-indigo-800"
        >
          Ver detalhes
        </Link>
      </div>
    </article>
  );
}
