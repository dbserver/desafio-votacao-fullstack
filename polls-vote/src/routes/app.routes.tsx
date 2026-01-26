import { Suspense, lazy } from "react";
import { useRoutes, Navigate } from "react-router-dom";
import { MainLayout } from "../layout/MainLayout";
import Home from "../pages/home";
import Poll from "../pages/poll";
import PollsList from "../pages/polls.tsx";
import {PrivateRoute} from "./private.route.tsx";

const Login = lazy(() => import('../pages/login'));
const Vote = lazy(() => import('../pages/vote'));

export function AppRoutes() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      {useRoutes([
        {
            element: <MainLayout />,
            children: [
                { path: "/login", element: <Login /> },
            ]
        },
        {
          element:(
              <PrivateRoute>
                  <MainLayout />
              </PrivateRoute>
          ),
          children: [
            { path: "/", element: <Home /> },
            { path: "/polls", element: <Poll /> },
            { path: "/vote", element: <PollsList /> },
            { path: "/vote/:id", element: <Vote />}
          ],
        },
        {
          path: "*",
          element: <div >Not Found</div>,
        },
      ])}
    </Suspense>
  );
}
