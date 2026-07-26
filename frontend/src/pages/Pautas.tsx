import { useEffect, useState } from "react";
import axios from "axios";
import { PautaForm, type NovaPauta } from "../components/pautas/PautaForm";

import type { Pauta } from "../types/pauta";
import { criarPauta, listarPautas } from "../services/pautaService";
import { Alert, Chip, Container, Grid } from "@mui/material";
import { SectionCard } from "../components/layout/SectionCard";
import { PageHeader } from "../components/layout/PageHeader";
import { PautaList } from "../components/pautas/PautaList";

export function Pautas() {
  const [pautas, setPautas] = useState<Pauta[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [salvando, setSalvando] = useState(false);
  const [erro, setErro] = useState("");

  useEffect(() => {
    let cancelado = false;

    listarPautas()
      .then((pautasEncontradas) => {
        if (!cancelado) {
          setPautas(pautasEncontradas);
          setErro("");
        }
      })
      .catch(() => {
        if (!cancelado) {
          setErro("Não foi possível carregar as pautas.");
        }
      })
      .finally(() => {
        if (!cancelado) {
          setCarregando(false);
        }
      });

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
            <PautaList pautas={pautas} carregando={carregando} />
          </SectionCard>
        </Grid>
      </Grid>
    </Container>
  );
}
