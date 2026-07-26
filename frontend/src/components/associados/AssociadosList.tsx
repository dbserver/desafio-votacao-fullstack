import {
  Chip,
  CircularProgress,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { GroupsRounded as GroupsRoundedIcon } from "@mui/icons-material";

import { SectionCard } from "../layout/SectionCard";
import type { Associado } from "../../types/associado";
import { formatarCpf } from "../../utils/cpf";

interface AssociadoListProps {
  associados: Associado[];
  carregando: boolean;
}

export function AssociadoList({ associados, carregando }: AssociadoListProps) {
  return (
    <SectionCard
      eyebrow="Listagem"
      title="Associados cadastrados"
      description="Consulte o ID que deve ser utilizado no momento da votação."
    >
      {carregando ? (
        <Stack
          alignItems="center"
          justifyContent="center"
          spacing={1.5}
          sx={{ minHeight: 240 }}
        >
          <CircularProgress size={32} />

          <Typography variant="body2" color="text.secondary">
            Carregando associados...
          </Typography>
        </Stack>
      ) : associados.length === 0 ? (
        <Stack
          alignItems="center"
          justifyContent="center"
          spacing={1.5}
          sx={{
            minHeight: 240,
            textAlign: "center",
            color: "text.secondary",
          }}
        >
          <GroupsRoundedIcon sx={{ fontSize: 48 }} />

          <Typography variant="h6" color="text.primary">
            Nenhum associado cadastrado
          </Typography>

          <Typography variant="body2">
            Utilize o formulário ao lado para realizar o primeiro cadastro.
          </Typography>
        </Stack>
      ) : (
        <TableContainer sx={{ maxHeight: 480 }}>
          <Table stickyHeader aria-label="Associados cadastrados">
            <TableHead>
              <TableRow>
                <TableCell width={100}>
                  <strong>ID</strong>
                </TableCell>

                <TableCell>
                  <strong>CPF</strong>
                </TableCell>

                <TableCell>
                  <strong>Data do cadastro</strong>
                </TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {associados.map((associado) => {
                return (
                  <TableRow key={associado.id} hover>
                    <TableCell>
                      <Chip label={associado.id} size="small" />
                    </TableCell>

                    <TableCell>{formatarCpf(associado.cpf)}</TableCell>

                    <TableCell>
                      {new Date(associado.dthCriacao).toLocaleString("pt-BR", {
                        day: "2-digit",
                        month: "2-digit",
                        year: "numeric",
                        hour: "2-digit",
                        minute: "2-digit",
                      })}
                    </TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </SectionCard>
  );
}
