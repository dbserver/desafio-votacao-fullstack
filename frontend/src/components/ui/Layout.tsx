import type { PropsWithChildren } from "react";
import { NavLink } from "react-router";
import "./Layout.css";

const navLinkClass = ({ isActive }: { isActive: boolean }) =>
  isActive ? "layout__link layout__link--active" : "layout__link";

export const Layout = ({ children }: PropsWithChildren) => {

  return (
    <div className="layout">
      <header className="layout__header">
        <div className="layout__container layout__bar">
          <NavLink to="/" className="layout__brand">
            Votação
          </NavLink>

          <nav className="layout__nav">
            <NavLink to="/" end className={navLinkClass}>
              Pautas
            </NavLink>
            <NavLink to="/associados" className={navLinkClass}>
              Associados
            </NavLink>
          </nav>
        </div>
      </header>

      <main className="layout__container layout__main">
        {children}
      </main>
    </div>
  )
}
