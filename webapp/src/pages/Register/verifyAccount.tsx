import { useVerifyAccount } from '@/data/useAuthenticate';
import { FC, useEffect, useRef } from 'react';
import { Navigate, useParams } from 'react-router-dom';

const VerifyAccount: FC = () => {
  const called = useRef(false);
  const { token } = useParams<{ token: string }>();
  const { verifyAccount } = useVerifyAccount();

  useEffect(() => {
    if (called.current) return;
    const fetchData = async () => {
      if (token) {
        await verifyAccount(token);
      }
    };
    fetchData();
  }, [token, verifyAccount]);

  return <Navigate to="/" replace={true} />;
};

export default VerifyAccount;
