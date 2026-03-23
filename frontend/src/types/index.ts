export interface Pauta {
  id: number;
  titulo: string;
  descricao: string;
  criadaEm: string;
}

export interface SessaoVotacao {
  id: number;
  pautaId: number;
  pautaTitulo: string;
  dataInicio: string;
  dataFim: string;
  aberta: boolean;
}

export interface VotoRequest {
  associadoId: string;
  voto: "SIM" | "NAO";
}

export interface VotoResponse {
  id: number;
  associadoId: string;
  voto: "SIM" | "NAO";
}

export interface ResultadoVotacao {
  pautaId: number;
  pautaTitulo: string;
  totalVotos: number;
  votosSim: number;
  votosNao: number;
  resultado: string;
}

export interface ApiError {
  timestamp: string;
  status: number;
  mensagem: string;
}
