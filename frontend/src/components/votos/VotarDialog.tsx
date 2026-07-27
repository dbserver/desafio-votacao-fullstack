import { useEffect, useState } from "react";
import type { SubmitEvent } from "react";
import axios from "axios";

import {
  Alert,
  Button,
  Chip,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Paper,
  Stack,
  TextField,
  ToggleButton,
  ToggleButtonGroup,
  Typography,
} from "@mui/material";

import {
  ThumbDownRounded as ThumbDownRoundedIcon,
  ThumbUpRounded as ThumbUpRoundedIcon,
} from "@mui/icons-material";

import { buscarResultado } from "../../services/resultadoVotacaoService";
import { registrarVoto } from "../../services/votoService";
import type { SessaoVotacao } from "../../types/sessao";
import { resultadoLabels, type ResultadoVotacao } from "../../types/resultado";
import type { OpcaoVoto } from "../../types/voto";
import { TempoRestante } from "../sessoes/TempoRestante";
import { useNow } from "../../hooks/useNow";

interface VotarDialogProps {
  open: boolean;
  pautaId: number | null;
  pautaTitulo: string;
  sessao: SessaoVotacao | null;
  onClose: () => void;
}

export function VotarDialog({
  open,
  pautaId,
  pautaTitulo,
  sessao,
  onClose,
}: VotarDialogProps) {
  const [associadoId, setAssociadoId] = useState("");
  const [opcao, setOpcao] = useState<OpcaoVoto | null>(null);
  const [enviando, setEnviando] = useState(false);
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState(false);
  const [resultado, setResultado] = useState<ResultadoVotacao | null>(null);
  const [carregandoResultado, setCarregandoResultado] = useState(true);

  const now = useNow();

  useEffect(() => {
    if (!open || pautaId === null) {
      return;
    }

    let requisicaoAtiva = true;

    buscarResultado(pautaId)
      .then((resultadoEncontrado) => {
        if (requisicaoAtiva) {
          setResultado(resultadoEncontrado);
        }
      })
      .catch(() => {
        if (requisicaoAtiva) {
          setErro("Não foi possível carregar a contagem dos votos.");
        }
      })
      .finally(() => {
        if (requisicaoAtiva) {
          setCarregandoResultado(false);
        }
      });

    return () => {
      requisicaoAtiva = false;
    };
  }, [open, pautaId]);

  async function atualizarResultadoDepoisDoVoto() {
    if (pautaId === null) {
      return;
    }

    try {
      const resultadoEncontrado = await buscarResultado(pautaId);

      setResultado(resultadoEncontrado);
    } catch {
      setErro("Não foi possível carregar a contagem dos votos.");
    } finally {
      setCarregandoResultado(false);
    }
  }

  function limparFormulario() {
    setAssociadoId("");
    setOpcao(null);
    setErro("");
    setSucesso(false);
    setResultado(null);
    setCarregandoResultado(true);
  }

  function handleFechar() {
    limparFormulario();
    onClose();
  }

  const sessaoDentroDoPrazo =
    sessao !== null && new Date(sessao.dthFim).getTime() > now;

  const podeVotar = resultado?.sessaoAberta === true && sessaoDentroDoPrazo;

  async function handleSubmit(event: SubmitEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!podeVotar) {
      setErro("A sessão foi encerrada e não aceita mais votos.");
      return;
    }

    const idNumerico = Number(associadoId);

    if (!Number.isInteger(idNumerico) || idNumerico <= 0) {
      setErro("Informe um ID de associado válido.");
      return;
    }

    if (!opcao) {
      setErro("Selecione Sim ou Não.");
      return;
    }

    if (pautaId === null) {
      return;
    }

    try {
      setEnviando(true);
      setErro("");

      await registrarVoto(pautaId, {
        associadoId: idNumerico,
        opcao,
      });

      await atualizarResultadoDepoisDoVoto();

      setSucesso(true);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        if (error.response?.data?.error === "EXPIRED_SESSION") {
          await atualizarResultadoDepoisDoVoto();

          setErro("A sessão foi encerrada e não aceita mais votos.");
        } else {
          setErro(
            error.response?.data?.message ??
              "Não foi possível registrar o voto.",
          );
        }
      } else {
        setErro("Não foi possível registrar o voto.");
      }
    } finally {
      setEnviando(false);
    }
  }

  return (
    <Dialog open={open} onClose={handleFechar} fullWidth maxWidth="xs">
      <DialogTitle>Sessão de votação</DialogTitle>

      <DialogContent>
        <Stack
          component="form"
          id="votar-form"
          onSubmit={handleSubmit}
          spacing={2.5}
          sx={{ pt: 1 }}
        >
          <Typography variant="body2" color="text.secondary">
            Pauta: <strong>{pautaTitulo}</strong>
          </Typography>

          {erro && <Alert severity="error">{erro}</Alert>}

          {sucesso && (
            <Alert severity="success">Voto registrado com sucesso.</Alert>
          )}

          {carregandoResultado ? (
            <Stack alignItems="center" spacing={1} sx={{ py: 2 }}>
              <CircularProgress size={28} />

              <Typography variant="body2" color="text.secondary">
                Carregando resultado...
              </Typography>
            </Stack>
          ) : (
            resultado && (
              <>
                <Stack direction="row" spacing={2}>
                  <Paper
                    variant="outlined"
                    sx={{
                      flex: 1,
                      p: 2,
                      textAlign: "center",
                      borderColor: "success.light",
                    }}
                  >
                    <Typography variant="caption" color="text.secondary">
                      Votos Sim
                    </Typography>

                    <Typography variant="h4" color="success.main">
                      {resultado.votosSim}
                    </Typography>
                  </Paper>

                  <Paper
                    variant="outlined"
                    sx={{
                      flex: 1,
                      p: 2,
                      textAlign: "center",
                      borderColor: "error.light",
                    }}
                  >
                    <Typography variant="caption" color="text.secondary">
                      Votos Não
                    </Typography>

                    <Typography variant="h4" color="error.main">
                      {resultado.votosNao}
                    </Typography>
                  </Paper>
                </Stack>

                <Typography
                  variant="body1"
                  color="text.secondary"
                  align="center"
                  sx={{ fontWeight: "bold" }}
                >
                  {resultado.resultado === "EM_ANDAMENTO"
                    ? "Resultado: Em andamento"
                    : `Resultado: ${
                        resultadoLabels[resultado.resultado] ||
                        resultado.resultado
                      }`}
                </Typography>

                <Stack
                  direction="row"
                  justifyContent="space-between"
                  alignItems="center"
                >
                  <Typography variant="body2" color="text.secondary">
                    Total: {resultado.totalVotos}
                  </Typography>

                  <Chip
                    size="small"
                    label={
                      podeVotar && sessao ? (
                        <TempoRestante dthFim={sessao.dthFim} />
                      ) : (
                        "Sessão encerrada"
                      )
                    }
                    color={podeVotar ? "success" : "default"}
                  />
                </Stack>
              </>
            )
          )}

          {!carregandoResultado && resultado && !podeVotar && (
            <Alert severity="info">
              A sessão foi encerrada. A contagem permanece disponível, mas novos
              votos não são permitidos.
            </Alert>
          )}

          {!sucesso && podeVotar && (
            <>
              <TextField
                label="ID do associado"
                type="number"
                value={associadoId}
                onChange={(event) => {
                  setAssociadoId(event.target.value);
                  setErro("");
                }}
                inputProps={{
                  min: 1,
                  step: 1,
                }}
                required
                autoFocus
              />

              <Stack spacing={1}>
                <Typography variant="body2" fontWeight={600}>
                  Selecione seu voto
                </Typography>

                <ToggleButtonGroup
                  value={opcao}
                  exclusive
                  fullWidth
                  onChange={(_, novaOpcao: OpcaoVoto | null) => {
                    setOpcao(novaOpcao);
                    setErro("");
                  }}
                >
                  <ToggleButton value="SIM" color="success">
                    <ThumbUpRoundedIcon sx={{ mr: 1 }} />
                    Sim
                  </ToggleButton>

                  <ToggleButton value="NAO" color="error">
                    <ThumbDownRoundedIcon sx={{ mr: 1 }} />
                    Não
                  </ToggleButton>
                </ToggleButtonGroup>
              </Stack>
            </>
          )}
        </Stack>
      </DialogContent>

      <DialogActions sx={{ px: 3, pb: 3 }}>
        {sucesso || !podeVotar ? (
          <Button
            variant="contained"
            onClick={handleFechar}
            disabled={enviando || carregandoResultado}
          >
            Concluir
          </Button>
        ) : (
          <>
            <Button onClick={handleFechar} disabled={enviando}>
              Cancelar
            </Button>

            <Button
              type="submit"
              form="votar-form"
              variant="contained"
              disabled={enviando || carregandoResultado}
              startIcon={
                enviando ? (
                  <CircularProgress size={18} color="inherit" />
                ) : undefined
              }
            >
              {enviando ? "Registrando..." : "Confirmar voto"}
            </Button>
          </>
        )}
      </DialogActions>
    </Dialog>
  );
}
