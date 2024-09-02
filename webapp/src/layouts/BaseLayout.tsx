import LoginForm from "@/components/login/login-form";
import { ModeToggle } from "@/components/mode-toogle";
import { useUserStore } from "@/store/userStore";
import { FC } from "react";
import { Outlet } from "react-router-dom";

const BaseLayout: FC = () => {

  const { token } = useUserStore();
  const isLogged = token !== undefined;

  return (
    <div className=" h-screen w-screen flex">
      <header className="w-72 flex flex-col pt-3 items-center">
        <h1 className="text-3xl font-ec">Voting App</h1>
        <div className="mt-16 w-10/12">
          {!isLogged ? <LoginForm /> : <div>abcd</div>}
        </div>
        <div className="w-full h-full flex items-end justify-center pb-4">
          <ModeToggle />
        </div>
      </header>
      <main className="flex h-full w-full bg-main-background shadow-lg">
        <Outlet />
      </main>
    </div>
  );

};

export default BaseLayout;