import { useQuery } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import { User } from '@/types/user';
import { AxiosError } from 'axios';
import { useTranslation } from 'react-i18next';
import { useToast } from '@/hooks/use-toast';
import { ApplicationError } from '@/types/applicationError';

export const useUsers = () => {
  const { api } = useAxiosPrivate();
  const { t } = useTranslation('errors');
  const { toast } = useToast();

  return useQuery({
    queryKey: ['users'],
    queryFn: async () => {
      try {
        const response = await api.get<User[]>('/users');
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
};
