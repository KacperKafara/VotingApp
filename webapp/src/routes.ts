import { RouteObject } from 'react-router-dom';
import loadable from '@loadable/component';

const BaseLayout = loadable(() => import('./layouts/BaseLayout'));

const RegisterPage = loadable(() => import('./pages/Register'));
const ResetPasswordPage = loadable(() => import('./pages/ResetPassword'));
const MainPage = loadable(() => import('./pages/Main'));
const VerifyAccountPage = loadable(
  () => import('./pages/Register/verifyAccount')
);
const OAuthRedirectPage = loadable(() => import('./pages/OAuthRedirect'));
const OAuthFillDataPage = loadable(() => import('./pages/OAuthFillData'));

const AdminUsersPage = loadable(() => import('./pages/@Admin/users'));
const AdminUserPage = loadable(() => import('./pages/@Admin/user'));
const AdminRoleRequestsPage = loadable(
  () => import('./pages/@Admin/roleRequests')
);

const ModeratorSurveysListPage = loadable(
  () => import('./pages/@Moderator/surveys')
);
const ModeratorSurveyPage = loadable(() => import('./pages/@Moderator/survey'));
const ModeratorVotingListPage = loadable(
  () => import('./pages/@Moderator/votingList')
);
const ModeratorVotingDetailsPage = loadable(
  () => import('./pages/@Moderator/votingDetails')
);

const UserSurveysListPage = loadable(() => import('./pages/@User/surveys'));
const UserSurveyPage = loadable(() => import('./pages/@User/survey'));
const UserVotingPage = loadable(() => import('./pages/@User/voting'));
const UserVotingListPage = loadable(() => import('./pages/@User/votingList'));
const UserVotingAndSurveysYouCanVotePage = loadable(
  () => import('./pages/@User/votingAndSurveysYouCanVote')
);
const UserVotingListEverActivePage = loadable(
  () => import('./pages/@User/wasActiveVotingList')
);

const ProfilePage = loadable(() => import('./pages/Profile'));

const SharedRoutes: RouteObject[] = [
  { path: '/profile', Component: ProfilePage },
];

const Routes: RouteObject[] = [
  { index: true, Component: MainPage },
  { path: '/register', Component: RegisterPage },
  { path: '/resetPassword/:token', Component: ResetPasswordPage },
  { path: '/verify/:token', Component: VerifyAccountPage },
  { path: '/login/oauth2/code/google', Component: OAuthRedirectPage },
  { path: '/oauth/fill-data', Component: OAuthFillDataPage },
];

const AdminRoutes: RouteObject[] = [
  { index: true, Component: MainPage },
  { path: 'users', Component: AdminUsersPage },
  { path: 'users/:username', Component: AdminUserPage },
  { path: 'role-requests', Component: AdminRoleRequestsPage },
];
const ModeratorRoutes: RouteObject[] = [
  { index: true, Component: MainPage },
  { path: 'surveys', Component: ModeratorSurveysListPage },
  { path: 'surveys/:id', Component: ModeratorSurveyPage },
  { path: 'votings', Component: ModeratorVotingListPage },
  { path: 'votings/:id', Component: ModeratorVotingDetailsPage },
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
  {
    path: 'ever-active-votings',
    Component: UserVotingListEverActivePage,
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
