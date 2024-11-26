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

export interface VotingResponse {
  id: string;
  title: string;
  description: string;
  topic: string;
  date: Date;
  kind: 'ELECTRONIC' | 'TRADITIONAL' | 'ON_LIST';
  votes: VoteResponse[];
  prints: Print[];
}

export interface VotingWithoutVotesResponse {
  id: string;
  title: string;
  description: string;
  topic: string;
  date: Date;
  kind: 'ELECTRONIC' | 'TRADITIONAL' | 'ON_LIST';
}
