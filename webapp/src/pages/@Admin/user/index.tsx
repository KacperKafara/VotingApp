import LoadingIcon from '@/components/loading';
import {
  Card,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { useUser } from '@/data/useUsers';
import { toast } from '@/hooks/use-toast';
import { ApplicationError } from '@/types/applicationError';
import axios from 'axios';
import { FC, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate, useParams } from 'react-router-dom';
import UserRolesCard from './UserRolesCard';

const UserPage: FC = () => {
  const { username } = useParams<{ username: string }>();
  const { data: user, isLoading, isError, error } = useUser(username!);
  const { t } = useTranslation(['user', 'errors']);
  const naviagte = useNavigate();

  useEffect(() => {
    if (isError && axios.isAxiosError(error)) {
      toast({
        variant: 'destructive',
        title: t('errors:defaultTitle'),
        description: t(
          'errors:' + (error.response?.data as ApplicationError).code
        ),
      });
      if (error.response?.status === 404) {
        naviagte('/admin/users');
      }
    }
  }, [error, isError, naviagte, t]);

  if (isLoading || isError) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoadingIcon />
      </div>
    );
  }

  return (
    <div className="flex flex-wrap gap-3 p-10">
      <Card>
        <CardHeader>
          <CardTitle>{t('basicInformation')}</CardTitle>
          <CardDescription>
            {t('basicInformationDescription')}
            {user?.username}
          </CardDescription>
        </CardHeader>
      </Card>
      <UserRolesCard user={user!} />
    </div>
  );
};

export default UserPage;
