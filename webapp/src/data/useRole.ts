import { useToast } from '@/hooks/use-toast';
import { useMutation } from '@tanstack/react-query';
import { useTranslation } from 'react-i18next';
import useAxiosPrivate from './useAxiosPrivate';
import { AxiosError } from 'axios';
import { ApplicationError } from '@/types/applicationError';

export const useRole = () => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'roles']);
  const { api } = useAxiosPrivate();

  const { mutateAsync: mutateAddRole, isSuccess } = useMutation({
    mutationFn: async ({ role, userId }: { role: string; userId: string }) => {
      const response = await api.put<unknown>('/users/role', {
        userId: userId,
        role: role,
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
  });

  const { mutateAsync: mutateDeleteRole, isSuccess } = useMutation({
    mutationFn: async ({ role, userId }: { role: string; userId: string }) => {
      const response = await api.delete<unknown>('/users/role', {
        data: {
          userId: userId,
          role: role,
        },
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
  });

  return { addRole: mutateAddRole, deleteRole: mutateDeleteRole, isSuccess };
};
