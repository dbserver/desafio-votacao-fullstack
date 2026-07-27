import {
  Button,
  Card,
  CardContent,
  Chip,
  CircularProgress,
  Divider,
  Stack,
  TablePagination,
  Typography,
} from "@mui/material";
import {
  TimerRounded as TimerRoundedIcon,
  AccessTimeRounded as AccessTimeRoundedIcon,
  DescriptionRounded as DescriptionRoundedIcon,
  HowToVoteRounded as HowToVoteRoundedIcon,
  PlayArrowRounded as PlayArrowRoundedIcon,
} from "@mui/icons-material";
import { useCallback, useEffect, useState } from "react";

import { buscarResultado } from "../../services/resultadoVotacaoService";
import type { Pauta } from "../../types/pauta";
import type { ResultadoVotacao } from "../../types/resultado";
import type { SessaoVotacao } from "../../types/sessao";
import { AbrirSessaoDialog } from "../sessoes/AbrirSessaoDialog";
import { VotarDialog } from "../votos/VotarDialog";
import { TempoRestante } from "../sessoes/TempoRestante";

const resultadoLabels: Record<ResultadoVotacao["resultado"], string> = {
  EM_ANDAMENTO: "Em andamento",
  APROVADA: "Aprovada",
  REPROVADA: "Reprovada",
  EMPATE: "Empate",
  SEM_VOTOS: "Sem votos",
};

function resultadoColor(
  resultado?: ResultadoVotacao | null,
): "default" | "primary" | "success" | "error" | "warning" {
  switch (resultado?.resultado) {
    case "EM_ANDAMENTO":
      return "primary";
    case "APROVADA":
      return "success";
    case "REPROVADA":
      return "error";
    case "EMPATE":
      return "warning";
    default:
      return "default";
  }
}

interface PautaListProps {
  pautas: Pauta[];
  carregando: boolean;
  sessoesPorPauta: Record<number, SessaoVotacao>;
  pautaAbrindoId: number | null;
  pagina: number;
  tamanhoPagina: number;
  totalElementos: number;
  onAbrirSessao: (pautaId: number, duracao?: number) => Promise<boolean>;
  onMudarPagina: (pagina: number) => void;
  onMudarTamanhoPagina: (tamanho: number) => void;
}

