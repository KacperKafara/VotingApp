import { FC } from 'react';
import { Sheet, SheetContent, SheetHeader, SheetTitle } from './ui/sheet';
import { User } from '@/types/user';
import { TFunction } from 'i18next';
import { z } from 'zod';
import { isValidPhoneNumber } from 'react-phone-number-input';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useTranslation } from 'react-i18next';
import { Form, FormControl, FormField, FormItem, FormMessage } from './ui/form';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { PhoneInput } from './ui/phone-input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from './ui/select';
import ConfirmDialog from './ConfirmDialog';
import { useUpdatePersonalData } from '@/data/useUpdatePersonalData';
import { useNavigate } from 'react-router-dom';

interface EditProfileProps {
  open: boolean;
  onOpenChange: () => void;
  user: User;
  variant: 'personalData' | 'otherUserData';
}

const getEditProfileInfoSchema = (t: TFunction<'profile'>) =>
  z.object({
    username: z
      .string()
      .min(3, { message: t('usernameToShort') })
      .max(50, { message: t('usernameToLong') }),
    email: z.string().email({ message: t('emailInvalid') }),
    firstName: z.string().min(1, { message: t('firstNameRequired') }),
    lastName: z.string().min(1, { message: t('lastNameRequired') }),
    phoneNumber: z
      .string()
      .refine(isValidPhoneNumber, { message: t('phoneNumberInvalid') }),
    gender: z
      .string({
        required_error: t('genderRequired'),
      })
      .regex(/^(MALE|FEMALE|OTHER)$/, { message: t('genderInvalid') }),
  });

type EditProfileInfoSchema = z.infer<
  ReturnType<typeof getEditProfileInfoSchema>
>;

const EditProfile: FC<EditProfileProps> = ({
  open,
  onOpenChange,
  user,
  variant,
}) => {
  const { t } = useTranslation('profile');
  const navigate = useNavigate();
  const { updatePersonalData, updateOtherUserPersonalData } =
    useUpdatePersonalData();

  const form = useForm<EditProfileInfoSchema>({
    resolver: zodResolver(getEditProfileInfoSchema(t)),
    defaultValues: {
      username: user.username,
      email: user.email,
      firstName: user.firstName,
      lastName: user.lastName,
      phoneNumber: user.phoneNumber,
      gender: user.gender,
    },
  });

  const handleFormSubmit = form.handleSubmit(async (data) => {
    if (variant === 'personalData') {
      await updatePersonalData(data);
    } else if (variant == 'otherUserData') {
      const updatedData = await updateOtherUserPersonalData({
        data,
        userId: user.id,
      });
      navigate(`/admin/users/${updatedData.username}`, { replace: true });
    }
    onOpenChange();
  });

  return (
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>{t('updateData')}</SheetTitle>
        </SheetHeader>
        <Form {...form}>
          <form onSubmit={handleFormSubmit} className="grid gap-4 py-4">
            <FormField
              control={form.control}
              name="username"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <div className="grid grid-cols-4 items-center gap-4">
                      <Label htmlFor="username" className="text-left">
                        {t('username')}
                      </Label>
                      <Input
                        id="username"
                        className="col-span-3"
                        {...field}
                      ></Input>
                    </div>
                  </FormControl>
                  <FormMessage className="text-center" />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="firstName"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <div className="grid grid-cols-4 items-center gap-4">
                      <Label htmlFor="firstName" className="text-left">
                        {t('firstName')}
                      </Label>
                      <Input
                        id="firstName"
                        className="col-span-3"
                        {...field}
                      ></Input>
                    </div>
                  </FormControl>
                  <FormMessage className="text-center" />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="lastName"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <div className="grid grid-cols-4 items-center gap-4">
                      <Label htmlFor="lastName" className="text-left">
                        {t('lastName')}
                      </Label>
                      <Input
                        id="lastName"
                        className="col-span-3"
                        {...field}
                      ></Input>
                    </div>
                  </FormControl>
                  <FormMessage className="text-center" />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="email"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <div className="grid grid-cols-4 items-center gap-4">
                      <Label htmlFor="email" className="text-left">
                        {t('email')}
                      </Label>
                      <Input
                        id="email"
                        className="col-span-3"
                        {...field}
                      ></Input>
                    </div>
                  </FormControl>
                  <FormMessage className="text-center" />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="phoneNumber"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <div className="grid grid-cols-4 items-center gap-4">
                      <Label htmlFor="phoneNumber" className="text-left">
                        {t('phoneNumber')}
                      </Label>
                      <PhoneInput
                        className="col-span-3"
                        id="phoneNumber"
                        {...field}
                      />
                    </div>
                  </FormControl>
                  <FormMessage className="text-center" />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="gender"
              render={({ field }) => (
                <FormItem>
                  <Select
                    onValueChange={field.onChange}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="selectGender" className="text-left">
                          {t('gender')}
                        </Label>
                        <SelectTrigger id="selectGender" className="col-span-3">
                          <SelectValue placeholder={t('selectGender')} />
                        </SelectTrigger>
                      </div>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value="MALE">{t('MALE')}</SelectItem>
                      <SelectItem value="FEMALE">{t('FEMALE')}</SelectItem>
                      <SelectItem value="OTHER">{t('OTHER')}</SelectItem>
                    </SelectContent>
                  </Select>
                  <FormMessage className="text-center" />
                </FormItem>
              )}
            />
            <div className="flex w-full justify-end">
              <ConfirmDialog
                className="w-5/12"
                buttonText={t('save')}
                dialogTitle={t('saveChanges')}
                dialogDescription={t('saveChangesDescription')}
                confirmAction={handleFormSubmit}
              />
            </div>
          </form>
        </Form>
      </SheetContent>
    </Sheet>
  );
};

export default EditProfile;
