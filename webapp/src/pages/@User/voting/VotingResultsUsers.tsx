import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import VotingResultChartByGender from '@/components/voting/VotingResultChartByGender';
import VotingResultChartUser from '@/components/voting/VotingResultChartUser';
import VotingResultsChartByAge from '@/components/voting/VotingResultsChartByAge';
import { VotingResponse } from '@/types/voting';
import { FC } from 'react';
import { useTranslation } from 'react-i18next';

interface VotingResultsUsersProps {
  data: VotingResponse;
}

const VotingResultsUsers: FC<VotingResultsUsersProps> = ({ data }) => {
  const { t } = useTranslation('voting');

  return (
    <div className="mt-3 grid grid-cols-1 gap-3 md:grid-cols-2">
      <Card className="flex flex-col justify-between">
        <CardHeader>
          <CardTitle>{t('headers.resultUsers')}</CardTitle>
        </CardHeader>
        <CardContent>
          <VotingResultChartUser data={data} tFunction={t} />
        </CardContent>
      </Card>
      <Card className="flex flex-col justify-between">
        <CardHeader>
          <CardTitle>{t('headers.resultUsersByGender')}</CardTitle>
        </CardHeader>
        <CardContent className="h-full">
          <VotingResultChartByGender data={data} tFunction={t} />
        </CardContent>
      </Card>
      <Card className="col-span-1 md:col-span-2">
        <CardHeader>
          <CardTitle>{t('headers.resultUsersByAge')}</CardTitle>
        </CardHeader>
        <CardContent>
          <VotingResultsChartByAge data={data} tFunction={t} />
        </CardContent>
      </Card>
    </div>
  );
};

export default VotingResultsUsers;
