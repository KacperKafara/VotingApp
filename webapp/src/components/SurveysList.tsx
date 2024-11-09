import { FC, useEffect, useState } from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from './ui/table';
import PageChanger from './PageChanger';
import { useSurveysFilters } from '@/hooks/useSurveysFilters';
import { useSurveys } from '@/data/useSurvey';
import { useDebounce } from 'use-debounce';
import { useTranslation } from 'react-i18next';
import axios from 'axios';
import { toast } from '@/hooks/use-toast';
import { ApplicationError } from '@/types/applicationError';
import LoadingIcon from './loading';
import { FaAngleDown, FaAngleUp } from 'react-icons/fa';
import { Input } from './ui/input';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectSeparator,
  SelectTrigger,
  SelectValue,
} from './ui/select';
import { SurveyKind, SurveyKinds } from '@/types/survey';
import { Button } from './ui/button';
import { useLocation, useNavigate } from 'react-router-dom';

interface SurveyListProps {
  children?: React.ReactNode;
}

const SurveyList: FC<SurveyListProps> = ({ children }) => {
  const { t } = useTranslation(['survey', 'errors', 'pageChanger']);
  const { title, kind, sort, setFilters } = useSurveysFilters();
  const { isError, isLoading, error, data } = useSurveys();
  const [debouncedTitle, setDebouncedTitle] = useState<string>(title!);
  const [value] = useDebounce(debouncedTitle, 500);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    setFilters({ title: value });
  }, [value, setFilters]);

  const handleChangeSortDirection = () => {
    if (sort === 'asc') {
      setFilters({ sort: 'desc' });
    } else {
      setFilters({ sort: 'asc' });
    }
  };

  useEffect(() => {
    if (isError) {
      if (axios.isAxiosError(error)) {
        toast({
          variant: 'destructive',
          title: t('errors:defaultTitle'),
          description: t(
            'errors:' + (error.response?.data as ApplicationError).code
          ),
        });
      }
    }
  }, [error, isError, t]);

  if (isLoading || isError) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoadingIcon />
      </div>
    );
  }

  return (
    <div className="flex min-h-full w-full flex-col items-center justify-center p-3">
      <div className="w-4/5">
        <div className="my-3 flex items-end justify-between">
          <div className="w-1/2">
            <h1 className="text-2xl font-bold">{t('surveys')}</h1>
            <span className="mt-3 flex w-full gap-4">
              <Select
                onValueChange={(e) => {
                  setFilters({
                    kind: e as SurveyKind,
                  });
                }}
                defaultValue={kind?.toUpperCase()}
              >
                <SelectTrigger>
                  <SelectValue placeholder={t(`kind`)} />
                </SelectTrigger>
                <SelectContent>
                  <SelectGroup>
                    <SelectItem value="all">{t('all')}</SelectItem>
                    <SelectSeparator />
                    {Object.values(SurveyKinds).map((kind) => (
                      <SelectItem key={kind} value={kind}>
                        {t(`kinds.${kind}`)}
                      </SelectItem>
                    ))}
                  </SelectGroup>
                </SelectContent>
              </Select>
              <Input
                placeholder={t('title')}
                defaultValue={title}
                onChange={(e) => {
                  setDebouncedTitle(e.target.value);
                }}
              />
            </span>
          </div>
          {children}
        </div>
        <div className="rounded-md border">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead className="w-1/4">{t('title')}</TableHead>
                <TableHead className="w-1/4">{t('kind')}</TableHead>
                <TableHead className="w-1/4">{t('endDate')}</TableHead>
                <TableHead
                  onClick={handleChangeSortDirection}
                  className="w-1/4 hover:cursor-pointer"
                >
                  <span className="flex w-full items-center gap-3">
                    {t('createdAt')}{' '}
                    {sort === 'desc' ? (
                      <FaAngleDown className="size-3" />
                    ) : (
                      <FaAngleUp className="size-3" />
                    )}
                  </span>
                </TableHead>
                <TableHead className="pr-8" />
              </TableRow>
            </TableHeader>
            <TableBody>
              {data?.surveys &&
                data.surveys.map((survey) => (
                  <TableRow key={survey.id}>
                    <TableCell>{survey.title}</TableCell>
                    <TableCell>{t(`kinds.${survey.surveyKind}`)}</TableCell>
                    <TableCell>
                      {new Date(survey.endDate).toLocaleDateString(
                        navigator.language || 'pl-PL'
                      )}
                    </TableCell>
                    <TableCell>
                      {new Date(survey.createdAt).toLocaleDateString(
                        navigator.language || 'pl-PL'
                      )}
                    </TableCell>
                    <TableCell className="p-0">
                      <Button
                        variant="ghost"
                        onClick={() => {
                          navigate(`${location.pathname}/${survey.id}`);
                        }}
                      >
                        ...
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
            </TableBody>
          </Table>
        </div>
        <div className="mt-1 flex justify-end">
          <PageChanger
            t={t}
            totalPages={data?.totalPages}
            useFilters={useSurveysFilters}
          />
        </div>
      </div>
    </div>
  );
};

export default SurveyList;
