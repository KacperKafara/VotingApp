import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./index.css";
import { Toaster } from "./components/ui/toaster.tsx";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ErrorBoundary } from "react-error-boundary";
import { ThemeProvider } from "./components/theme-provider.tsx";
import {
  AdminProtectedRoutes,
  ModeratorProtectedRoutes,
  UnprotectedRoutes,
  UserProtectedRoutes,
} from "./routes.ts";
import NotFoundPage from "./pages/NotFoundPage/index.tsx";
import ProtectedAuthGuard from "./utils/RouteGuards/ProtectedAuthGuard.tsx";
import "@/i18n";
import UnprotectedAuthGuard from "./utils/RouteGuards/UnprotectedAuthGuard.tsx";
import { UserRoles } from "./types/roles.ts";

const router = createBrowserRouter([
  {
    path: "*",
    Component: NotFoundPage,
  },
  {
    Component: UnprotectedAuthGuard,
    children: UnprotectedRoutes,
  },
  {
    element: <ProtectedAuthGuard role={`ROLE_${UserRoles.moderator}`} />,
    children: ModeratorProtectedRoutes,
  },
  {
    element: <ProtectedAuthGuard role={`ROLE_${UserRoles.admin}`} />,
    children: AdminProtectedRoutes,
  },
  {
    element: <ProtectedAuthGuard role={`ROLE_${UserRoles.user}`} />,
    children: UserProtectedRoutes,
  },
]);

const queryClient = new QueryClient();

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ErrorBoundary fallback={<div>Error</div>}>
      <ThemeProvider defaultTheme="light" storageKey="vite-ui-theme">
        <QueryClientProvider client={queryClient}>
          <RouterProvider router={router} />
          <Toaster />
        </QueryClientProvider>
      </ThemeProvider>
    </ErrorBoundary>
  </StrictMode>
);
