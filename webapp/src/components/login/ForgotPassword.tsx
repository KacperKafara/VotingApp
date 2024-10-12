import { FC } from 'react';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '../ui/dialog';
import { Button } from '../ui/button';
import { useTranslation } from 'react-i18next';
import i18next, { TFunction } from 'i18next';
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from '../ui/form';
import { Input } from '../ui/input';
import LoadingButton from '../LoadingButton';
import { useResetPassword } from '@/data/useResetPassword';

interface ResetPasswordProps {
  open: boolean;
  onOpenChange: () => void;
}

const getResetPasswordSchema = (t: TFunction<'resetPassword'>) =>
  z.object({
    email: z
      .string()
      .email({ message: t('resetPassword:errors.emailInvalid') }),
  });

type ResetPasswordSchema = z.infer<ReturnType<typeof getResetPasswordSchema>>;

const ResetPassword: FC<ResetPasswordProps> = ({ open, onOpenChange }) => {
  const { t } = useTranslation(['common', 'resetPassword']);
  const { resetPassword, isPending } = useResetPassword(onOpenChange);

  const form = useForm<ResetPasswordSchema>({
    resolver: zodResolver(getResetPasswordSchema(t)),
    defaultValues: {
      email: '',
    },
  });

  const onSubmit = form.handleSubmit(async (data) => {
    await resetPassword({
      email: data.email,
      language: i18next.language,
    });
  });

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{t('resetPassword:title')}</DialogTitle>
          <DialogDescription>
            {t('resetPassword:description')}
          </DialogDescription>
          <Form {...form}>
            <form onSubmit={onSubmit}>
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem>
                    <FormControl>
                      <Input
                        {...field}
                        type="email"
                        autoComplete="email"
                        placeholder="Email"
                      />
                    </FormControl>
                    <FormMessage className="text-center" />
                  </FormItem>
                )}
              />
              <div className="mt-2 flex justify-between">
                <LoadingButton
                  type="submit"
                  text={t('common:submit')}
                  isLoading={isPending}
                />
                <DialogClose asChild>
                  <Button type="button" variant="secondary">
                    {t('common:close')}
                  </Button>
                </DialogClose>
              </div>
            </form>
          </Form>
        </DialogHeader>
      </DialogContent>
    </Dialog>
  );
};

export default ResetPassword;
