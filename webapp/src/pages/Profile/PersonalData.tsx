import ChangePasswordDialog from '@/components/ChangePasswordDialog';
import ConfirmDialog from '@/components/ConfirmDialog';
import EditProfile from '@/components/EditProfile';
import QRCodeDialog from '@/components/QRCodeDialog';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { useUpdate2FA } from '@/data/useProfile';
import { useCreateRoleRequest } from '@/data/useRoleRequest';
import { cn } from '@/lib/utils';
import { User } from '@/types/user';
import { FC, useEffect, useState } from 'react';
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
  const [displayQRCodeVotingOpen, setDisplayQRCodeVotingOpen] = useState(false);
  const [displayQRCodeAuthorisationOpen, setDisplayQRCodeAuthorisationOpen] =
    useState(false);
  const { createRoleRequest, isPending } = useCreateRoleRequest();
  const { update2FA, isLoading, isSuccess } = useUpdate2FA();

  const activate2FA = async () => {
    await update2FA(true);
  };

  useEffect(() => {
    if (isSuccess) {
      setDisplayQRCodeAuthorisationOpen(true);
    }
  }, [isSuccess]);

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
          {user.active2fa && (
            <>
              <DropdownMenuItem
                onClick={() => {
                  setDisplayQRCodeAuthorisationOpen(
                    !displayQRCodeAuthorisationOpen
                  );
                }}
              >
                {t('displayQRCodeForAuthorisation')}
              </DropdownMenuItem>
              <DropdownMenuItem asChild>
                <ConfirmDialog
                  className="w-full px-2 py-1.5 text-sm outline-none"
                  variant="ghost"
                  isLoading={isLoading}
                  buttonText={t('deactivate2FADialog')}
                  dialogTitle={t('deactivate2FA')}
                  dialogDescription={t('deactivate2FADescription')}
                  confirmAction={() => update2FA(false)}
                />
              </DropdownMenuItem>
            </>
          )}
          {!user.active2fa && (
            <DropdownMenuItem asChild>
              <ConfirmDialog
                // className="w-full px-2 py-1.5 text-sm outline-none"
                variant="ghost"
                isLoading={isLoading}
                buttonText={t('activate2FADialog')}
                dialogTitle={t('activate2FA')}
                dialogDescription={t('activate2FADescription')}
                confirmAction={activate2FA}
              />
            </DropdownMenuItem>
          )}
          {user.roles.includes('VOTER') ? (
            <DropdownMenuItem
              onClick={() => {
                setDisplayQRCodeVotingOpen(!displayQRCodeVotingOpen);
              }}
            >
              {t('displayQRCodeForVoting')}
            </DropdownMenuItem>
          ) : user.roles.includes('USER') &&
            !user.roles.includes('VOTER') &&
            !user.activeRoleRequest ? (
            <DropdownMenuItem asChild>
              <ConfirmDialog
                className="w-full px-2 py-1.5 text-sm outline-none"
                variant="ghost"
                isLoading={isPending}
                buttonText={t('createRoleRequest')}
                dialogTitle={t('send')}
                dialogDescription={t('sendDescription')}
                confirmAction={createRoleRequest}
              />
            </DropdownMenuItem>
          ) : (
            <></>
          )}
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
      <QRCodeDialog
        open={displayQRCodeVotingOpen}
        onOpenChange={() =>
          setDisplayQRCodeVotingOpen(!displayQRCodeVotingOpen)
        }
        kind="voting"
        tFunction={t}
      />
      <QRCodeDialog
        open={displayQRCodeAuthorisationOpen}
        onOpenChange={() =>
          setDisplayQRCodeAuthorisationOpen(!displayQRCodeAuthorisationOpen)
        }
        kind="login"
        tFunction={t}
      />
    </Card>
  );
};

export default PersonalData;
