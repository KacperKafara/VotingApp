import { SurveyKind } from '@/types/survey';
import { useCallback } from 'react';
import { useSearchParams } from 'react-router-dom';

interface SurveysFilters {
  pageNumber?: number;
  pageSize?: number;
  title?: string;
  sort?: 'asc' | 'desc';
  kind?: SurveyKind | '' | 'all';
}

export const useSurveysFilters = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const pageNumber = parseInt(
    searchParams.get('page') || '0'
  ) as SurveysFilters['pageNumber'];
  const pageSize = parseInt(
    searchParams.get('size') || '10'
  ) as SurveysFilters['pageSize'];
  const title = (searchParams.get('title') || '') as SurveysFilters['title'];
  const kind = (searchParams.get('kind') || '') as SurveysFilters['kind'];
  const sort =
    ((searchParams.get('sort') || '') as SurveysFilters['sort']) || 'asc';

  const setFilters = useCallback(
    (filters: SurveysFilters) => {
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
        if (filters.kind !== undefined && filters.kind !== 'all') {
          params.set('kind', filters.kind);
        }
        if (filters.sort !== undefined) {
          params.set('sort', filters.sort);
        }
        if (filters.title === '') {
          params.delete('title');
        }
        if (filters.kind === '' || filters.kind === 'all') {
          params.delete('kind');
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
    kind,
    sort,
    setFilters,
  };
};
