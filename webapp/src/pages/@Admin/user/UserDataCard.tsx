import EditProfile from '@/components/EditProfile';
import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { User } from '@/types/user';
import { FC, startTransition, useState } from 'react';
import { useTranslation } from 'react-i18next';

interface UserDataCardProps {
  user: User;
  tag_value: string;
}

const UserDataCard: FC<UserDataCardProps> = ({ user, tag_value }) => {
  const { t } = useTranslation(['user']);
  const [editProfileOpen, setEditProfileOpen] = useState(false);

  return (
    <Card className="relative w-full min-w-96">
      <CardHeader>
        <CardTitle>{t('basicInformation')}</CardTitle>
        <CardDescription>
          {t('basicInformationDescription')}
          {user.username}
        </CardDescription>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('firstName')}
            </span>
            <p>{user.firstName}</p>
          </div>
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('lastName')}
            </span>
            <p>{user.lastName}</p>
          </div>
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('email')}
            </span>
            <p>{user.email}</p>
          </div>
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('phoneNumber')}
            </span>
            <p>{user.phoneNumber}</p>
          </div>
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('birthDate')}
            </span>
            <p>
              {new Date(user.birthDate).toLocaleDateString(
                navigator.language || 'pl-PL'
              )}
            </p>
          </div>
          <div>
            <span className="text-sm font-semibold text-gray-500">
              {t('gender')}
            </span>
            <p>{t(user.gender)}</p>
          </div>
        </div>
      </CardContent>
      <Button
        variant="ghost"
        className="absolute right-0 top-0 m-1"
        onClick={() => {
          startTransition(() => setEditProfileOpen(!editProfileOpen));
        }}
      >
        {t('updateData')}
      </Button>
      {editProfileOpen && (
        <EditProfile
          open={editProfileOpen}
          onOpenChange={() => setEditProfileOpen(!editProfileOpen)}
          user={user}
          variant="otherUserData"
          tag_value={tag_value}
        />
      )}
    </Card>
  );
};

export default UserDataCard;
