import { decodeJwt } from '@/utils/jwt';
import { create } from 'zustand';

interface UserStore {
  token?: string;
  refreshToken?: string;
  id?: string;
  roles?: string[];
  useOAuth?: boolean;
  setToken: (token: string, useOAuth: boolean | undefined) => void;
  setRefreshToken: (token: string) => void;
  logOut: () => void;
}

export const roleMapping: Record<string, string> = {
  ROLE_ADMINISTRATOR: 'admin',
  ROLE_MODERATOR: 'moderator',
  ROLE_USER: 'user',
};

export const rolePriority: Record<string, number> = {
  ROLE_ADMINISTRATOR: 0,
  ROLE_MODERATOR: 2,
  ROLE_USER: 3,
  ROLE_VOTER: 4,
};

export const getActiveRole = (roles: string[]): string =>
  roles.sort((a, b) => rolePriority[a] - rolePriority[b]).at(0)!;

const LSToken = localStorage.getItem('token');
const LSRefreshToken = localStorage.getItem('refreshToken');

const decodedLSToken = LSToken === null ? undefined : decodeJwt(LSToken!);

export const useUserStore = create<UserStore>((set) => ({
  token: LSToken === null ? undefined : LSToken,
  refreshToken: LSRefreshToken === null ? undefined : LSRefreshToken,
  id: LSToken === null ? undefined : decodedLSToken!.sub,
  roles: LSToken === null ? undefined : decodedLSToken!.authorities,
  useOAuth: undefined,
  setToken: (token: string, useOAuth?: boolean) =>
    set(() => {
      const payload = decodeJwt(token);
      localStorage.setItem('token', token);
      return {
        token,
        id: payload.sub,
        roles: payload.authorities,
        useOAuth: useOAuth,
      };
    }),
  setRefreshToken: (token: string) =>
    set(() => {
      localStorage.setItem('refreshToken', token);
      return {
        refreshToken: token,
      };
    }),
  logOut: () =>
    set(() => {
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      return {
        token: undefined,
        refreshToken: undefined,
        id: undefined,
        roles: undefined,
        useOAuth: undefined,
      };
    }),
}));
