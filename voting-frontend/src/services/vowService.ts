import { api } from "./api";

export type VoteEnum = "YES" | "NO";

export interface VotarRequest {
  agendaId: number;
  associatedId: string;
  vote: VoteEnum;
}

export const voteRegister = async (data: VotarRequest) => {
  await api.post("/vote/register", data);
};