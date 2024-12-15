import { useTotpAuthenticate } from '@/data/useAuthenticate';
import { getActiveRole, roleMapping, useUserStore } from '@/store/userStore';
import { zodResolver } from '@hookform/resolvers/zod';
import { TFunction } from 'i18next';
import { FC } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { z } from 'zod';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '../ui/dialog';
import { Form, FormControl, FormField, FormItem } from '../ui/form';
import { InputOTP, InputOTPGroup, InputOTPSlot } from '../ui/input-otp';
import LoadingButton from '../LoadingButton';

const getTotpFormSchema = (t: TFunction<'loginPage'>) =>
  z.object({
    totp: z.string().length(6, { message: t('invalidTotp') }),
  });

type TotpFormSchema = z.infer<ReturnType<typeof getTotpFormSchema>>;

interface TotpInputProps {
  open: boolean;
  onOpenChange: () => void;
  username: string;
}

const TotpInput: FC<TotpInputProps> = ({ open, onOpenChange, username }) => {
  const { t } = useTranslation('loginPage');
  const { authenticate, isPending } = useTotpAuthenticate();
  const { setToken, setRefreshToken } = useUserStore();
  const navigate = useNavigate();

  const form = useForm<TotpFormSchema>({
    resolver: zodResolver(getTotpFormSchema(t)),
    defaultValues: {
      totp: '',
    },
  });

  const onSubmit = form.handleSubmit(async ({ totp }) => {
    const result = await authenticate({ username, totp });

    setToken(result.token);
    setRefreshToken(result.refreshToken);
    const unsubscribe = useUserStore.subscribe((state) => {
      const roles = state.roles;
      if (roles) {
        navigate(`/${roleMapping[getActiveRole(roles)]}`);
        unsubscribe();
      }
    });
  });

  return (
    <Dialog
      open={open}
      onOpenChange={() => {
        onOpenChange();
        form.reset();
      }}
    >
      <DialogContent className="w-fit p-10 py-6">
        <DialogHeader>
          <DialogTitle>{t('totp.title')}</DialogTitle>
          <DialogDescription>{t('totp.description')}</DialogDescription>
        </DialogHeader>
        <Form {...form}>
          <form
            onSubmit={onSubmit}
            className="flex w-full flex-col items-center"
          >
            <FormField
              control={form.control}
              name="totp"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <InputOTP maxLength={6} minLength={6} {...field}>
                      <InputOTPGroup>
                        <InputOTPSlot index={0} />
                        <InputOTPSlot index={1} />
                        <InputOTPSlot index={2} />
                        <InputOTPSlot index={3} />
                        <InputOTPSlot index={4} />
                        <InputOTPSlot index={5} />
                      </InputOTPGroup>
                    </InputOTP>
                  </FormControl>
                </FormItem>
              )}
            />
            <LoadingButton
              className="mt-4 w-full"
              type="submit"
              isLoading={isPending}
              text={t('totp.submit')}
            />
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};

export default TotpInput;
