import LoadingIcon from '@/components/loading';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { ScrollArea } from '@/components/ui/scroll-area';
import VotingResultChart from '@/components/voting/VotingResultChart';
import VotingResultChartByClub from '@/components/voting/VotingResultChartByClub';
import { useVoting } from '@/data/useVoting';
import { FC, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router-dom';

interface VoteResult {
  name: string;
  surname: string;
  club: string;
  result: string;
}

const VotingPage: FC = () => {
  const { id } = useParams<{ id: string }>();
  const { data, isLoading, isError } = useVoting(id!);
  const { t } = useTranslation('voting');
  const [envoyName, setEnvoyName] = useState<string>('');

  if (isLoading || isError || data === undefined) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoadingIcon />
      </div>
    );
  }

  const votes: VoteResult[] = [];

  if (data.kind === 'ON_LIST') {
    data.votes.forEach(({ envoy, votingOption, vote }) => {
      votes.push({
        name: envoy.name,
        surname: envoy.surname,
        club: envoy.club,
        result:
          vote !== 'VOTE_VALID' ? t(vote) : (votingOption ?? t('ABSTAIN')),
      });
    });
  } else {
    data.votes.forEach(({ envoy, vote }) => {
      votes.push({
        name: envoy.name,
        surname: envoy.surname,
        club: envoy.club,
        result: t(vote),
      });
    });
  }

  return (
    <div className="flex min-h-full w-full flex-col items-center justify-center p-3">
      <div className="w-3/4">
        <Card>
          <CardHeader>
            <CardTitle>{data.title}</CardTitle>
          </CardHeader>
          <CardContent>{data.description}</CardContent>
        </Card>
        <div className="mt-3 grid grid-cols-2 gap-3">
          <Card className="flex flex-col justify-between">
            <CardHeader>
              <CardTitle>{t('headers.result')}</CardTitle>
            </CardHeader>
            <CardContent>
              <VotingResultChart data={data} tFunction={t} />
            </CardContent>
          </Card>
          <Card className="flex flex-col justify-between">
            <CardHeader>
              <CardTitle>{t('headers.resultByEnvoys')}</CardTitle>
            </CardHeader>
            <CardContent>
              <Input
                placeholder={t('envoyName')}
                onChange={(e) => {
                  setEnvoyName(e.target.value);
                }}
              />
              <ScrollArea className="mt-5 h-80">
                {votes
                  .filter((vote) =>
                    (
                      vote.name.toLowerCase() + vote.surname.toLowerCase()
                    ).includes(envoyName.toLowerCase())
                  )
                  .map((vote, index) => (
                    <div key={index} className="flex justify-between pr-4">
                      <div>
                        {vote.name} {vote.surname} ({vote.club})
                      </div>
                      <div>{vote.result}</div>
                    </div>
                  ))}
              </ScrollArea>
            </CardContent>
          </Card>
          <Card className="col-span-2">
            <CardHeader>
              <CardTitle>{t('headers.resultByClub')}</CardTitle>
            </CardHeader>
            <CardContent>
              <VotingResultChartByClub data={data} tFunction={t} />
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default VotingPage;
