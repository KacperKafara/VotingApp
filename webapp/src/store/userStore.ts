import { decodeJwt } from "@/utils/jwt";
import { create } from "zustand";

interface UserStore {
  token?: string;
  id?: string;
  roles?: string[];
  activeRole?: string;
  setActiveRole: (role: string) => void;
  setToken: (token: string) => void;
  clearToken: () => void;
}

export type Role = "administrator" | "moderator" | "user";

export const roleMapping: Record<string, string> = {
  ADMINISTRATOR: "admin",
  MODERATOR: "moderator",
  USER: "user",
};

export const rolePriority: Record<string, number> = {
  ADMINISTRATOR: 0,
  MODERATOR: 2,
  USER: 3,
};

const getActiveRole = (roles: string[]): string =>
  roles.sort((a, b) => rolePriority[a] - rolePriority[b]).at(0)!;

const LSToken = localStorage.getItem("token");
const LSActiveRole = localStorage.getItem("activeRole");

const decodedLSToken = LSToken === null ? undefined : decodeJwt(LSToken!);

export const useUserStore = create<UserStore>((set) => ({
  token: LSToken === null ? undefined : LSToken,
  id: LSToken === null ? undefined : decodedLSToken!.sub,
  roles: LSToken === null ? undefined : decodedLSToken!.authorities,
  activeRole: LSActiveRole === null ? undefined : LSActiveRole,
  setToken: (token: string) =>
    set(() => {
      const payload = decodeJwt(token);
      localStorage.setItem("token", token);
      localStorage.setItem("activeRole", getActiveRole(payload.authorities));
      return {
        token,
        id: payload.sub,
        roles: payload.authorities,
        activeRole: getActiveRole(payload.authorities),
      };
    }),
  clearToken: () =>
    set(() => {
      localStorage.removeItem("token");
      return {
        token: undefined,
        id: undefined,
        roles: undefined,
        activeRole: undefined,
      };
    }),
  setActiveRole: (role: string) =>
    set(() => {
      localStorage.setItem("activeRole", role);
      return {
        activeRole: role,
      };
    }),
}));