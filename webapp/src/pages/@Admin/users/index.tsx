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

const UsersPage: FC = () => {
  const { data: users } = useUsers();
  const { t } = useTranslation('users');

  return (
    <div className="flex w-full flex-col items-center justify-center">
      <div className="w-4/5 rounded-md border">
        <Table>
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
    </div>
  );
};

export default UsersPage;
