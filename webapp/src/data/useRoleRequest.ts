import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import { AxiosError } from 'axios';
import { toast } from '@/hooks/use-toast';
import { ApplicationError } from '@/types/applicationError';
import { useTranslation } from 'react-i18next';
import { useVoterRoleRequestFilters } from '@/hooks/useRoleRequestFilters';
import { VoterRoleRequestListResponse } from '@/types/voterRoleRequest';

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
      if (error.status === 409) {
        toast({
          variant: 'destructive',
          title: t('defaultTitle'),
          description: t('roleRequest:errors.roleRequestAlreadyExists'),
        });
      } else {
        toast({
          variant: 'destructive',
          title: t('defaultTitle'),
          description: `${t((error.response?.data as ApplicationError).code)}`,
        });
      }
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

export const useAcceptRoleRequest = () => {
  const { api } = useAxiosPrivate();
  const { t } = useTranslation(['errors', 'roleRequest']);
  const queryClient = useQueryClient();

  const { mutateAsync, isPending } = useMutation({
    mutationFn: async (id: string) => {
      const response = await api.post(`/voterRoleRequest/${id}`);
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
        title: t('roleRequest:accept.title'),
        description: t('roleRequest:accept.description'),
      });
      queryClient.invalidateQueries({ queryKey: ['roleRequests'] });
    },
  });

  return { acceptRoleRequest: mutateAsync, isPending };
};

export const useRejectRoleRequest = () => {
  const { api } = useAxiosPrivate();
  const { t } = useTranslation(['errors', 'roleRequest']);
  const queryClient = useQueryClient();

  const { mutateAsync, isPending } = useMutation({
    mutationFn: async (id: string) => {
      const response = await api.delete(`/voterRoleRequest/${id}`);
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
        title: t('roleRequest:reject.title'),
        description: t('roleRequest:reject.description'),
      });
      queryClient.invalidateQueries({ queryKey: ['roleRequests'] });
    },
  });

  return { rejectRoleRequest: mutateAsync, isPending };
};

export const useRoleRequests = () => {
  const { api } = useAxiosPrivate();
  const { pageNumber, pageSize, username, sort } = useVoterRoleRequestFilters();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['roleRequests', pageNumber, pageSize, username, sort],
    queryFn: async () => {
      try {
        const { data } = await api.get<VoterRoleRequestListResponse>(
          '/voterRoleRequest',
          {
            params: {
              page: pageNumber,
              size: pageSize,
              username: username,
              sort: sort,
            },
          }
        );
        return data;
      } catch (e) {
        return Promise.reject(e);
      }
    },
  });

  return { isError, isLoading, error, data };
};
