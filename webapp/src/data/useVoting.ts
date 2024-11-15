import { useQuery } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import { VotingResponse, VotingWithoutVotesResponse } from '@/types/voting';
import { useVotingListFilters } from '@/hooks/useVotingListFilters';

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

interface VotingListResponse {
  votingList: VotingWithoutVotesResponse[];
  sittings: number[];
  totalPages: number;
  pageNumber: number;
  pageSize: number;
}

export const useVotingList = () => {
  const { api } = useAxiosPrivate();
  const { pageNumber, pageSize, title, sitting, sort } = useVotingListFilters();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['votingList', pageNumber, pageSize, title, sitting, sort],
    queryFn: async () => {
      try {
        const { data } = await api.get<VotingListResponse>('/votings', {
          params: {
            page: pageNumber,
            size: pageSize,
            sort: sort,
            title: title,
            sitting: sitting,
          },
        });
        return data;
      } catch (e) {
        return Promise.reject(e);
      }
    },
  });

  return { isError, isLoading, error, data };
};
