import { useUserStore } from '@/store/userStore';
import { useEffect } from 'react';
import { api, refreshApi } from './api';
import { useNavigate } from 'react-router-dom';
import { useToast } from '@/hooks/use-toast';
import { useTranslation } from 'react-i18next';
import { AxiosError } from 'axios';

const useAxiosPrivate = () => {
  const { token, refreshToken, setToken, logOut } = useUserStore();
  const { t } = useTranslation('errors');
  const navigate = useNavigate();
  const { toast } = useToast();
  useEffect(() => {
    const requestInterceptor = api.interceptors.request.use(
      (config) => {
        if (token) config.headers.Authorization = `Bearer ${token}`;
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    const responseInterceptor = api.interceptors.response.use(
      (response) => response,
      async (error) => {
        const prevRequest = error.config;
        error = error as AxiosError;
        if (refreshToken === undefined) {
          logOut();
          return Promise.reject(error);
        }
        if (error.response?.status === 401 && !prevRequest._retry) {
          prevRequest._retry = true;

          try {
            const response = await refreshApi.post('/refresh', {
              data: refreshToken,
            });
            const newToken = response.data.token;
            setToken(newToken);
            prevRequest.headers.Authorization = `Bearer ${newToken}`;
            return api.request(prevRequest);
          } catch (error) {
            logOut();
            navigate('/');
            toast({
              title: t('sessionExpired'),
              description: t('sessionExpiredMessage'),
            });
            return Promise.reject(error);
          }
        }
        return Promise.reject(error);
      }
    );

    return () => {
      api.interceptors.request.eject(requestInterceptor);
      api.interceptors.response.eject(responseInterceptor);
    };
  }, [token, logOut, navigate, toast, t, refreshToken, setToken]);

  return { api };
};

export default useAxiosPrivate;
