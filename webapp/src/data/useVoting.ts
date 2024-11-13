import { useQuery } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import { VotingResponse } from '@/types/voting';

export const useVoting = (id: string) => {
  const { api } = useAxiosPrivate();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['voting', id],
    queryFn: async () => {
      try {
        const { data } = await api.get<VotingResponse>(`/votings/${id}`);
        return data;
      } catch (e) {
        return Promise.reject(e);
      }
    },
  });

  return { isError, isLoading, error, data };
};
