import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { User } from '@/types/user';
import { FC } from 'react';
import { useTranslation } from 'react-i18next';

interface LastLoginDataProps {
  user: User;
  className?: string;
}

const LastLoginData: FC<LastLoginDataProps> = ({ user, className }) => {
  const { t } = useTranslation('profile');

  return (
    <Card className={className}>
      <CardHeader>
        <CardTitle>{t('lastLoginData')}</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('lastSuccessfulLogin')}
            </span>
            <p>
              {new Date(user.lastLogin).toLocaleDateString(
                navigator.language || 'pl-PL'
              )}
            </p>
          </div>
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('lastFailedLogin')}
            </span>
            <p>
              {new Date(user.lastFailedLogin).toLocaleDateString(
                navigator.language || 'pl-PL'
              )}
            </p>
          </div>
        </div>
      </CardContent>
    </Card>
  );
};

export default LastLoginData;
