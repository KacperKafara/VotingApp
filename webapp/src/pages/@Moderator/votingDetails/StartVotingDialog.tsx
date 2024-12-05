import LoadingButton from '@/components/LoadingButton';
import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from '@/components/ui/form';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { useActivateVoting } from '@/data/useVoting';
import { zodResolver } from '@hookform/resolvers/zod';
import { enUS, pl } from 'date-fns/locale';
import i18next, { TFunction } from 'i18next';
import { CalendarIcon } from 'lucide-react';
import { FC } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { z } from 'zod';

interface StartVotingDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  votingId: string;
}

const getStartVotingSchema = (t: TFunction<'voting'>) =>
  z.object({
    endDate: z
      .date({
        required_error: t('voting:startVoting.errors.endDateRequired'),
      })
      .min(new Date(), {
        message: t('voting:startVoting.errors.endDateInvalid'),
      }),
  });

type StartVotingSchema = z.infer<ReturnType<typeof getStartVotingSchema>>;

const StartVotingDialog: FC<StartVotingDialogProps> = ({
  open,
  onOpenChange,
  votingId,
}) => {
  const { t } = useTranslation('voting');
  const { activateVoting, isLoading } = useActivateVoting();

  const form = useForm<StartVotingSchema>({
    resolver: zodResolver(getStartVotingSchema(t)),
    defaultValues: {
      endDate: (() => {
        const date = new Date();
        date.setDate(date.getDate() + 5);
        return date;
      })(),
    },
  });

  const onSubmit = form.handleSubmit(async (data) => {
    await activateVoting({ id: votingId, endDate: data.endDate });
    onOpenChange(false);
  });

  return (
    <Dialog onOpenChange={onOpenChange} open={open}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{t('startVoting.dialogTitle')}</DialogTitle>
          <DialogDescription>
            {t('startVoting.dialogDescription')}
          </DialogDescription>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={onSubmit} className="flex flex-col gap-2">
            <FormField
              control={form.control}
              name="endDate"
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
                            <span>{t('startVoting.pickEndDate')}</span>
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
                          return date < new Date();
                        }}
                        locale={i18next.language === 'pl' ? pl : enUS}
                        initialFocus
                      />
                    </PopoverContent>
                  </Popover>
                  <FormMessage className="text-center" />
                </FormItem>
              )}
            />
            <LoadingButton
              type="submit"
              className="w-5/12"
              isLoading={isLoading}
              text={t('startVoting.dialogTitle')}
            />
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};

export default StartVotingDialog;
