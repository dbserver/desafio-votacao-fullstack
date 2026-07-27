import { useNow } from "../../hooks/useNow";

interface TempoRestanteProps {
  dthFim: string;
}

export function TempoRestante({ dthFim }: TempoRestanteProps) {
  const agora = useNow();

  const fimEmMilissegundos = new Date(dthFim).getTime();
  const tempoRestante = fimEmMilissegundos - agora;

  if (Number.isNaN(fimEmMilissegundos) || tempoRestante <= 0) {
    return <span>Sessão encerrada</span>;
  }

  const minutosRestantes = Math.ceil(tempoRestante / (60 * 1000));

  return (
    <span>
      {minutosRestantes}{" "}
      {minutosRestantes === 1 ? "minuto restante" : "minutos restantes"}
    </span>
  );
}
