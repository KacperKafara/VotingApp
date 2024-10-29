import { useQuery } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import { User } from '@/types/user';

export const useProfile = () => {
  const { api } = useAxiosPrivate();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['profile'],
    queryFn: async () => {
      try {
        const { data, headers } = await api.get<User>('/me');
        return { data, headers };
      } catch (e) {
        return Promise.reject(e);
      }
    },
  });

  return { isError, isLoading, error, data };
};
