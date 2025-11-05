import type { Metadata } from 'next';
import { Poppins } from 'next/font/google';
import '../styles/globals.css';
import ProviderQueryClient from '../providers/queryClientProvider';

const poppins = Poppins({
  variable: '--font-poppins',
  subsets: ['latin'],
  weight: ['400', '600', '700'],
});

export const metadata: Metadata = {
  title: 'Sessão Digital',
  description: 'Crie pautas, abra sessões e contabilize seus votos',
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="pt">
      <body className={`${poppins.variable} antialiased`}>
        <ProviderQueryClient>{children}</ProviderQueryClient>
      </body>
    </html>
  );
}
