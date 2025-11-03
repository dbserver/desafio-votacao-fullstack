import { useState } from "react";
import api from "../api/api";
import { showSuccess, showError } from "../utils/toast";

export default function FormVoto() {
  const [idSessao, setIdSessao] = useState("");
  const [escolha, setEscolha] = useState("SIM");

  const registrarVoto = async () => {
    try {
      await api.post("/votos", { idSessao, escolha });
      showSuccess("Voto registrado!");
      setIdSessao("");
      setEscolha("SIM");
    } catch {
      showError("Erro ao registrar voto.");
    }
  };

  return (
    <div>
      <h2>Registrar Voto</h2>
      <input placeholder="ID da Sessão" value={idSessao} onChange={(e) => setIdSessao(e.target.value)} />
      <select value={escolha} onChange={(e) => setEscolha(e.target.value)}>
        <option value="SIM">SIM</option>
        <option value="NAO">NÃO</option>
      </select>
      <button onClick={registrarVoto}>Votar</button>
    </div>
  );
}
