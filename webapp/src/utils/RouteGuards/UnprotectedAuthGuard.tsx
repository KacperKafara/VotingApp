import { FC } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { roleMapping, useUserStore } from "../../store/userStore";

const UnprotectedAuthGuard: FC = () => {
  const { token, activeRole } = useUserStore();

  const isLoggedIn = token !== undefined;

  return (
    <>
      {isLoggedIn ? (
        <Navigate to={`/${roleMapping[activeRole!]}`} replace={true} />
      ) : (
        <div>
          <Outlet />
        </div>
      )}
    </>
  );
};

export default UnprotectedAuthGuard;
