import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { resultadoSessao } from "../api/votacaoApi";

export default function Resultado() {
  const { id } = useParams<{ id: string }>();
  const [resultado, setResultado] = useState<any>(null);

  useEffect(() => {
    resultadoSessao(Number(id))
      .then(res => setResultado(res.data))
      .catch(err => console.error(err));
  }, [id]);

  if (!resultado) return <div className="p-4">Carregando resultados...</div>;

  return (
    <div className="p-4 max-w-md mx-auto">
      <h1 className="text-2xl font-bold mb-4">Resultado da Sessão</h1>

      

      <div className="grid grid-cols-2 gap-4 mb-4">
        <div className="p-4 border rounded text-center">
          <p className="font-bold">Votos SIM</p>
          <p>{resultado.votosSim}</p>
        </div>
        <div className="p-4 border rounded text-center">
          <p className="font-bold">Votos NÃO</p>
          <p>{resultado.votosNao}</p>
        </div>
      </div>

      <div className="p-4 border rounded text-center mb-4">
        <p className="font-bold">Total de votos</p>
        <p>{resultado.total}</p>
      </div>

      <div className="p-4 border rounded text-center bg-yellow-100">
        <p className="font-bold">Resultado final</p>
        <p className="text-lg">{resultado.resultado}</p>
      </div>
    </div>
  );
}