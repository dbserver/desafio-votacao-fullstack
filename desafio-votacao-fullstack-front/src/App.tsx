import Home from './pages/Home';
import Agenda from './pages/Agenda';
import Session from './pages/Session';
import AssociateTable from './pages/Associate/AssociateTable'
import {BrowserRouter, Routes, Link, Route} from 'react-router-dom'
import { Nav, Navbar, Container } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
    return (
        <div className='App'>
            <h1>Votação de Pautas</h1>
            <BrowserRouter>
                <Navbar bg="dark" data-bs-theme="dark">
                    <Container>
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/pautas">Pauta</Nav.Link>
                        <Nav.Link as={Link} to="/associates">Associados</Nav.Link>
                    </Nav>
                    </Container>
                </Navbar>

                <Routes>
                    <Route path="/pautas" index element={<Agenda />}></Route>
                    <Route path="/sessoes" element={<Session />}></Route>
                    <Route path="/associates" element={<AssociateTable />}></Route>
                </Routes>
            </BrowserRouter>
        </div>
    )
}

export default App;