import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { Layout } from "./pages/Layout";
import { CriarPauta } from "./pages/Pauta/criar_pauta";
import { CriarAssociado } from "./pages/Associado/criar_associado";
import { Index } from "./pages/Pauta";
const queryClient = new QueryClient();

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      {
        index: true,
        element: <div className="bg-white p-6 rounded-lg shadow">Dashboard </div>,
      },
      {
        path: "criar-pauta",
        element: <CriarPauta />,
      },
      {
        path: "criar-associado",
        element: <CriarAssociado />,
      },
      {
        path: "List-pautas",
        element: <Index />,
      },
    ],
  },
]);

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  </StrictMode>
);
