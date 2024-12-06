import { FC } from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '../ui/button';
import { FaGoogle } from 'react-icons/fa';
import { useOAuthUrl } from '@/data/useAuthenticate';

const GoogleLoginButton: FC = () => {
  const { t } = useTranslation('loginPage');
  const { googleOAuthUrl } = useOAuthUrl();

  return (
    <Button
      variant="outline"
      type="button"
      className="flex items-center justify-around"
      onClick={() => {
        window.location.href = googleOAuthUrl?.url || '';
      }}
    >
      <FaGoogle className="h-4 w-4" />
      {t('googleLogin')}
    </Button>
  );
};

export default GoogleLoginButton;
