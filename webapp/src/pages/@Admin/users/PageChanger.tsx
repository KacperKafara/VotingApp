import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import {
  FaAngleRight,
  FaAngleLeft,
  FaAngleDoubleLeft,
  FaAngleDoubleRight,
} from 'react-icons/fa';
import { FC } from 'react';
import { RiExpandUpDownLine } from 'react-icons/ri';
import { useTranslation } from 'react-i18next';
import { useUsersFilters } from '@/hooks/useUsersFilters';

interface PageChangerProps {
  totalPages?: number;
}

const PageChanger: FC<PageChangerProps> = ({ totalPages = 0 }) => {
  const { t } = useTranslation('users');
  const { pageNumber, pageSize, setFilters } = useUsersFilters();

  if (pageNumber === undefined || pageSize === undefined) {
    return;
  }

  const prevPage = () => {
    if (pageNumber > 0) {
      setFilters({ pageNumber: pageNumber - 1 });
    }
  };

  const nextPage = () => {
    if (pageNumber < totalPages - 1) {
      setFilters({ pageNumber: pageNumber + 1 });
    }
  };

  const handlePageSizeChange = (size: number) => {
    console.log(size);
    setFilters({
      pageSize: size,
    });
    setFilters({ pageNumber: 0 });
  };

  return (
    <div className="flex items-center gap-7">
      <div className="flex items-center gap-2">
        <p className="mr-1">{t('pageChanger.numberOfElements')}</p>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              className="flex h-8 items-center px-2"
              variant="outline"
              role="combobox"
            >
              {pageSize}
              <RiExpandUpDownLine className="ml-3 text-sm" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent>
            <DropdownMenuItem
              onSelect={() => handlePageSizeChange(5)}
              className="h-8 px-2"
            >
              5
            </DropdownMenuItem>
            <DropdownMenuItem
              onSelect={() => handlePageSizeChange(10)}
              className="h-8 px-2"
            >
              10
            </DropdownMenuItem>
            <DropdownMenuItem
              onSelect={() => handlePageSizeChange(15)}
              className="h-8 px-2"
            >
              15
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
      <p>
        {t('pageChanger.page')} {pageNumber + 1} {t('pageChanger.of')}{' '}
        {totalPages}
      </p>
      <div className="flex gap-1">
        <Button
          className="h-8 px-2"
          onClick={() => setFilters({ pageNumber: 0 })}
          variant="outline"
          disabled={pageNumber === 0}
        >
          <FaAngleDoubleLeft />
        </Button>
        <Button
          className="h-8 px-2"
          onClick={() => prevPage()}
          variant="outline"
          disabled={pageNumber === 0}
        >
          <FaAngleLeft />
        </Button>
        <Button
          className="h-8 px-2"
          onClick={() => nextPage()}
          variant="outline"
          disabled={pageNumber === totalPages - 1}
        >
          <FaAngleRight />
        </Button>
        <Button
          className="h-8 px-2"
          onClick={() => setFilters({ pageNumber: totalPages - 1 })}
          variant="outline"
          disabled={pageNumber === totalPages - 1}
        >
          <FaAngleDoubleRight />
        </Button>
      </div>
    </div>
  );
};

export default PageChanger;
