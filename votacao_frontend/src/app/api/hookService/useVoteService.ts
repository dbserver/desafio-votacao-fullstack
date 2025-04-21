import { useMutation, useQueryClient } from '@tanstack/react-query';

import { TVoteBody } from '../types/vote';
import { voteService } from '../service/voteService';

export const useCreateVote = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (body: TVoteBody) => voteService.create(body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["allSections"] });
    },
  });
};
