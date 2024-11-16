import LoadingIcon from '@/components/loading';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { useActiveSurveys } from '@/data/useActiveSurveys';
import { FC } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

const VotingAndSurveysYouCanVote: FC = () => {
  const { isError, isLoading, data } = useActiveSurveys();
  const { t } = useTranslation(['activeSurveys']);
  const navigate = useNavigate();

  if (isLoading || isError || data === undefined) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoadingIcon />
      </div>
    );
  }

  return (
    <div className="flex min-h-full w-full flex-col items-center justify-center p-3">
      <div className="flex min-h-[600px] w-4/5 gap-2">
        <div className="flex min-h-full w-1/2 flex-col gap-3 rounded-md border border-gray-400 p-3">
          <p className="text-center font-bold">{t('activeSurveys')}</p>
          {data.surveys.length === 0 ? (
            <p className="my-auto text-center">{t('noActiveSurveys')}</p>
          ) : (
            data.surveys.map((survey) => (
              <Card
                key={survey.id}
                className="hover:cursor-pointer"
                onClick={() => {
                  navigate(`/user/surveys/${survey.id}`);
                }}
              >
                <CardHeader>
                  <CardTitle>{survey.title}</CardTitle>
                </CardHeader>
                <CardContent>
                  <p>{survey.description}</p>
                </CardContent>
              </Card>
            ))
          )}
        </div>
        <div className="flex min-h-full w-1/2 flex-col gap-3 rounded-md border border-gray-400 p-3">
          <p className="text-center font-bold">{t('activeVotings')}</p>
          {data.votingList.length === 0 ? (
            <p className="my-auto text-center">{t('noActiveVotings')}</p>
          ) : (
            data.votingList.map((voting) => (
              <Card
                key={voting.id}
                className="hover:cursor-pointer"
                onClick={() => {
                  navigate(`/user/votings/${voting.id}`);
                }}
              >
                <CardHeader>
                  <CardTitle>{voting.title}</CardTitle>
                </CardHeader>
                <CardContent>
                  <p>{voting.description}</p>
                </CardContent>
              </Card>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default VotingAndSurveysYouCanVote;
