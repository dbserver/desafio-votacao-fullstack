import { useCallback, useEffect, useRef, useState } from "react";
import axios from "axios";
import { Alert, Chip, Container, Grid } from "@mui/material";
import { PageHeader } from "../../components/layout/PageHeader";
import { SectionCard } from "../../components/layout/SectionCard";
import { type NovaPauta, PautaForm } from "../../components/pautas/PautaForm";
import { PautaList } from "../../components/pautas/PautaList";
import { listarPautas, criarPauta } from "../../services/pautaService";
import {
  buscarSessaoPorPauta,
  abrirSessao,
} from "../../services/sessaoVotacaoService";
import type { Pauta } from "../../types/pauta";
import type { SessaoVotacao } from "../../types/sessao";

export function Pautas() {
  const [pautas, setPautas] = useState<Pauta[]>([]);
  const [pagina, setPagina] = useState(0);
  const [tamanhoPagina, setTamanhoPagina] = useState(10);
  const [totalPautas, setTotalPautas] = useState(0);
  const [carregando, setCarregando] = useState(true);
  const [salvando, setSalvando] = useState(false);
  const [erro, setErro] = useState("");
  const [sessoesPorPauta, setSessoesPorPauta] = useState<
    Record<number, SessaoVotacao>
  >({});
  const [pautaAbrindoId, setPautaAbrindoId] = useState<number | null>(null);
  const [sucesso, setSucesso] = useState("");
  const ultimaRequisicao = useRef(0);

  const carregarDados = useCallback(
    async (paginaDesejada: number, tamanhoDesejado: number) => {
      const requisicaoAtual = ++ultimaRequisicao.current;

      try {
        setCarregando(true);
        setErro("");

        const resultado = await listarPautas(paginaDesejada, tamanhoDesejado);

        const sessoes = await Promise.all(
          resultado.content.map(async (pauta) => ({
            pautaId: pauta.id,
            sessao: await buscarSessaoPorPauta(pauta.id),
          })),
        );

        if (requisicaoAtual !== ultimaRequisicao.current) {
          return;
        }

        const sessoesEncontradas = sessoes.reduce<
          Record<number, SessaoVotacao>
        >((mapa, resultadoSessao) => {
          if (resultadoSessao.sessao) {
            mapa[resultadoSessao.pautaId] = resultadoSessao.sessao;
          }

          return mapa;
        }, {});

        setPautas(resultado.content);
        setTotalPautas(resultado.totalElements);
        setSessoesPorPauta(sessoesEncontradas);
      } catch (error) {
        if (requisicaoAtual !== ultimaRequisicao.current) {
          return;
        }

        if (axios.isAxiosError(error)) {
          setErro(
            error.response?.data?.message ??
              "Não foi possível carregar as pautas e suas sessões.",
          );
        } else {
          setErro("Não foi possível carregar as pautas e suas sessões.");
        }
      } finally {
        if (requisicaoAtual === ultimaRequisicao.current) {
          setCarregando(false);
        }
      }
    },
    [],
  );

  useEffect(() => {
    void carregarDados(pagina, tamanhoPagina);
  }, [carregarDados, pagina, tamanhoPagina]);

  async function cadastrarPauta(dados: NovaPauta): Promise<boolean> {
    try {
      setErro("");
      setSucesso("");
      setSalvando(true);

      await criarPauta(dados);
      setSucesso("Pauta cadastrada com sucesso.");

      if (pagina === 0) {
        await carregarDados(0, tamanhoPagina);
      } else {
        setPagina(0);
      }

      return true;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        setErro(
          error.response?.data?.message ??
            "Não foi possível cadastrar a pauta.",
        );
      } else {
        setErro("Não foi possível cadastrar a pauta.");
      }

      return false;
    } finally {
      setSalvando(false);
    }
  }

  async function handleAbrirSessao(
    pautaId: number,
    duracao?: number,
  ): Promise<boolean> {
    try {
      setErro("");
      setSucesso("");
      setPautaAbrindoId(pautaId);

      const sessao = await abrirSessao(pautaId, { duracao });

      setSessoesPorPauta((sessoesAtuais) => ({
        ...sessoesAtuais,
        [pautaId]: sessao,
      }));

      setSucesso("Sessão de votação aberta com sucesso.");

      return true;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        setErro(
          error.response?.data?.message ??
            "Não foi possível abrir a sessão de votação.",
        );
      } else {
        setErro("Não foi possível abrir a sessão de votação.");
      }

      return false;
    } finally {
      setPautaAbrindoId(null);
    }
  }

  return (
    <Container maxWidth="lg" sx={{ py: { xs: 3, md: 5 } }}>
      <PageHeader
        eyebrow="Gestão de pautas"
        title="Pautas"
        description="Cadastre e acompanhe as pautas disponíveis para votação."
        action={
          <Chip
            color="primary"
            variant="outlined"
            label={`${totalPautas} ${totalPautas === 1 ? "pauta" : "pautas"}`}
          />
        }
      />

      {sucesso && (
        <Alert severity="success" onClose={() => setSucesso("")} sx={{ mb: 3 }}>
          {sucesso}
        </Alert>
      )}
      {erro && (
        <Alert severity="error" sx={{ mb: 3 }}>
          {erro}
        </Alert>
      )}

      <Grid container spacing={3} alignItems="flex-start">
        <Grid item xs={12} md={4}>
          <SectionCard
            eyebrow="Cadastro"
            title="Nova pauta"
            description="Informe os dados do assunto que será submetido à votação."
            sticky
          >
            <PautaForm salvando={salvando} onCadastrar={cadastrarPauta} />
          </SectionCard>
        </Grid>

        <Grid item xs={12} md={8}>
          <SectionCard eyebrow="Pautas disponíveis" title="Pautas cadastradas">
            <PautaList
              pautas={pautas}
              carregando={carregando}
              sessoesPorPauta={sessoesPorPauta}
              pautaAbrindoId={pautaAbrindoId}
              pagina={pagina}
              tamanhoPagina={tamanhoPagina}
              totalElementos={totalPautas}
              onAbrirSessao={handleAbrirSessao}
              onMudarPagina={setPagina}
              onMudarTamanhoPagina={(novoTamanho) => {
                setPagina(0);
                setTamanhoPagina(novoTamanho);
              }}
            />
          </SectionCard>
        </Grid>
      </Grid>
    </Container>
  );
}
