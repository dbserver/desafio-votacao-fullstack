import { useState } from "react";
import api from "../api/api";
import ResultadoCard from "../components/ResultadoCard";
import { showError } from "../utils/toast";

export default function ResultadoPage() {
  const [idSessao, setIdSessao] = useState("");
  const [resultado, setResultado] = useState(null);

  const buscarResultado = async () => {
    try {
      const res = await api.get(`/resultados/${idSessao}`);
      setResultado(res.data);
    } catch {
      showError("Erro ao buscar resultado.");
    }
  };

  return (
    <div>
      <h2>Consultar Resultado</h2>
      <input placeholder="ID da Sessão" value={idSessao} onChange={(e) => setIdSessao(e.target.value)} />
      <button onClick={buscarResultado}>Buscar</button>
      <ResultadoCard resultado={resultado} />
    </div>
  );
}
