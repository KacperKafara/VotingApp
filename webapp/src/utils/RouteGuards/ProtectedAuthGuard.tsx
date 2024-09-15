import { FC } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { roleMapping, useUserStore } from "../../store/userStore";

interface AuthGuardProps {
  role: string;
}

const ProtectedAuthGuard: FC<AuthGuardProps> = ({ role }) => {
  const { token, roles, activeRole } = useUserStore();

  const isLoggedIn = token !== undefined && roles?.includes(role);

  return (
    <>
      {!isLoggedIn ? (
        <Navigate to={`/${roleMapping[activeRole!]}`} replace={true} />
      ) : (
        <Outlet />
      )}
    </>
  );
};

export default ProtectedAuthGuard;
