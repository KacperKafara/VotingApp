import { zodResolver } from "@hookform/resolvers/zod";
import { TFunction } from "i18next";
import { FC } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { Form } from "react-router-dom";
import { z } from "zod";

const getRegistrationSchema = (t: TFunction<'registerPage'>) =>
  z.object({
    username: z
      .string()
      .min(3, { message: t('usernameToShort') })
      .max(50, { message: t('usernameToLong') }),
    password: z
      .string()
      .min(8, { message: t('passwordToShort') })
      .max(50, { message: t('passwordToLong') }),
    confirmPassword: z
      .string()
      .min(8, { message: t('passwordToShort') })
      .max(50, { message: t('passwordToLong') }),
    email: z
      .string()
      .email({ message: t('emailInvalid') }),
    firstName: z
      .string()
      .max(50, { message: t('firstNameToLong') }),
    lastName: z
      .string()
      .max(50, { message: t('lastNameToLong') }),
    phoneNumber: z
      .string()
      .min(9, { message: t('phoneNumberToShort') })
      .max(9, { message: t('phoneNumberToLong') }),
    birthDate: z
      .coerce.date()
      .max(new Date(), { message: t('birthDateInvalid') }),
    gender: z
      .number()
      .min(0, { message: t('genderInvalid') })
      .max(2, { message: t('genderInvalid') }),
  })
  .refine(data => data.password === data.confirmPassword, {
    message: t('passwordsNotMatch'),
    path: ['confirmPassword'],
  });

type RegistrationSchema = z.infer<ReturnType<typeof getRegistrationSchema>>;

const RegisterPage: FC = () => {
  const { t } = useTranslation('registerPage');
  const form = useForm<RegistrationSchema>({
    resolver: zodResolver(getRegistrationSchema(t)),
    values: {
      username: "",
      password: "",
      confirmPassword: "",
      email: "",
      firstName: "",
      lastName: "",
      phoneNumber: "",
      birthDate: new Date(),
      gender: 0,
    },
  });

  const onSubmit = form.handleSubmit(async (data) => {
    console.log(data);
  });

  return (
    <div className="w-full h-full">
      <Form className="flex h-full w-full items-center justify-center" {...form}>
        <form
          onSubmit={onSubmit}
          className="w-4/5 h-4/6 flex items-center justify-center border-1 shadow-2xl bg-background rounded-md shadow-slate-900"
        >
          <h1>Registration page</h1>
        </form>
      </Form>
    </div>
  )
}

export default RegisterPage;