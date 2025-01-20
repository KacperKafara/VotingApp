import { VotingResponse } from '@/types/voting';
import { generateChartDataForVotingUserByClub } from '@/utils/extraFunctions';
import { TFunction } from 'i18next';
import {
  ChartContainer,
  ChartLegend,
  ChartLegendContent,
  ChartTooltip,
  ChartTooltipContent,
} from '../ui/chart';
import {
  Bar,
  BarChart,
  CartesianGrid,
  Customized,
  Text,
  XAxis,
} from 'recharts';
import { FC } from 'react';

interface VotingResultChartUserByClubProps {
  data: VotingResponse;
  tFunction: TFunction;
}

const VotingResultChartUserByClub: FC<VotingResultChartUserByClubProps> = ({
  data,
  tFunction,
}) => {
  const t = tFunction;

  const { chartData, chartConfig } = generateChartDataForVotingUserByClub(
    data,
    t
  );

  return (
    <ChartContainer config={chartConfig}>
      <BarChart accessibilityLayer data={chartData}>
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
          dataKey="name"
          tickLine={false}
          tickMargin={10}
          axisLine={false}
          tickFormatter={(value) => value.slice(0, 3)}
        />
        {data.userVotes.length === 0 ? (
          <ChartTooltip
            cursor={false}
            content={<ChartTooltipContent indicator="dashed" />}
          />
        ) : (
          <ChartTooltip content={<ChartTooltipContent indicator="dashed" />} />
        )}
        <ChartLegend content={<ChartLegendContent />} />
        {Object.entries(chartConfig).map(([key, config]) => (
          <Bar key={key} dataKey={key} stackId="a" fill={config.color} />
        ))}
      </BarChart>
    </ChartContainer>
  );
};

export default VotingResultChartUserByClub;
