import AccountsLinks from '@/components/account-links';
import { LanguageToogle } from '@/components/language-toogle';
import LoginForm from '@/components/login/login-form';
import LogoutButton from '@/components/logout-button';
import { ModeToggle } from '@/components/mode-toogle';
import { useUserStore } from '@/store/userStore';
import { FC } from 'react';
import { Outlet } from 'react-router-dom';

const BaseLayout: FC = () => {
  const { token } = useUserStore();
  const isLogged = token !== undefined;

  return (
    <div className="flex h-screen w-screen">
      <header className="flex w-72 flex-col items-center pt-6">
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
      <main className="scrollbar scrollbar-thumb-primary scrollbar-thumb-rounded-full scrollbar-w-1 w-full overflow-y-auto bg-main-background shadow-lg">
        <Outlet />
      </main>
    </div>
  );
};

export default BaseLayout;
