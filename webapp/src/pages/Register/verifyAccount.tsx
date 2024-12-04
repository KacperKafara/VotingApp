import { useVerifyAccount } from '@/data/useAuthenticate';
import { FC, useEffect, useRef } from 'react';
import { Navigate } from 'react-router-dom';

const VerifyAccount: FC = () => {
  const called = useRef(false);
  const { verifyAccount } = useVerifyAccount();

  useEffect(() => {
    if (called.current) return;
    called.current = true;
    verifyAccount();
  }, [verifyAccount]);

  return <Navigate to="/" replace={true} />;
};

export default VerifyAccount;
