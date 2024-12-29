import DataField from '@/components/DataField';
import LoadingIcon from '@/components/loading';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { useVotingDetails } from '@/data/useVoting';
import { FC, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router-dom';
import StartVotingDialog from './StartVotingDialog';

const VotingDetailsPage: FC = () => {
  const { id } = useParams<{ id: string }>();
  const [startVotingDialogOpen, setStartVotingDialogOpen] = useState(false);
  const { data: voting, isLoading, isError } = useVotingDetails(id!);
  const { t } = useTranslation('voting');

  if (
    isLoading ||
    isError ||
    voting === undefined ||
    voting.data === undefined
  ) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoadingIcon />
      </div>
    );
  }

  const etag = voting.headers.etag as string;

  const data = voting.data;

  return (
    <div className="flex min-h-full w-full flex-col items-center justify-center p-3">
      <div className="w-full md:w-3/4">
        <div className="flex flex-col gap-3">
          <Card>
            <CardHeader>
              <CardTitle>{data.title}</CardTitle>
            </CardHeader>
          </Card>
          <div className="grid grid-cols-1 gap-2 md:grid-cols-2">
            <Card>
              <CardHeader>
                <CardTitle>{t('votingDetails.sittingDetails.title')}</CardTitle>
              </CardHeader>
              <CardContent className="flex flex-col gap-3">
                <DataField
                  label={t('votingDetails.sittingDetails.sittingTitle')}
                  value={data.sittingTitle}
                />
                <DataField
                  label={t('votingDetails.sittingDetails.sittingDay')}
                  value={data.sittingDay}
                />
              </CardContent>
            </Card>
            <Card className="relative">
              <CardHeader>
                <CardTitle>{t('votingDetails.votingDetails.title')}</CardTitle>
              </CardHeader>
              <CardContent className="flex flex-col gap-3">
                <DataField
                  label={t('votingDetails.votingDetails.description')}
                  value={data.description}
                />
                <DataField
                  label={t('votingDetails.votingDetails.topic')}
                  value={data.topic}
                />
                <DataField
                  label={t('votingDetails.votingDetails.date')}
                  value={new Date(data.date).toLocaleDateString(
                    navigator.language || 'pl-PL'
                  )}
                />
                <DataField
                  label={t('votingDetails.votingDetails.endDate')}
                  value={
                    data.endDate
                      ? new Date(data.endDate).toLocaleDateString(
                          navigator.language || 'pl-PL'
                        )
                      : t('votingDetails.votingDetails.noEndDate')
                  }
                />
                {!data.endDate && (
                  <Button
                    className="absolute right-1 top-1"
                    variant="secondary"
                    onClick={() => setStartVotingDialogOpen(true)}
                  >
                    {t('votingDetails.votingDetails.startVoting')}
                  </Button>
                )}
              </CardContent>
            </Card>
            {data.votingOptions.length > 0 && (
              <Card
                className={
                  data.prints.length === 0 ? 'col-span-1 md:col-span-2' : ''
                }
              >
                <CardHeader>
                  <CardTitle>
                    {t('votingDetails.votingOptions.title')}
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <ul className="pl-2">
                    {data.votingOptions.map((option, index) => (
                      <li key={index}>{option.option}</li>
                    ))}
                  </ul>
                </CardContent>
              </Card>
            )}
            {data.prints.length > 0 && (
              <Card
                className={
                  data.votingOptions.length === 0
                    ? 'col-span-1 md:col-span-2'
                    : ''
                }
              >
                <CardHeader>
                  <CardTitle>{t('prints')}</CardTitle>
                  <CardContent className="flex flex-col pt-3">
                    {data.prints.map((print, index) => (
                      <a
                        key={index}
                        href={print.url}
                        target="_blank"
                        rel="noreferrer"
                        className="text-blue-500"
                      >
                        {print.title}
                      </a>
                    ))}
                  </CardContent>
                </CardHeader>
              </Card>
            )}
            <StartVotingDialog
              open={startVotingDialogOpen}
              onOpenChange={setStartVotingDialogOpen}
              votingId={data.id}
              tag_value={etag.substring(1, etag.length - 1)}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default VotingDetailsPage;
