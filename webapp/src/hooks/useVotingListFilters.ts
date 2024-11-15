import { useCallback } from 'react';
import { useSearchParams } from 'react-router-dom';

interface VotingListFilters {
  pageNumber?: number;
  pageSize?: number;
  sort?: 'asc' | 'desc';
  title?: string;
  sitting?: number;
}

export const useVotingListFilters = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const pageNumber = parseInt(
    searchParams.get('page') || '0'
  ) as VotingListFilters['pageNumber'];
  const pageSize = parseInt(
    searchParams.get('size') || '10'
  ) as VotingListFilters['pageSize'];
  const title = (searchParams.get('title') || '') as VotingListFilters['title'];
  const sitting = (searchParams.get('sitting') ||
    undefined) as VotingListFilters['sitting'];
  const sort =
    ((searchParams.get('sort') || '') as VotingListFilters['sort']) || 'desc';

  const setFilters = useCallback(
    (filters: VotingListFilters) => {
      setSearchParams((params) => {
        if (filters.pageNumber !== undefined) {
          params.set('page', filters.pageNumber.toString());
        }
        if (filters.pageSize !== undefined) {
          params.set('size', filters.pageSize.toString());
        }
        if (filters.title !== undefined) {
          params.set('title', filters.title);
        }
        if (filters.sitting !== undefined && filters.sitting !== 0) {
          params.set('sitting', filters.sitting.toString());
        } else if (filters.sitting === 0) {
          params.delete('sitting');
        }
        if (filters.sort !== undefined) {
          params.set('sort', filters.sort);
        }
        if (filters.title === '') {
          params.delete('title');
        }

        return params;
      });
    },
    [setSearchParams]
  );

  return {
    pageNumber,
    pageSize,
    title,
    sitting,
    sort,
    setFilters,
  };
};
