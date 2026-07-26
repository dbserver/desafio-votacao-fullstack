import {
  Card,
  CardContent,
  Chip,
  CircularProgress,
  Divider,
  Stack,
  Typography,
} from "@mui/material";
import {
  AccessTimeRounded as AccessTimeRoundedIcon,
  DescriptionRounded as DescriptionRoundedIcon,
} from "@mui/icons-material";

interface PautaListItem {
  id: number;
  titulo: string;
  descricao: string;
  dthCriacao: string;
}

interface PautaListProps {
  pautas: PautaListItem[];
  carregando: boolean;
}

export function PautaList({ pautas, carregando }: PautaListProps) {
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
    <Stack spacing={2}>
      {pautas.map((pauta) => (
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
                label="Cadastrada"
                color="success"
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
              direction="row"
              alignItems="center"
              spacing={0.75}
              color="text.secondary"
            >
              <AccessTimeRoundedIcon fontSize="small" />

              <Typography variant="caption">
                Criada em{" "}
                {new Date(pauta.dthCriacao).toLocaleString("pt-BR", {
                  hour: "2-digit",
                  minute: "2-digit",
                })}
              </Typography>
            </Stack>
          </CardContent>
        </Card>
      ))}
    </Stack>
  );
}
