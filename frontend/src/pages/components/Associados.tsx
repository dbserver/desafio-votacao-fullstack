import { useCallback, useEffect, useState } from "react";
import axios from "axios";
import { Alert, Chip, Container, Grid, Stack } from "@mui/material";
import { GroupsRounded as GroupsRoundedIcon } from "@mui/icons-material";

import { AssociadoForm } from "../../components/associados/AssociadosForm";
import { AssociadoList } from "../../components/associados/AssociadosList";
import { PageHeader } from "../../components/layout/PageHeader";
import { SectionCard } from "../../components/layout/SectionCard";

import {
  criarAssociado,
  listarAssociados,
} from "../../services/associadoService";
import type { Associado } from "../../types/associado";

export function Associados() {
  const [associados, setAssociados] = useState<Associado[]>([]);
  const [pagina, setPagina] = useState(0);
  const [tamanhoPagina, setTamanhoPagina] = useState(10);
  const [totalAssociados, setTotalAssociados] = useState(0);
  const [salvando, setSalvando] = useState(false);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");
  const [associadoCriado, setAssociadoCriado] = useState<Associado | null>(
    null,
  );

  const carregarAssociados = useCallback(
    async (paginaDesejada: number, tamanhoDesejado: number) => {
      try {
        setCarregando(true);
        setErro("");

        const resultado = await listarAssociados(
          paginaDesejada,
          tamanhoDesejado,
        );

        setAssociados(resultado.content);
        setTotalAssociados(resultado.totalElements);
      } catch (error) {
        if (axios.isAxiosError(error)) {
          setErro(
            error.response?.data?.message ??
              "Não foi possível carregar os associados.",
          );
        } else {
          setErro("Não foi possível carregar os associados.");
        }
      } finally {
        setCarregando(false);
      }
    },
    [],
  );

  useEffect(() => {
    void carregarAssociados(pagina, tamanhoPagina);
  }, [carregarAssociados, pagina, tamanhoPagina]);

  async function cadastrarAssociado(cpf: string): Promise<boolean> {
    try {
      setErro("");
      setSalvando(true);
      setAssociadoCriado(null);

      const novoAssociado = await criarAssociado({ cpf });

      setAssociadoCriado(novoAssociado);

      if (pagina === 0) {
        await carregarAssociados(0, tamanhoPagina);
      } else {
        setPagina(0);
      }

      return true;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        setErro(
          error.response?.data?.message ??
            "Não foi possível cadastrar o associado.",
        );
      } else {
        setErro("Não foi possível cadastrar o associado.");
      }

      return false;
    } finally {
      setSalvando(false);
    }
  }

  return (
    <Container maxWidth="lg" sx={{ py: { xs: 3, md: 5 } }}>
      <PageHeader
        eyebrow="Gestão de associados"
        title="Associados"
        description="Cadastre e consulte os associados disponíveis para participar das votações."
        action={
          <Chip
            color="primary"
            variant="outlined"
            icon={<GroupsRoundedIcon />}
            label={`${totalAssociados} ${
              totalAssociados === 1 ? "associado" : "associados"
            }`}
          />
        }
      />

      {erro && (
        <Alert severity="error" onClose={() => setErro("")} sx={{ mb: 3 }}>
          {erro}
        </Alert>
      )}

      <Grid container spacing={3} alignItems="flex-start">
        <Grid item xs={12} md={4}>
          <SectionCard
            eyebrow="Cadastro"
            title="Novo associado"
            description="O CPF deve conter exatamente 11 números e não pode estar cadastrado."
            sticky
          >
            <Stack spacing={2}>
              {associadoCriado && (
                <Alert severity="success">
                  Associado cadastrado com sucesso. O ID para votação é{" "}
                  <strong>{associadoCriado.id}</strong>.
                </Alert>
              )}

              <AssociadoForm
                salvando={salvando}
                onCadastrar={cadastrarAssociado}
              />
            </Stack>
          </SectionCard>
        </Grid>

        <Grid item xs={12} md={8}>
          <AssociadoList
            associados={associados}
            carregando={carregando}
            associadoDestacadoId={associadoCriado?.id}
            pagina={pagina}
            tamanhoPagina={tamanhoPagina}
            totalElementos={totalAssociados}
            onMudarPagina={setPagina}
            onMudarTamanhoPagina={(novoTamanho) => {
              setPagina(0);
              setTamanhoPagina(novoTamanho);
            }}
          />
        </Grid>
      </Grid>
    </Container>
  );
}
