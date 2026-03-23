import { api } from "./api";

export interface CriarAgendaRequest {
  title: string;
  description: string;
}

export const createAgenda = async (data: CriarAgendaRequest) => {
  const response = await api.post("/agenda", data);
  return response.data;
};

export const listAgendas = async () => {
  const response = await api.get("/agenda");
  return response.data;
};

export const searchAgendaById = async (id: number) => {
  const response = await api.get(`/agenda/${id}`);
  return response.data;
};