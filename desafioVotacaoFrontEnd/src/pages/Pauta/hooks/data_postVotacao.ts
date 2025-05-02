import { useMutation, useQueryClient } from "@tanstack/react-query";
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

export const criarVoto = async (data: { pautaId: number; cpf: string; opcao: string }) => {
  const response = await api.post("/votos", data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
  return response.data;
};

export function useVotoMutation() {
  const queryCliente = useQueryClient();
  const mutate = useMutation({
    mutationFn: criarVoto,
  });
  return mutate;
}
