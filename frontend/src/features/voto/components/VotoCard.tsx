import { Card } from "../../../components/ui/Card";
import { ApiError } from "../../../lib/http";
import { useSessao } from "../../sessao/hooks";
import { useTimeRemaining } from "../../sessao/useTimeRemaining";
import { VotoForm } from "./VotoForm";
import { VotoResumo } from "./VotoResumo";
import "./VotoCard.css";

type VotoCardProps = {
  pautaId: number;
};

export const VotoCard = ({ pautaId }: VotoCardProps) => {
  const { data: sessao, isPending, isError, error } = useSessao(pautaId);
  const { isOpen } = useTimeRemaining(sessao?.fim);

  if (isPending) {
    return (
      <Card title="Votação">
        <p className="voto-card__aviso">Carregando votação...</p>
      </Card>
    );
  }

  if (isError) {
    const semSessao = error instanceof ApiError && error.status === 404;

    return (
      <Card title="Votação">
        <p className="voto-card__aviso">
          {semSessao
            ? "Abra a sessão de votação para receber votos."
            : error.message}
        </p>
      </Card>
    );
  }

  return (
    <Card title="Votação">
      <VotoResumo pautaId={pautaId} />

      {isOpen ? (
        <VotoForm pautaId={pautaId} />
      ) : (
        <p className="voto-card__aviso">
          Sessão encerrada, não é mais possível votar.
        </p>
      )}
    </Card>
  );
};
