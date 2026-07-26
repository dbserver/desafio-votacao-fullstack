export interface SessaoVotacao {
  id: number;
  pautaId: number;
  dthAbertura: string;
  dthFim: string;
  open: boolean;
}

export interface CreateSessaoVotacaoRequest {
  duracao?: number;
}
