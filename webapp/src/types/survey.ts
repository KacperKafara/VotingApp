export type SurveyKind = 'PARLIAMENTARY_CLUB' | 'OTHER';

export const SurveyKinds = {
  parliamentaryClub: 'PARLIAMENTARY_CLUB',
  other: 'OTHER',
} as const;

export interface CreateSurveyRequest {
  title: string;
  description: string;
  endDate: Date;
  surveyKind: SurveyKind;
}

export interface SurveyResponse {
  id: string;
  title: string;
  description: string;
  endDate: Date;
  createdAt: Date;
  surveyKind: SurveyKind;
}

export interface SurveyListResponse {
  surveys: SurveyResponse[];
  totalPages: number;
  pageNumber: number;
  pageSize: number;
}
