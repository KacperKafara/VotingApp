import { useToast } from '@/hooks/use-toast';
import { useTranslation } from 'react-i18next';
import { useMutation } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { ApplicationError } from '@/types/applicationError';
import { CreateSurveyRequest } from '@/types/survey';
import useAxiosPrivate from './useAxiosPrivate';

export const useCreateSurvey = () => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'survey']);
  const { api } = useAxiosPrivate();

  const { mutateAsync, isPending } = useMutation({
    mutationFn: async (data: CreateSurveyRequest) => {
      const response = await api.post<unknown>('/surveys', data);
      return response.data;
    },
    onError: (error: AxiosError) => {
      if (error.response?.status === 409) {
        toast({
          variant: 'destructive',
          title: t('survey:create.errorTitle'),
          description: t('survey:create.errorConflict'),
        });
      } else {
        toast({
          variant: 'destructive',
          title: t('survey:create.errorTitle'),
          description: t(
            'errors:' + (error.response?.data as ApplicationError).code
          ),
        });
      }
    },
    onSuccess: () => {
      toast({
        title: t('survey:create.successTitle'),
        description: t('survey:create.successMessage'),
      });
    },
  });

  return { createSurvey: mutateAsync, isLoading: isPending };
};
