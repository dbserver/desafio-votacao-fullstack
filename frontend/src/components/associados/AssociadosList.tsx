import {
  Chip,
  CircularProgress,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  Typography,
} from "@mui/material";
import { GroupsRounded as GroupsRoundedIcon } from "@mui/icons-material";

import { SectionCard } from "../layout/SectionCard";
import type { Associado } from "../../types/associado";
import { formatarCpf } from "../../utils/cpf.ts";

interface AssociadoListProps {
  associados: Associado[];
  carregando: boolean;
  associadoDestacadoId?: number | null;
  pagina: number;
  tamanhoPagina: number;
  totalElementos: number;
  onMudarPagina: (pagina: number) => void;
  onMudarTamanhoPagina: (tamanho: number) => void;
}

export function AssociadoList({
  associados,
  carregando,
  associadoDestacadoId,
  pagina,
  tamanhoPagina,
  totalElementos,
  onMudarPagina,
  onMudarTamanhoPagina,
}: AssociadoListProps) {
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
        <>
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
                  const estaDestacado = associado.id === associadoDestacadoId;

                  return (
                    <TableRow
                      key={associado.id}
                      hover
                      sx={{
                        bgcolor: estaDestacado ? "success.light" : undefined,
                      }}
                    >
                      <TableCell>
                        <Chip
                          label={associado.id}
                          size="small"
                          color={estaDestacado ? "success" : "default"}
                        />
                      </TableCell>

                      <TableCell>{formatarCpf(associado.cpf)}</TableCell>

                      <TableCell>
                        {new Date(associado.dthCriacao).toLocaleString("pt-BR")}
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </TableContainer>

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
          />
        </>
      )}
    </SectionCard>
  );
}
