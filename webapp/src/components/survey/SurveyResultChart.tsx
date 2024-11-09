import { SurveyResponse } from '@/types/survey';
import { FC } from 'react';
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from '../ui/chart';
import { Customized, Legend, Pie, Text, PieChart } from 'recharts';
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
        ? t(chartConfig[key].label as string)
        : chartConfig[key].label,
    value: voteCounts[key] || 0,
    fill: chartConfig[key].color,
  }));

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
        />
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
