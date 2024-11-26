export const UserRoles = {
  admin: 'ADMINISTRATOR',
  user: 'USER',
  moderator: 'MODERATOR',
} as const;

export const UsetRolesWithPrefix = {
  admin: 'ROLE_ADMINISTRATOR',
  moderator: 'ROLE_MODERATOR',
  user: 'ROLE_USER',
  voter: 'ROLE_VOTER',
} as const;

export type UserRole = 'ADMINISTRATOR' | 'MODERATOR' | 'USER' | 'VOTER';
