'use client';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React, { useState } from 'react';

type TProviderQueryClientProps = {
  children: React.ReactNode;
};

export default function ProviderQueryClient({
  children,
}: TProviderQueryClientProps) {
  const [queryClient] = useState(() => new QueryClient());

  return (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
}
