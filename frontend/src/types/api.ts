export type OpcaoVoto = 'SIM' | 'NAO';

export interface PautaResponse {
  id: string;
  titulo: string;
  descricao: string | null;
  criadoEm: string;
}

export interface PageResponse<T> {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export type PautaPageResponse = PageResponse<PautaResponse>;

export interface CriarPautaRequest {
  titulo: string;
  descricao?: string;
}

export interface SessaoResponse {
  id: string;
  pautaId: string;
  abertaEm: string;
  expiraEm: string;
  encerrada: boolean;
}

export interface AbrirSessaoRequest {
  duracaoMinutos?: number;
}

export interface RegistrarVotoRequest {
  associadoId: string;
  opcao: OpcaoVoto;
}

export interface VotoResponse {
  id: string;
  associadoId: string;
  opcao: OpcaoVoto;
  votadoEm: string;
}

export interface ResultadoResponse {
  pautaId: string;
  sessaoId: string;
  sim: number;
  nao: number;
  total: number;
  encerrada: boolean;
}

export interface ErrorResponse {
  status: number;
  mensagem: string;
  timestamp: string;
}
