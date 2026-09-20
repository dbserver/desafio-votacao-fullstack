import { useVotos } from "../hooks";
import "./VotoResumo.css";

type VotoResumoProps = {
  pautaId: number;
};

export const VotoResumo = ({ pautaId }: VotoResumoProps) => {
  const { data: votos, isPending, isError } = useVotos(pautaId);

  if (isPending || isError) {
    return null;
  }

  const sim = votos.filter((voto) => voto.opcao === "SIM").length;
  const nao = votos.length - sim;

  return (
    <dl className="voto-resumo">
      <div className="voto-resumo__item">
        <dt>Sim</dt>
        <dd>{sim}</dd>
      </div>

      <div className="voto-resumo__item">
        <dt>Não</dt>
        <dd>{nao}</dd>
      </div>

      <div className="voto-resumo__item">
        <dt>Total</dt>
        <dd>{votos.length}</dd>
      </div>
    </dl>
  );
};
