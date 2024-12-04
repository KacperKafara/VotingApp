/* eslint-disable @typescript-eslint/no-explicit-any */
import { CreateVoteRequest, SurveyKind } from '@/types/survey';
import { FC, useEffect, useState } from 'react';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
} from './ui/sheet';
import { useTranslation } from 'react-i18next';
import { InputOTP, InputOTPGroup, InputOTPSlot } from './ui/input-otp';
import { REGEXP_ONLY_DIGITS } from 'input-otp';
import { RadioGroup, RadioGroupItem } from './ui/radio-group';
import { useParliamentaryClubs } from '@/data/useParliamentaryClubs';
import { z } from 'zod';
import { TFunction } from 'i18next';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from './ui/form';
import LoadingButton from './LoadingButton';
import { VotingKind, VotingOption } from '@/types/voting';
import { UseMutateAsyncFunction } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { useVotingOptions } from '@/data/useVoting';

interface VoteSheetProps {
  id: string;
  description: string;
  kind: SurveyKind | VotingKind;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  createVote: UseMutateAsyncFunction<
    any,
    AxiosError<unknown, any>,
    CreateVoteRequest,
    unknown
  >;
  isLoading: boolean;
}

const getFormSchema = (t: TFunction<'survey'>) =>
  z.object({
    voteResult: z.string(),
    totp: z.string().min(6, {
      message: t('sheet.totpInvalid'),
    }),
  });

type FormSchema = z.infer<ReturnType<typeof getFormSchema>>;

const VoteSheet: FC<VoteSheetProps> = ({
  id,
  kind,
  description,
  open,
  onOpenChange,
  createVote,
  isLoading,
}) => {
  const { t } = useTranslation('survey');
  const [posibleVotes, setPosibleVotes] = useState<string[]>([]);
  const [posibleVotesOnList, setPosibleVotesOnList] = useState<VotingOption[]>(
    []
  );
  const { data, isError } = useParliamentaryClubs(
    kind === 'PARLIAMENTARY_CLUB'
  );

  const { data: dataOnList, isError: isErrorOnList } = useVotingOptions(
    id,
    kind === 'ON_LIST'
  );
  const form = useForm<FormSchema>({
    resolver: zodResolver(getFormSchema(t)),
  });
  useEffect(() => {
    if (kind !== 'PARLIAMENTARY_CLUB' && kind !== 'ON_LIST') {
      setPosibleVotes([
        'DEFINITELY_YES',
        'YES',
        'I_DONT_KNOW',
        'NO',
        'DEFINITELY_NO',
      ]);
    } else if (data && !isError) {
      setPosibleVotes(data.data);
    } else if (dataOnList && !isErrorOnList) {
      setPosibleVotesOnList(dataOnList);
    } else {
      onOpenChange(false);
    }
  }, [data, dataOnList, isError, isErrorOnList, kind, onOpenChange]);

  const onSubmit = form.handleSubmit(async (data) => {
    await createVote({ surveyId: id, ...data });
    onOpenChange(false);
  });

  return (
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>{t('sheet.vote')}</SheetTitle>
          <SheetDescription>{description}</SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form className="mt-4 flex flex-col gap-4" onSubmit={onSubmit}>
            <FormField
              control={form.control}
              name="voteResult"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>{t('sheet.voteOptions')}</FormLabel>
                  <FormControl>
                    <RadioGroup onValueChange={field.onChange}>
                      {kind !== 'ON_LIST'
                        ? posibleVotes.map((vote, index) => (
                            <FormItem
                              key={index}
                              className="flex items-center space-x-2"
                            >
                              <FormControl>
                                <RadioGroupItem value={vote} />
                              </FormControl>
                              <FormLabel>
                                {kind === 'PARLIAMENTARY_CLUB'
                                  ? vote
                                  : t(`userResults.${vote}`)}
                              </FormLabel>
                            </FormItem>
                          ))
                        : posibleVotesOnList.map((vote, index) => (
                            <FormItem
                              key={index}
                              className="flex items-center space-x-2"
                            >
                              <FormControl>
                                <RadioGroupItem value={vote.id} />
                              </FormControl>
                              <FormLabel>{vote.option}</FormLabel>
                            </FormItem>
                          ))}
                    </RadioGroup>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="totp"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>{t('sheet.totp')}</FormLabel>
                  <FormControl>
                    <InputOTP
                      maxLength={6}
                      pattern={REGEXP_ONLY_DIGITS}
                      {...field}
                    >
                      <InputOTPGroup>
                        <InputOTPSlot index={0} />
                        <InputOTPSlot index={1} />
                        <InputOTPSlot index={2} />
                        <InputOTPSlot index={3} />
                        <InputOTPSlot index={4} />
                        <InputOTPSlot index={5} />
                      </InputOTPGroup>
                    </InputOTP>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <LoadingButton
              isLoading={isLoading}
              type="submit"
              text={t('sheet.castVote')}
            />
          </form>
        </Form>
      </SheetContent>
    </Sheet>
  );
};

export default VoteSheet;
