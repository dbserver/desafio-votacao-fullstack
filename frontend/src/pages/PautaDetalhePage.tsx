import { Link, useParams } from "react-router";
import { Card } from "../components/ui/Card";
import { Layout } from "../components/ui/Layout";
import { usePauta } from "../features/pauta/hooks";
import { SessaoCard } from "../features/sessao/components/SessaoCard";
import { formatarDataHora } from "../lib/format";
import "./PautaDetalhePage.css";

export const PautaDetalhePage = () => {
  const { pautaId: parametro } = useParams();
  const pautaId = Number(parametro);

  const { data: pauta, isPending, isError, error } = usePauta(pautaId);

  if (!Number.isInteger(pautaId)) {
    return (
      <Layout>
        <p>Pauta inválida.</p>
        <Link to="/">Voltar para as pautas</Link>
      </Layout>
    );
  }

  if (isPending) {
    return (
      <Layout>
        <p>Carregando pauta...</p>
      </Layout>
    );
  }

  if (isError) {
    return (
      <Layout>
        <p>{error.message}</p>
        <Link to="/">Voltar para as pautas</Link>
      </Layout>
    );
  }

  if (!pauta) {
    return (
      <Layout>
        <p>Pauta não encontrada.</p>
        <Link to="/">Voltar para as pautas</Link>
      </Layout>
    );
  }

  return (
    <Layout>
      <Link className="pauta-detalhe__voltar" to="/">
        Voltar para as pautas
      </Link>

      <h1>{pauta.titulo}</h1>

      <p className="pauta-detalhe__descricao">{pauta.descricao}</p>

      <span className="pauta-detalhe__data">
        Criada em {formatarDataHora(pauta.criadaEm)}
      </span>

      <div className="pauta-detalhe__conteudo">
        <SessaoCard pautaId={pautaId} />

        <Card title="Votação">
          <p className="pauta-detalhe__aviso">Em breve.</p>
        </Card>
      </div>
    </Layout>
  );
};
