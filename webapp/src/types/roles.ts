export const UserRoles = {
  admin: 'ADMINISTRATOR',
  user: 'USER',
  moderator: 'MODERATOR',
} as const;

export type UserRole = 'ADMINISTRATOR' | 'MODERATOR' | 'USER' | 'VOTER';
