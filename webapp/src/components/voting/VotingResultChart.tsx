import { VotingResponse } from '@/types/voting';
import { TFunction } from 'i18next';
import { FC } from 'react';
import { ChartContainer, ChartTooltip, ChartTooltipContent } from '../ui/chart';
import { Customized, Legend, Pie, PieChart, Text } from 'recharts';
import { generateChartDataForVoting } from '@/utils/extraFunctions';

interface VotingResultChartProps {
  data: VotingResponse;
  tFunction: TFunction;
}

const VotingResultChart: FC<VotingResultChartProps> = ({ data, tFunction }) => {
  const t = tFunction;

  const { chartData, chartConfig } = generateChartDataForVoting(data, t);

  return (
    <ChartContainer
      config={chartConfig}
      className="mx-auto aspect-square max-h-[380px] w-full px-0"
    >
      <PieChart>
        <Customized
          component={() => {
            return data.votes.length === 0 ? (
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
        <ChartTooltip
          cursor={false}
          content={<ChartTooltipContent nameKey="name" hideLabel />}
        />
        <Pie
          data={chartData}
          dataKey="value"
          labelLine={false}
          nameKey="name"
        />
        <Legend
          iconType="square"
          iconSize={8}
          formatter={(value, entry) => {
            return (
              <span style={{ color: 'hsla(var(--foreground))' }}>
                {value} ({entry.payload?.value})
              </span>
            );
          }}
        />
      </PieChart>
    </ChartContainer>
  );
};

export default VotingResultChart;
