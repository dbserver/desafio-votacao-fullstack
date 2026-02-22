import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { obterSessao, criarSessao, votar, SessaoResponse, resultadoSessao, ResultadoVotacao } from "../api/votacaoApi";

export default function Sessao() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [sessao, setSessao] = useState<SessaoResponse | null>(null);
  const [cpf, setCpf] = useState("");
  const [duracao, setDuracao] = useState(5);
  const [resultado, setResultado] = useState<ResultadoVotacao | null>(null);

  const formatDate = (s?: string) => {
    if (!s) return "-";

    console.debug("formatDate input:", s);

    let d = new Date(s);
    if (!isNaN(d.getTime())) {
      return d.toLocaleString("pt-BR", { timeZone: "America/Sao_Paulo" });
    }

    const truncated = s.replace(/(\.\d{3})\d+/, "$1");
    d = new Date(truncated + "Z");
    if (!isNaN(d.getTime())) {
      return d.toLocaleString("pt-BR", { timeZone: "America/Sao_Paulo" });
    }

    const re = /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})(?:\.(\d+))?/;
    const m = s.trim().match(re);
    if (!m) return "-";
    const year = Number(m[1]);
    const month = Number(m[2]) - 1;
    const day = Number(m[3]);
    const hour = Number(m[4]);
    const minute = Number(m[5]);
    const second = Number(m[6]);
    const ms = Number((m[7] || "0").padEnd(3, "0").slice(0, 3));
    const utc = Date.UTC(year, month, day, hour, minute, second, ms);
    const dateObj = new Date(utc);
    if (isNaN(dateObj.getTime())) return "-";
    return dateObj.toLocaleString("pt-BR", { timeZone: "America/Sao_Paulo" });
  };

  const mapApiSessao = (s: any) => {
    return { ...s, dataInicio: s.dataAbertura, dataFim: s.dataFechamento };
  };

  useEffect(() => {
    if (id) {
      resultadoSessao(Number(id))
        .then(res => setResultado(res.data))
        .catch(err => console.debug("resultado não disponível", err));

      obterSessao(Number(id))
        .then(res => setSessao(mapApiSessao(res.data)))
        .catch(err => console.error(err));
    }
  }, [id]);

  const handleCriarSessao = async () => {
    try {
      const res = await criarSessao(Number(id), duracao);
      setSessao(mapApiSessao(res.data));
      toast.success("Sessão criada com sucesso!");
    } catch (err: any) {
      toast.error(err.response?.data?.message || "Erro ao criar sessão");
    }
  };

  const handleVotar = async (tipoVoto: "SIM" | "NAO") => {
    if (!cpf) {
      toast.error("Digite um CPF válido!");
      return;
    }

    try {
      await votar(sessao!.id, cpf, tipoVoto);
      toast.success("Voto registrado com sucesso!");
    } catch (err: any) {
      toast.error(err.response?.data?.message || "Erro ao registrar voto");
    }
  };

  return (
    <div className="p-4 max-w-md mx-auto">
      <h1 className="text-2xl font-bold mb-4">Sessão {id}</h1>

      

      {!sessao && (
        <div className="space-y-2">
          <p>Nenhuma sessão encontrada para esta pauta.</p>
          <div className="flex gap-2">
            <input
              type="number"
              min={1}
              className="border p-2 rounded flex-1"
              value={duracao}
              onChange={e => setDuracao(Number(e.target.value))}
            />
            <button
              onClick={handleCriarSessao}
              className="bg-blue-500 text-white px-4 py-2 rounded cursor-pointer"
            >
              Criar Sessão
            </button>
          </div>
        </div>
      )}

      {sessao && (
        <div>
          <p className="mb-2">
            Status: {sessao.aberta ? "ABERTA" : "FECHADA"}
          </p>
          <p className="mb-4">
            Início: {formatDate(sessao.dataInicio)} <br />
            Fim: {formatDate(sessao.dataFim)}
          </p>

          {sessao.aberta && (
            <>
              <input
                type="text"
                placeholder="Digite seu CPF"
                className="border p-2 rounded w-full mb-4"
                value={cpf}
                onChange={e => setCpf(e.target.value)}
              />

              <div className="flex gap-4 mb-4">
                <button
                  onClick={() => handleVotar("SIM")}
                  className="bg-green-500 text-white px-4 py-2 rounded flex-1 cursor-pointer"
                >
                  SIM
                </button>
                <button
                  onClick={() => handleVotar("NAO")}
                  className="bg-red-500 text-white px-4 py-2 rounded flex-1 cursor-pointer"
                >
                  NÃO
                </button>
              </div>
            </>
          )}

          {!sessao.aberta && (
            <p className="text-gray-600">Sessão encerrada. Não é possível votar.</p>
          )}
        </div>
      )}
      {sessao && !sessao.aberta && (
        <button
          onClick={() => navigate(`/resultado/${sessao.id}`)}
          className="bg-blue-500 text-white px-4 py-2 rounded w-full cursor-pointer"
        >
          Ver Resultado
        </button>
      )}
    </div>
  );
}