import { useState } from "react";
import type { SubmitEvent } from "react";
import { Box, Button, CircularProgress, Stack, TextField } from "@mui/material";
import { AddRounded as AddRoundedIcon } from "@mui/icons-material";

export interface NovaPauta {
  titulo: string;
  descricao: string;
}

interface PautaFormProps {
  salvando: boolean;
  onCadastrar: (pauta: NovaPauta) => Promise<boolean>;
}

export function PautaForm({ salvando, onCadastrar }: PautaFormProps) {
  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");

  async function handleSubmit(event: SubmitEvent<HTMLFormElement>) {
    event.preventDefault();

    const cadastrada = await onCadastrar({
      titulo,
      descricao,
    });

    if (cadastrada) {
      setTitulo("");
      setDescricao("");
    }
  }

  return (
    <Box component="form" onSubmit={handleSubmit}>
      <Stack spacing={2.5}>
        <TextField
          id="titulo"
          label="Título"
          value={titulo}
          onChange={(event) => setTitulo(event.target.value)}
          inputProps={{
            maxLength: 255,
          }}
          required
        />

        <TextField
          id="descricao"
          label="Descrição"
          value={descricao}
          onChange={(event) => setDescricao(event.target.value)}
          multiline
          minRows={5}
          inputProps={{
            maxLength: 1000,
          }}
          required
        />

        <Button
          type="submit"
          variant="contained"
          size="large"
          disabled={salvando}
          startIcon={
            salvando ? (
              <CircularProgress size={18} color="inherit" />
            ) : (
              <AddRoundedIcon />
            )
          }
        >
          {salvando ? "Cadastrando..." : "Cadastrar pauta"}
        </Button>
      </Stack>
    </Box>
  );
}
