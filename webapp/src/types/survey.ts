export type SurveyKind = 'PARLIAMENTARY_CLUB' | 'OTHER';

export interface CreateSurveyRequest {
  title: string;
  description: string;
  endDate: Date;
  surveyKind: SurveyKind;
}
