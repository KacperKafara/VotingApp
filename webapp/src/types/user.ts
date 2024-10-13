import { UserRole } from './roles';

export interface User {
  id: string;
  username: string;
  email: string;
  roles: UserRole[];
  firstName: string;
  lastName: string;
  phoneNumber: string;
  birthDate: Date;
  blocked: boolean;
  verified: boolean;
  lastLogin: Date;
  lastFailedLogin: Date;
}

export interface UsersFiltered {
  users: User[];
  totalPages: number;
  pageNumber: number;
  pageSize: number;
}
