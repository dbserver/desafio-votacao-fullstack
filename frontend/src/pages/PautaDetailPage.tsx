import { useState, useEffect, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Typography,
  Paper,
  Box,
  Button,
  TextField,
  RadioGroup,
  FormControlLabel,
  Radio,
  Alert,
  Snackbar,
  Divider,
  Chip,
  LinearProgress,
  Grid,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  IconButton,
} from "@mui/material";
import { ArrowBack, Timer, HowToVote, BarChart } from "@mui/icons-material";
import { Pauta, SessaoVotacao, ResultadoVotacao } from "../types";
import { buscarPauta } from "../services/pautaService";
import { abrirSessao, buscarSessao } from "../services/sessaoService";
import { registrarVoto, buscarResultado } from "../services/votoService";

function PautaDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const pautaId = Number(id);

  const [pauta, setPauta] = useState<Pauta | null>(null);
  const [sessao, setSessao] = useState<SessaoVotacao | null>(null);
  const [resultado, setResultado] = useState<ResultadoVotacao | null>(null);
  const [associadoId, setAssociadoId] = useState("");
  const [votoSelecionado, setVotoSelecionado] = useState<"SIM" | "NAO">("SIM");
  const [duracaoMinutos, setDuracaoMinutos] = useState("1");
  const [dialogSessaoOpen, setDialogSessaoOpen] = useState(false);
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");
  const [loading, setLoading] = useState(true);

  const carregarDados = useCallback(async () => {
    try {
      setLoading(true);
      const { data: pautaData } = await buscarPauta(pautaId);
      setPauta(pautaData);

      try {
        const { data: sessaoData } = await buscarSessao(pautaId);
        setSessao(sessaoData);

        try {
          const { data: resultadoData } = await buscarResultado(pautaId);
          setResultado(resultadoData);
        } catch {
          setResultado(null);
        }
      } catch {
        setSessao(null);
      }
    } catch {
      setErro("Erro ao carregar dados da pauta");
    } finally {
      setLoading(false);
    }
  }, [pautaId]);

  useEffect(() => {
    carregarDados();
  }, [carregarDados]);

  const handleAbrirSessao = async () => {
    try {
      const duracao = parseInt(duracaoMinutos) || 1;
      await abrirSessao(pautaId, duracao);
      setDialogSessaoOpen(false);
      setSucesso("Sessão aberta com sucesso!");
      carregarDados();
    } catch (err: any) {
      setErro(err.response?.data?.mensagem || "Erro ao abrir sessão");
    }
  };

  const handleVotar = async () => {
    if (!associadoId.trim()) {
      setErro("Informe o CPF do associado");
      return;
    }
    try {
      await registrarVoto(pautaId, { associadoId, voto: votoSelecionado });
      setSucesso("Voto registrado com sucesso!");
      setAssociadoId("");
      carregarDados();
    } catch (err: any) {
      setErro(err.response?.data?.mensagem || "Erro ao registrar voto");
    }
  };

  if (loading) return <LinearProgress />;
  if (!pauta) return <Alert severity="error">Pauta não encontrada</Alert>;

  const calcularPorcentagem = (parte: number, total: number) =>
    total > 0 ? ((parte / total) * 100).toFixed(1) : "0.0";

  return (
    <>
      <Box sx={{ mb: 3, display: "flex", alignItems: "center" }}>
        <IconButton onClick={() => navigate("/")} sx={{ mr: 1 }}>
          <ArrowBack />
        </IconButton>
        <Typography variant="h4" fontWeight={500}>
          {pauta.titulo}
        </Typography>
      </Box>

      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3, mb: 3 }}>
            <Typography variant="h6" gutterBottom>
              Detalhes da Pauta
            </Typography>
            <Typography variant="body1" color="text.secondary" paragraph>
              {pauta.descricao || "Sem descrição"}
            </Typography>
            <Chip
              label={`Criada em ${new Date(pauta.criadaEm).toLocaleString("pt-BR")}`}
              size="small"
              variant="outlined"
            />
          </Paper>

          <Paper sx={{ p: 3 }}>
            <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
              <Timer sx={{ mr: 1, color: "primary.main" }} />
              <Typography variant="h6">Sessão de Votação</Typography>
            </Box>

            {sessao ? (
              <>
                <Chip
                  label={sessao.aberta ? "Aberta" : "Encerrada"}
                  color={sessao.aberta ? "success" : "default"}
                  sx={{ mb: 2 }}
                />
                <Typography variant="body2" sx={{ mb: 0.5 }}>
                  Início: {new Date(sessao.dataInicio).toLocaleString("pt-BR")}
                </Typography>
                <Typography variant="body2">
                  Fim: {new Date(sessao.dataFim).toLocaleString("pt-BR")}
                </Typography>
              </>
            ) : (
              <Button
                variant="contained"
                onClick={() => setDialogSessaoOpen(true)}
                startIcon={<Timer />}
              >
                Abrir Sessão
              </Button>
            )}
          </Paper>
        </Grid>

        <Grid item xs={12} md={6}>
          {sessao?.aberta && (
            <Paper sx={{ p: 3, mb: 3 }}>
              <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
                <HowToVote sx={{ mr: 1, color: "primary.main" }} />
                <Typography variant="h6">Votar</Typography>
              </Box>
              <TextField
                fullWidth
                label="CPF do Associado"
                value={associadoId}
                onChange={(e) => setAssociadoId(e.target.value)}
                sx={{ mb: 2 }}
                placeholder="000.000.000-00"
              />
              <RadioGroup
                value={votoSelecionado}
                onChange={(e) =>
                  setVotoSelecionado(e.target.value as "SIM" | "NAO")
                }
                row
              >
                <FormControlLabel value="SIM" control={<Radio />} label="Sim" />
                <FormControlLabel value="NAO" control={<Radio />} label="Não" />
              </RadioGroup>
              <Button
                variant="contained"
                color="primary"
                onClick={handleVotar}
                sx={{ mt: 2 }}
                fullWidth
              >
                Confirmar Voto
              </Button>
            </Paper>
          )}

          {resultado && resultado.totalVotos > 0 && (
            <Paper sx={{ p: 3 }}>
              <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
                <BarChart sx={{ mr: 1, color: "primary.main" }} />
                <Typography variant="h6">Resultado</Typography>
              </Box>
              <Chip
                label={resultado.resultado}
                color={
                  resultado.resultado === "APROVADA"
                    ? "success"
                    : resultado.resultado === "REPROVADA"
                      ? "error"
                      : "warning"
                }
                sx={{ mb: 2 }}
              />
              <Divider sx={{ my: 2 }} />
              <Typography variant="body1" sx={{ mb: 2 }}>
                Total de votos: <strong>{resultado.totalVotos}</strong>
              </Typography>
              <Box>
                <Box
                  sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    mb: 0.5,
                  }}
                >
                  <Typography variant="body2">
                    Sim: {resultado.votosSim}
                  </Typography>
                  <Typography variant="body2">
                    {calcularPorcentagem(
                      resultado.votosSim,
                      resultado.totalVotos,
                    )}
                    %
                  </Typography>
                </Box>
                <LinearProgress
                  variant="determinate"
                  value={
                    resultado.totalVotos > 0
                      ? (resultado.votosSim / resultado.totalVotos) * 100
                      : 0
                  }
                  color="success"
                  sx={{ mb: 2, height: 10, borderRadius: 5 }}
                />
                <Box
                  sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    mb: 0.5,
                  }}
                >
                  <Typography variant="body2">
                    Não: {resultado.votosNao}
                  </Typography>
                  <Typography variant="body2">
                    {calcularPorcentagem(
                      resultado.votosNao,
                      resultado.totalVotos,
                    )}
                    %
                  </Typography>
                </Box>
                <LinearProgress
                  variant="determinate"
                  value={
                    resultado.totalVotos > 0
                      ? (resultado.votosNao / resultado.totalVotos) * 100
                      : 0
                  }
                  color="error"
                  sx={{ height: 10, borderRadius: 5 }}
                />
              </Box>
            </Paper>
          )}
        </Grid>
      </Grid>

      <Dialog
        open={dialogSessaoOpen}
        onClose={() => setDialogSessaoOpen(false)}
      >
        <DialogTitle>Abrir Sessão de Votação</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Duração (minutos)"
            type="number"
            fullWidth
            value={duracaoMinutos}
            onChange={(e) => setDuracaoMinutos(e.target.value)}
            helperText="Padrão: 1 minuto"
            inputProps={{ min: 1 }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogSessaoOpen(false)}>Cancelar</Button>
          <Button onClick={handleAbrirSessao} variant="contained">
            Abrir
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={!!erro}
        autoHideDuration={5000}
        onClose={() => setErro("")}
      >
        <Alert severity="error" onClose={() => setErro("")}>
          {erro}
        </Alert>
      </Snackbar>
      <Snackbar
        open={!!sucesso}
        autoHideDuration={3000}
        onClose={() => setSucesso("")}
      >
        <Alert severity="success" onClose={() => setSucesso("")}>
          {sucesso}
        </Alert>
      </Snackbar>
    </>
  );
}

export default PautaDetailPage;
