import { useToast } from '@/hooks/use-toast';
import { useTranslation } from 'react-i18next';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { ApplicationError } from '@/types/applicationError';
import {
  CreateSurveyRequest,
  CreateVoteRequest,
  SurveyListResponse,
  SurveyResponse,
} from '@/types/survey';
import useAxiosPrivate from './useAxiosPrivate';
import { useSurveysFilters } from '@/hooks/useSurveysFilters';

export const useCreateSurvey = () => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'survey']);
  const { api } = useAxiosPrivate();
  const queryClient = useQueryClient();

  const { mutateAsync, isPending } = useMutation({
    mutationFn: async (data: CreateSurveyRequest) => {
      const response = await api.post<SurveyResponse>('/surveys', data);
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
      queryClient.invalidateQueries({ queryKey: ['surveys'] });
    },
  });

  return { createSurvey: mutateAsync, isLoading: isPending };
};

export const useSurveys = () => {
  const { api } = useAxiosPrivate();
  const { pageNumber, pageSize, title, kind, sort } = useSurveysFilters();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['surveys', pageNumber, pageSize, title, kind, sort],
    queryFn: async () => {
      try {
        const { data } = await api.get<SurveyListResponse>('/surveys', {
          params: {
            page: pageNumber,
            size: pageSize,
            title: title,
            kind: kind,
            sort: sort,
          },
        });
        return data;
      } catch (e) {
        return Promise.reject(e);
      }
    },
  });

  return { isError, isLoading, error, data };
};

export const useSurvey = (id: string) => {
  const { api } = useAxiosPrivate();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['survey', id],
    queryFn: async () => {
      try {
        const { data } = await api.get<SurveyResponse>(`/surveys/${id}`);
        return data;
      } catch (e) {
        return Promise.reject(e);
      }
    },
  });

  return { isError, isLoading, error, data };
};

export const useLatestSurvey = () => {
  const { api } = useAxiosPrivate();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['latest'],
    queryFn: async () => {
      try {
        const { data } = await api.get<SurveyResponse>('/surveys/latest');
        return data;
      } catch (e) {
        return Promise.reject(e);
      }
    },
  });

  return { isError, isLoading, error, data };
};

export const useCreateVote = () => {
  const { api } = useAxiosPrivate();
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'survey']);
  const queryClient = useQueryClient();

  const { mutateAsync, isPending } = useMutation({
    mutationFn: async (voteRequest: CreateVoteRequest) => {
      const response = await api.post(`/surveys/${voteRequest.surveyId}/vote`, {
        voteResult: voteRequest.voteResult,
        totp: voteRequest.totp,
      });
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: 'destructive',
        title: t('survey:sheet.errorTitle'),
        description: t(
          'errors:' + (error.response?.data as ApplicationError).code
        ),
      });
    },
    onSuccess: () => {
      toast({
        title: t('survey:sheet.successTitle'),
        description: t('survey:sheet.successMessage'),
      });
      queryClient.invalidateQueries({ queryKey: ['survey'] });
    },
  });

  return { createVote: mutateAsync, isLoading: isPending };
};
