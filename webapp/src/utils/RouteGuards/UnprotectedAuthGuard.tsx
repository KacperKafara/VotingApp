import { FC } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import {
  getActiveRole,
  roleMapping,
  useUserStore,
} from '../../store/userStore';

const UnprotectedAuthGuard: FC = () => {
  const { token, roles } = useUserStore();

  const isLoggedIn = token !== undefined;

  return (
    <>
      {isLoggedIn ? (
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

export default UnprotectedAuthGuard;
