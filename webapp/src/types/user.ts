import { UserRole } from './roles';

export interface User {
  id: string;
  username: string;
  email: string;
  role: UserRole[];
  firstName: string;
  lastName: string;
  phoneNumber: string;
  birthDate: Date;
}

export interface UsersFiltered {
  users: User[];
  totalPages: number;
  pageNumber: number;
  pageSize: number;
}
