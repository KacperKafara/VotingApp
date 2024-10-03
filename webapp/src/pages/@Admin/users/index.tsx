import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { useUsers } from '@/data/useUsers';
import { FC, useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import PageChanger from './PageChanger';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Button } from '@/components/ui/button';
import { UserRole, UserRoles } from '@/types/roles';
import { useUsersFilters } from '@/hooks/useUsersFilters';
import { useDebounce } from 'use-debounce';
import { Input } from '@/components/ui/input';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectSeparator,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { FaAngleDown, FaAngleUp } from 'react-icons/fa';

const UsersPage: FC = () => {
  const { users, totalPages } = useUsers();
  const { t } = useTranslation('users');
  const { username, role, sort, setFilters } = useUsersFilters();
  const [debouncedUsername, setDebouncedUsername] = useState<string>(username!);
  const [value] = useDebounce(debouncedUsername, 500);

  useEffect(() => {
    setFilters({ username: value });
  }, [value, setFilters, sort]);

  const handleChangeSortDirection = () => {
    if (sort === 'asc') {
      setFilters({ sort: 'desc' });
    } else {
      setFilters({ sort: 'asc' });
    }
  };

  return (
    <div className="flex min-h-full w-full flex-col items-center justify-center p-3">
      <div className="w-4/5">
        <h1 className="text-2xl font-bold">{t('users')}</h1>
        <span className="my-3 flex w-1/2 gap-4">
          <Select
            onValueChange={(e) => {
              setFilters({
                role: e as UserRole,
              });
            }}
            defaultValue={role?.toUpperCase()}
          >
            <SelectTrigger>
              <SelectValue placeholder={t(`filter.role`)} />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value="all">{t('filter.all')}</SelectItem>
                <SelectSeparator />
                <SelectItem value={UserRoles.admin}>
                  {t(`filter.${UserRoles.admin}`)}
                </SelectItem>
                <SelectItem value={UserRoles.moderator}>
                  {t(`filter.${UserRoles.moderator}`)}
                </SelectItem>
                <SelectItem value={UserRoles.user}>
                  {t(`filter.${UserRoles.user}`)}
                </SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
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
                <TableHead
                  className="w-1/5 hover:cursor-pointer"
                  onClick={handleChangeSortDirection}
                >
                  <span className="flex w-full items-center gap-3">
                    {t('username')}{' '}
                    {sort === 'desc' ? (
                      <FaAngleDown className="size-3" />
                    ) : (
                      <FaAngleUp className="size-3" />
                    )}
                  </span>
                </TableHead>
                <TableHead className="w-1/5">{t('firstName')}</TableHead>
                <TableHead className="w-1/5">{t('lastName')}</TableHead>
                <TableHead className="w-1/5">{t('email')}</TableHead>
                <TableHead className="w-1/5">{t('phoneNumber')}</TableHead>
                <TableHead className="pr-8"></TableHead>
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
                    <TableCell className="p-0">
                      <DropdownMenu>
                        <DropdownMenuTrigger asChild className="w-fit">
                          <Button variant="ghost">...</Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent>
                          <DropdownMenuItem>abcd</DropdownMenuItem>
                          <DropdownMenuItem>efgh</DropdownMenuItem>
                        </DropdownMenuContent>
                      </DropdownMenu>
                    </TableCell>
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
