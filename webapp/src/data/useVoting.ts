import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import {
  VotingDetailsResponse,
  VotingOption,
  VotingResponse,
  VotingWithoutVotesResponse,
} from '@/types/voting';
import { useVotingListFilters } from '@/hooks/useVotingListFilters';
import { useToast } from '@/hooks/use-toast';
import { useTranslation } from 'react-i18next';
import { CreateVoteRequest } from '@/types/survey';
import { AxiosError } from 'axios';
import { ApplicationError } from '@/types/applicationError';

export const useVoting = (id: string) => {
  const { api } = useAxiosPrivate();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['voting', id],
    queryFn: async () => {
      try {
        const { data } = await api.get<VotingResponse>(`/votings/${id}`);
        return data;
      } catch (e) {
        return Promise.reject(e);
      }
    },
  });

  return { isError, isLoading, error, data };
};

interface VotingListResponse {
  votingList: VotingWithoutVotesResponse[];
  sittings: number[];
  totalPages: number;
  pageNumber: number;
  pageSize: number;
}

export const useVotingList = (wasActive = false) => {
  const { api } = useAxiosPrivate();
  const { pageNumber, pageSize, title, sitting, sort } = useVotingListFilters();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['votingList', pageNumber, pageSize, title, sitting, sort],
    queryFn: async () => {
      try {
        const { data } = await api.get<VotingListResponse>('/votings', {
          params: {
            page: pageNumber,
            size: pageSize,
            sort: sort,
            title: title,
            sitting: sitting,
            wasActive: wasActive,
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

export const useCreateVote = () => {
  const { api } = useAxiosPrivate();
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'voting']);
  const queryClient = useQueryClient();

  const { mutateAsync, isPending } = useMutation({
    mutationFn: async (voteRequest: CreateVoteRequest) => {
      const response = await api.post(`/votings/${voteRequest.surveyId}/vote`, {
        voteResult: voteRequest.voteResult,
        totp: voteRequest.totp,
      });

      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: 'destructive',
        title: t('voting:sheet.errorTitle'),
        description: t(
          'errors:' + (error.response?.data as ApplicationError).code
        ),
      });
    },
    onSuccess: () => {
      toast({
        title: t('voting:sheet.successTitle'),
        description: t('voting:sheet.successMessage'),
      });
      queryClient.invalidateQueries({ queryKey: ['voting'] });
    },
  });

  return { createVote: mutateAsync, isLoading: isPending };
};

export const useVotingOptions = (id: string, enabled: boolean) => {
  const { api } = useAxiosPrivate();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['votingOptions', id],
    queryFn: async () => {
      try {
        const { data } = await api.get<VotingOption[]>(
          `/votings/${id}/votingOptions`
        );
        return data;
      } catch (e) {
        return Promise.reject(e);
      }
    },
    enabled,
  });

  return { isError, isLoading, error, data };
};

export const useActivateVoting = () => {
  const { api } = useAxiosPrivate();
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'voting']);
  const queryClient = useQueryClient();

  const { mutateAsync, isPending } = useMutation({
    mutationFn: async ({ id, endDate }: { id: string; endDate: Date }) => {
      const response = await api.patch(`/votings/${id}`, {
        endDate: endDate,
      });

      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: 'destructive',
        title: t('defaultTitle'),
        description: t(
          'errors:' + (error.response?.data as ApplicationError).code
        ),
      });
    },
    onSuccess: () => {
      toast({
        title: t('voting:activateVoting.successTitle'),
        description: t('voting:activateVoting.successMessage'),
      });
      queryClient.invalidateQueries({ queryKey: ['votingDetails'] });
    },
  });

  return { activateVoting: mutateAsync, isLoading: isPending };
};

export const useVotingDetails = (id: string) => {
  const { api } = useAxiosPrivate();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['votingDetails', id],
    queryFn: async () => {
      try {
        const { data } = await api.get<VotingDetailsResponse>(
          `/votings/${id}/details`
        );
        return data;
      } catch (e) {
        return Promise.reject(e);
      }
    },
  });

  return { isError, isLoading, error, data };
};
