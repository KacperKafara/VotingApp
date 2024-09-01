import { FC } from "react";
import { Outlet } from "react-router-dom";

const AuthGuard: FC = () => {
  const token = undefined;

  const isLoggedIn = token === undefined;

  return (
    <>
      {!isLoggedIn ? (
        <div>
          <h1>Logged In</h1>
        </div>
      ) : (
        <div>
          <Outlet />
        </div>
      )}
    </>
  )
}

export default AuthGuard;