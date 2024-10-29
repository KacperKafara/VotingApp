import LoadingIcon from '@/components/loading';
import { useUser } from '@/data/useUsers';
import { toast } from '@/hooks/use-toast';
import { ApplicationError } from '@/types/applicationError';
import axios from 'axios';
import { FC, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate, useParams } from 'react-router-dom';
import UserRolesCard from './UserRolesCard';
import UserDataCard from './UserDataCard';
import UserBuisnessData from './UserBuisnessData';

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
        naviagte(-1);
      }
    }
  }, [error, isError, naviagte, t]);

  if (isLoading || isError || !user) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoadingIcon />
      </div>
    );
  }

  const etag = user.headers.etag as string;

  return (
    <div className="flex h-full w-full items-center justify-center">
      <div className="flex h-4/5 w-11/12 flex-wrap gap-3 p-10">
        <UserDataCard
          user={user.data}
          tag_value={etag.substring(1, etag.length - 1)}
        />
        <div className="flex w-full gap-3">
          <UserBuisnessData
            user={user.data}
            tag_value={etag.substring(1, etag.length - 1)}
          />
          <UserRolesCard
            user={user.data}
            tag_value={etag.substring(1, etag.length - 1)}
          />
        </div>
      </div>
    </div>
  );
};

export default UserPage;
