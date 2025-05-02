import { useMutation, useQueryClient } from "@tanstack/react-query";
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

export const criarPauta = async (pautaData: { titulo: string; descricao: string }) => {
  const response = await api.post("/pautas", pautaData, {
    headers: {
      "Content-Type": "application/json",
    },
  });
  return response.data;
};

export function usePautaMutation() {
  const queryCliente = useQueryClient();
  const mutate = useMutation({
    mutationFn: criarPauta,
    onSuccess: () => {
      queryCliente.fetchInfiniteQuery({ queryKey: ["pautasKey"], initialPageParam: undefined });
    },
  });
  return mutate;
}
