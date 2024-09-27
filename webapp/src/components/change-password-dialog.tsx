import { FC } from 'react';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from './ui/dialog';
import { useTranslation } from 'react-i18next';
import { Form, FormControl, FormField, FormItem, FormMessage } from './ui/form';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { TFunction } from 'i18next';
import { z } from 'zod';
import { Input } from './ui/input';
import LoadingButton from './loading-button';
import { Button } from './ui/button';
import { useChangePassword } from '@/data/useChangePassword';

interface ChangePasswordDialogProps {
  open: boolean;
  onOpenChange: () => void;
}

const getChangePasswordSchema = (t: TFunction<'changePassword'>) =>
  z
    .object({
      oldPassword: z
        .string()
        .min(8, { message: t('errors.oldPasswordToShort') })
        .max(50, { message: t('errors.oldPasswordToLong') }),
      newPassword: z
        .string()
        .min(8, { message: t('errors.newPasswordToShort') })
        .max(50, { message: t('errors.newPasswordToLong') }),
      confirmPassword: z.string(),
    })
    .refine((data) => data.newPassword === data.confirmPassword, {
      message: t('errors.passwordsNotMatch'),
    });

type ChangePasswordSchema = z.infer<ReturnType<typeof getChangePasswordSchema>>;

const ChangePasswordDialog: FC<ChangePasswordDialogProps> = ({
  open,
  onOpenChange,
}) => {
  const { t } = useTranslation('changePassword');
  const { changePassword, isPending } = useChangePassword(onOpenChange);

  const form = useForm<ChangePasswordSchema>({
    resolver: zodResolver(getChangePasswordSchema(t)),
    defaultValues: {
      oldPassword: '',
      newPassword: '',
      confirmPassword: '',
    },
  });

  const handleSubmit = form.handleSubmit(async (data) => {
    await changePassword(data);
  });

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{t('dialogTitle')}</DialogTitle>
          <DialogDescription>{t('dialogDescription')}</DialogDescription>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={handleSubmit} className="flex flex-col gap-3">
            <FormField
              control={form.control}
              name="oldPassword"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <Input
                      {...field}
                      type="password"
                      placeholder={t('oldPassword')}
                    />
                  </FormControl>
                  <FormMessage className="text-center" />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="newPassword"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <Input
                      {...field}
                      type="password"
                      placeholder={t('newPassword')}
                    />
                  </FormControl>
                  <FormMessage className="text-center" />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="confirmPassword"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <Input
                      {...field}
                      type="password"
                      placeholder={t('confirmPassword')}
                    />
                  </FormControl>
                  <FormMessage className="text-center" />
                </FormItem>
              )}
            />
            <div className="mt-2 flex justify-between">
              <LoadingButton
                type="submit"
                text={t('submit')}
                isLoading={isPending}
              />
              <DialogClose asChild>
                <Button type="button" variant="secondary">
                  {t('cancel')}
                </Button>
              </DialogClose>
            </div>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};

export default ChangePasswordDialog;
