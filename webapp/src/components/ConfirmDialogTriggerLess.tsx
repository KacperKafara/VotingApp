import { FC } from 'react';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';
import { Button } from './ui/button';
import { useTranslation } from 'react-i18next';

interface Props {
  dialogTitle?: string;
  dialogDescription?: string;
  confirmAction?: () => void;
  cancelAction?: () => void;
  open: boolean;
  onOpenChange: (value: boolean) => void;
}

const ConfirmDialogTriggerLess: FC<Props> = ({
  dialogTitle,
  dialogDescription,
  confirmAction,
  cancelAction,
  open,
  onOpenChange,
}) => {
  const { t } = useTranslation('common');
  return (
    <AlertDialog open={open} onOpenChange={onOpenChange}>
      <AlertDialogContent>
        <AlertDialogHeader>
          {dialogTitle && <AlertDialogTitle>{dialogTitle}</AlertDialogTitle>}
          {dialogDescription && (
            <AlertDialogDescription>{dialogDescription}</AlertDialogDescription>
          )}
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel onClick={() => cancelAction?.()}>
            {t('cancel')}
          </AlertDialogCancel>
          <AlertDialogAction asChild>
            <Button onClick={() => confirmAction?.()}>{t('confirm')}</Button>
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};
export default ConfirmDialogTriggerLess;
