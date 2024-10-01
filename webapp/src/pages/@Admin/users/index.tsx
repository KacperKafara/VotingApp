import {
  Table,
  TableBody,
  TableCell,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { useUsers } from '@/data/useUsers';
import { FC } from 'react';
import { useTranslation } from 'react-i18next';
import PageChanger from './PageChanger';

const UsersPage: FC = () => {
  const { users, totalPages } = useUsers();
  const { t } = useTranslation('users');

  return (
    <div className="flex min-h-full w-full flex-col items-center justify-center p-3">
      <div className="w-4/5">
        <div className="rounded-md border">
          <Table className="min-h-400px">
            <TableHeader className="font-bold">
              <TableRow>
                <TableCell>{t('username')}</TableCell>
                <TableCell>{t('firstName')}</TableCell>
                <TableCell>{t('lastName')}</TableCell>
                <TableCell>{t('email')}</TableCell>
                <TableCell>{t('phoneNumber')}</TableCell>
              </TableRow>
            </TableHeader>
            <TableBody>
              {users &&
                users.map((user) => (
                  <TableRow key={user.id}>
                    <TableCell>{user.username}</TableCell>
                    <TableCell>{user.firstName}</TableCell>
                    <TableCell>{user.lastName}</TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>{user.phoneNumber}</TableCell>
                  </TableRow>
                ))}
            </TableBody>
          </Table>
        </div>
        <div className="mt-1 flex justify-end">
          <PageChanger totalPages={totalPages} />
        </div>
      </div>
    </div>
  );
};

export default UsersPage;
