import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { sectionService } from '../service/sectionService';
import { ISection, TSectionBody } from '../types/section';

const QUERY_KEYS = {
  allSections: 'allSections',
};

export const useGetAllSections = ({
  isSectionsOPen,
}: {
  isSectionsOPen: boolean;
}) => {
  return useQuery<ISection[], Error>({
    queryKey: [QUERY_KEYS.allSections],
    queryFn: () => sectionService.getAllSection(isSectionsOPen),
  });
};

export const useCreateSection = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (body: TSectionBody) => sectionService.create(body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.allSections] });
    },
  });
};
