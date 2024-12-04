import { SurveyResponse } from '@/types/survey';
import { FC } from 'react';
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from '../ui/chart';
import { Customized, Legend, Pie, Text, PieChart, Label } from 'recharts';
import { TFunction } from 'i18next';
import { generateChartConfig } from '@/utils/extraFunctions';

interface SurveyResultChartProps {
  data: SurveyResponse;
  tFunction: TFunction;
}

const SurveyResultChart: FC<SurveyResultChartProps> = ({ data, tFunction }) => {
  const t = tFunction;

  const voteCounts: { [key: string]: number } = {};
  data.results.forEach((result) => {
    const vote = result.voteResult;
    voteCounts[vote] = (voteCounts[vote] || 0) + 1;
  });

  const chartConfig = generateChartConfig(data.results) satisfies ChartConfig;

  const pieChartData = Object.keys(chartConfig).map((key) => ({
    name:
      data.surveyKind === 'OTHER'
        ? t(`userResults.${chartConfig[key].label}`)
        : chartConfig[key].label,
    value: voteCounts[key] || 0,
    fill: chartConfig[key].color,
  }));

  const totalValue = pieChartData.reduce((sum, item) => sum + item.value, 0);

  return (
    <ChartContainer
      className="mx-auto aspect-square max-h-[380px] px-0"
      config={chartConfig}
    >
      <PieChart>
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
        <ChartTooltip
          cursor={false}
          content={<ChartTooltipContent nameKey="value" hideLabel />}
        />
        <Pie
          data={pieChartData}
          dataKey="value"
          labelLine={false}
          innerRadius={80}
          strokeWidth={5}
          label={({ payload, ...props }) => {
            return (
              <text
                cx={props.cx}
                cy={props.cy}
                x={props.x}
                y={props.y}
                textAnchor={props.textAnchor}
                dominantBaseline={props.dominantBaseline}
                fill="hsla(var(--foreground))"
              >
                {payload.value}
              </text>
            );
          }}
          nameKey="name"
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
          formatter={(value) => {
            return (
              <span style={{ color: 'hsla(var(--foreground))' }}>{value}</span>
            );
          }}
        />
      </PieChart>
    </ChartContainer>
  );
};

export default SurveyResultChart;
