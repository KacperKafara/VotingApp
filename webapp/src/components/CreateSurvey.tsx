import ConfirmDialog from '@/components/ConfirmDialog';
import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group';
import { Textarea } from '@/components/ui/textarea';
import { zodResolver } from '@hookform/resolvers/zod';
import { TFunction } from 'i18next';
import { CalendarIcon } from 'lucide-react';
import { FC } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { z } from 'zod';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
} from './ui/sheet';
import { useCreateSurvey } from '@/data/useSurvey';
import { Card, CardContent } from './ui/card';

interface CreateSurveyProps {
  open: boolean;
  onOpenChange: () => void;
}

const getCreateSurveySchema = (t: TFunction<'survey'>) =>
  z.object({
    title: z
      .string()
      .min(3, { message: t('form.titleToShort') })
      .max(20, { message: t('form.titleToLong') }),
    description: z
      .string()
      .min(3, { message: t('form.descriptionToShort') })
      .max(500, { message: t('form.descriptionToLong') }),
    endDate: z.date().min(new Date(), { message: t('form.endDateInPast') }),
    surveyKind: z.enum(['PARLIAMENTARY_CLUB', 'OTHER']),
  });

type CreateSurveySchema = z.infer<ReturnType<typeof getCreateSurveySchema>>;

const CreateSurvey: FC<CreateSurveyProps> = ({ open, onOpenChange }) => {
  const { t } = useTranslation('survey');
  const { createSurvey, isLoading } = useCreateSurvey();

  const form = useForm<CreateSurveySchema>({
    resolver: zodResolver(getCreateSurveySchema(t)),
    defaultValues: {
      title: '',
      description: '',
      endDate: new Date(new Date().setDate(new Date().getDate() + 7)),
      surveyKind: 'PARLIAMENTARY_CLUB',
    },
  });

  const onSubmit = form.handleSubmit(async (data) => {
    await createSurvey(data);
  });

  return (
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent>
        <SheetHeader className="pb-3">
          <SheetTitle>{t('form.createSurvey')}</SheetTitle>
          <SheetDescription />
        </SheetHeader>
        <Form {...form}>
          <form
            onSubmit={onSubmit}
            className="flex h-full w-full flex-col gap-5"
          >
            <FormField
              control={form.control}
              name="title"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <Input {...field} placeholder={t('form.title')} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem className="mb-3 h-1/3">
                  <FormControl>
                    <Textarea
                      maxLength={500}
                      {...field}
                      className="h-full resize-none"
                      placeholder={t('form.description')}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="endDate"
              render={({ field }) => (
                <FormItem>
                  <Popover>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          className="w-full pl-3 text-left font-normal"
                        >
                          {field.value ? (
                            new Date(field.value).toLocaleDateString(
                              navigator.language || 'pl-PL'
                            )
                          ) : (
                            <span>{t('form.pickDate')}</span>
                          )}
                          <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent>
                      <Calendar
                        mode="single"
                        selected={field.value}
                        onSelect={field.onChange}
                        disabled={(date) => {
                          return date <= new Date();
                        }}
                        initialFocus
                      />
                    </PopoverContent>
                  </Popover>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="surveyKind"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <Card>
                      <CardContent className="m-0 py-3 pl-3">
                        <RadioGroup
                          onValueChange={field.onChange}
                          defaultValue={field.value}
                        >
                          <FormItem className="flex items-center space-x-3 space-y-0">
                            <FormControl>
                              <RadioGroupItem value="PARLIAMENTARY_CLUB" />
                            </FormControl>
                            <FormLabel className="font-normal">
                              {t('form.parliamentaryClub')}
                            </FormLabel>
                          </FormItem>
                          <FormItem className="flex items-center space-x-3 space-y-0">
                            <FormControl>
                              <RadioGroupItem value="OTHER" />
                            </FormControl>
                            <FormLabel className="font-normal">
                              {t('form.other')}
                            </FormLabel>
                          </FormItem>
                        </RadioGroup>
                      </CardContent>
                    </Card>
                  </FormControl>
                </FormItem>
              )}
            />
            <ConfirmDialog
              buttonText={t('form.submit')}
              dialogTitle={t('form.createSurvey')}
              isLoading={isLoading}
              dialogDescription={t('form.createSurveyDescription')}
              confirmAction={onSubmit}
            />
          </form>
        </Form>
      </SheetContent>
    </Sheet>
  );
};

export default CreateSurvey;
