import Menu from '@/app/components/Menu';

export default function PagesLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      <Menu />
      <main className="p-10">{children}</main>
    </>
  );
}
