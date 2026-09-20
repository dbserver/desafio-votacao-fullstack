import { Link } from "react-router";
import { formatarDataHora } from "../../../lib/format";
import { usePautas } from "../hooks";
import "./PautaList.css";

export const PautaList = () => {
  const { data: pautas, isPending, isError, error } = usePautas();

  if (isPending) {
    return <p className="pauta-list__aviso">Carregando pautas...</p>;
  }

  if (isError) {
    return <p className="pauta-list__aviso">{error.message}</p>;
  }

  if (pautas.length === 0) {
    return <p className="pauta-list__aviso">Nenhuma pauta cadastrada ainda.</p>;
  }

  return (
    <ul className="pauta-list">
      {pautas.map((pauta) => (
        <li className="pauta-list__item" key={pauta.id}>
          <Link className="pauta-list__titulo" to={`/pauta/${pauta.id}`}>
            {pauta.titulo}
          </Link>

          <p className="pauta-list__descricao">{pauta.descricao}</p>

          <span className="pauta-list__data">
            Criada em {formatarDataHora(pauta.criadaEm)}
          </span>
        </li>
      ))}
    </ul>
  );
};
