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
                    <Navbar.Brand as={Link} to="/">Navbar</Navbar.Brand>
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/">Página Inicial</Nav.Link>
                        <Nav.Link as={Link} to="/pautas">Pauta</Nav.Link>
                        <Nav.Link as={Link} to="/associates">Associados</Nav.Link>
                    </Nav>
                    </Container>
                </Navbar>

                <Routes>
                    <Route path="/" index element={<Home />}></Route>
                    <Route path="/pautas" element={<Agenda />}></Route>
                    <Route path="/associates" element={<AssociateTable />}></Route>
                </Routes>
            </BrowserRouter>
        </div>
    )
}

export default App;