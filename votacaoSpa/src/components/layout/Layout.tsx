import { Outlet } from 'react-router-dom'
import './Layout.css'

function Layout() {
  return (
    <div className="layout-shell">
      <header className="app-header">
        <div className="brand-block">
          <p className="brand-kicker">Sistema de Votacao</p>
          <h1 className="brand-title">Painel de Votacao</h1>
        </div>
      </header>

      <main className="app-main">
        <section className="content-container">
          <Outlet />
        </section>
      </main>
    </div>
  )
}

export default Layout
