import LoadingIcon from '@/components/loading';
import { TwoFactorProps } from '@/components/login/LoginForm';
import TotpInput from '@/components/login/TotpInput';
import { api } from '@/data/api';
import { LoginResponse } from '@/data/useAuthenticate';
import { useToast } from '@/hooks/use-toast';
import { getActiveRole, roleMapping, useUserStore } from '@/store/userStore';
import { ApplicationError } from '@/types/applicationError';
import { AxiosError } from 'axios';
import { FC, useEffect, useRef, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

const OAuthRedirect: FC = () => {
  const called = useRef(false);
  const navigate = useNavigate();
  const { setToken, setRefreshToken, roles } = useUserStore();
  const { toast } = useToast();
  const [isLoading, setIsLoading] = useState(false);
  const { t } = useTranslation(['loginPage', 'errors', 'common']);
  const [twoFactorOpen, setTwoFactorOpen] = useState<TwoFactorProps>({
    username: '',
    open: false,
  });

  useEffect(() => {
    (async () => {
      if (called.current) return;
      setIsLoading(true);
      try {
        called.current = true;
        const code = window.location.search;

        const result = await api.get<
          LoginResponse | { username: { data: string } }
        >(`/oauth/google/token${code}`);

        if (result.status === 201) {
          navigate('/');
          toast({
            title: t('common:registration.sucessTitle'),
            description: t('common:registration.sucessMessage'),
          });
          return;
        } else if (result.status === 200 && 'username' in result.data) {
          setTwoFactorOpen({ username: result.data.username.data, open: true });
          return;
        } else if (result.status === 200 && 'token' in result.data) {
          setToken('token' in result.data ? result.data.token : '', true);
          setRefreshToken(
            'refreshToken' in result.data ? result.data.refreshToken : ''
          );
          const unsubscribe = useUserStore.subscribe((state) => {
            const roles = state.roles;
            if (roles) {
              navigate(`/${roleMapping[getActiveRole(roles)]}`);
              unsubscribe();
            }
          });
        }
      } catch (e) {
        const error = e as AxiosError;
        if (error.response?.status === 422) {
          toast({
            description: t('common:fillData'),
          });
          navigate('/oauth/fill-data', {
            state: {
              data: error.response.data,
            },
          });
          return;
        }

        if (
          error.response &&
          error.response.data &&
          typeof error.response.data === 'object' &&
          'code' in error.response.data &&
          'message' in error.response.data
        ) {
          toast({
            variant: 'destructive',
            title: t('errors:authenticationFailed'),
            description: t(
              `errors:${(error.response?.data as ApplicationError).code}`
            ),
          });
        } else {
          toast({
            variant: 'destructive',
            title: t('errors:authenticationFailed'),
          });
        }
        navigate('/');
      } finally {
        setIsLoading(false);
      }
    })();
  }, [navigate, roles, setRefreshToken, setToken, t, toast]);

  return (
    <>
      {isLoading && (
        <div className="flex h-full items-center justify-center">
          <LoadingIcon />
        </div>
      )}
      <div>
        <TotpInput
          open={twoFactorOpen.open}
          onOpenChange={() => setTwoFactorOpen({ username: '', open: false })}
          username={twoFactorOpen.username}
          useOAuth={true}
        />
      </div>
    </>
  );
};

export default OAuthRedirect;
