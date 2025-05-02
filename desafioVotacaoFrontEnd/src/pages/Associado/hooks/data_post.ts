import { useMutation, useQueryClient } from "@tanstack/react-query";
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

export const criarAssociado = async (data: { nome: string; cpf: string }) => {
  const response = await api.post("/associado", data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
  return response.data;
};

export function useAssociadoMutation() {
  const queryCliente = useQueryClient();
  const mutate = useMutation({
    mutationFn: criarAssociado,
  });
  return mutate;
}
