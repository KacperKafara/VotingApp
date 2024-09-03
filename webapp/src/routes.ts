import { RouteObject } from 'react-router-dom';
import loadable from '@loadable/component'

const BaseLayout = loadable(() => import('./layouts/BaseLayout'));
const TestPage = loadable(() => import('./pages/TestPage'));
const RegisterPage = loadable(() => import('./pages/Register'));

const Routes: RouteObject[] = [
  { index: true, Component: TestPage },
  { path: "/register", Component: RegisterPage },
];

export const ProtectedRoutes: RouteObject[] = [
  {index: true, Component: TestPage},
];

export const UnprotectedRoutes: RouteObject[] = [
  { path: "/", Component: BaseLayout, children: Routes },
];