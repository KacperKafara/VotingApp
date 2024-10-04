import { useQuery } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import { UsersFiltered } from '@/types/user';
import { useUsersFilters } from '@/hooks/useUsersFilters';

export const useUsers = () => {
  const { api } = useAxiosPrivate();
  const { pageNumber, pageSize, username, role, sort } = useUsersFilters();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['users', pageNumber, pageSize, username, role, sort],
    queryFn: async () => {
      try {
        const { data } = await api.get<UsersFiltered>('/users', {
          params: {
            page: pageNumber,
            size: pageSize,
            username: username,
            role: role,
            sort: sort,
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
