import LoadingIcon from '@/components/loading';
import SurveyResultChart from '@/components/survey/SurveyResultChart';
import SurveyResultChartByAge from '@/components/survey/SurveyResultChartByAge';
import SurveyResultChartByGender from '@/components/survey/SurveyResultChartByGender';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { useSurvey } from '@/data/useSurvey';
import { FC } from 'react';
import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router-dom';

const SurveyPage: FC = () => {
  const { id } = useParams<{ id: string }>();
  const { data, isLoading, isError } = useSurvey(id!);
  const { t } = useTranslation('survey');

  if (isLoading || isError || data === undefined) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoadingIcon />
      </div>
    );
  }
  return (
    <div className="flex min-h-full w-full flex-col items-center justify-center p-3">
      <div className="w-full md:w-3/4">
        <Card>
          <CardHeader>
            <CardTitle>{data.title}</CardTitle>
          </CardHeader>
          <CardContent className="flex items-center justify-between">
            <p>{data.description}</p>
          </CardContent>
        </Card>
        <div className="mt-3 grid grid-cols-1 gap-3 md:grid-cols-2">
          <Card className="flex flex-col justify-between">
            <CardHeader>
              <CardTitle>{t('headers.result')}</CardTitle>
            </CardHeader>
            <CardContent>
              <SurveyResultChart data={data} tFunction={t} />
            </CardContent>
          </Card>
          <Card className="flex flex-col justify-between">
            <CardHeader>
              <CardTitle>{t('headers.resultByGender')}</CardTitle>
            </CardHeader>
            <CardContent className="h-full">
              <SurveyResultChartByGender data={data} tFunction={t} />
            </CardContent>
          </Card>
          <Card className="col-span-1 md:col-span-2">
            <CardHeader>
              <CardTitle>{t('headers.resultByAge')}</CardTitle>
            </CardHeader>
            <CardContent>
              <SurveyResultChartByAge data={data} tFunction={t} />
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default SurveyPage;
