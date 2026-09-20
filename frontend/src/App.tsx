import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import type { PropsWithChildren } from "react";
import { Toaster } from "sonner";
import { Router } from "./router";

function App({children}: PropsWithChildren) {
  const queryClient = new QueryClient();

  return (
      <QueryClientProvider client={queryClient}>
        <Toaster richColors/>
        <Router/>
        {children}
      </QueryClientProvider>
  )
}

export default App
