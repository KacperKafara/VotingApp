import { User } from './user';

export interface VoterRoleRequestResponse {
  id: string;
  user: User;
  requestDate: string;
}

export interface VoterRoleRequestListResponse {
  requests: VoterRoleRequestResponse[];
  totalPages: number;
  pageNumber: number;
  pageSize: number;
}
