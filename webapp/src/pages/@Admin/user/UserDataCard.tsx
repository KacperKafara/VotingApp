import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { User } from '@/types/user';
import { FC } from 'react';
import { useTranslation } from 'react-i18next';

interface UserDataCardProps {
  user: User;
}

const UserDataCard: FC<UserDataCardProps> = ({ user }) => {
  const { t } = useTranslation(['user']);

  return (
    <Card>
      <CardHeader>
        <CardTitle>{t('basicInformation')}</CardTitle>
        <CardDescription>
          {t('basicInformationDescription')}
          {user.username}
        </CardDescription>
      </CardHeader>
      <CardContent>
        <p>
          <b>{t('firstName')}: </b>
          {user.firstName}
        </p>
        <p>
          <b>{t('lastName')}: </b>
          {user.lastName}
        </p>
        <p>
          <b>{t('email')}: </b>
          {user.email}
        </p>
        <p>
          <b>{t('phoneNumber')}: </b>
          {user.phoneNumber}
        </p>
        <p>
          <b>{t('birthDate')}: </b>
          {new Date(user.birthDate).toLocaleDateString(
            navigator.language || 'pl-PL'
          )}
        </p>
        <p>
          <b>{t('lastLogin')}: </b>
          {new Date(user.lastLogin).toLocaleString(
            navigator.language || 'pl-PL'
          )}
        </p>
        <p>
          <b>{t('lastFailedLogin')}: </b>
          {new Date(user.lastFailedLogin).toLocaleString(
            navigator.language || 'pl-PL'
          )}
        </p>
        <p>
          <b>{t('blocked')}: </b>
          {user.blocked ? t('yes') : t('no')}
        </p>
        <p>
          <b>{t('verified')}: </b>
          {user.verified ? t('yes') : t('no')}
        </p>
      </CardContent>
    </Card>
  );
};

export default UserDataCard;
