import { useCallback } from 'react';
import { useSearchParams } from 'react-router-dom';

interface VoterRoleRequestFilters {
  pageNumber?: number;
  pageSize?: number;
  username?: string;
  sort?: 'asc' | 'desc';
}

export const useVoterRoleRequestFilters = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const pageNumber = parseInt(
    searchParams.get('page') || '0'
  ) as VoterRoleRequestFilters['pageNumber'];
  const pageSize = parseInt(
    searchParams.get('size') || '10'
  ) as VoterRoleRequestFilters['pageSize'];
  const username = (searchParams.get('username') ||
    '') as VoterRoleRequestFilters['username'];
  const sort =
    ((searchParams.get('sort') || '') as VoterRoleRequestFilters['sort']) ||
    'asc';

  const setFilters = useCallback(
    (filters: VoterRoleRequestFilters) => {
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
        if (filters.sort !== undefined) {
          params.set('sort', filters.sort);
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
    sort,
    setFilters,
  };
};
