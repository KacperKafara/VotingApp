import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import useAxiosPrivate from './useAxiosPrivate';
import { useTranslation } from 'react-i18next';
import { useToast } from '@/hooks/use-toast';
import { AxiosError } from 'axios';
import { ApplicationError } from '@/types/applicationError';

export interface ParliamentaryClubsResponse {
  id: string;
  shortName: string;
}

export const useParliamentaryClubs = (enabled: boolean) => {
  const { api } = useAxiosPrivate();

  const { isError, isLoading, error, data } = useQuery({
    queryKey: ['parliamentaryClubs'],
    queryFn: async () => {
      try {
        const { data } = await api.get<ParliamentaryClubsResponse[]>(
          '/parliamentary-clubs'
        );
        return { data };
      } catch (e) {
        return Promise.reject(e);
      }
    },
    enabled,
  });

  return { isError, isLoading, error, data };
};

export const useUpdateParliamentaryClub = () => {
  const { api } = useAxiosPrivate();
  const { t } = useTranslation(['errors', 'user']);
  const { toast } = useToast();
  const queryClient = useQueryClient();

  const { mutateAsync, isSuccess, isPending } = useMutation({
    mutationFn: async ({ id, if_match }: { id: string; if_match: string }) => {
      const response = await api.patch(
        '/me/parliamentaryClub',
        { parliamentaryClubId: id },
        {
          headers: {
            'If-Match': if_match,
          },
        }
      );
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
        title: t('user:parliamentaryClub.successTitle'),
        description: t('user:parliamentaryClub.successDescription'),
      });
      queryClient.invalidateQueries({ queryKey: ['profile'] });
    },
  });

  return { updateParliamentaryClub: mutateAsync, isSuccess, isPending };
};
