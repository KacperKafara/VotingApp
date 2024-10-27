import { RouteObject } from 'react-router-dom';
import loadable from '@loadable/component';

const BaseLayout = loadable(() => import('./layouts/BaseLayout'));

const RegisterPage = loadable(() => import('./pages/Register'));
const ResetPasswordPage = loadable(() => import('./pages/ResetPassword'));
const MainPage = loadable(() => import('./pages/Main'));

const AdminTestPage = loadable(() => import('./pages/@Admin/test'));
const UsersPage = loadable(() => import('./pages/@Admin/users'));
const UserPage = loadable(() => import('./pages/@Admin/user'));

const ModeratorTestPage = loadable(() => import('./pages/@Moderator/test'));

const UserTestPage = loadable(() => import('./pages/@User/test'));

const ProfilePage = loadable(() => import('./pages/Profile'));

const SharedRoutes: RouteObject[] = [
  { path: '/profile', Component: ProfilePage },
];

const Routes: RouteObject[] = [
  { index: true, Component: MainPage },
  { path: '/register', Component: RegisterPage },
  { path: '/resetPassword/:token', Component: ResetPasswordPage },
];

const AdminRoutes: RouteObject[] = [
  { index: true, Component: AdminTestPage },
  { path: 'users', Component: UsersPage },
  { path: 'users/:username', Component: UserPage },
];
const ModeratorRoutes: RouteObject[] = [
  { index: true, Component: ModeratorTestPage },
];
const UserRoutes: RouteObject[] = [{ index: true, Component: UserTestPage }];

export const AdminProtectedRoutes: RouteObject[] = [
  { path: '/admin', Component: BaseLayout, children: AdminRoutes },
];
export const ModeratorProtectedRoutes: RouteObject[] = [
  { path: '/moderator', Component: BaseLayout, children: ModeratorRoutes },
];
export const UserProtectedRoutes: RouteObject[] = [
  { path: '/user', Component: BaseLayout, children: UserRoutes },
];

export const ProtectedRoutes: RouteObject[] = [
  { path: '/', Component: BaseLayout, children: SharedRoutes },
];

export const UnprotectedRoutes: RouteObject[] = [
  { path: '/', Component: BaseLayout, children: Routes },
];
