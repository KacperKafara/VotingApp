import { useVotingList } from '@/data/useVoting';
import { toast } from '@/hooks/use-toast';
import { useVotingListFilters } from '@/hooks/useVotingListFilters';
import { ApplicationError } from '@/types/applicationError';
import axios from 'axios';
import { FC, useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useDebounce } from 'use-debounce';
import LoadingIcon from '../loading';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '../ui/table';
import { FaAngleDown, FaAngleUp } from 'react-icons/fa';
import { Button } from '../ui/button';
import { CardDescription } from '../ui/card';
import PageChanger from '../PageChanger';
import { Input } from '../ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select';
import { useNavigate } from 'react-router-dom';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '../ui/tooltip';

interface VotingListProps {
  type: string;
}

const VotingListComponent: FC<VotingListProps> = ({ type }) => {
  const { t } = useTranslation(['voting', 'errors', 'pageChanger']);
  const { title, sort, sitting, setFilters } = useVotingListFilters();
  const { isError, isLoading, data, error } = useVotingList();
  const [debouncedTitle, setDebouncedTitle] = useState<string>(title!);
  const [value] = useDebounce(debouncedTitle, 500);
  const navigate = useNavigate();

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

  if (isLoading || isError || data === undefined) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoadingIcon />
      </div>
    );
  }

  return (
    <div className="flex min-h-full w-full flex-col items-center justify-center p-3">
      <div className="w-full md:w-4/5">
        <div className="my-3 flex items-end justify-between">
          <div className="w-full md:w-1/2">
            <h1 className="text-2xl font-bold">{t('votings')}</h1>
            <span className="mt-3 flex w-full gap-4">
              <Input
                placeholder={t('title')}
                defaultValue={title}
                onChange={(e) => {
                  setDebouncedTitle(e.target.value);
                }}
              />
              <Select
                onValueChange={(e) => {
                  setFilters({ sitting: parseInt(e) });
                }}
                defaultValue={sitting?.toString()}
              >
                <SelectTrigger>
                  <SelectValue placeholder={t(`sitting`)} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="0">{t('all')}</SelectItem>
                  {data.sittings.map((sitting) => (
                    <SelectItem key={sitting} value={sitting.toString()}>
                      {t('sitting')} {sitting}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </span>
          </div>
        </div>
        <div className="rounded-md border">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead className="w-5/6">{t('title')}</TableHead>
                <TableHead
                  onClick={handleChangeSortDirection}
                  className="w-1/6 hover:cursor-pointer"
                >
                  <span className="flex w-full items-center gap-3">
                    {t('date')}{' '}
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
              {data.votingList.map((voting) => (
                <TableRow key={voting.id}>
                  <TableCell className="flex w-5/6 flex-col gap-2">
                    {voting.title}
                    <CardDescription>{voting.topic}</CardDescription>
                  </TableCell>
                  <TableCell className="w-1/6">
                    {new Date(voting.date).toLocaleDateString(
                      navigator.language || 'pl-PL'
                    )}
                  </TableCell>
                  <TableCell className="pr-8">
                    {type === 'user' && (
                      <TooltipProvider>
                        <Tooltip>
                          <TooltipTrigger asChild>
                            <Button
                              variant="ghost"
                              onClick={() => {
                                navigate(`/user/votings/${voting.id}`);
                              }}
                            >
                              ...
                            </Button>
                          </TooltipTrigger>
                          <TooltipContent>{t('results')}</TooltipContent>
                        </Tooltip>
                      </TooltipProvider>
                    )}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
        <div className="mt-1 flex justify-end">
          <PageChanger
            t={t}
            totalPages={data.totalPages}
            useFilters={useVotingListFilters}
          />
        </div>
      </div>
    </div>
  );
};

export default VotingListComponent;
