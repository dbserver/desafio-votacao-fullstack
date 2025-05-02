import { FiFileText, FiUserPlus } from "react-icons/fi";
import { NavLink, Outlet } from "react-router-dom";
import { GiChoice } from "react-icons/gi";

export const Layout = () => {
  return (
    <div className="grid min-h-screen grid-cols-[240px_1fr] ">
      <aside className="bg-blue-700 text-white">
        <div className="p-4">
          <h1 className="text-xl font-bold mb-8">Sistema de Votação</h1>
          <nav className="space-y-2">
            <NavLink to="/criar-pauta" className={({ isActive }) => `flex items-center p-3 rounded-lg transition-colors ${isActive ? "bg-blue-800" : "hover:bg-blue-600"}`}>
              <FiFileText className="mr-5" size={30} />
              <span>Criar Pauta</span>
            </NavLink>
            <NavLink to="/criar-associado" className={({ isActive }) => `flex items-center p-3 rounded-lg transition-colors ${isActive ? "bg-blue-800" : "hover:bg-blue-600"}`}>
              <FiUserPlus className="mr-5" size={30} />
              <span>Criar Associado</span>
            </NavLink>
            <NavLink to="/List-pautas" className={({ isActive }) => `flex items-center p-3 rounded-lg transition-colors ${isActive ? "bg-blue-800" : "hover:bg-blue-600"}`}>
              <GiChoice className="mr-5" size={30} />
              <span>Votação</span>
            </NavLink>
          </nav>
        </div>
      </aside>

      <div className="">
        <header className="bg-gray-500 shadow p-4">
          <div className="flex justify-between items-center">
            <h2 className="text-md font-semibold text-white">Dashboard</h2>
            <div className="w-10 h-10 rounded-full bg-gray-300"></div>
          </div>
        </header>

        <main className="p-4  w-full ">
          <Outlet />
        </main>
      </div>
    </div>
  );
};
