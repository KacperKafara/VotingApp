import { useQuery } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import { VotingWithoutVotesResponse } from '@/types/voting';
import { SurveyWithoutVotesResponse } from '@/types/survey';

export const useActiveSurveys = () => {
  const { api } = useAxiosPrivate();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['activeSurveys'],
    queryFn: async () => {
      try {
        const { data } = await api.get<{
          votingList: VotingWithoutVotesResponse[];
          surveys: SurveyWithoutVotesResponse[];
        }>('/activeSurveys');
        return data;
      } catch (e) {
        return Promise.reject(e);
      }
    },
  });

  return { isError, isLoading, error, data };
};
