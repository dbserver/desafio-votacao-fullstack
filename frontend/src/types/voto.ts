export type OpcaoVoto = "SIM" | "NAO";

export interface CreateVotoRequest {
  associadoId: number;
  opcao: OpcaoVoto;
}

export interface Voto {
  id: number;
  sessaoId: number;
  pautaId: number;
  associadoId: number;
  opcao: OpcaoVoto;
  dthVoto: string;
}
