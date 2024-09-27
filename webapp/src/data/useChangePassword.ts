import { useToast } from '@/hooks/use-toast';
import { ApplicationError } from '@/types/applicationError';
import { useMutation } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { useTranslation } from 'react-i18next';
import useAxiosPrivate from './useAxiosPrivate';

interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}

export const useChangePassword = (onOpenChange: () => void) => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'changePassword']);
  const { api } = useAxiosPrivate();

  const { mutateAsync, isSuccess, isPending } = useMutation({
    mutationFn: async (data: ChangePasswordRequest) => {
      const response = await api.patch<unknown>('/me/password', data);
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
      onOpenChange();
      toast({
        title: t('changePassword:passwordChanged'),
        description: t('changePassword:passwordChangedDescription'),
      });
    },
  });

  return { changePassword: mutateAsync, isPending, isSuccess };
};
