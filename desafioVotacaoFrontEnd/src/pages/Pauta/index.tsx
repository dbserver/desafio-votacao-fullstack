import { useEffect, useState } from "react";
import { Datapauta } from "./hooks/data_fecht";
import { usePautaSessaoMutation } from "./hooks/data_postIniciarsessao";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { schemaVoto } from "./schema/schemaVoto";
import { useVotoMutation } from "./hooks/data_postVotacao";
import { useNavigate } from "react-router-dom";
import axios from "axios";

type CriarVotoFormData = z.infer<typeof schemaVoto>;
export const Index = () => {
  const { data, isLoading, isError } = Datapauta();
  const { mutate } = usePautaSessaoMutation();
  const { mutate: votoMutate } = useVotoMutation();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isModalVotacaoOpen, setIsModalVotacaoOpen] = useState(false);
  const [tempo, setTempo] = useState(1);
  const [idSelecionado, setIdSelecionado] = useState<number | null>(null);
  const [tempoRestante, setTempoRestante] = useState<{ [key: number]: number }>({});
  const [votosContabilizados, setVotosContabilizados] = useState<{ [key: number]: { SIM: number; NAO: number } }>({});

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CriarVotoFormData>({
    resolver: zodResolver(schemaVoto),
  });

  const navigate = useNavigate();

  const abrirModal = (id: number) => {
    setIdSelecionado(id);
    setIsModalOpen(true);
  };

  const abrirModalVotacao = (id: number) => {
    setIdSelecionado(id);
    setIsModalVotacaoOpen(true);
  };

  const iniciarSessao = () => {
    if (idSelecionado !== null) {
      mutate({ idpauta: idSelecionado, tempo });

      setIsModalOpen(false);
      setTempo(1);
    }
  };

  const iniciarVotacoa = (data: CriarVotoFormData) => {
    try {
      votoMutate({
        pautaId: idSelecionado ?? 0,
        cpf: data.cpf,
        opcao: data.opcao ?? "",
      });
      navigate("/");
      reset();
    } catch (error) {}
  };

  useEffect(() => {
    const interval = setInterval(() => {
      const novoTempo: typeof tempoRestante = {};

      data?.forEach((pauta: any) => {
        if (pauta.sessaoAtiva) {
          const fechamento = new Date(pauta.dataFechamento).getTime();
          const agora = new Date().getTime();
          const restante = Math.max(Math.floor((fechamento - agora) / 1000), 0);
          novoTempo[pauta.id] = restante;

          if (restante === 0 && !votosContabilizados[pauta.id]) {
            axios
              .get(`http://localhost:8080/api/votos/contabilizar/${pauta.id}`)
              .then((res) => {
                setVotosContabilizados((prev) => ({
                  ...prev,
                  [pauta.id]: res.data,
                }));
              })
              .catch((err) => {
                console.error("Erro ao contabilizar votos:", err);
              });
          }
        }
      });

      setTempoRestante(novoTempo);
    }, 1000);

    return () => clearInterval(interval);
  }, [data, votosContabilizados]);

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (isError) {
    return (
      <div className="bg-red-100 border-l-4 border-red-500 text-red-700 p-4" role="alert">
        <p className="font-bold">Erro</p>
        <p>Não foi possível carregar as pautas</p>
      </div>
    );
  }

  return (
    <div className=" bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
      <div className="max-w-7xl mx-auto">
        <div className="text-center mb-12">
          <h1 className="text-3xl font-extrabold text-gray-900 sm:text-4xl">Sistema de Votação</h1>
          <p className="mt-3 max-w-2xl mx-auto text-xl text-gray-500 sm:mt-4">Gerencie as sessões de votação</p>
        </div>

        <div className=" grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {data?.map((pauta: any) => (
            <div
              key={pauta.id}
              className={`bg-white overflow-hidden shadow rounded-lg transition-all duration-300 hover:shadow-lg ${
                !pauta.sessaoAtiva ? "border-l-8 border-red-500" : tempoRestante[pauta.id] === 0 ? "border-l-8 border-yellow-600" : "border-l-8 border-green-500"
              }`}
            >
              <div className="px-4 py-5 sm:p-6">
                <div className="flex items-center">
                  <div className={`flex-shrink-0 h-12 w-12 rounded-full flex items-center justify-center ${pauta.sessaoAtiva ? "bg-green-100" : "bg-red-100"}`}>
                    <span className={`font-bold ${!pauta.sessaoAtiva ? "text-red-600" : tempoRestante[pauta.id] === 0 ? "text-yellow-600" : "text-green-600"}`}>
                      {pauta.sessaoAtiva ? "A" : "I"}
                    </span>
                  </div>
                  <div className="ml-4">
                    <h3 className="text-lg leading-6 font-medium text-gray-900">{pauta.titulo}</h3>
                    <p className="text-sm text-gray-500">{pauta.descricao}</p>
                  </div>
                </div>

                <div className="mt-4">
                  <div className="flex justify-between text-sm text-gray-600">
                    <span>Status:</span>

                    <span className={`font-semibold ${pauta.sessaoAtiva ? (tempoRestante[pauta.id] === 0 ? "text-yellow-600 " : "text-green-600") : "text-red-600"}`}>
                      {pauta.sessaoAtiva ? (tempoRestante[pauta.id] === 0 ? "Encerrada" : "Ativa") : "Inativa"}
                    </span>
                  </div>

                  {pauta.sessaoAtiva ? (
                    <div className="mt-2 space-y-1 text-sm text-gray-600">
                      <div>
                        <span className="font-medium">Abertura:</span> {new Date(pauta.dataAbertura).toLocaleString()}
                      </div>
                      <div>
                        <span className="font-medium">Fechamento:</span> {new Date(pauta.dataFechamento).toLocaleString()}
                      </div>
                      {tempoRestante[pauta.id] > 0 ? (
                        <button
                          onClick={() => abrirModalVotacao(pauta.id)}
                          className="mt-4 w-full inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-all"
                        >
                          Vota
                        </button>
                      ) : (
                        ""
                      )}
                    </div>
                  ) : (
                    <button
                      onClick={() => abrirModal(pauta.id)}
                      className="mt-4 w-full inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-all"
                    >
                      Iniciar Sessão
                    </button>
                  )}

                  {votosContabilizados[pauta.id] && (
                    <div className="mt-2 text-sm text-gray-700">
                      <p>
                        <strong>Votos SIM:</strong> {votosContabilizados[pauta.id].SIM}
                      </p>
                      <p>
                        <strong>Votos NÃO:</strong> {votosContabilizados[pauta.id].NAO}
                      </p>
                    </div>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
      {isModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center z-50 bg-black bg-opacity-50">
          <div className="bg-white rounded-lg p-6 w-96">
            <h2 className="text-lg font-semibold mb-4">Digite o tempo da sessão (em minutos)</h2>
            <input type="number" value={tempo} onChange={(e) => setTempo(Number(e.target.value))} className="w-full border border-gray-300 rounded-md px-3 py-2 mb-4" min={1} />
            <div className="flex justify-end space-x-2">
              <button onClick={() => setIsModalOpen(false)} className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400">
                Cancelar
              </button>
              <button onClick={iniciarSessao} className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                Iniciar
              </button>
            </div>
          </div>
        </div>
      )}

      {isModalVotacaoOpen && (
        <form onSubmit={handleSubmit(iniciarVotacoa)}>
          <div className="fixed inset-0 flex items-center justify-center z-50 bg-black bg-opacity-50">
            <div className="bg-white rounded-lg p-6 w-96">
              <h2 className="text-lg font-semibold mb-4">Votação</h2>
              <label className="text-sm font-semibold">CPF</label>
              <input type="text" {...register("cpf")} className="w-full border border-gray-300 rounded-md px-3 py-2 mb-4" />

              <label className="text-sm font-semibold">Opções</label>
              <div className="flex space-x-4 mb-4">
                <label className="inline-flex items-center">
                  <input type="radio" {...register("opcao")} value="SIM" className="form-radio h-4 w-4 text-blue-600" />
                  <span className="ml-2">SIM</span>
                </label>
                <label className="inline-flex items-center">
                  <input type="radio" {...register("opcao")} value="NAO" className="form-radio h-4 w-4 text-blue-600" />
                  <span className="ml-2">NÃO</span>
                </label>
              </div>

              <div className="flex justify-end space-x-2">
                <button onClick={() => setIsModalVotacaoOpen(false)} className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400">
                  Cancelar
                </button>
                <button className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Iniciar</button>
              </div>
            </div>
          </div>
        </form>
      )}
    </div>
  );
};
