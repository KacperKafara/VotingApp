import { FC } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useUserStore } from "./store/userStore";

const AuthGuard: FC = () => {
  const { token } = useUserStore();

  const isLoggedIn = token === undefined;

  return (
    <>
      {!isLoggedIn ? (
        <Navigate to="/" replace={true} />
      ) : (
        <div>
          <Outlet />
        </div>
      )}
    </>
  )
}

export default AuthGuard;