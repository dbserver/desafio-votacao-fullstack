import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import {
  Button,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { PlayArrowRounded as PlayArrowRoundedIcon } from "@mui/icons-material";

interface AbrirSessaoDialogProps {
  open: boolean;
  pautaTitulo: string;
  abrindo: boolean;
  onClose: () => void;
  onConfirmar: (duracao?: number) => Promise<boolean>;
}

export function AbrirSessaoDialog({
  open,
  pautaTitulo,
  abrindo,
  onClose,
  onConfirmar,
}: AbrirSessaoDialogProps) {
  const [duracao, setDuracao] = useState("");
  const [erro, setErro] = useState("");

  useEffect(() => {
    if (open) {
      setDuracao("");
      setErro("");
    }
  }, [open]);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const valorInformado = duracao.trim();

    if (valorInformado === "") {
      const sucesso = await onConfirmar();

      if (sucesso) {
        onClose();
      }

      return;
    }

    const duracaoNumerica = Number(valorInformado);

    if (!Number.isInteger(duracaoNumerica) || duracaoNumerica <= 0) {
      setErro("Informe uma duração inteira maior que zero.");
      return;
    }

    setErro("");

    const sucesso = await onConfirmar(duracaoNumerica);

    if (sucesso) {
      onClose();
    }
  }

  return (
    <Dialog
      open={open}
      onClose={() => {
        if (!abrindo) {
          onClose();
        }
      }}
      fullWidth
      maxWidth="xs"
    >
      <DialogTitle>Abrir sessão de votação</DialogTitle>

      <DialogContent>
        <Stack
          component="form"
          id="abrir-sessao-form"
          onSubmit={handleSubmit}
          spacing={2.5}
          sx={{ pt: 1 }}
        >
          <Typography variant="body2" color="text.secondary">
            Defina por quanto tempo a pauta <strong>{pautaTitulo}</strong>{" "}
            ficará disponível para votação.
          </Typography>

          <TextField
            label="Duração em minutos"
            value={duracao}
            onChange={(event) => {
              setDuracao(event.target.value);
              setErro("");
            }}
            type="number"
            placeholder="1"
            error={Boolean(erro)}
            helperText={
              erro || "Deixe vazio para utilizar o padrão de 1 minuto."
            }
            inputProps={{
              min: 1,
              step: 1,
            }}
            autoFocus
          />
        </Stack>
      </DialogContent>

      <DialogActions sx={{ px: 3, pb: 3 }}>
        <Button onClick={onClose} disabled={abrindo}>
          Cancelar
        </Button>

        <Button
          type="submit"
          form="abrir-sessao-form"
          variant="contained"
          disabled={abrindo}
          startIcon={
            abrindo ? (
              <CircularProgress size={18} color="inherit" />
            ) : (
              <PlayArrowRoundedIcon />
            )
          }
        >
          {abrindo ? "Abrindo..." : "Abrir sessão"}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
