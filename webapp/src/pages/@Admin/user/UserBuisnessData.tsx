import ConfirmDialog from '@/components/ConfirmDialog';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { useBlockUser, useUnblockUser } from '@/data/useBlock';
import { User } from '@/types/user';
import { useQueryClient } from '@tanstack/react-query';
import { FC } from 'react';
import { useTranslation } from 'react-i18next';

interface UserBuisnessDataProps {
  user: User;
  tag_value: string;
}

const UserBuisnessData: FC<UserBuisnessDataProps> = ({ user, tag_value }) => {
  const { t } = useTranslation(['user']);
  const { blockUser } = useBlockUser();
  const { unblockUser } = useUnblockUser();
  const queryClient = useQueryClient();

  const handleClick = async (blocked: boolean) => {
    if (blocked) {
      await blockUser({ userId: user.id, if_match: tag_value });
      queryClient.invalidateQueries({ queryKey: ['user'] });
    } else {
      await unblockUser({ userId: user.id, if_match: tag_value });
      queryClient.invalidateQueries({ queryKey: ['user'] });
    }
  };

  return (
    <Card className="relative w-full">
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
      <div className="absolute right-0 top-0 m-1">
        {user.blocked ? (
          <ConfirmDialog
            variant="ghost"
            buttonText={t('block.unblock')}
            dialogTitle={t('block.unblockUser')}
            dialogDescription={t('block.unblockUserDescription')}
            confirmAction={() => handleClick(false)}
          />
        ) : (
          <ConfirmDialog
            variant="ghost"
            buttonText={t('block.block')}
            dialogTitle={t('block.blockUser')}
            dialogDescription={t('block.blockUserDescription')}
            confirmAction={() => handleClick(true)}
          />
        )}
      </div>
    </Card>
  );
};

export default UserBuisnessData;
