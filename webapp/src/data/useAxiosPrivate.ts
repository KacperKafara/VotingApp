import { useUserStore } from '@/store/userStore';
import { useEffect } from 'react';
import { api } from './api';
import { useNavigate } from 'react-router-dom';
import { useToast } from '@/hooks/use-toast';
import { t } from 'i18next';

const useAxiosPrivate = () => {
  const { token, logOut } = useUserStore();
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
      (error) => {
        if (error.response?.status === 401) {
          logOut();
          navigate('/');
          toast({
            title: t('sessionExpired'),
            description: t('sessionExpiredMessage'),
          });
        }
        return Promise.reject(error);
      }
    );

    return () => {
      api.interceptors.request.eject(requestInterceptor);
      api.interceptors.response.eject(responseInterceptor);
    };
  }, [token, logOut, navigate, toast]);

  return { api };
};

export default useAxiosPrivate;
