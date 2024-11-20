import ConfirmDialog from '@/components/ConfirmDialog';
import LoadingIcon from '@/components/loading';
import PageChanger from '@/components/PageChanger';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  useAcceptRoleRequest,
  useRejectRoleRequest,
  useRoleRequests,
} from '@/data/useRoleRequest';
import { toast } from '@/hooks/use-toast';
import { useVoterRoleRequestFilters } from '@/hooks/useRoleRequestFilters';
import { ApplicationError } from '@/types/applicationError';
import axios from 'axios';
import { FC, useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { FaAngleDown, FaAngleUp } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import { useDebounce } from 'use-debounce';

const RoleRequestsPage: FC = () => {
  const { isError, isLoading, error, data } = useRoleRequests();
  const { t } = useTranslation(['roleRequest', 'errors']);
  const { username, sort, setFilters } = useVoterRoleRequestFilters();
  const [debouncedUsername, setDebouncedUsername] = useState<string>(username!);
  const [value] = useDebounce(debouncedUsername, 500);
  const { acceptRoleRequest } = useAcceptRoleRequest();
  const { rejectRoleRequest } = useRejectRoleRequest();
  const navigate = useNavigate();

  useEffect(() => {
    setFilters({ username: value });
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

  if (data?.requests.length === 0) {
    return (
      <div className="flex h-full items-center justify-center">
        <span>{t('noRoleRequests')}</span>
      </div>
    );
  }

  return (
    <div className="flex min-h-full w-full flex-col items-center justify-center p-3">
      <div className="w-full md:w-4/5">
        <h1 className="text-2xl font-bold">{t('roleRequests')}</h1>
        <span className="my-3 flex w-full gap-2 md:w-1/2 md:gap-4">
          <Input
            placeholder={t('username')}
            defaultValue={username}
            onChange={(e) => {
              setDebouncedUsername(e.target.value);
            }}
          />
        </span>
        <div className="rounded-md border">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead className="w-1/2">{t('username')}</TableHead>
                <TableHead
                  className="w-2/5 hover:cursor-pointer"
                  onClick={handleChangeSortDirection}
                >
                  <span className="flex w-full items-center gap-3">
                    {t('requestDate')}{' '}
                    {sort === 'desc' ? (
                      <FaAngleDown className="size-3" />
                    ) : (
                      <FaAngleUp className="size-3" />
                    )}
                  </span>
                </TableHead>
                <TableHead className="w-2/12"></TableHead>
                <TableHead className="w-2/12"></TableHead>
                <TableHead className="w-2/12"></TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {data?.requests.map((request) => (
                <TableRow key={request.id}>
                  <TableCell>{request.user.username}</TableCell>
                  <TableCell className="w-2/5">
                    {new Date(request.requestDate).toLocaleDateString(
                      navigator.language || 'pl-PL'
                    )}
                  </TableCell>
                  <TableCell>
                    <Button
                      variant="secondary"
                      onClick={() => {
                        navigate(`/admin/users/${request.user.username}`);
                      }}
                    >
                      {t('viewProfileButton')}
                    </Button>
                  </TableCell>
                  <TableCell>
                    <ConfirmDialog
                      buttonText={t('accept.acceptButton')}
                      dialogTitle={t('accept.dialogTitle')}
                      dialogDescription={t('accept.dialogDescription')}
                      confirmAction={() => acceptRoleRequest(request.id)}
                    />
                  </TableCell>
                  <TableCell>
                    <ConfirmDialog
                      variant="destructive"
                      buttonText={t('reject.rejectButton')}
                      dialogTitle={t('reject.dialogTitle')}
                      dialogDescription={t('reject.dialogDescription')}
                      confirmAction={() => rejectRoleRequest(request.id)}
                    />
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
            useFilters={useVoterRoleRequestFilters}
          />
        </div>
      </div>
    </div>
  );
};

export default RoleRequestsPage;
