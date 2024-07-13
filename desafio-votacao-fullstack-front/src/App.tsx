import Home from './pages/Home';
import Agenda from './pages/Agenda';
import Session from './pages/Session';
import {BrowserRouter, Routes, Link, Route} from 'react-router-dom'

function App() {
    return (
        <div className='App'>
            <h1>Votação de Pautas</h1>
            <BrowserRouter>
                <ul>
                    <li><Link to="/">Página Inicial</Link></li>
                    <li><Link to="/pautas">Pauta</Link></li>
                    <li><Link to="/sessoes">Sessão</Link></li>
                </ul>

                <Routes>
                    <Route path="/" index element={<Home />}></Route>
                    <Route path="/pautas" element={<Agenda />}></Route>
                    <Route path="/sessoes" element={<Session />}></Route>
                </Routes>
            </BrowserRouter>
        </div>
    )
}

export default App;