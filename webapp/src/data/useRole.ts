import { useToast } from '@/hooks/use-toast';
import { useMutation } from '@tanstack/react-query';
import { useTranslation } from 'react-i18next';
import useAxiosPrivate from './useAxiosPrivate';
import { AxiosError } from 'axios';
import { ApplicationError } from '@/types/applicationError';

export const useRole = () => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'user']);
  const { api } = useAxiosPrivate();

  const { mutateAsync } = useMutation({
    mutationFn: async ({
      roles,
      userId,
    }: {
      roles: string[];
      userId: string;
    }) => {
      const response = await api.put<unknown>(`/users/${userId}/roles`, {
        roles,
      });
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
    onSuccess: () => {
      toast({
        title: t('user:roles.successTitle'),
        description: t('user:roles.successDescription'),
      });
    },
  });

  return { modifyRoles: mutateAsync };
};
