import { api } from "./api";

export type VoteResult = {
  totalYes: number;
  totalNo: number;
  numberOfVotes: number;
  result: "APPROVED" | "REJECTED" | "TIED";
}

export const getVowsResults = async (agendaId: number) => {
  const response = await api.get(`/vote-result/${agendaId}`);
  return response.data;
};