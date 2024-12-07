import { useMutation, useQuery } from '@tanstack/react-query';
import { api } from './api';
import { AxiosError } from 'axios';
import { useToast } from '@/hooks/use-toast';
import { useTranslation } from 'react-i18next';
import { ApplicationError } from '@/types/applicationError';
import { RegistrationData } from '@/types/registrationData';
import { useNavigate, useParams } from 'react-router-dom';

interface LoginRequest {
  username: string;
  password: string;
  language: string;
}

interface LoginResponse {
  token: string;
  refreshToken: string;
}

export const useAuthenticate = () => {
  const { toast } = useToast();
  const { t } = useTranslation('errors');

  const { mutateAsync, isSuccess, isPending } = useMutation({
    mutationFn: async (data: LoginRequest) => {
      const response = await api.post<LoginResponse | ApplicationError>(
        '/authenticate',
        data
      );
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: 'destructive',
        title: t('authenticationFailed'),
        description: t((error.response?.data as ApplicationError).code),
      });
    },
  });

  return { authenticate: mutateAsync, isSuccess, isPending };
};

interface TotpLoginRequest {
  username: string;
  totp: string;
}

export const useTotpAuthenticate = () => {
  const { toast } = useToast();
  const { t } = useTranslation('errors');

  const { mutateAsync, isSuccess, isPending } = useMutation({
    mutationFn: async (data: TotpLoginRequest) => {
      const response = await api.post<LoginResponse>('/verifyTotp', data);
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: 'destructive',
        title: t('authenticationFailed'),
        description: t((error.response?.data as ApplicationError).code),
      });
    },
  });

  return { authenticate: mutateAsync, isSuccess, isPending };
};

export const useRegister = () => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'common']);
  const navigate = useNavigate();

  const { mutateAsync, isSuccess, isPending } = useMutation({
    mutationFn: async (data: RegistrationData) => {
      const response = await api.post('/register', data);
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
        title: t('common:registration.sucessTitle'),
        description: t('common:registration.sucessMessage'),
      });
      navigate('/');
    },
  });

  return { register: mutateAsync, isSuccess, isPending };
};

export const useVerifyAccount = () => {
  const { toast } = useToast();
  const { token } = useParams<{ token: string }>();
  const { t } = useTranslation(['errors', 'registerPage']);

  const { mutateAsync } = useMutation({
    mutationFn: async () => {
      const response = await api.post(`/verify/${token}`);
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
        title: t('registerPage:verification.accountVerified'),
        description: t('registerPage:verification.accountVerifiedMessage'),
      });
    },
  });

  return { verifyAccount: mutateAsync };
};

interface OAuthResponse {
  url: string;
}

export const useOAuthUrl = () => {
  const { toast } = useToast();
  const { t } = useTranslation('errors');
  const { data } = useQuery({
    queryKey: ['oauthUrl'],
    queryFn: async () => {
      try {
        const response = await api.get<OAuthResponse>('/oauth/google');
        return response.data;
      } catch (error) {
        const axiosError = error as AxiosError;
        toast({
          variant: 'destructive',
          title: t('defaultTitle'),
          description: t((axiosError.response?.data as ApplicationError).code),
        });
        return Promise.reject(error);
      }
    },
  });

  return { googleOAuthUrl: data };
};
