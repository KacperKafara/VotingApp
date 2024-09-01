import { RouteObject } from 'react-router-dom';
import BaseLayout from './layouts/BaseLayout';
import loadable from '@loadable/component'

const TestPage = loadable(() => import('./pages/TestPage'));

const Routes: RouteObject[] = [
  { index: true, Component: TestPage },
];

export const ProtectedRoutes: RouteObject[] = [];

export const UnprotectedRoutes: RouteObject[] = [
  { path: "/", Component: BaseLayout, children: Routes },
];