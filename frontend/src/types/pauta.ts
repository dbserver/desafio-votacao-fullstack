export interface Pauta {
  id: number;
  titulo: string;
  descricao: string;
  dthCriacao: string;
}

export interface CreatePautaRequest {
  titulo: string;
  descricao: string;
}
