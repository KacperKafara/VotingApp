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

export interface VotingResponse {
  id: string;
  title: string;
  description: string;
  topic: string;
  yes: number;
  no: number;
  abstain: number;
  notParticipating: number;
  date: Date;
  kind: 'ELECTRONIC' | 'TRADITIONAL' | 'ON_LIST';
  votes: VoteResponse[];
}
