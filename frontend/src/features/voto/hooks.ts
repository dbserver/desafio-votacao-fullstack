import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { ApiError } from "../../lib/http";
import { pautaKeys } from "../pauta/hooks";
import { listarVotos, votar } from "./api";
import type { CriarVotoRequest } from "./schema";

export const votoKeys = {
  daPauta: (pautaId: number) =>
    [...pautaKeys.detalhe(pautaId), "votos"] as const,
};

export const useVotos = (pautaId: number) => {
  return useQuery({
    queryKey: votoKeys.daPauta(pautaId),
    queryFn: () => listarVotos(pautaId),
    retry: (falhas, erro) =>
      !(erro instanceof ApiError && erro.status === 404) && falhas < 2,
  });
};

export const useVotar = (pautaId: number) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: CriarVotoRequest) => votar(pautaId, request),
    onSuccess: () =>
      queryClient.invalidateQueries({ queryKey: pautaKeys.detalhe(pautaId) }),
  });
};
