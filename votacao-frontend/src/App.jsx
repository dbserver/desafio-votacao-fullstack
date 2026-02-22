import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Sessao from "./pages/Sessao";
import Resultado from "./pages/Resultado";
import { ToastContainer } from "react-toastify";
import Navbar from "./components/Navbar";

export default function App() {
  return (
    <Router>
      <ToastContainer />
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/sessao/:id" element={<Sessao />} />
        <Route path="/resultado/:id" element={<Resultado />} />
      </Routes>
    </Router>
  );
}