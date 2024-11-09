import { FC } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import {
  getActiveRole,
  roleMapping,
  useUserStore,
} from '../../store/userStore';

interface AuthGuardProps {
  role?: string;
}

const ProtectedAuthGuard: FC<AuthGuardProps> = ({ role }) => {
  const { token, roles } = useUserStore();
  let isLoggedIn = false;

  if (role === undefined) {
    isLoggedIn = token !== undefined;
  } else {
    isLoggedIn =
      token !== undefined && roles !== undefined && roles.includes(role);
  }

  return (
    <>
      {!isLoggedIn ? (
        <Navigate
          to={`/${roleMapping[getActiveRole(roles!)]}`}
          replace={true}
        />
      ) : (
        <Outlet />
      )}
    </>
  );
};

export default ProtectedAuthGuard;
