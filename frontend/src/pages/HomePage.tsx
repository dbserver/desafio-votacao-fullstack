import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Typography,
  Button,
  Grid,
  Card,
  CardContent,
  CardActions,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Alert,
  Snackbar,
  Chip,
  Box,
} from "@mui/material";
import { Add } from "@mui/icons-material";
import { Pauta } from "../types";
import { listarPautas, criarPauta } from "../services/pautaService";

function HomePage() {
  const navigate = useNavigate();
  const [pautas, setPautas] = useState<Pauta[]>([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");

  const carregarPautas = async () => {
    try {
      const { data } = await listarPautas();
      setPautas(data);
    } catch {
      setErro("Erro ao carregar pautas");
    }
  };

  useEffect(() => {
    carregarPautas();
  }, []);

  const handleCriarPauta = async () => {
    if (!titulo.trim()) {
      setErro("Título é obrigatório");
      return;
    }
    try {
      await criarPauta(titulo, descricao);
      setDialogOpen(false);
      setTitulo("");
      setDescricao("");
      setSucesso("Pauta criada com sucesso!");
      carregarPautas();
    } catch {
      setErro("Erro ao criar pauta");
    }
  };

  const handleFecharDialog = () => {
    setDialogOpen(false);
    setTitulo("");
    setDescricao("");
  };

  return (
    <>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 3,
        }}
      >
        <Typography variant="h4" fontWeight={500}>
          Pautas
        </Typography>
        <Button
          variant="contained"
          startIcon={<Add />}
          onClick={() => setDialogOpen(true)}
        >
          Nova Pauta
        </Button>
      </Box>

      <Grid container spacing={3}>
        {pautas.map((pauta) => (
          <Grid item xs={12} sm={6} md={4} key={pauta.id}>
            <Card
              sx={{
                height: "100%",
                display: "flex",
                flexDirection: "column",
                transition: "box-shadow 0.2s",
                "&:hover": { boxShadow: 4 },
              }}
            >
              <CardContent sx={{ flex: 1 }}>
                <Typography variant="h6" gutterBottom>
                  {pauta.titulo}
                </Typography>
                <Typography
                  variant="body2"
                  color="text.secondary"
                  sx={{ mb: 1 }}
                >
                  {pauta.descricao || "Sem descrição"}
                </Typography>
                <Chip
                  label={new Date(pauta.criadaEm).toLocaleDateString("pt-BR")}
                  size="small"
                  variant="outlined"
                />
              </CardContent>
              <CardActions>
                <Button
                  size="small"
                  onClick={() => navigate(`/pautas/${pauta.id}`)}
                >
                  Ver Detalhes
                </Button>
              </CardActions>
            </Card>
          </Grid>
        ))}
        {pautas.length === 0 && (
          <Grid item xs={12}>
            <Typography
              variant="body1"
              color="text.secondary"
              align="center"
              sx={{ py: 4 }}
            >
              Nenhuma pauta cadastrada. Clique em "Nova Pauta" para começar.
            </Typography>
          </Grid>
        )}
      </Grid>

      <Dialog
        open={dialogOpen}
        onClose={handleFecharDialog}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Nova Pauta</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Título"
            fullWidth
            required
            value={titulo}
            onChange={(e) => setTitulo(e.target.value)}
          />
          <TextField
            margin="dense"
            label="Descrição"
            fullWidth
            multiline
            rows={3}
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleFecharDialog}>Cancelar</Button>
          <Button onClick={handleCriarPauta} variant="contained">
            Criar
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={!!erro}
        autoHideDuration={4000}
        onClose={() => setErro("")}
      >
        <Alert severity="error" onClose={() => setErro("")}>
          {erro}
        </Alert>
      </Snackbar>
      <Snackbar
        open={!!sucesso}
        autoHideDuration={4000}
        onClose={() => setSucesso("")}
      >
        <Alert severity="success" onClose={() => setSucesso("")}>
          {sucesso}
        </Alert>
      </Snackbar>
    </>
  );
}

export default HomePage;
