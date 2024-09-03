import { LanguageToogle } from "@/components/language-toogle";
import LoginForm from "@/components/login/login-form";
import LogoutButton from "@/components/logout-button";
import { ModeToggle } from "@/components/mode-toogle";
import { useUserStore } from "@/store/userStore";
import { FC } from "react";
import { Outlet } from "react-router-dom";

const BaseLayout: FC = () => {

  const { token } = useUserStore();
  const isLogged = token !== undefined;

  return (
    <div className=" h-screen w-screen flex">
      <header className="w-72 flex flex-col pt-6 items-center">
        <h1 className="text-3xl font-ec">Voting App</h1>
        <div className="mt-14 w-10/12">
          {!isLogged ? <LoginForm /> : <div>abcd</div>}
        </div>
        <div className="w-full h-full flex items-end justify-center pb-4">
          <div className="flex flex-col w-full items-center gap-2">
            {isLogged && <LogoutButton />}
            <LanguageToogle />
            <ModeToggle />
          </div>
        </div>
      </header>
      <main className="flex h-full w-full bg-main-background shadow-lg">
        <Outlet />
      </main>
    </div>
  );

};

export default BaseLayout;