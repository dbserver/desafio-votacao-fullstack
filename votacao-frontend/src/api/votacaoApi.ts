import axios from "axios";

const api = axios.create({
  baseURL: "/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

export interface VotoRequest {
  cpf: string;
  voto: "SIM" | "NAO";
}

export interface ResultadoVotacao {
  sessaoId: number;
  votosSim: number;
  votosNao: number;
  total: number;
  resultado: string;
}

export interface Pauta {
  id: number;
  titulo: string;
  descricao?: string;
}

export interface SessaoResponse {
    id: number;
    pautaId: number;
    dataInicio: string;
    dataFim: string;
    aberta: boolean;
}

export const listarPautas = () => api.get<Pauta[]>("/pautas");

export const criarPauta = (pauta: { titulo: string; descricao?: string }) =>
  api.post("/pautas", pauta);

export const votar = (sessaoId: number, cpf: string, voto: "SIM" | "NAO") =>
  api.post(`/votos/sessao/${sessaoId}`, { cpf, voto });

export const criarSessao = (pautaId: number, duracaoMinutos?: number) =>
  api.post(`/sessoes/pauta/${pautaId}`, { duracaoMinutos });

export const resultadoSessao = (sessaoId: number) =>
  api.get<ResultadoVotacao>(`/votos/sessao/${sessaoId}/resultado`);

export const obterSessao = (sessaoId: number) =>
  api.get<SessaoResponse>(`/sessoes/${sessaoId}`);

export const tratarErro = (error: any) => {
  if (axios.isAxiosError(error)) {
    if (error.response) {
      return error.response.data?.message || "Erro na requisição ao servidor";
    } else if (error.request) {
      return "Não foi possível conectar ao servidor";
    } else {
      return error.message;
    }
  }
  return "Erro desconhecido";
};