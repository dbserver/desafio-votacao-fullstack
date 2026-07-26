import { useEffect, useState } from "react";
import axios from "axios";
import { PautaForm, type NovaPauta } from "../components/pautas/PautaForm";

import type { Pauta } from "../types/pauta";
import { criarPauta, listarPautas } from "../services/pautaService";
import { Alert, Chip, Container, Grid } from "@mui/material";
import { SectionCard } from "../components/layout/SectionCard";
import { PageHeader } from "../components/layout/PageHeader";
import { PautaList } from "../components/pautas/PautaList";

import type { SessaoVotacao } from "../types/sessao";
import {
  abrirSessao,
  buscarSessaoPorPauta,
} from "../services/sessaoVotacaoService";

export function Pautas() {
  const [pautas, setPautas] = useState<Pauta[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [salvando, setSalvando] = useState(false);
  const [erro, setErro] = useState("");
  const [sessoesPorPauta, setSessoesPorPauta] = useState<
    Record<number, SessaoVotacao>
  >({});
  const [pautaAbrindoId, setPautaAbrindoId] = useState<number | null>(null);
  const [sucesso, setSucesso] = useState("");

  useEffect(() => {
    let cancelado = false;

    async function carregarDados() {
      try {
        setCarregando(true);
        setErro("");

        const pautasEncontradas = await listarPautas();

        const resultados = await Promise.all(
          pautasEncontradas.map(async (pauta) => ({
            pautaId: pauta.id,
            sessao: await buscarSessaoPorPauta(pauta.id),
          })),
        );

        if (cancelado) {
          return;
        }

        const sessoesEncontradas = resultados.reduce<
          Record<number, SessaoVotacao>
        >((mapa, resultado) => {
          if (resultado.sessao) {
            mapa[resultado.pautaId] = resultado.sessao;
          }

          return mapa;
        }, {});

        setPautas(pautasEncontradas);
        setSessoesPorPauta(sessoesEncontradas);
      } catch (error) {
        if (!cancelado) {
          console.error(error);
          setErro("Não foi possível carregar as pautas e suas sessões.");
        }
      } finally {
        if (!cancelado) {
          setCarregando(false);
        }
      }
    }

    carregarDados();

    return () => {
      cancelado = true;
    };
  }, []);

  async function cadastrarPauta(dados: NovaPauta): Promise<boolean> {
    try {
      setErro("");
      setSalvando(true);

      const novaPauta = await criarPauta(dados);

      setPautas((pautasAtuais) => [novaPauta, ...pautasAtuais]);

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

      const sessao = await abrirSessao(pautaId, {
        duracao,
      });

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
            label={
              carregando
                ? "Carregando..."
                : `${pautas.length} ${pautas.length === 1 ? "pauta" : "pautas"}`
            }
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
              onAbrirSessao={handleAbrirSessao}
            />
          </SectionCard>
        </Grid>
      </Grid>
    </Container>
  );
}
