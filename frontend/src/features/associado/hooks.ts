import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { buscarAssociadoPorCpf, criarAssociado, listarAssociados } from "./api"

export const associadoKeys = {
  todos: ["associado"] as const,
}

export const useAssociados = () => {
  return useQuery({
    queryKey: associadoKeys.todos,
    queryFn: listarAssociados,
  })
}

export const useCriarAssociado = () => {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: criarAssociado,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: associadoKeys.todos }),
  })
}

export const useBuscarAssociadoPorCpf = () => {
  return useMutation({
    mutationFn: buscarAssociadoPorCpf,
  })
}
