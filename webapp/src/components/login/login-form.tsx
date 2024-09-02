import { FC } from "react";
import { useForm } from "react-hook-form";
import { Form, FormControl, FormField, FormItem, FormMessage } from "../ui/form";
import { Input } from "../ui/input";
import { cn } from "@/lib/utils";
import LoadingButton from "../loading-button";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useAuthenticate } from "@/data/useAuthenticate";
import { useTranslation } from "react-i18next";
import { TFunction } from "i18next";
import { useUserStore } from "@/store/userStore";

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
  })

type LoginSchema = z.infer<ReturnType<typeof getLoginSchema>>;

interface LoginFormProps {
  className?: string;
}

const LoginForm:FC<LoginFormProps> = ({ className }) => {
  const { authenticate, isPending } = useAuthenticate();
  const { t } = useTranslation('loginPage');
  const { setToken } = useUserStore();
  const form = useForm<LoginSchema>({
    resolver: zodResolver(getLoginSchema(t)),
    values: {
      username: "",
      password: "",
    },
  });

  const onSubmit = form.handleSubmit(async ({ username, password }) => {
    const result = await authenticate({ username, password });
    setToken(result.token);
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
                  <Input {...field} autoComplete="username" placeholder={t('username')}/>
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
                  <Input {...field} autoComplete="password" type="password" placeholder={t('password')}/>
                </FormControl>
                <FormMessage className="text-center" />
              </FormItem>
            )}
          />
          <LoadingButton 
            type="submit"
            text={t('loginButton')}
            className="h-fit mt-1"
            isLoading={isPending}
          />
        </form>
      </Form>
    </div>
  );
}

export default LoginForm;