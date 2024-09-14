import { RouteObject } from "react-router-dom";
import loadable from "@loadable/component";

const BaseLayout = loadable(() => import("./layouts/BaseLayout"));

const RegisterPage = loadable(() => import("./pages/Register"));
const ResetPasswordPage = loadable(() => import("./pages/ResetPassword"));
const MainPage = loadable(() => import("./pages/Main"));

const AdminTestPage = loadable(() => import("./pages/@Admin/test"));

const ModeratorTestPage = loadable(() => import("./pages/@Moderator/test"));

const UserTestPage = loadable(() => import("./pages/@User/test"));

const Routes: RouteObject[] = [
  { index: true, Component: MainPage },
  { path: "/register", Component: RegisterPage },
  { path: "/resetPassword/:token", Component: ResetPasswordPage },
];

const AdminRoutes: RouteObject[] = [{ index: true, Component: AdminTestPage }];
const ModeratorRoutes: RouteObject[] = [
  { index: true, Component: ModeratorTestPage },
];
const UserRoutes: RouteObject[] = [{ index: true, Component: UserTestPage }];

export const AdminProtectedRoutes: RouteObject[] = [
  { path: "/admin", Component: BaseLayout, children: AdminRoutes },
];
export const ModeratorProtectedRoutes: RouteObject[] = [
  { path: "/moderator", Component: BaseLayout, children: ModeratorRoutes },
];
export const UserProtectedRoutes: RouteObject[] = [
  { path: "/user", Component: BaseLayout, children: UserRoutes },
];

export const UnprotectedRoutes: RouteObject[] = [
  { path: "/", Component: BaseLayout, children: Routes },
];
