import { useQuery } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';

export interface ParliamentaryClubsResponse {
  id: string;
  shortName: string;
}

export const useParliamentaryClubs = (enabled: boolean) => {
  const { api } = useAxiosPrivate();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['parliamentaryClubs'],
    queryFn: async () => {
      try {
        const { data } = await api.get<ParliamentaryClubsResponse[]>(
          '/parliamentary-clubs'
        );
        return { data };
      } catch (e) {
        return Promise.reject(e);
      }
    },
    enabled,
  });

  return { isError, isLoading, error, data };
};
