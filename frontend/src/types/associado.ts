export interface CreateAssociadoRequest {
  cpf: string;
}

export interface Associado {
  id: number;
  cpf: string;
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
