import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import { Toaster } from './components/ui/toaster.tsx'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ErrorBoundary } from 'react-error-boundary'
import { ThemeProvider } from './components/theme-provider.tsx'
import { ProtectedRoutes, UnprotectedRoutes } from './routes.ts'
import NotFoundPage from './pages/NotFoundPage/index.tsx'
import AuthGuard from './AuthGuard.tsx'

const router = createBrowserRouter([
  ...UnprotectedRoutes,
  {
    path: "*",
    Component: NotFoundPage,
  },
  {
    path: "/",
    Component: AuthGuard,
    children: ProtectedRoutes,
  }
]);

const queryClient = new QueryClient()

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ErrorBoundary fallback={<div>Error</div>}>
      <ThemeProvider defaultTheme='light' storageKey='vite-ui-theme'>
        <QueryClientProvider client={queryClient}>
          <RouterProvider router={router} />
          <Toaster />
        </QueryClientProvider>
      </ThemeProvider>
    </ErrorBoundary>
  </StrictMode>,
)
