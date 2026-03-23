import api from "./api";
import { Pauta } from "../types";

export const listarPautas = () => api.get<Pauta[]>("/pautas");

export const criarPauta = (titulo: string, descricao: string) =>
  api.post<Pauta>("/pautas", { titulo, descricao });

export const buscarPauta = (id: number) => api.get<Pauta>(`/pautas/${id}`);
