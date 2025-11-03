import { useState } from "react";
import api from "../api/api";
import { showSuccess, showError } from "../utils/toast";

export default function FormSessao() {
  const [idPauta, setIdPauta] = useState("");
  const [inicioSessao, setInicioSessao] = useState("");
  const [fimSessao, setFimSessao] = useState("");

  const abrirSessao = async () => {
    try {
      await api.post("/sessao", { idPauta, inicioSessao, fimSessao  });
      showSuccess("Sessão aberta!");
      setIdPauta("");
      setInicioSessao("");
      setFimSessao("");
    } catch {
      showError("Erro ao abrir sessão.");
    }
  };

  return (
    <div>
      <h2>Abrir Sessão</h2>
      <input placeholder="ID da Pauta" value={idPauta} onChange={(e) => setIdPauta(e.target.value)} />
      <input type="datetime-local" placeholder="Início" value={inicioSessao} onChange={(e) => setInicioSessao(e.target.value)} />
      <input type="datetime-local" placeholder="Fim" value={fimSessao} onChange={(e) => setFimSessao(e.target.value)} />
      <button onClick={abrirSessao}>Abrir</button>
    </div>
  );
}
