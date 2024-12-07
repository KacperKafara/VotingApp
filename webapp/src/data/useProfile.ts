import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import { User } from '@/types/user';
import { useToast } from '@/hooks/use-toast';
import { useTranslation } from 'react-i18next';
import { AxiosError } from 'axios';
import { ApplicationError } from '@/types/applicationError';

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

export const useUpdate2FA = () => {
  const { api } = useAxiosPrivate();
  const { toast } = useToast();
  const { t } = useTranslation('errors');
  const queryClient = useQueryClient();

  const { mutateAsync, isPending, isSuccess } = useMutation({
    mutationFn: async (active: boolean) => {
      const response = await api.patch('/me/2fa', { active: active });
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: 'destructive',
        title: t('defaultTitle'),
        description: t((error.response?.data as ApplicationError).code),
      });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['profile'] });
    },
  });

  return { update2FA: mutateAsync, isLoading: isPending, isSuccess };
};
