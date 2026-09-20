import { BrowserRouter, Route, Routes } from "react-router";
import { AssociadosPage } from "./pages/AssociadosPage";
import { HomePage } from "./pages/HomePage";
import { NaoEncontradaPage } from "./pages/NaoEncontradaPage";
import { PautaDetalhePage } from "./pages/PautaDetalhePage";

export const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/pauta/:pautaId" element={<PautaDetalhePage />} />
        <Route path="/associados" element={<AssociadosPage />} />
        <Route path="*" element={<NaoEncontradaPage />} />
      </Routes>
    </BrowserRouter>
  );
};
