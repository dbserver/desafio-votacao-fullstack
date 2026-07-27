export interface CreatePautaRequest {
  titulo: string;
  descricao: string;
}

export interface Pauta {
  id: number;
  titulo: string;
  descricao: string;
  dthCriacao: string;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}
