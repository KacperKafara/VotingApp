import { FC, useState } from 'react';
import { useForm } from 'react-hook-form';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from '../ui/form';
import { Input } from '../ui/input';
import { cn } from '@/lib/utils';
import LoadingButton from '../LoadingButton';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useAuthenticate } from '@/data/useAuthenticate';
import { useTranslation } from 'react-i18next';
import i18next, { TFunction } from 'i18next';
import { getActiveRole, roleMapping, useUserStore } from '@/store/userStore';
import { NavLink, useNavigate } from 'react-router-dom';
import { Button } from '../ui/button';
import ResetPassword from './ForgotPassword';
import TotpInput from './TotpInput';
import GoogleLoginButton from './GoogleLoginButton';

const getLoginSchema = (t: TFunction<'loginPage'>) =>
  z.object({
    username: z
      .string()
      .min(3, { message: t('loginToShort') })
      .max(50, { message: t('loginToLong') }),
    password: z
      .string()
      .min(8, { message: t('passwordToShort') })
      .max(50, { message: t('passwordToLong') }),
  });

type LoginSchema = z.infer<ReturnType<typeof getLoginSchema>>;

interface LoginFormProps {
  className?: string;
}

export interface TwoFactorProps {
  username: string;
  open: boolean;
}

const LoginForm: FC<LoginFormProps> = ({ className }) => {
  const { authenticate, isPending } = useAuthenticate();
  const { t } = useTranslation('loginPage');
  const { setToken, setRefreshToken } = useUserStore();
  const navigate = useNavigate();
  const [resetPasswordOpen, setResetPasswordOpen] = useState(false);
  const [twoFactorOpen, setTwoFactorOpen] = useState<TwoFactorProps>({
    username: '',
    open: false,
  });

  const form = useForm<LoginSchema>({
    resolver: zodResolver(getLoginSchema(t)),
    defaultValues: {
      username: '',
      password: '',
    },
  });

  const onSubmit = form.handleSubmit(async ({ username, password }) => {
    const { data: result, headers } = await authenticate({
      username,
      password,
      language: i18next.language,
    });

    if (
      'code' in result &&
      'message' in result &&
      result.code === 'MFA_REQUIRED'
    ) {
      setTwoFactorOpen({ username, open: true });
      return;
    }

    setToken('token' in result ? result.token : '', undefined);
    setRefreshToken('refreshToken' in result ? result.refreshToken : '');
    if (headers.etag) {
      localStorage.setItem(
        'etag-parliamentary-club',
        headers.etag.substring(1, headers.etag.length - 1)
      );
    }
    const unsubscribe = useUserStore.subscribe((state) => {
      const roles = state.roles;
      if (roles) {
        setTimeout(() => {
          navigate(`/${roleMapping[getActiveRole(roles)]}`);
          unsubscribe();
        }, 500);
      }
    });
  });

  return (
    <div className={cn(className)}>
      <Form {...form}>
        <form onSubmit={onSubmit} className="flex flex-col gap-2">
          <FormField
            control={form.control}
            name="username"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    {...field}
                    autoComplete="username"
                    placeholder={t('username')}
                  />
                </FormControl>
                <FormMessage className="text-center" />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    {...field}
                    autoComplete="password"
                    type="password"
                    placeholder={t('password')}
                  />
                </FormControl>
                <FormMessage className="text-center" />
              </FormItem>
            )}
          />
          <div className="flex justify-between">
            <Button
              variant="link"
              asChild
              className="self-left h-fit w-fit py-0 pl-1 pr-0 text-xs"
            >
              <NavLink to="/register">{t('register')}</NavLink>
            </Button>
            <Button
              type="button"
              onClick={() => setResetPasswordOpen(true)}
              variant="link"
              className="self-right h-fit w-fit py-0 pl-0 pr-1 text-xs font-medium text-primary underline-offset-4 hover:underline"
            >
              {t('forgotPassword')}
            </Button>
          </div>
          <LoadingButton
            type="submit"
            text={t('loginButton')}
            className="mt-1 h-fit"
            isLoading={isPending}
          />
          <GoogleLoginButton />
        </form>
      </Form>
      <ResetPassword
        open={resetPasswordOpen}
        onOpenChange={() => setResetPasswordOpen(!resetPasswordOpen)}
      />
      <TotpInput
        open={twoFactorOpen.open}
        onOpenChange={() => setTwoFactorOpen({ username: '', open: false })}
        username={twoFactorOpen.username}
      />
    </div>
  );
};

export default LoginForm;
