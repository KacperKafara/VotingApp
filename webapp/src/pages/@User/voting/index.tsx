import LoadingIcon from '@/components/loading';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';
import VoteSheet from '@/components/VoteSheet';
import { useCreateVote, useVoting } from '@/data/useVoting';
import { useUserStore } from '@/store/userStore';
import { UsetRolesWithPrefix } from '@/types/roles';
import { FC, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router-dom';
import VotingResultsEnvoys from './VotingResultsEnvoys';
import VotingResultsUsers from './VotingResultsUsers';

const VotingPage: FC = () => {
  const { id } = useParams<{ id: string }>();
  const { data, isLoading, isError } = useVoting(id!);
  const { t } = useTranslation('voting');
  const [sheetOpen, setSheetOpen] = useState(false);
  const { roles } = useUserStore();
  const { createVote, isLoading: isLoadingCreateVote } = useCreateVote();

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
        <div className="flex flex-col gap-3">
          <Card>
            <CardHeader>
              <CardTitle>{data.title}</CardTitle>
            </CardHeader>
            <CardContent className="flex items-center justify-between">
              <p>{data.description}</p>
              {roles?.includes(UsetRolesWithPrefix.voter) &&
                new Date() < new Date(data.endDate) && (
                  <Button
                    variant="secondary"
                    disabled={data.userVoted}
                    onClick={() => {
                      setSheetOpen(!sheetOpen);
                    }}
                  >
                    {t('vote')}
                  </Button>
                )}
            </CardContent>
          </Card>
          {data.prints.length > 0 && (
            <Card>
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
        </div>
        {data.endDate ? (
          <Carousel className="w-full">
            <CarouselContent>
              <CarouselItem>
                <VotingResultsEnvoys data={data} />
              </CarouselItem>
              <CarouselItem>
                <VotingResultsUsers data={data} />
              </CarouselItem>
            </CarouselContent>
            <CarouselPrevious />
            <CarouselNext />
          </Carousel>
        ) : (
          <VotingResultsEnvoys data={data} />
        )}
      </div>
      {roles?.includes(UsetRolesWithPrefix.voter) &&
        new Date() < new Date(data.endDate) && (
          <VoteSheet
            id={data.id}
            description={data.title}
            kind={data.kind}
            open={sheetOpen}
            onOpenChange={setSheetOpen}
            createVote={createVote}
            isLoading={isLoadingCreateVote}
          />
        )}
    </div>
  );
};

export default VotingPage;
