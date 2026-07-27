import { useState } from "react";
import type { SubmitEvent } from "react";
import { Box, Button, CircularProgress, Stack, TextField } from "@mui/material";
import { PersonAddRounded as PersonAddRoundedIcon } from "@mui/icons-material";
import { formatarCpf } from "../../utils/cpf";

function somenteNumeros(valor: string): string {
  return valor.replace(/\D/g, "").slice(0, 11);
}

interface AssociadoFormProps {
  salvando: boolean;
  onCadastrar: (cpf: string) => Promise<boolean>;
}

export function AssociadoForm({ salvando, onCadastrar }: AssociadoFormProps) {
  const [cpf, setCpf] = useState("");
  const [cpfInvalido, setCpfInvalido] = useState(false);

  async function handleSubmit(event: SubmitEvent<HTMLFormElement>) {
    event.preventDefault();

    if (cpf.length !== 11) {
      setCpfInvalido(true);
      return;
    }

    const cadastrado = await onCadastrar(cpf);

    if (cadastrado) {
      setCpf("");
      setCpfInvalido(false);
    }
  }

  return (
    <Box component="form" onSubmit={handleSubmit} noValidate>
      <Stack spacing={2.5}>
        <TextField
          id="cpf"
          label="CPF"
          value={formatarCpf(cpf)}
          onChange={(event) => {
            setCpf(somenteNumeros(event.target.value));
            setCpfInvalido(false);
          }}
          placeholder="000.000.000-00"
          error={cpfInvalido}
          helperText={
            cpfInvalido
              ? "Informe um CPF com exatamente 11 números."
              : "Digite apenas os 11 números do CPF."
          }
          inputProps={{
            inputMode: "numeric",
            maxLength: 14,
          }}
          autoComplete="off"
          autoFocus
          required
          fullWidth
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
              <PersonAddRoundedIcon />
            )
          }
        >
          {salvando ? "Cadastrando..." : "Cadastrar associado"}
        </Button>
      </Stack>
    </Box>
  );
}
