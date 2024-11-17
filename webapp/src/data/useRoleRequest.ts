import { useMutation, useQueryClient } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import { AxiosError } from 'axios';
import { toast } from '@/hooks/use-toast';
import { ApplicationError } from '@/types/applicationError';
import { useTranslation } from 'react-i18next';

export const useCreateRoleRequest = () => {
  const { api } = useAxiosPrivate();
  const { t } = useTranslation(['errors', 'roleRequest']);
  const queryClient = useQueryClient();

  const { mutateAsync, isPending } = useMutation({
    mutationFn: async () => {
      const response = await api.post('/voterRoleRequest');
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: 'destructive',
        title: t('defaultTitle'),
        description: `${t((error.response?.data as ApplicationError).code)}`,
      });
    },
    onSuccess: () => {
      toast({
        title: t('roleRequest:create.title'),
        description: t('roleRequest:create.description'),
      });
      queryClient.invalidateQueries({ queryKey: ['profile'] });
    },
  });

  return { createRoleRequest: mutateAsync, isPending };
};
