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

const getLoginSchema = () =>
  z.object({
    login: z
      .string()
      .min(3, { message: "Login must be at least 3 characters long" })
      .max(50, { message: "Login must be at most 50 characters long" }),
    password: z
      .string()
      .min(8, { message: "Password must be at least 8 characters long" })
      .max(50, { message: "Password must be at most 50 characters long" }),
  })

type LoginSchema = z.infer<ReturnType<typeof getLoginSchema>>;

interface LoginFormProps {
  className?: string;
}

const LoginForm:FC<LoginFormProps> = ({ className }) => {
  const { authenticate, isPending } = useAuthenticate();
  const { t } = useTranslation('loginPage');
  const form = useForm<LoginSchema>({
    resolver: zodResolver(getLoginSchema()),
    values: {
      login: "",
      password: "",
    },
  });

  const onSubmit = form.handleSubmit(async ({ login, password }) => {
    await authenticate({ login, password });
  });

  return (
    <div className={cn(className)}>
      <Form {...form}>
        <form onSubmit={onSubmit} className="flex flex-col gap-2">
          <FormField
            control={form.control}
            name="login"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input {...field} autoComplete="username" placeholder={t('login')}/>
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
            text="Login"
            className="h-fit mt-1"
            isLoading={isPending}
          />
        </form>
      </Form>
    </div>
  );
}

export default LoginForm;