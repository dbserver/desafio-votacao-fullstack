import { Card } from "../../../components/ui/Card";
import { ApiError } from "../../../lib/http";
import { formatarDataHora } from "../../../lib/format";
import { useSessao } from "../hooks";
import { useTimeRemaining } from "../useTimeRemaining";
import { SessaoForm } from "./SessaoForm";
import "./SessaoCard.css";

type SessaoCardProps = {
  pautaId: number;
};

export const SessaoCard = ({ pautaId }: SessaoCardProps) => {
  const { data: sessao, isPending, isError, error } = useSessao(pautaId);
  const { isOpen, text } = useTimeRemaining(sessao?.fim);

  if (isPending) {
    return (
      <Card title="Sessão">
        <p className="sessao-card__aviso">Carregando sessão...</p>
      </Card>
    );
  }

  if (isError) {
    if (error instanceof ApiError && error.status === 404) {
      return (
        <Card title="Sessão">
          <p className="sessao-card__aviso">
            Esta pauta ainda não tem uma sessão de votação. Sem duração
            informada, a sessão fica aberta por 1 minuto.
          </p>

          <SessaoForm pautaId={pautaId} />
        </Card>
      );
    }

    return (
      <Card title="Sessão">
        <p className="sessao-card__aviso">{error.message}</p>
      </Card>
    );
  }

  return (
    <Card title="Sessão">
      <p
        className={
          isOpen
            ? "sessao-card__status sessao-card__status--aberta"
            : "sessao-card__status"
        }
      >
        {isOpen ? `Aberta, encerra em ${text}` : "Encerrada"}
      </p>

      <dl className="sessao-card__periodo">
        <dt>Início</dt>
        <dd>{formatarDataHora(sessao.inicio)}</dd>

        <dt>Fim</dt>
        <dd>{formatarDataHora(sessao.fim)}</dd>
      </dl>
    </Card>
  );
};
