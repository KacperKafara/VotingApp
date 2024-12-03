import { UserVote } from './survey';

interface EnvoyResponse {
  name: string;
  surname: string;
  club: string;
}

export type Vote = 'YES' | 'NO' | 'ABSTAIN' | 'ABSENT' | 'VOTE_VALID';

export interface VoteResponse {
  vote: Vote;
  club: string;
  envoy: EnvoyResponse;
  votingOption: string;
}

export interface Print {
  number: string;
  title: string;
  url: string;
}

export type VotingKind = 'ELECTRONIC' | 'TRADITIONAL' | 'ON_LIST';

export interface VotingResponse {
  id: string;
  title: string;
  description: string;
  topic: string;
  date: Date;
  endDate: Date;
  kind: VotingKind;
  votes: VoteResponse[];
  prints: Print[];
  userVoted: boolean;
  userVotes: UserVote[];
}

export interface VotingWithoutVotesResponse {
  id: string;
  title: string;
  description: string;
  topic: string;
  date: Date;
  kind: VotingKind;
}

export interface VotingOption {
  id: string;
  option: string;
}
