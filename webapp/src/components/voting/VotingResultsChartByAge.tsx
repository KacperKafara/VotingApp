import { UserVote } from '@/types/survey';
import { getAgeRange, generateChartConfig } from '@/utils/extraFunctions';
import { TFunction } from 'i18next';
import { FC } from 'react';
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from '../ui/chart';
import {
  Bar,
  BarChart,
  CartesianGrid,
  Customized,
  LabelList,
  Text,
  Legend,
  XAxis,
} from 'recharts';
import { VotingResponse } from '@/types/voting';

interface VotingResultChartByAgeProps {
  data: VotingResponse;
  tFunction: TFunction;
}

interface ChartDataItem {
  ageRange: string;
  [voteResult: string]: number | string;
}

const VotingResultChartByAge: FC<VotingResultChartByAgeProps> = ({
  data,
  tFunction,
}) => {
  const t = tFunction;

  const countVotesByAgeRange = (votes: UserVote[]): ChartDataItem[] => {
    const chartDataMap: { [ageRange: string]: ChartDataItem } = {};

    if (data.kind !== 'ON_LIST') {
      votes.forEach((vote) => {
        vote.voteResult = t(`userResults.${vote.voteResult}`);
      });
    }

    votes.forEach((vote) => {
      const ageRange = getAgeRange(vote.age);
      const { voteResult } = vote;

      if (!chartDataMap[ageRange]) {
        chartDataMap[ageRange] = { ageRange: ageRange };
      }

      if (!chartDataMap[ageRange][voteResult]) {
        chartDataMap[ageRange][voteResult] = 0;
      }

      chartDataMap[ageRange][voteResult] =
        (chartDataMap[ageRange][voteResult] as number) + 1;
    });

    return Object.values(chartDataMap);
  };

  const copy = data.userVotes.map((vote) => ({ ...vote }));
  const chartData = countVotesByAgeRange(copy);
  const chartConfig = generateChartConfig(copy) satisfies ChartConfig;

  return (
    <ChartContainer
      config={chartConfig}
      className="mx-auto h-full max-h-[380px] max-w-full px-0"
    >
      <BarChart accessibilityLayer data={chartData} margin={{ top: 30 }}>
        <Customized
          component={() => {
            return data.userVotes.length === 0 ? (
              <Text
                style={{ transform: `translate(50%, 50%)`, fontSize: 14 }}
                x={0}
                textAnchor="middle"
                verticalAnchor="middle"
                fill="hsla(var(--foreground))"
              >
                {t('noData')}
              </Text>
            ) : null;
          }}
        />
        <CartesianGrid vertical={false} />
        <XAxis
          dataKey="ageRange"
          tickLine={false}
          tickMargin={10}
          axisLine={false}
        />
        {data.userVotes.length === 0 ? (
          <ChartTooltip
            cursor={false}
            content={<ChartTooltipContent indicator="dashed" />}
          />
        ) : (
          <ChartTooltip content={<ChartTooltipContent indicator="dashed" />} />
        )}
        {Object.entries(chartConfig).map(([key, config]) => (
          <Bar key={key} dataKey={key} fill={config.color} radius={4}>
            <LabelList
              position="top"
              offset={12}
              className="fill-foreground"
              fontSize={12}
            />
          </Bar>
        ))}
        <Legend
          iconType="square"
          iconSize={8}
          formatter={(value) => {
            return (
              <span style={{ color: 'hsla(var(--foreground))' }}>{value}</span>
            );
          }}
        />
      </BarChart>
    </ChartContainer>
  );
};

export default VotingResultChartByAge;
