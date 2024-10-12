import { FC, useState } from 'react';
import { Button } from './ui/button';
import { cn } from '@/lib/utils';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import ChangePasswordDialog from './ChangePasswordDialog';

interface AccountsLinksProps {
  className?: string;
}

const AccountsLinks: FC<AccountsLinksProps> = ({ className }) => {
  const navigate = useNavigate();
  const [changePasswordDialogOpen, setChangePasswordDialogOpen] =
    useState(false);
  const { t } = useTranslation('navbar');

  return (
    <div className={cn(className, 'flex flex-col items-center gap-2')}>
      <Button onClick={() => navigate('/')} className="w-3/4">
        {t('home')}
      </Button>
      <Button
        onClick={() => setChangePasswordDialogOpen(true)}
        className="w-3/4"
      >
        {t('changePassword')}
      </Button>

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
