import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { agendaService } from '../service/agendaService';
import { IAgenda, TAgendaBody } from '../types/agenda';

const QUERY_KEYS = {
  allAgendas: 'allAgendas',
};

export const useGetAllAvaiableAgendas = ({ sortByName = false }) => {
  return useQuery<IAgenda[], Error>({
    queryKey: [QUERY_KEYS.allAgendas],
    queryFn: () => agendaService.getAllAvaiableAgendas(sortByName),
  });
};

export const useCreateAgenda = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (body: TAgendaBody) => agendaService.create(body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.allAgendas] });
    },
  });
};
