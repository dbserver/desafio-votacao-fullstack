import { useState } from "react";
import api from "../api/api";
import { showSuccess, showError } from "../utils/toast";

export default function FormPauta() {
  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");
 
  const criarPauta = async () => {
    try {
      await api.post("/pauta", { titulo, descricao});
      showSuccess("Pauta criada com sucesso!");
      setTitulo("");
      setDescricao("");
    } catch {
      showError("Erro ao criar pauta.");
    }
  };

  return (
    <div>
      <h2>Criar Pauta</h2>
      <input placeholder="Título" value={titulo} onChange={(e) => setTitulo(e.target.value)} />
      <input placeholder="Descrição" value={descricao} onChange={(e) => setDescricao(e.target.value)} />
      <button onClick={criarPauta}>Criar</button>
    </div>
  );
}
