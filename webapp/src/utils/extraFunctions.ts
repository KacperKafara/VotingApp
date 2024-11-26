import { ChartConfig } from '@/components/ui/chart';
import { UserVote } from '@/types/survey';
import { VoteResponse, VotingResponse } from '@/types/voting';
import { TFunction } from 'i18next';
import uniqolor from 'uniqolor';

export const calculateAge = (birthDate: Date) => {
  const today = new Date();
  const diffInMiliseconds = today.getTime() - birthDate.getTime();
  const diffInYears = diffInMiliseconds / 1000 / 60 / 60 / 24 / 365.25;
  return Math.abs(Math.round(diffInYears));
};

export const getAgeRange = (age: number) => {
  if (age >= 16 && age <= 30) return '16-30';
  if (age > 30 && age <= 40) return '30-40';
  if (age > 40 && age <= 50) return '40-50';
  if (age > 50 && age <= 65) return '50-65';
  return '65+';
};

interface ChartConfigItem {
  label: string;
  color: string;
}

export const generateChartConfig = (votes: UserVote[]): ChartConfig => {
  const uniqueVoteResults = Array.from(
    new Set(votes.map((vote) => vote.voteResult))
  );

  const chartConfig: ChartConfig = {};
  uniqueVoteResults.forEach((voteResult, index) => {
    chartConfig[voteResult] = {
      label: voteResult,
      color: uniqolor(index * 10, { format: 'hsl' }).color,
    } as ChartConfigItem;
  });

  return chartConfig;
};

interface ChartDataItem {
  name: string;
  value: number;
  fill: string;
}

export const generateChartDataForVoting = (
  data: VotingResponse,
  t: TFunction
) => {
  const voteCounts: Record<string, number> = {};
  data.votes.forEach((vote) => {
    let value;

    if (data.kind === 'ON_LIST') {
      if (vote.votingOption === null && vote.vote === 'VOTE_VALID') {
        value = t('ABSTAIN');
      } else if (vote.vote !== 'VOTE_VALID') {
        value = t(vote.vote);
      } else {
        value = vote.votingOption;
      }
    } else {
      value = vote.vote;
    }

    voteCounts[value] = (voteCounts[value] || 0) + 1;
  });

  console.log(voteCounts);

  delete voteCounts['ABSENT'];
  delete voteCounts[t('ABSENT')];

  const chartData: ChartDataItem[] = Object.keys(voteCounts).map(
    (key, index) => ({
      name: data.kind !== 'ON_LIST' ? t(key) : key,
      value: voteCounts[key],
      fill: uniqolor(index * 10, { format: 'hsl' }).color,
    })
  );

  const chartConfig: ChartConfig = {};

  chartData.forEach((key) => {
    chartConfig[key.name] = {
      label: key.name,
      color: key.fill,
    };
  });

  return { chartData, chartConfig };
};

export const generateChartDataForVotingByClub = (
  data: VotingResponse,
  t: TFunction
) => {
  const voteCounts: Record<string, Record<string, number>> = {};

  const getVoteValue = (vote: VoteResponse): string => {
    if (data.kind === 'ON_LIST') {
      if (vote.votingOption === null && vote.vote === 'VOTE_VALID') {
        return t('ABSTAIN');
      } else if (vote.vote !== 'VOTE_VALID') {
        return t(vote.vote);
      }
      return vote.votingOption;
    }
    return t(vote.vote);
  };

  data.votes.forEach((vote) => {
    const value = getVoteValue(vote);
    voteCounts[vote.club] = voteCounts[vote.club] || {};
    voteCounts[vote.club][value] = (voteCounts[vote.club][value] || 0) + 1;
  });

  const chartData = Object.entries(voteCounts).map(([key, value]) => ({
    name: key,
    ...value,
  }));

  const uniqueLabels = Array.from(
    new Set(
      chartData.flatMap((item) =>
        Object.keys(item).filter((key) => key !== 'name')
      )
    )
  );

  const chartConfig: ChartConfig = uniqueLabels.reduce(
    (result, label, index) => {
      result[label] = {
        label,
        color: uniqolor(index * 10, { format: 'hsl' }).color,
      };
      return result;
    },
    {} as ChartConfig
  );

  return { chartData, chartConfig };
};
