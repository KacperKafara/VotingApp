import ChangePasswordDialog from '@/components/ChangePasswordDialog';
import ConfirmDialogTriggerLess from '@/components/ConfirmDialogTriggerLess';
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
import { useUserStore } from '@/store/userStore';
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
  const [activate2FAOpen, setActivate2FAOpen] = useState(false);
  const [deactivate2FAOpen, setDeactivate2FAOpen] = useState(false);
  const [createRoleRequestOpen, setCreateRoleRequestOpen] = useState(false);
  const [editProfileOpen, setEditProfileOpen] = useState(false);
  const [changePasswordDialogOpen, setChangePasswordDialogOpen] =
    useState(false);
  const [displayQRCodeVotingOpen, setDisplayQRCodeVotingOpen] = useState(false);
  const [displayQRCodeAuthorisationOpen, setDisplayQRCodeAuthorisationOpen] =
    useState(false);
  const { createRoleRequest } = useCreateRoleRequest();
  const { update2FA } = useUpdate2FA();
  const { useOAuth } = useUserStore();

  const activate2FA = async () => {
    await update2FA(true);
    setDisplayQRCodeAuthorisationOpen(true);
  };

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
          {useOAuth === false ||
            (useOAuth === undefined && (
              <DropdownMenuItem
                onClick={() =>
                  setChangePasswordDialogOpen(!changePasswordDialogOpen)
                }
              >
                {t('changePassword')}
              </DropdownMenuItem>
            ))}
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
              <DropdownMenuItem onClick={() => setDeactivate2FAOpen(true)}>
                {t('deactivate2FADialog')}
              </DropdownMenuItem>
            </>
          )}
          {!user.active2fa && (
            <DropdownMenuItem onClick={() => setActivate2FAOpen(true)}>
              {t('activate2FADialog')}
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
            <DropdownMenuItem onClick={() => setCreateRoleRequestOpen(true)}>
              {t('createRoleRequest')}
            </DropdownMenuItem>
          ) : (
            <></>
          )}
        </DropdownMenuContent>
      </DropdownMenu>
      <ConfirmDialogTriggerLess
        open={deactivate2FAOpen}
        onOpenChange={() => setDeactivate2FAOpen(!deactivate2FAOpen)}
        dialogTitle={t('deactivate2FA')}
        dialogDescription={t('deactivate2FADescription')}
        confirmAction={() => update2FA(false)}
      />
      <ConfirmDialogTriggerLess
        open={createRoleRequestOpen}
        onOpenChange={() => setCreateRoleRequestOpen(!createRoleRequestOpen)}
        dialogTitle={t('send')}
        dialogDescription={t('sendDescription')}
        confirmAction={createRoleRequest}
      />
      <ConfirmDialogTriggerLess
        open={activate2FAOpen}
        onOpenChange={() => setActivate2FAOpen(!activate2FAOpen)}
        dialogTitle={t('activate2FA')}
        dialogDescription={t('activate2FADescription')}
        confirmAction={activate2FA}
      />
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
