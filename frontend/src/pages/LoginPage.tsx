import { useState, type FormEvent } from 'react';
import { Link } from 'react-router-dom';

function EyeIcon() {
  return (
    <svg viewBox="0 0 24 24" fill="none" aria-hidden="true" className="h-5 w-5">
      <path
        d="M2 12s3.5-6 10-6 10 6 10 6-3.5 6-10 6S2 12 2 12Z"
        stroke="currentColor"
        strokeWidth="1.8"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
      <circle cx="12" cy="12" r="2.8" stroke="currentColor" strokeWidth="1.8" />
    </svg>
  );
}

function EyeOffIcon() {
  return (
    <svg viewBox="0 0 24 24" fill="none" aria-hidden="true" className="h-5 w-5">
      <path
        d="M3 3l18 18"
        stroke="currentColor"
        strokeWidth="1.8"
        strokeLinecap="round"
      />
      <path
        d="M10.6 5.4A10.7 10.7 0 0 1 12 5c6.5 0 10 7 10 7a18.7 18.7 0 0 1-3.2 4.3"
        stroke="currentColor"
        strokeWidth="1.8"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
      <path
        d="M6.1 7.3C3.3 9.4 2 12 2 12s3.5 7 10 7c1 0 2-.2 3-.5"
        stroke="currentColor"
        strokeWidth="1.8"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
      <path
        d="M9.4 10.4A2.8 2.8 0 0 0 12 15"
        stroke="currentColor"
        strokeWidth="1.8"
        strokeLinecap="round"
      />
    </svg>
  );
}

export function LoginPage() {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [senhaVisivel, setSenhaVisivel] = useState(false);

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
  }

  return (
    <section className="flex min-h-[calc(100vh-10rem)] items-center justify-center py-6">
      <div className="w-full max-w-xl rounded-[2rem] border border-slate-200 bg-white p-6 shadow-soft sm:p-8">
        <div className="mb-6">
          <p className="text-sm font-medium text-indigo-700">Login</p>
          <h1 className="mt-1 text-2xl font-semibold text-slate-900">Acesso ao painel</h1>
          <p className="mt-3 text-sm leading-6 text-slate-600">
            Preencha os campos abaixo apenas para fins visuais.
          </p>
        </div>

        <form className="space-y-5" onSubmit={handleSubmit}>
          <label className="block">
            <span className="mb-2 block text-sm font-medium text-slate-700">Email</span>
            <input
              type="text"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              autoComplete="email"
              placeholder="seuemail@exemplo.com"
              className="w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
            />
          </label>

          <label className="block">
            <span className="mb-2 block text-sm font-medium text-slate-700">Senha</span>
            <div className="relative">
              <input
                type={senhaVisivel ? 'text' : 'password'}
                value={senha}
                onChange={(event) => setSenha(event.target.value)}
                autoComplete="current-password"
                placeholder="Digite sua senha"
                className="w-full rounded-2xl border border-slate-300 bg-white px-4 py-3 pr-12 text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
              />
              <button
                type="button"
                onClick={() => setSenhaVisivel((current) => !current)}
                className="absolute inset-y-0 right-0 flex items-center justify-center px-4 text-slate-500 transition hover:text-indigo-700"
                aria-label={senhaVisivel ? 'Ocultar senha' : 'Mostrar senha'}
              >
                {senhaVisivel ? <EyeOffIcon /> : <EyeIcon />}
              </button>
            </div>
          </label>

          <button
            type="submit"
            className="inline-flex w-full items-center justify-center rounded-full bg-indigo-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-indigo-700"
          >
            Entrar
          </button>

          <p className="text-center text-xs leading-5 text-slate-500">
            Tela visual sem autenticação real.
          </p>
        </form>

        <div className="mt-8 flex justify-center">
          <Link
            to="/pautas"
            className="inline-flex items-center justify-center rounded-full border border-slate-300 px-5 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
          >
            Voltar ao sistema
          </Link>
        </div>
      </div>
    </section>
  );
}
