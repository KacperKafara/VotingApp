import { jwtDecode } from 'jwt-decode';

interface JwtPayload {
  iss: string;
  sub: string;
  exp: number;
  iat: number;
  authorities: string[];
}

export const decodeJwt = (token: string) => jwtDecode<JwtPayload>(token);
export const isTokenValid = (token: string): boolean => {
  const payload = decodeJwt(token);
  return Date.now() < payload.exp * 1000;
}