import { VotingResponse } from '@/types/voting';
import { generateChartDataForVotingByClub } from '@/utils/extraFunctions';
import { TFunction } from 'i18next';
import { FC } from 'react';
import {
  ChartContainer,
  ChartLegend,
  ChartLegendContent,
  ChartTooltip,
  ChartTooltipContent,
} from '../ui/chart';
import { Bar, BarChart, CartesianGrid, XAxis } from 'recharts';

interface VotingResultByClubProps {
  data: VotingResponse;
  tFunction: TFunction;
}

const VotingResultChartByClub: FC<VotingResultByClubProps> = ({
  data,
  tFunction,
}) => {
  const t = tFunction;

  const { chartData, chartConfig } = generateChartDataForVotingByClub(data, t);

  return (
    <ChartContainer config={chartConfig}>
      <BarChart accessibilityLayer data={chartData}>
        <CartesianGrid vertical={false} />
        <XAxis
          dataKey="name"
          tickLine={false}
          tickMargin={10}
          axisLine={false}
          tickFormatter={(value) => value.slice(0, 3)}
        />
        <ChartTooltip content={<ChartTooltipContent />} />
        <ChartLegend content={<ChartLegendContent />} />
        {Object.entries(chartConfig).map(([key, config]) => (
          <Bar key={key} dataKey={key} stackId="a" fill={config.color} />
        ))}
      </BarChart>
    </ChartContainer>
  );
};

export default VotingResultChartByClub;
