import LoadingButton from "@/components/loading-button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { useChangePassword, useVerifyToken } from "@/data/useResetPassword";
import { zodResolver } from "@hookform/resolvers/zod";
import { TFunction } from "i18next";
import { FC, useEffect } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";
import { z } from "zod";

const getResetPasswordSchema = (t: TFunction<["resetPassword", "common"]>) =>
  z
    .object({
      password: z
        .string()
        .min(8, { message: t("resetPassword:errors.passwordToShort") })
        .max(50, { message: t("resetPassword:errors.passwordToLong") }),
      confirmPassword: z
        .string()
        .min(8, { message: t("resetPassword:errors.passwordToShort") })
        .max(50, { message: t("resetPassword:errors.passwordToLong") }),
    })
    .refine((data) => data.password === data.confirmPassword, {
      message: t("resetPassword:errors.passwordsNotMatch"),
    });

type ResetPasswordSchema = z.infer<ReturnType<typeof getResetPasswordSchema>>;

const ResetPasswordPage: FC = () => {
  const { token } = useParams<{ token: string }>();
  const { t } = useTranslation(["common", "resetPassword"]);
  const { verifyToken } = useVerifyToken();
  const { changePassword, isPending } = useChangePassword();

  const form = useForm<ResetPasswordSchema>({
    resolver: zodResolver(getResetPasswordSchema(t)),
    defaultValues: {
      password: "",
      confirmPassword: "",
    },
  });

  useEffect(() => {
    verifyToken(token!);
  }, [token, verifyToken]);

  const onSubmit = form.handleSubmit(async ({ password }) => {
    console.log(password);
    if (!token) return;

    await changePassword({ password, token });
  });

  return (
    <div className="flex h-full w-full items-center justify-center">
      <Card className="h-2/5 w-2/5 shadow-lg shadow-slate-900">
        <CardHeader>
          <CardTitle>{t("resetPassword:resetPassword")}</CardTitle>
          <CardDescription>
            {t("resetPassword:resetPasswordDescription")}
          </CardDescription>
        </CardHeader>
        <CardContent className="px-16">
          <Form {...form}>
            <form
              onSubmit={onSubmit}
              className="flex flex-col gap-4 self-center"
            >
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <FormControl>
                      <Input
                        {...field}
                        type="password"
                        placeholder={t("resetPassword:password")}
                      />
                    </FormControl>
                    <FormMessage />
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
                        placeholder={t("resetPassword:confirmPassword")}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <LoadingButton
                type="submit"
                className="min-w-fit"
                isLoading={isPending}
                text={t("common:submit")}
              />
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
};

export default ResetPasswordPage;
