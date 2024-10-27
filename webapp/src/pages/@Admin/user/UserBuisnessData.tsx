import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { User } from '@/types/user';
import { FC } from 'react';
import { useTranslation } from 'react-i18next';

interface UserBuisnessDataProps {
  user: User;
}

const UserBuisnessData: FC<UserBuisnessDataProps> = ({ user }) => {
  const { t } = useTranslation(['user']);

  return (
    <Card className="w-full">
      <CardHeader>
        <CardTitle>{t('otherData')}</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('lastLogin')}
            </span>
            <p>
              {new Date(user.lastLogin).toLocaleString(
                navigator.language || 'pl-PL'
              )}
            </p>
          </div>
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('lastFailedLogin')}
            </span>
            <p>
              {new Date(user.lastFailedLogin).toLocaleString(
                navigator.language || 'pl-PL'
              )}
            </p>
          </div>
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('blocked')}
            </span>
            <p>{user.blocked ? t('yes') : t('no')}</p>
          </div>
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('verified')}
            </span>
            <p>{user.verified ? t('yes') : t('no')}</p>
          </div>
        </div>
      </CardContent>
    </Card>
  );
};

export default UserBuisnessData;
