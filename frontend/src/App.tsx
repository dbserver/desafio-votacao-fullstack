import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import type { PropsWithChildren } from "react";
import { Toaster } from "sonner";
import { Router } from "./router";

const queryClient = new QueryClient();

function App({children}: PropsWithChildren) {

  return (
      <QueryClientProvider client={queryClient}>
        <Toaster richColors/>
        <Router/>
        {children}
      </QueryClientProvider>
  )
}

export default App
