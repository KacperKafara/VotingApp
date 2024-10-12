import ConfirmDialog from '@/components/ConfirmDialog';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Checkbox } from '@/components/ui/checkbox';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
} from '@/components/ui/form';
import { useUserStore } from '@/store/userStore';
import { UserRoles } from '@/types/roles';
import { User } from '@/types/user';
import { zodResolver } from '@hookform/resolvers/zod';
import { useQueryClient } from '@tanstack/react-query';
import { FC } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { z } from 'zod';

interface UserRolesCardProps {
  user: User;
}

const rolesFormSchema = z.object({
  user: z.boolean().optional(),
  administrator: z.boolean().optional(),
  moderator: z.boolean().optional(),
});

const UserRolesCard: FC<UserRolesCardProps> = ({ user }) => {
  const { t } = useTranslation(['user', 'common']);
  const { id } = useUserStore();
  // const { addRole, deleteRole } = useRole();
  const queryClient = useQueryClient();
  const isCurrentUser = id === user.id;

  const form = useForm<z.infer<typeof rolesFormSchema>>({
    resolver: zodResolver(rolesFormSchema),
    values: {
      user: user.roles.includes(UserRoles.user),
      administrator: user.roles.includes(UserRoles.admin),
      moderator: user.roles.includes(UserRoles.moderator),
    },
  });

  const handleFormSubmit = form.handleSubmit(
    async ({ user, administrator, moderator }) => {
      console.log(administrator, moderator, user);
      await queryClient.invalidateQueries({ queryKey: ['user'] });
    }
  );

  const selectRolesCount = Object.values(form.getValues()).filter(
    Boolean
  ).length;
  const isSubmitDisabled = isCurrentUser || selectRolesCount === 0;

  return (
    <Card>
      <CardHeader>
        <CardTitle>{t('userRoles')}</CardTitle>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={handleFormSubmit}>
            <FormField
              control={form.control}
              name="user"
              render={({ field }) => (
                <FormItem className="flex items-start space-x-3 space-y-0 pb-4">
                  <FormControl>
                    <Checkbox
                      checked={field.value}
                      onCheckedChange={field.onChange}
                    />
                  </FormControl>
                  <div className="space-y-1 leading-none">
                    <FormLabel>{t('roles.user')}</FormLabel>
                  </div>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="moderator"
              render={({ field }) => (
                <FormItem className="flex items-start space-x-3 space-y-0 py-2">
                  <FormControl>
                    <Checkbox
                      checked={field.value}
                      onCheckedChange={field.onChange}
                    />
                  </FormControl>
                  <div className="space-y-1 leading-none">
                    <FormLabel>{t('roles.moderator')}</FormLabel>
                  </div>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="administrator"
              render={({ field }) => (
                <FormItem className="flex items-start space-x-3 space-y-0 py-4">
                  <FormControl>
                    <Checkbox
                      checked={field.value}
                      onCheckedChange={field.onChange}
                    />
                  </FormControl>
                  <div className="space-y-1 leading-none">
                    <FormLabel>{t('roles.administrator')}</FormLabel>
                  </div>
                </FormItem>
              )}
            />
            <ConfirmDialog
              disabled={isSubmitDisabled}
              buttonText={t('common:submit')}
              dialogTitle={t('roles.updateUserRoles')}
              dialogDescription={t('roles.updateUserRolesDescription')}
              confirmAction={handleFormSubmit}
            />
          </form>
        </Form>
      </CardContent>
    </Card>
  );
};

export default UserRolesCard;
