import { useState } from "react";
import PautaPage from "./pages/PautaPage";
import SessaoPage from "./pages/SessaoPage";
import VotoPage from "./pages/VotoPage";
import ResultadoPage from "./pages/ResultadoPage";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function App() {
  const [tab, setTab] = useState("pauta");

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "top",
        backgroundColor: "#000",
        color: "#fff",
        textAlign: "center",
        padding: "1rem",
      }}
    >
      <div style={{ width: "100%", maxWidth: "800px" }}>
        <h1 style={{ marginBottom: "1.5rem" }}>Desafio Votação</h1>

        <div
          style={{
            display: "flex",
            justifyContent: "center",
            flexWrap: "wrap",
            gap: "0.5rem",
            marginBottom: "1.5rem",
          }}
        >
          <button
            onClick={() => setTab("pauta")}
            className={tab === "pauta" ? "active" : ""}
          >
            Criar Pauta
          </button>
          <button
            onClick={() => setTab("sessao")}
            className={tab === "sessao" ? "active" : ""}
          >
            Abrir Sessão
          </button>
          <button
            onClick={() => setTab("voto")}
            className={tab === "voto" ? "active" : ""}
          >
            Registrar Voto
          </button>
          <button
            onClick={() => setTab("resultado")}
            className={tab === "resultado" ? "active" : ""}
          >
            Consultar Resultado
          </button>
        </div>

        <div style={{ marginTop: "1rem", textAlign: "left" }}>
          {tab === "pauta" && <PautaPage />}
          {tab === "sessao" && <SessaoPage />}
          {tab === "voto" && <VotoPage />}
          {tab === "resultado" && <ResultadoPage />}
        </div>
      </div>
      <ToastContainer position="top-center" autoClose={2500} theme="dark" />
    </div>
  );
}
