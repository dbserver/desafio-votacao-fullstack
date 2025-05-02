import axios from "axios";
import { useQuery } from "@tanstack/react-query";

interface IPauta {
  id: number;
  titulo: string;
  descricao: string;
  dataAbertura: string;
  dataFechamento: string;
  sessaoAtiva: boolean;
}

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});
const fetchPautas = async (): Promise<IPauta[]> => {
  const resposta = await api.get<IPauta[]>("pautas");
  return resposta.data;
};

export function Datapauta() {
  return useQuery<IPauta[]>({
    queryFn: () => fetchPautas(),
    queryKey: ["pautasKey"],
    refetchInterval: 30000,
  });
}
