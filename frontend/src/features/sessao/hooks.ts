import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { ApiError } from "../../lib/http";
import { pautaKeys } from "../pauta/hooks";
import { abrirSessao, buscarSessao } from "./api";
import type { CriarSessaoRequest } from "./schema";

export const sessaoKeys = {
  daPauta: (pautaId: number) =>
    [...pautaKeys.detalhe(pautaId), "sessao"] as const,
};

export const useSessao = (pautaId: number) => {
  return useQuery({
    queryKey: sessaoKeys.daPauta(pautaId),
    queryFn: () => buscarSessao(pautaId),
    retry: (falhas, erro) =>
      !(erro instanceof ApiError && erro.status === 404) && falhas < 2,
  });
};

export const useAbrirSessao = (pautaId: number) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: CriarSessaoRequest) => abrirSessao(pautaId, request),
    onSuccess: () =>
      queryClient.invalidateQueries({ queryKey: sessaoKeys.daPauta(pautaId) }),
  });
};
