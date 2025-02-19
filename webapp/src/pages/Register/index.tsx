import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { zodResolver } from '@hookform/resolvers/zod';
import i18next, { TFunction } from 'i18next';
import { FC } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { z } from 'zod';
import { CalendarIcon } from 'lucide-react';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectValue,
  SelectTrigger,
} from '@/components/ui/select';
import { isValidPhoneNumber } from 'react-phone-number-input';
import { PhoneInput } from '@/components/ui/phone-input';
import { RegistrationData } from '@/types/registrationData';
import LoadingButton from '@/components/LoadingButton';
import { pl, enUS } from 'date-fns/locale';
import { useRegister } from '@/data/useAuthenticate';

const getRegistrationSchema = (t: TFunction<'registerPage'>) =>
  z
    .object({
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
      email: z.string().email({ message: t('emailInvalid') }),
      firstName: z
        .string()
        .min(1, { message: t('firstNameRequired') })
        .max(50, { message: t('firstNameToLong') }),
      lastName: z
        .string()
        .min(1, { message: t('lastNameRequired') })
        .max(50, { message: t('lastNameToLong') }),
      phoneNumber: z
        .string()
        .refine(isValidPhoneNumber, { message: t('phoneNumberInvalid') }),
      birthDate: z
        .date({
          required_error: t('birthDateRequired'),
        })
        .max(new Date(), { message: t('birthDateInvalid') }),
      gender: z
        .string({
          required_error: t('genderRequired'),
        })
        .min(0, { message: t('genderInvalid') })
        .max(2, { message: t('genderInvalid') }),
    })
    .refine((data) => data.password === data.confirmPassword, {
      message: t('passwordsNotMatch'),
      path: ['confirmPassword'],
    });

type RegistrationSchema = z.infer<ReturnType<typeof getRegistrationSchema>>;

const RegisterPage: FC = () => {
  const { t } = useTranslation('registerPage');
  const { register, isPending } = useRegister();
  const form = useForm<RegistrationSchema>({
    resolver: zodResolver(getRegistrationSchema(t)),
    defaultValues: {
      username: '',
      password: '',
      confirmPassword: '',
      email: '',
      firstName: '',
      lastName: '',
      phoneNumber: '',
    },
  });

  const onSubmit = form.handleSubmit(async (data) => {
    const registrationData: RegistrationData = {
      username: data.username,
      password: data.password,
      email: data.email,
      firstName: data.firstName,
      lastName: data.lastName,
      phoneNumber: data.phoneNumber,
      birthDate: data.birthDate,
      gender: parseInt(data.gender),
      language: i18next.language,
    };
    await register(registrationData);
  });

  return (
    <div className="flex h-full w-full items-center justify-center">
      <Form {...form}>
        <form
          onSubmit={onSubmit}
          className="border-1 flex h-full flex-wrap rounded-md bg-background p-2 shadow-2xl shadow-slate-900 md:h-4/6 md:w-4/5 md:p-9"
        >
          <FormField
            control={form.control}
            name="username"
            render={({ field }) => (
              <FormItem className="w-full">
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
              <FormItem className="w-full">
                <FormControl>
                  <Input
                    {...field}
                    type="password"
                    placeholder={t('password')}
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
              <FormItem className="w-full">
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
          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem className="w-full">
                <FormControl>
                  <Input {...field} type="email" placeholder={t('email')} />
                </FormControl>
                <FormMessage className="text-center" />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="firstName"
            render={({ field }) => (
              <FormItem className="w-full">
                <FormControl>
                  <Input {...field} placeholder={t('firstName')} />
                </FormControl>
                <FormMessage className="text-center" />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="lastName"
            render={({ field }) => (
              <FormItem className="w-full">
                <FormControl>
                  <Input {...field} placeholder={t('lastName')} />
                </FormControl>
                <FormMessage className="text-center" />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="phoneNumber"
            render={({ field }) => (
              <FormItem className="w-full">
                <FormControl>
                  <PhoneInput
                    defaultCountry="PL"
                    placeholder={t('phoneNumber')}
                    {...field}
                  />
                </FormControl>
                <FormMessage className="text-center" />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="birthDate"
            render={({ field }) => (
              <FormItem className="w-full">
                <Popover>
                  <PopoverTrigger asChild>
                    <FormControl>
                      <Button variant="outline" className="w-full">
                        {field.value ? (
                          new Date(field.value).toLocaleDateString(
                            navigator.language || 'pl-PL'
                          )
                        ) : (
                          <span>{t('pickDate')}</span>
                        )}
                        <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                      </Button>
                    </FormControl>
                  </PopoverTrigger>
                  <PopoverContent className="w-auto p-0" align="start">
                    <Calendar
                      mode="single"
                      selected={field.value}
                      onSelect={field.onChange}
                      disabled={(date) => {
                        return (
                          date > new Date() || date < new Date('1900-01-01')
                        );
                      }}
                      locale={i18next.language === 'pl' ? pl : enUS}
                      initialFocus
                      captionLayout="dropdown-buttons"
                      fromYear={1960}
                      toYear={new Date().getFullYear() - 18}
                    />
                  </PopoverContent>
                </Popover>
                <FormMessage className="text-center" />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="gender"
            render={({ field }) => (
              <FormItem className="w-full">
                <Select
                  onValueChange={field.onChange}
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder={t('selectGender')} />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectItem value="1">{t('genderMale')}</SelectItem>
                    <SelectItem value="2">{t('genderFemale')}</SelectItem>
                    <SelectItem value="0">{t('genderOther')}</SelectItem>
                  </SelectContent>
                </Select>
                <FormMessage className="text-center" />
              </FormItem>
            )}
          />
          <LoadingButton
            className="w-full"
            type="submit"
            isLoading={isPending}
            text={t('register')}
          />
        </form>
      </Form>
    </div>
  );
};

export default RegisterPage;
