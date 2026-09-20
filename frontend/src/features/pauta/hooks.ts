import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { criarPauta, listarPautas } from "./api"

export const pautaKeys = {
  todas: ["pauta"] as const,
  detalhe: (pautaId: number) => ["pauta", pautaId] as const,
}

export const usePautas = () => {
  return useQuery({
    queryKey: pautaKeys.todas,
    queryFn: listarPautas,
  })
}

export const usePauta = (pautaId: number) => {
  return useQuery({
    queryKey: pautaKeys.todas,
    queryFn: listarPautas,
    select: (pautas) => pautas.find((pauta) => pauta.id === pautaId),
  })
}

export const useCriarPauta = () => {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: criarPauta,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: pautaKeys.todas }),
  })
}
