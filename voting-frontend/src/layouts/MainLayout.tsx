import type { ReactNode } from "react";
import { Link, useLocation } from "react-router-dom";

interface MainLayoutProps {
  children: ReactNode;
  hideNavigation?: boolean;
}

export function MainLayout({ children, hideNavigation }: MainLayoutProps) {
  const location = useLocation();

  const isActive = (path: string) => {
    return location.pathname === path || location.pathname.startsWith(path + "/");
  };

  const navLinks = [
    { path: "/pautas", label: "📋 Pautas", active: isActive("/pautas") },
    { path: "/pautas/nova", label: "✏️ Nova Pauta", active: isActive("/pautas/nova") },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-50 to-white">
      {/* Header */}
      <header className="bg-white shadow-md border-b-2 border-blue-100 sticky top-0 z-40">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex justify-between items-center">
            {/* Logo */}
            <Link to="/pautas" className="flex items-center gap-3 group">
              <div className="bg-gradient-to-br from-blue-600 to-indigo-600 text-white p-3 rounded-lg group-hover:shadow-lg transition">
                <span className="text-xl font-bold">🗳️</span>
              </div>
              <div>
                <h1 className="text-2xl font-bold text-gray-900 group-hover:text-blue-600 transition">
                  Votação
                </h1>
                <p className="text-xs text-gray-500">Sistema de Pautas</p>
              </div>
            </Link>

            {/* Navigation */}
            {!hideNavigation && (
              <nav className="flex items-center gap-8">
                {navLinks.map((link) => (
                  <Link
                    key={link.path}
                    to={link.path}
                    className={`
                      font-semibold transition-all relative group
                      ${link.active
                        ? "text-blue-600"
                        : "text-gray-600 hover:text-blue-600"
                      }
                    `}
                  >
                    {link.label}
                    {link.active && (
                      <div className="absolute bottom-0 left-0 right-0 h-1 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-full" />
                    )}
                  </Link>
                ))}
              </nav>
            )}

            {/* Info Badge */}
            <div className="flex items-center gap-3 bg-blue-50 px-4 py-2 rounded-lg border border-blue-200">
              <span className="text-lg">ℹ️</span>
              <span className="text-sm font-semibold text-blue-900">
                Versão 1.0
              </span>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-6 py-12">
        {children}
      </main>

      {/* Footer */}
      <footer className="bg-gray-900 text-gray-300 mt-20 border-t-4 border-blue-600">
        <div className="max-w-7xl mx-auto px-6 py-12">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8 mb-8">
            {/* Brand */}
            <div>
              <div className="flex items-center gap-2 mb-4">
                <span className="text-2xl">🗳️</span>
                <h3 className="text-xl font-bold text-white">Votação</h3>
              </div>
              <p className="text-sm text-gray-400">
                Sistema de votação e pautas
              </p>
            </div>

            {/* Quick Links */}
            <div>
              <h4 className="font-bold text-white mb-4">Links Rápidos</h4>
              <ul className="space-y-2 text-sm">
                <li>
                  <Link to="/pautas" className="text-gray-400 hover:text-white transition">
                    Todas as Pautas
                  </Link>
                </li>
                <li>
                  <Link to="/pautas/nova" className="text-gray-400 hover:text-white transition">
                    Criar Pauta
                  </Link>
                </li>
              </ul>
            </div>

            {/* Features */}
            <div>
              <h4 className="font-bold text-white mb-4">Recursos</h4>
              <ul className="space-y-2 text-sm">
                <li className="text-gray-400">✓ Votação Segura</li>
                <li className="text-gray-400">✓ Resultados em Tempo Real</li>
                <li className="text-gray-400">✓ Gerenciamento Fácil</li>
              </ul>
            </div>

            {/* Status */}
            <div>
              <h4 className="font-bold text-white mb-4">Status</h4>
              <div className="flex items-center gap-2 text-green-400 text-sm">
                <span className="inline-block w-2 h-2 bg-green-400 rounded-full" />
                Sistema Online
              </div>
              <p className="text-gray-400 text-sm mt-2">
                Última atualização: 22/03/2026
              </p>
            </div>
          </div>

          {/* Divider */}
          <div className="border-t border-gray-800 pt-8">
            <div className="flex justify-between items-center text-sm text-gray-400">
              <p>© 2026 Sistema de Votação. Todos os direitos reservados.</p>
              <div className="flex gap-4">
                <a href="#" className="hover:text-white transition">
                  Privacidade
                </a>
                <a href="#" className="hover:text-white transition">
                  Termos
                </a>
                <a href="#" className="hover:text-white transition">
                  Suporte
                </a>
              </div>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}