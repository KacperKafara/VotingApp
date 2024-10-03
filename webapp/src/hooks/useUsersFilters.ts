import { UserRole } from '@/types/roles';
import { useCallback } from 'react';
import { useSearchParams } from 'react-router-dom';

interface UsersFilters {
  pageNumber?: number;
  pageSize?: number;
  username?: string;
  role?: UserRole | '' | 'all';
  sort?: 'asc' | 'desc';
}

export const useUsersFilters = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const pageNumber = parseInt(
    searchParams.get('page') || '0'
  ) as UsersFilters['pageNumber'];
  const pageSize = parseInt(
    searchParams.get('size') || '10'
  ) as UsersFilters['pageSize'];
  const username = (searchParams.get('username') ||
    '') as UsersFilters['username'];
  const role = (searchParams.get('role') || '') as UsersFilters['role'];
  const sort =
    ((searchParams.get('sort') || '') as UsersFilters['sort']) || 'asc';

  const setFilters = useCallback(
    (filters: UsersFilters) => {
      setSearchParams((params) => {
        if (filters.pageNumber !== undefined) {
          params.set('page', filters.pageNumber.toString());
        }
        if (filters.pageSize !== undefined) {
          params.set('size', filters.pageSize.toString());
        }
        if (filters.username !== undefined) {
          params.set('username', filters.username);
        }
        if (filters.role !== undefined && filters.role !== 'all') {
          params.set('role', filters.role.toLowerCase());
        }
        if (filters.sort !== undefined) {
          params.set('sort', filters.sort);
        }
        if (filters.role === '' || filters.role === 'all') {
          params.delete('role');
        }
        if (filters.username === '') {
          params.delete('username');
        }

        return params;
      });
    },
    [setSearchParams]
  );

  return {
    pageNumber,
    pageSize,
    username,
    role,
    sort,
    setFilters,
  };
};
