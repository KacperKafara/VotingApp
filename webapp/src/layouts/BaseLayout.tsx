import AccountsLinks from '@/components/AccountLinks';
import { LanguageToogle } from '@/components/language-toogle';
import LoginForm from '@/components/login/LoginForm';
import LogoutButton from '@/components/LogoutButton';
import { ModeToggle } from '@/components/mode-toogle';
import { Button } from '@/components/ui/button';
import { useUserStore } from '@/store/userStore';
import { FC, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { FaAngleLeft, FaAngleRight } from 'react-icons/fa';
import { Outlet } from 'react-router-dom';

const BaseLayout: FC = () => {
  const [isHeaderOpen, setIsHeaderOpen] = useState(false);
  const { token } = useUserStore();
  const isLogged = token !== undefined;
  const { t } = useTranslation('message');

  return (
    <div className="flex h-screen w-screen">
      <Button
        variant="ghost"
        className="absolute right-0 z-50 m-4 rounded p-2 shadow-lg md:hidden"
        onClick={() => setIsHeaderOpen(!isHeaderOpen)}
      >
        {isHeaderOpen ? <FaAngleLeft /> : <FaAngleRight />}
      </Button>

      <header
        className={`fixed inset-0 z-40 transform transition-transform duration-300 ease-in-out md:static md:translate-x-0 ${
          isHeaderOpen ? 'translate-x-0' : '-translate-x-full'
        } flex h-full w-full flex-col items-center bg-background pt-6 shadow-md md:h-screen md:w-72 md:shadow-none`}
      >
        <h1 className="font-ec text-3xl">Voting App</h1>
        <div className="mt-14 w-10/12">
          {!isLogged ? <LoginForm /> : <AccountsLinks />}
        </div>
        <div className="flex h-full w-full items-end justify-center pb-4">
          <div className="flex w-full flex-col items-center gap-2">
            {isLogged && <LogoutButton />}
            <LanguageToogle />
            <ModeToggle />
          </div>
        </div>
      </header>

      <main className="flex w-full flex-col bg-main-background shadow-lg">
        <div
          className="flex-grow overflow-y-auto scrollbar scrollbar-thumb-primary scrollbar-thumb-rounded-full scrollbar-w-1"
          style={{ minHeight: '0' }}
        >
          <Outlet />
        </div>
        {import.meta.env.VITE_APP_ENVIROMENT === 'development' && (
          <footer className="py-2 text-center text-xs">
            {t('development')}
          </footer>
        )}
      </main>
    </div>
  );
};

export default BaseLayout;
