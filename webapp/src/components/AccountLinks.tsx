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
import { useUserStore } from '@/store/userStore';
import { UserRoles } from '@/types/roles';

interface AccountsLinksProps {
  className?: string;
}

const AccountsLinks: FC<AccountsLinksProps> = ({ className }) => {
  const navigate = useNavigate();
  const [changePasswordDialogOpen, setChangePasswordDialogOpen] =
    useState(false);
  const { t } = useTranslation('navbar');
  const { roles } = useUserStore();

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
              onClick={() => {
                navigate('/admin/users');
              }}
            >
              {t('users')}
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
              onClick={() => {
                navigate('/moderator/surveys');
              }}
            >
              {t('surveys')}
            </Button>
            <Button variant="link">sdfsd</Button>
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
              onClick={() => {
                navigate('/user/surveys');
              }}
            >
              {t('surveys')}
            </Button>
            <Button variant="link">sdfsd</Button>
          </CollapsibleContent>
        </Collapsible>
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
