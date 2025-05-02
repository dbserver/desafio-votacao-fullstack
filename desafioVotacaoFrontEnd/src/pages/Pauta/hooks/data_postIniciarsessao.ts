// hooks/data_postIniciarsessao.ts
import { useMutation, useQueryClient } from "@tanstack/react-query";
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

export const criarSessao = async (idpauta: number, tempo: number) => {
  const response = await api.post(
    `/pautas/${idpauta}/abrir-sessao?minutos=${tempo}`,
    {},
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );
  return response.data;
};

export function usePautaSessaoMutation() {
  const queryClient = useQueryClient();
  const mutate = useMutation({
    mutationFn: ({ idpauta, tempo }: { idpauta: number; tempo: number }) => criarSessao(idpauta, tempo),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["pautasKey"] });
    },
  });
  return mutate;
}
