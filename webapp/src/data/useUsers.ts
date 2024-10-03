import { useQuery } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import { UsersFilteded } from '@/types/user';
import { AxiosError } from 'axios';
import { useTranslation } from 'react-i18next';
import { useToast } from '@/hooks/use-toast';
import { ApplicationError } from '@/types/applicationError';
import { useUsersFilters } from '@/hooks/useUsersFilters';

export const useUsers = () => {
  const { api } = useAxiosPrivate();
  const { t } = useTranslation('errors');
  const { toast } = useToast();
  const { pageNumber, pageSize, username, role, sort, setFilters } =
    useUsersFilters();

  const { data, isLoading } = useQuery({
    queryKey: ['users', pageNumber, pageSize, username, role, sort],
    queryFn: async () => {
      try {
        const response = await api.get<UsersFilteded>('/users', {
          params: {
            page: pageNumber,
            size: pageSize,
            username: username,
            role: role,
            sort: sort,
          },
        });
        setFilters({
          pageNumber: response.data.pageNumber,
          pageSize: response.data.pageSize,
        });
        return response.data;
      } catch (e) {
        console.log(e);
        const error = e as AxiosError;
        toast({
          variant: 'destructive',
          title: t('defaultTitle'),
          description: t((error.response?.data as ApplicationError).code),
        });
      }
    },
  });

  return {
    users: data?.users,
    isLoading,
    totalPages: data?.totalPages,
    pageNumber: data?.pageNumber,
    pageSize: data?.pageSize,
  };
};
