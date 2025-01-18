import { FC, useEffect, useState } from 'react';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from './ui/dialog';
import { useTranslation } from 'react-i18next';
import {
  ParliamentaryClubsResponse,
  useParliamentaryClubs,
  useUpdateParliamentaryClub,
} from '@/data/useParliamentaryClubs';
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Form, FormControl, FormField, FormItem, FormLabel } from './ui/form';
import { RadioGroup, RadioGroupItem } from './ui/radio-group';
import LoadingButton from './LoadingButton';

interface UpdateParliamentaryClubDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  etag: string;
}

const getFormSchema = () =>
  z.object({
    club: z.string().uuid(),
  });

type FormSchema = z.infer<ReturnType<typeof getFormSchema>>;

const UpdateParliamentaryClubDialog: FC<UpdateParliamentaryClubDialogProps> = ({
  open,
  onOpenChange,
  etag,
}) => {
  const { t } = useTranslation('profile');
  const { data } = useParliamentaryClubs(true);
  const [posibleClubs, setPosibleClubs] = useState<
    ParliamentaryClubsResponse[]
  >([]);
  const { updateParliamentaryClub, isPending } = useUpdateParliamentaryClub();

  useEffect(() => {
    const clubs = data?.data || [];
    clubs.push({
      id: '00000000-0000-0000-0000-000000000000',
      shortName: t('parliamentaryClub.noClub'),
    });
    setPosibleClubs(clubs);
  }, [data, t]);

  const form = useForm<FormSchema>({
    resolver: zodResolver(getFormSchema()),
  });

  const closeDialog = () => {
    if (localStorage.getItem('etag-parliamentary-club')) {
      localStorage.removeItem('etag-parliamentary-club');
    }
    onOpenChange(!open);
  };

  const onSubmit = form.handleSubmit(async (data) => {
    await updateParliamentaryClub({
      id: data.club,
      if_match: etag,
    });
    closeDialog();
  });

  return (
    <Dialog open={open} onOpenChange={closeDialog}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{t('parliamentaryClub.dialogTitle')}</DialogTitle>
          <DialogDescription>
            {t('parliamentaryClub.dialogDescription')}
          </DialogDescription>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={onSubmit} className="flex flex-col gap-4">
            <FormField
              control={form.control}
              name="club"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>{t('parliamentaryClub.options')}</FormLabel>
                  <FormControl>
                    <RadioGroup
                      onValueChange={field.onChange}
                      className="flex flex-wrap"
                    >
                      {posibleClubs.map((club) => (
                        <FormItem
                          key={club.id}
                          className="flex min-w-[30%] items-center space-x-2"
                        >
                          <FormControl>
                            <RadioGroupItem value={club.id} />
                          </FormControl>
                          <FormLabel>{club.shortName}</FormLabel>
                        </FormItem>
                      ))}
                    </RadioGroup>
                  </FormControl>
                </FormItem>
              )}
            />
            <LoadingButton
              isLoading={isPending}
              type="submit"
              text={t('parliamentaryClub.submit')}
            />
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};

export default UpdateParliamentaryClubDialog;