export function PautaList({
  pautas,
  carregando,
  sessoesPorPauta,
  pautaAbrindoId,
  pagina,
  tamanhoPagina,
  totalElementos,
  onAbrirSessao,
  onMudarPagina,
  onMudarTamanhoPagina,
}: PautaListProps) {
  const [pautaSelecionada, setPautaSelecionada] = useState<Pauta | null>(null);
  const [pautaParaVotar, setPautaParaVotar] = useState<Pauta | null>(null);
  const [resultadosPorPauta, setResultadosPorPauta] = useState<
    Record<number, ResultadoVotacao | null>
  >({});

  const atualizarResultadoDaPauta = useCallback(async (pautaId: number) => {
    try {
      const resultado = await buscarResultado(pautaId);

      setResultadosPorPauta((resultadosAtuais) => ({
        ...resultadosAtuais,
        [pautaId]: resultado,
      }));
    } catch {
      setResultadosPorPauta((resultadosAtuais) => ({
        ...resultadosAtuais,
        [pautaId]: null,
      }));
    }
  }, []);

  useEffect(() => {
    setResultadosPorPauta({});

    const pautasComSessao = pautas.filter(
      (pauta) => sessoesPorPauta[pauta.id] !== undefined,
    );

    void Promise.all(
      pautasComSessao.map((pauta) => atualizarResultadoDaPauta(pauta.id)),
    );
  }, [pautas, sessoesPorPauta, atualizarResultadoDaPauta]);

  useEffect(() => {
    const atualizacoesAoEncerrar = pautas.flatMap((pauta) => {
      const sessao = sessoesPorPauta[pauta.id];

      if (!sessao?.open) {
        return [];
      }

      const tempoRestante =
        new Date(sessao.dthFim).getTime() - Date.now() + 500;

      if (tempoRestante <= 0) {
        void atualizarResultadoDaPauta(pauta.id);
        return [];
      }

      const timeoutId = window.setTimeout(() => {
        void atualizarResultadoDaPauta(pauta.id);
      }, tempoRestante);

      return [timeoutId];
    });

    return () => {
      atualizacoesAoEncerrar.forEach((timeoutId) => {
        window.clearTimeout(timeoutId);
      });
    };
  }, [pautas, sessoesPorPauta, atualizarResultadoDaPauta]);

  if (carregando) {
    return (
      <Stack
        alignItems="center"
        justifyContent="center"
        spacing={2}
        sx={{ minHeight: 260 }}
      >
        <CircularProgress size={34} />

        <Typography color="text.secondary">Carregando pautas...</Typography>
      </Stack>
    );
  }

  if (pautas.length === 0) {
    return (
      <Stack
        alignItems="center"
        justifyContent="center"
        spacing={1}
        sx={{
          minHeight: 260,
          color: "text.secondary",
          textAlign: "center",
        }}
      >
        <DescriptionRoundedIcon sx={{ fontSize: 46 }} />

        <Typography fontWeight={700} color="text.primary">
          Nenhuma pauta cadastrada
        </Typography>

        <Typography variant="body2">
          Utilize o formulário para cadastrar a primeira pauta.
        </Typography>
      </Stack>
    );
  }

  return (
    <>
      <Stack spacing={2}>
        {pautas.map((pauta) => {
          const sessao = sessoesPorPauta[pauta.id];
          const sessaoAtiva = sessao?.open === true;
          const sessaoExpirada = sessao !== undefined && !sessao.open;
          const abrindoEstaPauta = pautaAbrindoId === pauta.id;
          const resultado = resultadosPorPauta[pauta.id];
          const resultadoFoiCarregado = Object.prototype.hasOwnProperty.call(
            resultadosPorPauta,
            pauta.id,
          );

          return (
            <Card
              key={pauta.id}
              variant="outlined"
              sx={{
                transition: "150ms",
                "&:hover": {
                  borderColor: "primary.light",
                  boxShadow: 2,
                  transform: "translateY(-2px)",
                },
              }}
            >
              <CardContent>
                <Stack
                  direction="row"
                  alignItems="center"
                  justifyContent="space-between"
                  spacing={2}
                  sx={{ mb: 1.5 }}
                >
                  <Typography variant="h6">
                    {pauta.id}. {pauta.titulo}
                  </Typography>

                  <Chip
                    label={
                      !sessao
                        ? "Cadastrada"
                        : !resultadoFoiCarregado
                          ? "Carregando resultado..."
                          : resultado
                            ? resultadoLabels[resultado.resultado]
                            : "Resultado indisponível"
                    }
                    color={!sessao ? "success" : resultadoColor(resultado)}
                    variant="outlined"
                    size="small"
                  />
                </Stack>

                <Typography
                  variant="body2"
                  color="text.secondary"
                  sx={{
                    mt: 0.75,
                    overflowWrap: "anywhere",
                  }}
                >
                  {pauta.descricao}
                </Typography>

                <Divider sx={{ my: 2 }} />

                <Stack
                  direction={{ xs: "column", sm: "row" }}
                  alignItems={{ xs: "flex-start", sm: "center" }}
                  justifyContent="space-between"
                  spacing={2}
                >
                  <Stack spacing={0.75} color="text.secondary">
                    <Stack direction="row" alignItems="center" spacing={0.75}>
                      <AccessTimeRoundedIcon fontSize="small" />

                      <Typography variant="caption">
                        Criada em{" "}
                        {new Date(pauta.dthCriacao).toLocaleString("pt-BR", {
                          day: "2-digit",
                          month: "2-digit",
                          year: "numeric",
                          hour: "2-digit",
                          minute: "2-digit",
                        })}
                      </Typography>
                    </Stack>

                    {sessao && (
                      <Stack direction="row" alignItems="center" spacing={0.75}>
                        <TimerRoundedIcon fontSize="small" />

                        <Typography variant="caption" fontWeight={600}>
                          <TempoRestante dthFim={sessao.dthFim} />
                        </Typography>
                      </Stack>
                    )}
                  </Stack>

                  <Button
                    variant={
                      sessaoAtiva
                        ? "outlined"
                        : sessaoExpirada
                          ? "outlined"
                          : "contained"
                    }
                    size="small"
                    disabled={abrindoEstaPauta}
                    startIcon={
                      sessao ? (
                        <HowToVoteRoundedIcon />
                      ) : (
                        <PlayArrowRoundedIcon />
                      )
                    }
                    onClick={() => {
                      if (sessaoAtiva || sessaoExpirada) {
                        setPautaParaVotar(pauta);
                        return;
                      }

                      setPautaSelecionada(pauta);
                    }}
                  >
                    {sessao ? "Ver sessão" : "Abrir sessão"}
                  </Button>
                </Stack>
              </CardContent>
            </Card>
          );
        })}
      </Stack>

      <TablePagination
        component="div"
        count={totalElementos}
        page={pagina}
        rowsPerPage={tamanhoPagina}
        rowsPerPageOptions={[10, 25, 50]}
        onPageChange={(_, novaPagina) => onMudarPagina(novaPagina)}
        onRowsPerPageChange={(event) =>
          onMudarTamanhoPagina(Number(event.target.value))
        }
        labelRowsPerPage="Itens por página:"
        labelDisplayedRows={({ from, to, count }) =>
          `${from}–${to} de ${count}`
        }
        sx={{ mt: 1 }}
      />

      <AbrirSessaoDialog
        open={pautaSelecionada !== null}
        pautaTitulo={pautaSelecionada?.titulo ?? ""}
        abrindo={pautaAbrindoId === pautaSelecionada?.id}
        onClose={() => setPautaSelecionada(null)}
        onConfirmar={(duracao) => {
          if (!pautaSelecionada) {
            return Promise.resolve(false);
          }

          return onAbrirSessao(pautaSelecionada.id, duracao);
        }}
      />

      <VotarDialog
        open={pautaParaVotar !== null}
        pautaId={pautaParaVotar?.id ?? null}
        pautaTitulo={pautaParaVotar?.titulo ?? ""}
        sessao={
          pautaParaVotar ? (sessoesPorPauta[pautaParaVotar.id] ?? null) : null
        }
        onClose={() => {
          if (pautaParaVotar) {
            void atualizarResultadoDaPauta(pautaParaVotar.id);
          }

          setPautaParaVotar(null);
        }}
      />
    </>
  );
}
