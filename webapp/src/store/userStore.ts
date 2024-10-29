import { decodeJwt } from '@/utils/jwt';
import { create } from 'zustand';

interface UserStore {
  token?: string;
  id?: string;
  roles?: string[];
  activeRole?: string;
  setActiveRole: (role: string) => void;
  setToken: (token: string) => void;
  clearToken: () => void;
  clearActiveRole: () => void;
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

const getActiveRole = (roles: string[]): string =>
  roles.sort((a, b) => rolePriority[a] - rolePriority[b]).at(0)!;

const LSToken = localStorage.getItem('token');
const LSActiveRole = localStorage.getItem('activeRole');

const decodedLSToken = LSToken === null ? undefined : decodeJwt(LSToken!);

export const useUserStore = create<UserStore>((set) => ({
  token: LSToken === null ? undefined : LSToken,
  id: LSToken === null ? undefined : decodedLSToken!.sub,
  roles: LSToken === null ? undefined : decodedLSToken!.authorities,
  activeRole: LSActiveRole === null ? undefined : LSActiveRole,
  setToken: (token: string) =>
    set(() => {
      const payload = decodeJwt(token);
      localStorage.setItem('token', token);
      localStorage.setItem('activeRole', getActiveRole(payload.authorities));
      return {
        token,
        id: payload.sub,
        roles: payload.authorities,
        activeRole: getActiveRole(payload.authorities),
      };
    }),
  clearToken: () =>
    set(() => {
      localStorage.removeItem('token');
      return {
        token: undefined,
        id: undefined,
        roles: undefined,
        activeRole: undefined,
      };
    }),
  setActiveRole: (role: string) =>
    set(() => {
      localStorage.setItem('activeRole', role);
      return {
        activeRole: role,
      };
    }),
  clearActiveRole: () =>
    set(() => {
      localStorage.removeItem('activeRole');
      return {
        activeRole: undefined,
      };
    }),
  logOut: () =>
    set(() => {
      localStorage.removeItem('token');
      localStorage.removeItem('activeRole');
      return {
        token: undefined,
        id: undefined,
        roles: undefined,
        activeRole: undefined,
      };
    }),
}));
