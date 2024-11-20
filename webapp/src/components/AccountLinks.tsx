import { FC, useState } from 'react';
import { Button } from './ui/button';
import { cn } from '@/lib/utils';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import ChangePasswordDialog from './ChangePasswordDialog';
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from './ui/collapsible';
import { UserRoles } from '@/types/roles';
import { useCreateRoleRequest } from '@/data/useRoleRequest';
import ConfirmDialog from './ConfirmDialog';
import { useUserStore } from '@/store/userStore';

interface AccountsLinksProps {
  className?: string;
}

const AccountsLinks: FC<AccountsLinksProps> = ({ className }) => {
  const navigate = useNavigate();
  const [changePasswordDialogOpen, setChangePasswordDialogOpen] =
    useState(false);
  const { t } = useTranslation(['navbar', 'roleRequest']);
  const { roles } = useUserStore();
  const { createRoleRequest, isPending } = useCreateRoleRequest();

  return (
    <div className={cn(className, 'flex flex-col items-center gap-2')}>
      <Button onClick={() => navigate('/')} className="w-3/4">
        {t('home')}
      </Button>
      <Button onClick={() => navigate('/profile')} className="w-3/4">
        {t('profile')}
      </Button>
      <Button
        onClick={() => setChangePasswordDialogOpen(true)}
        className="w-3/4"
      >
        {t('changePassword')}
      </Button>
      {roles?.includes(`ROLE_${UserRoles.admin}`) && (
        <Collapsible className="w-3/4">
          <CollapsibleTrigger asChild>
            <Button className="w-full">{t(`ROLE_${UserRoles.admin}`)}</Button>
          </CollapsibleTrigger>
          <CollapsibleContent className="flex flex-col">
            <Button
              variant="link"
              className="text-wrap"
              onClick={() => {
                navigate('/admin/users');
              }}
            >
              {t('users')}
            </Button>
            <Button
              variant="link"
              className="text-wrap"
              onClick={() => {
                navigate('/admin/role-requests');
              }}
            >
              {t('roleRequests')}
            </Button>
          </CollapsibleContent>
        </Collapsible>
      )}
      {roles?.includes(`ROLE_${UserRoles.moderator}`) && (
        <Collapsible className="w-3/4">
          <CollapsibleTrigger asChild>
            <Button className="w-full">
              {t(`ROLE_${UserRoles.moderator}`)}
            </Button>
          </CollapsibleTrigger>
          <CollapsibleContent className="flex flex-col">
            <Button
              variant="link"
              className="text-wrap"
              onClick={() => {
                navigate('/moderator/surveys');
              }}
            >
              {t('surveys')}
            </Button>
            <Button
              variant="link"
              className="text-wrap"
              onClick={() => {
                navigate('/moderator/votings');
              }}
            >
              {t('votingList')}
            </Button>
          </CollapsibleContent>
        </Collapsible>
      )}
      {roles?.includes(`ROLE_${UserRoles.user}`) && (
        <Collapsible className="w-3/4">
          <CollapsibleTrigger asChild>
            <Button className="w-full">{t(`ROLE_${UserRoles.user}`)}</Button>
          </CollapsibleTrigger>
          <CollapsibleContent className="flex flex-col">
            <Button
              variant="link"
              className="text-wrap"
              onClick={() => {
                navigate('/user/surveys');
              }}
            >
              {t('surveys')}
            </Button>
            <Button
              variant="link"
              className="text-wrap"
              onClick={() => {
                navigate('/user/votings');
              }}
            >
              {t('votingList')}
            </Button>
            <Button
              variant="link"
              className="text-wrap"
              onClick={() => {
                navigate('/user/active-surveys-and-votings');
              }}
            >
              {t('activeSurveysAndVotings')}
            </Button>
          </CollapsibleContent>
        </Collapsible>
      )}
      {roles?.includes(`ROLE_${UserRoles.user}`) &&
        !roles?.includes('ROLE_VOTER') && (
          <ConfirmDialog
            className="w-3/4 text-wrap"
            isLoading={isPending}
            buttonText={t('roleRequest:createRoleRequest')}
            dialogTitle={t('roleRequest:send')}
            dialogDescription={t('roleRequest:sendDescription')}
            confirmAction={createRoleRequest}
          />
        )}

      <ChangePasswordDialog
        open={changePasswordDialogOpen}
        onOpenChange={() =>
          setChangePasswordDialogOpen(!changePasswordDialogOpen)
        }
      />
    </div>
  );
};

export default AccountsLinks;
