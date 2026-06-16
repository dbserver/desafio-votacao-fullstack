import { BrowserRouter, Route, Routes, Link, useLocation } from 'react-router-dom';
import type { ReactNode } from 'react';
import { DetalhePautaPage } from './pages/DetalhePautaPage';
import { LoginPage } from './pages/LoginPage';
import { PautasPage } from './pages/PautasPage';

function NotFoundPage() {
  return (
    <div className="rounded-3xl border border-slate-200 bg-white p-8 text-center shadow-soft">
      <p className="text-sm font-medium text-indigo-700">Página não encontrada</p>
      <h1 className="mt-3 text-2xl font-semibold text-slate-900">O caminho solicitado não existe.</h1>
      <Link
        to="/pautas"
        className="mt-6 inline-flex items-center justify-center rounded-full bg-indigo-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-indigo-700"
      >
        Voltar para pautas
      </Link>
    </div>
  );
}

function Shell({ children }: { children: ReactNode }) {
  const location = useLocation();
  const isLoginPage = location.pathname === '/' || location.pathname === '/login';

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <header className="border-b border-slate-200 bg-white/90 backdrop-blur">
        <div className="mx-auto flex max-w-6xl items-center justify-between gap-4 px-4 py-4 sm:px-6 lg:px-8">
          <Link to="/pautas" className="flex items-center gap-3">
            <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-indigo-600 text-sm font-bold text-white shadow-soft">
              VC
            </div>
            <div>
              <p className="text-sm font-semibold uppercase tracking-[0.18em] text-indigo-700">
                Votação cooperativa
              </p>
              <p className="text-xs text-slate-500">Pautas, sessões e resultados</p>
            </div>
          </Link>

          {!isLoginPage ? (
            <Link
              to="/login"
              className="inline-flex items-center justify-center rounded-full border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
            >
              Login
            </Link>
          ) : null}
        </div>
      </header>

      <main className="mx-auto max-w-6xl px-4 py-8 sm:px-6 lg:px-8">{children}</main>
    </div>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <Shell>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/pautas" element={<PautasPage />} />
          <Route path="/pautas/:pautaId" element={<DetalhePautaPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Shell>
    </BrowserRouter>
  );
}
