import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <nav className="bg-cyan-100 shadow-sm relative min-h-16 md:min-h-18">
      <div className="absolute left-0 top-0 h-full hidden lg:flex items-center px-4">
        <Link to="/" className="flex items-center gap-3 text-gray-900 group relative">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth={2}
            stroke="currentColor"
            className="w-6 h-6"
            aria-hidden="true"
          >
            <path strokeLinecap="round" strokeLinejoin="round" d="M3 10.5L12 4l9 6.5v7A2.5 2.5 0 0118.5 20h-13A2.5 2.5 0 013 17.5v-7z" />
            <path strokeLinecap="round" strokeLinejoin="round" d="M9 21V12h6v9" />
          </svg>
          <span className="text-xl sm:text-2xl font-semibold">Votação</span>
          <span className="absolute left-full ml-2 top-1/2 -translate-y-1/2 whitespace-nowrap bg-gray-800 text-white text-sm px-2 py-1 rounded opacity-0 group-hover:opacity-100 pointer-events-none transition-opacity">
            Voltar para Home
          </span>
        </Link>
      </div>

      <div className="max-w-7xl pt-4 mx-auto px-4 sm:px-6 md:px-8 h-full flex items-center justify-start">
        <Link to="/" className="flex items-center gap-3 text-gray-900 lg:hidden group relative">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth={2}
            stroke="currentColor"
            className="w-6 h-6"
            aria-hidden="true"
          >
            <path strokeLinecap="round" strokeLinejoin="round" d="M3 10.5L12 4l9 6.5v7A2.5 2.5 0 0118.5 20h-13A2.5 2.5 0 013 17.5v-7z" />
            <path strokeLinecap="round" strokeLinejoin="round" d="M9 21V12h6v9" />
          </svg>
          <span className="text-xl sm:text-2xl font-semibold">Votação</span>
          <span className="absolute left-full ml-2 top-1/2 -translate-y-1/2 whitespace-nowrap bg-gray-800 text-white text-sm px-2 py-1 rounded opacity-0 group-hover:opacity-100 pointer-events-none transition-opacity">
            Voltar para Home
          </span>
        </Link>
      </div>
    </nav>
  );
}
