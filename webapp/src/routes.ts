import { RouteObject } from 'react-router-dom';
import loadable from '@loadable/component';

const BaseLayout = loadable(() => import('./layouts/BaseLayout'));

const RegisterPage = loadable(() => import('./pages/Register'));
const ResetPasswordPage = loadable(() => import('./pages/ResetPassword'));
const MainPage = loadable(() => import('./pages/Main'));

const UsersPage = loadable(() => import('./pages/@Admin/users'));
const UserPage = loadable(() => import('./pages/@Admin/user'));

const ModeratorSurveysListPage = loadable(
  () => import('./pages/@Moderator/surveys')
);
const ModeratorSurveyPage = loadable(() => import('./pages/@Moderator/survey'));
const ModeratorVotingListPage = loadable(
  () => import('./pages/@Moderator/votingList')
);

const UserSurveysListPage = loadable(() => import('./pages/@User/surveys'));
const UserSurveyPage = loadable(() => import('./pages/@User/survey'));
const UserVotingPage = loadable(() => import('./pages/@User/voting'));
const UserVotingListPage = loadable(() => import('./pages/@User/votingList'));
const UserVotingAndSurveysYouCanVotePage = loadable(
  () => import('./pages/@User/votingAndSurveysYouCanVote')
);

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
  { index: true, Component: MainPage },
  { path: 'users', Component: UsersPage },
  { path: 'users/:username', Component: UserPage },
];
const ModeratorRoutes: RouteObject[] = [
  { index: true, Component: MainPage },
  { path: 'surveys', Component: ModeratorSurveysListPage },
  { path: 'surveys/:id', Component: ModeratorSurveyPage },
  { path: 'votings', Component: ModeratorVotingListPage },
];
const UserRoutes: RouteObject[] = [
  { index: true, Component: MainPage },
  { path: 'surveys', Component: UserSurveysListPage },
  { path: 'surveys/:id', Component: UserSurveyPage },
  { path: 'votings', Component: UserVotingListPage },
  { path: 'votings/:id', Component: UserVotingPage },
  {
    path: 'active-surveys-and-votings',
    Component: UserVotingAndSurveysYouCanVotePage,
  },
];

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
