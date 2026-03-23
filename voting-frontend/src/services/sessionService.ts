import { api } from "./api";

export const openSession = async (pautaId: number, durationMinutes: number) => {
  await api.post(`/session/${pautaId}/open`, { durationMinutes });
};