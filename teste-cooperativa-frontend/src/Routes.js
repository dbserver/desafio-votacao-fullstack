import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login.js';
import Menu from './pages/Menu.js';
import AssociateForm from './pages/AssociateForm.js';
import AgendaForm from './pages/AgendaForm.js';
import Agendas from './pages/Agendas.js';
import SessionForm from './pages/SessionForm.js';
import Sessions from './pages/Sessions.js';
import Vote from './pages/Vote.js';

function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/createAssociate" element={<AssociateForm />} />
        <Route path="/createSession" element={<SessionForm />} />
        <Route path="/createAgenda" element={<AgendaForm />} />
        <Route path="/agendas" element={<Agendas />} />
        <Route path="/sessions" element={<Sessions />} />
        <Route path="/vote" element={<Vote />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;
