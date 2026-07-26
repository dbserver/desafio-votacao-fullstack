export type ResultadoVotacaoStatus =
  | "EM_ANDAMENTO"
  | "APROVADA"
  | "REPROVADA"
  | "EMPATE"
  | "SEM_VOTOS";

export const resultadoLabels: Record<ResultadoVotacaoStatus, string> = {
  EM_ANDAMENTO: "Em andamento",
  APROVADA: "Aprovada",
  REPROVADA: "Reprovada",
  EMPATE: "Empate",
  SEM_VOTOS: "Sem votos",
};

export interface ResultadoVotacao {
  pautaId: number;
  sessaoId: number;
  votosSim: number;
  votosNao: number;
  totalVotos: number;
  sessaoAberta: boolean;
  resultado: ResultadoVotacaoStatus;
}
