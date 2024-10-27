import { useTranslation } from 'react-i18next';
import useAxiosPrivate from './useAxiosPrivate';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { User } from '@/types/user';
import { AxiosError } from 'axios';
import { toast } from '@/hooks/use-toast';
import { ApplicationError } from '@/types/applicationError';

interface UpdatePersonalDataRequest {
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
}

export const useUpdatePersonalData = () => {
  const { api } = useAxiosPrivate();
  const { t } = useTranslation(['errors', 'profile', 'common']);
  const queryClient = useQueryClient();

  const { mutateAsync } = useMutation({
    mutationFn: async (data: UpdatePersonalDataRequest) => {
      const response = await api.put<User>(`/me`, data);
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
      toast({
        title: t('common:defaultTitleSuccess'),
        description: t('profile:personalDataUpdated'),
      });
      queryClient.invalidateQueries({ queryKey: ['profile'] });
    },
  });

  const { mutateAsync: updateOtherUserPersonalData } = useMutation({
    mutationFn: async ({
      data,
      userId,
    }: {
      data: UpdatePersonalDataRequest;
      userId: string;
    }) => {
      const response = await api.put<User>(`/users/${userId}`, data);
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
      toast({
        title: t('common:defaultTitleSuccess'),
        description: t('profile:personalDataUpdated'),
      });
      queryClient.invalidateQueries({ queryKey: ['user'] });
    },
  });

  return { updatePersonalData: mutateAsync, updateOtherUserPersonalData };
};
