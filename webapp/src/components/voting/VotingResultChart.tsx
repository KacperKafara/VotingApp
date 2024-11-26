import { VotingResponse } from '@/types/voting';
import { TFunction } from 'i18next';
import { FC } from 'react';
import { ChartContainer, ChartTooltip, ChartTooltipContent } from '../ui/chart';
import { Customized, Label, Legend, Pie, PieChart, Text } from 'recharts';
import { generateChartDataForVoting } from '@/utils/extraFunctions';

interface VotingResultChartProps {
  data: VotingResponse;
  tFunction: TFunction;
}

const VotingResultChart: FC<VotingResultChartProps> = ({ data, tFunction }) => {
  const t = tFunction;

  const { chartData, chartConfig } = generateChartDataForVoting(data, t);

  const totalValue = chartData.reduce((sum, item) => sum + item.value, 0);

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
          innerRadius={80}
          strokeWidth={5}
        >
          <Label
            content={({ viewBox }) => {
              if (viewBox && 'cx' in viewBox && 'cy' in viewBox) {
                return (
                  <text
                    x={viewBox.cx}
                    y={viewBox.cy}
                    textAnchor="middle"
                    dominantBaseline="middle"
                  >
                    <tspan
                      x={viewBox.cx}
                      y={viewBox.cy}
                      className="fill-foreground text-3xl font-bold"
                    >
                      {totalValue.toLocaleString()}
                    </tspan>
                    <tspan
                      x={viewBox.cx}
                      y={(viewBox.cy || 0) + 24}
                      className="fill-muted-foreground"
                    >
                      {t('totalVotes')}
                    </tspan>
                  </text>
                );
              }
            }}
          />
        </Pie>
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
