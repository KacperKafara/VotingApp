import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { ScrollArea } from '@/components/ui/scroll-area';
import VotingResultChart from '@/components/voting/VotingResultChart';
import VotingResultChartByClub from '@/components/voting/VotingResultChartByClub';
import { VotingResponse } from '@/types/voting';
import { FC, useState } from 'react';
import { useTranslation } from 'react-i18next';

interface VoteResult {
  name: string;
  surname: string;
  club: string;
  result: string;
}

interface VotingResultsEnvoysProps {
  data: VotingResponse;
}

const VotingResultsEnvoys: FC<VotingResultsEnvoysProps> = ({ data }) => {
  const [envoyName, setEnvoyName] = useState<string>('');
  const { t } = useTranslation('voting');

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
    <div className="mt-3 grid grid-cols-1 gap-3 md:grid-cols-2">
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
                  vote.name.toLowerCase() +
                  vote.surname.toLowerCase() +
                  vote.club.toLowerCase()
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
      <Card className="col-span-1 md:col-span-2">
        <CardHeader>
          <CardTitle>{t('headers.resultByClub')}</CardTitle>
        </CardHeader>
        <CardContent>
          <VotingResultChartByClub data={data} tFunction={t} />
        </CardContent>
      </Card>
    </div>
  );
};

export default VotingResultsEnvoys;
