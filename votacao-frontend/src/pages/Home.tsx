import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { listarPautas, criarPauta } from "../api/votacaoApi";

interface Pauta {
  id: number;
  titulo: string;
  descricao?: string;
}

export default function Home() {
  const [pautas, setPautas] = useState<Pauta[]>([]);
  const [modalAberto, setModalAberto] = useState(false);
  const [novoTitulo, setNovoTitulo] = useState("");
  const [novaDescricao, setNovaDescricao] = useState("");
  const [loading, setLoading] = useState(false);

  const carregarPautas = () => {
    listarPautas().then(res => setPautas(res.data));
  };

  useEffect(() => {
    carregarPautas();
  }, []);

  const handleCriarPauta = async () => {
    if (!novoTitulo) return;

    setLoading(true);
    try {
      await criarPauta({ titulo: novoTitulo, descricao: novaDescricao });
      setNovoTitulo("");
      setNovaDescricao("");
      setModalAberto(false);
      carregarPautas();
    } catch (err) {
      console.error(err);
      alert("Erro ao criar pauta");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-4 max-w-3xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Pautas Disponíveis</h1>
        <button
          onClick={() => setModalAberto(true)}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 cursor-pointer"
        >
          Nova Pauta
        </button>
      </div>

      {pautas.length === 0 ? (
        <p className="text-gray-500 text-center">Nenhuma pauta cadastrada ainda.</p>
      ) : (
        <ul className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {pautas.map(pauta => (
            <li key={pauta.id} className="p-4 border rounded shadow hover:shadow-md transition">
              <Link
                to={`/sessao/${pauta.id}`}
                className="text-blue-600 font-semibold text-lg"
              >
                {pauta.titulo}
              </Link>
              {pauta.descricao && <p className="text-gray-600 mt-1">{pauta.descricao}</p>}
            </li>
          ))}
        </ul>
      )}

      {/* Modal */}
      {modalAberto && (
        <div className="fixed inset-0 flex items-center justify-center bg-white bg-opacity-50 z-50">
          <div className="bg-white w-11/12 sm:w-96 p-6 rounded shadow-lg relative">
            <h2 className="text-xl font-semibold mb-4">Criar Nova Pauta</h2>
            <input
              type="text"
              placeholder="Título"
              value={novoTitulo}
              onChange={e => setNovoTitulo(e.target.value)}
              className="w-full mb-2 p-2 border rounded"
            />
            <textarea
              placeholder="Descrição (opcional)"
              value={novaDescricao}
              onChange={e => setNovaDescricao(e.target.value)}
              className="w-full mb-4 p-2 border rounded"
            />
            <div className="flex justify-end space-x-2">
              <button
                onClick={() => setModalAberto(false)}
                className="px-4 py-2 rounded border hover:bg-gray-100 cursor-pointer"
              >
                Cancelar
              </button>
              <button
                onClick={handleCriarPauta}
                disabled={loading}
                className="px-4 py-2 rounded bg-blue-600 text-white hover:bg-blue-700 disabled:opacity-50 
                cursor-pointer"
              >
                {loading ? "Criando..." : "Criar"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}