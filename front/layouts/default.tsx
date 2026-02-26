import { ToastProvider } from "@heroui/toast";

import { Head } from "./head";

import Header from "@/components/Layout/header";

export default function DefaultLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <>
      <ToastProvider />
      <div className="relative flex flex-col h-screen">
        <Head />
        <main className="container mx-auto max-w-7xl">
          <Header />

          {children}
        </main>
      </div>
    </>
  );
}
