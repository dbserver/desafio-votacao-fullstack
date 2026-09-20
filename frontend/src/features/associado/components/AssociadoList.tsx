import { formatarCpf } from "../../../lib/format";
import { useAssociados } from "../hooks";
import "./AssociadoList.css";

export const AssociadoList = () => {
  const { data: associados, isPending, isError, error } = useAssociados();

  if (isPending) {
    return <p className="associado-list__aviso">Carregando associados...</p>;
  }

  if (isError) {
    return <p className="associado-list__aviso">{error.message}</p>;
  }

  if (associados.length === 0) {
    return (
      <p className="associado-list__aviso">Nenhum associado cadastrado ainda.</p>
    );
  }

  return (
    <ul className="associado-list">
      {associados.map((associado) => (
        <li className="associado-list__item" key={associado.id}>
          <span className="associado-list__nome">{associado.nome}</span>
          <span className="associado-list__cpf">
            {formatarCpf(associado.cpf)}
          </span>
        </li>
      ))}
    </ul>
  );
};
