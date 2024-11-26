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

export interface UserVote {
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  birthDate: Date;
  voteResult: string;
}

export interface SurveyResponse {
  id: string;
  title: string;
  description: string;
  endDate: Date;
  createdAt: Date;
  surveyKind: SurveyKind;
  results: UserVote[];
  userVoted: boolean;
}

export interface SurveyWithoutVotesResponse {
  id: string;
  title: string;
  description: string;
  endDate: Date;
  createdAt: Date;
  surveyKind: SurveyKind;
}

export interface SurveyListResponse {
  surveys: SurveyWithoutVotesResponse[];
  totalPages: number;
  pageNumber: number;
  pageSize: number;
}

export interface CreateVoteRequest {
  totp: string;
  surveyId: string;
  voteResult: string;
}
