import { ChartConfig } from '@/components/ui/chart';
import { UserVote } from '@/types/survey';
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
