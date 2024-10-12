import { useToast } from '@/hooks/use-toast';
import { useTranslation } from 'react-i18next';
import useAxiosPrivate from './useAxiosPrivate';
import { useMutation } from '@tanstack/react-query';
import { ApplicationError } from '@/types/applicationError';
import { AxiosError } from 'axios';

export const useBlockUser = () => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors']);
  const { api } = useAxiosPrivate();

  const { mutateAsync, isSuccess } = useMutation({
    mutationFn: async (userId: string) => {
      const response = await api.put<unknown>(`/users/block/${userId}`);
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: 'destructive',
        title: t('errors:defaultTitle'),
        description: t(
          'errors:' + (error.response?.data as ApplicationError).code
        ),
      });
    },
  });

  return { blockUser: mutateAsync, isSuccess };
};

export const useUnblockUser = () => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors']);
  const { api } = useAxiosPrivate();

  const { mutateAsync, isSuccess } = useMutation({
    mutationFn: async (userId: string) => {
      const response = await api.delete<unknown>(`/users/block/${userId}`);
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: 'destructive',
        title: t('errors:defaultTitle'),
        description: t(
          'errors:' + (error.response?.data as ApplicationError).code
        ),
      });
    },
  });

  return { unblockUser: mutateAsync, isSuccess };
};
