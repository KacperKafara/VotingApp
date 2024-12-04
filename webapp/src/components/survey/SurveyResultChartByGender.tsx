import { SurveyResponse, UserVote } from '@/types/survey';
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
  Legend,
  Text,
  XAxis,
} from 'recharts';
import { generateChartConfig } from '@/utils/extraFunctions';

interface SurveyResultChartByGenderProps {
  data: SurveyResponse;
  tFunction: TFunction;
}

interface ChartDataItem {
  gender: string;
  [voteResult: string]: number | string;
}

const SurveyResultChartByGender: FC<SurveyResultChartByGenderProps> = ({
  data,
  tFunction,
}) => {
  const t = tFunction;

  const countVotesByGender = (votes: UserVote[]): ChartDataItem[] => {
    const chartDataMap: { [gender: string]: ChartDataItem } = {};

    if (data.surveyKind === 'OTHER') {
      votes.forEach((vote) => {
        vote.voteResult = t(`userResults.${vote.voteResult}`);
      });
    }

    votes.forEach((vote) => {
      const { gender, voteResult } = vote;

      if (!chartDataMap[gender]) {
        chartDataMap[gender] = { gender: t(gender) };
      }

      if (!chartDataMap[gender][voteResult]) {
        chartDataMap[gender][voteResult] = 0;
      }

      chartDataMap[gender][voteResult] =
        (chartDataMap[gender][voteResult] as number) + 1;
    });

    return Object.values(chartDataMap);
  };

  const copy = data.results.map((result) => ({ ...result }));
  const chartData = countVotesByGender(copy);
  const chartConfig = generateChartConfig(copy) satisfies ChartConfig;

  return (
    <ChartContainer
      config={chartConfig}
      className="mx-auto h-full max-h-[380px] max-w-full px-0"
    >
      <BarChart accessibilityLayer data={chartData} margin={{ top: 30 }}>
        <Customized
          component={() => {
            return data.results.length === 0 ? (
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
          dataKey="gender"
          tickLine={false}
          tickMargin={10}
          axisLine={false}
        />
        <ChartTooltip
          cursor={false}
          content={<ChartTooltipContent indicator="dashed" />}
        />
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

export default SurveyResultChartByGender;
