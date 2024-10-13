import { useToast } from '@/hooks/use-toast';
import { useTranslation } from 'react-i18next';
import useAxiosPrivate from './useAxiosPrivate';
import { useMutation } from '@tanstack/react-query';
import { ApplicationError } from '@/types/applicationError';
import { AxiosError } from 'axios';

export const useBlockUser = () => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'user']);
  const { api } = useAxiosPrivate();

  const { mutateAsync, isSuccess } = useMutation({
    mutationFn: async (userId: string) => {
      const response = await api.put<unknown>(`/users/${userId}/block`);
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: 'destructive',
        title: t('user:block.blockTitle'),
        description: t(
          'errors:' + (error.response?.data as ApplicationError).code
        ),
      });
    },
    onSuccess: () => {
      toast({
        title: t('user:block.blockTitle'),
        description: t('user:block.blockDescriptionSuccess'),
      });
    },
  });

  return { blockUser: mutateAsync, isSuccess };
};

export const useUnblockUser = () => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'user']);
  const { api } = useAxiosPrivate();

  const { mutateAsync, isSuccess } = useMutation({
    mutationFn: async (userId: string) => {
      const response = await api.delete<unknown>(`/users/${userId}/block`);
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: 'destructive',
        title: t('user:block.unBlockTitle'),
        description: t(
          'errors:' + (error.response?.data as ApplicationError).code
        ),
      });
    },
    onSuccess: () => {
      toast({
        title: t('user:block.unBlockTitle'),
        description: t('user:block.unBlockDescriptionSuccess'),
      });
    },
  });

  return { unblockUser: mutateAsync, isSuccess };
};
