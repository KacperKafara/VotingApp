import LoadingIcon from '@/components/loading';
import { useProfile } from '@/data/useProfile';
import { toast } from '@/hooks/use-toast';
import { ApplicationError } from '@/types/applicationError';
import axios from 'axios';
import { FC, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import PersonalData from './PersonalData';
import LastLoginData from './LastLoginData';

const ProfilePage: FC = () => {
  const { data: user, isLoading, isError, error } = useProfile();
  const { t } = useTranslation(['profile', 'errors']);
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
    <div className="flex h-full justify-center">
      <div className="flex h-full w-full flex-col gap-y-4 px-2 md:w-4/5 md:px-0">
        <span className="pb-2 pt-10 text-center font-raleway text-4xl md:pb-5 md:pt-20">
          {t('hello') + user.data.username}
        </span>
        <PersonalData
          className="w-full py-4"
          user={user.data}
          tag_value={etag.substring(1, etag.length - 1)}
        />
        <LastLoginData className="w-full" user={user.data} />
      </div>
    </div>
  );
};

export default ProfilePage;
