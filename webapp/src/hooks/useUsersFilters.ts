import { useCallback } from 'react';
import { useSearchParams } from 'react-router-dom';

interface UsersFilters {
  pageNumber?: number;
  pageSize?: number;
}

export const useUsersFilters = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const pageNumber = parseInt(
    searchParams.get('page') || '0'
  ) as UsersFilters['pageNumber'];
  const pageSize = parseInt(
    searchParams.get('size') || '10'
  ) as UsersFilters['pageSize'];

  const setFilters = useCallback(
    (filters: UsersFilters) => {
      setSearchParams((params) => {
        if (filters.pageNumber !== undefined) {
          params.set('page', filters.pageNumber.toString());
        }
        if (filters.pageSize !== undefined) {
          params.set('size', filters.pageSize.toString());
        }
        return params;
      });
    },
    [setSearchParams]
  );

  return {
    pageNumber,
    pageSize,
    setFilters,
  };
};
