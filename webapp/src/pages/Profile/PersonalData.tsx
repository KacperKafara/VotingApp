import ChangePasswordDialog from '@/components/ChangePasswordDialog';
import EditProfile from '@/components/EditProfile';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { cn } from '@/lib/utils';
import { User } from '@/types/user';
import { FC, useState } from 'react';
import { useTranslation } from 'react-i18next';

interface PersonalDataProps {
  user: User;
  className?: string;
  tag_value: string;
}

const PersonalData: FC<PersonalDataProps> = ({
  user,
  className,
  tag_value,
}) => {
  const { t } = useTranslation('profile');
  const [editProfileOpen, setEditProfileOpen] = useState(false);
  const [changePasswordDialogOpen, setChangePasswordDialogOpen] =
    useState(false);

  return (
    <Card className={cn(className, 'relative')}>
      <CardHeader>
        <CardTitle>{t('personalData')}</CardTitle>
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
      <DropdownMenu>
        <DropdownMenuTrigger className="absolute right-0 top-0 m-1" asChild>
          <Button variant="ghost">...</Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent>
          <DropdownMenuItem
            onClick={() => setEditProfileOpen(!editProfileOpen)}
          >
            {t('updateData')}
          </DropdownMenuItem>
          <DropdownMenuItem
            onClick={() =>
              setChangePasswordDialogOpen(!changePasswordDialogOpen)
            }
          >
            {t('changePassword')}
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
      <EditProfile
        open={editProfileOpen}
        onOpenChange={() => setEditProfileOpen(!editProfileOpen)}
        user={user}
        variant="personalData"
        tag_value={tag_value}
      />
      <ChangePasswordDialog
        open={changePasswordDialogOpen}
        onOpenChange={() =>
          setChangePasswordDialogOpen(!changePasswordDialogOpen)
        }
      />
    </Card>
  );
};

export default PersonalData;
