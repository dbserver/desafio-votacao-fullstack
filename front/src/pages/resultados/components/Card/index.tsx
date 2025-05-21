import { StyledCard, CardHeader, CardContent, Title, Description } from './styles';
import { Grid2 as Grid, Typography } from '@mui/material';
import { COLOR_ERROR, COLOR_GRAY, COLOR_SUCCESS } from '@constants/colors';
import { useMemo } from 'react';
import type { IResultado } from '@customTypes/IResultado';

const ResultadoCard = ({ resultado }: { resultado: IResultado }) => {
  const pautaAprovada = useMemo(() => {
    return resultado.resultado === 'APROVADA';
  }, [resultado]);

  const pautaRejeitada = useMemo(() => {
    return resultado.resultado === 'REJEITADA';
  }, [resultado]);

  return (
    <Grid size={{ xs: 12, md: 6, lg: 4 }}>
      <StyledCard sx={{ backgroundColor: pautaAprovada ? COLOR_SUCCESS : pautaRejeitada ? COLOR_ERROR : COLOR_GRAY }}>
        <CardHeader>
          <Title>{resultado.titulo}</Title>
        </CardHeader>
        <CardContent sx={{ borderColor: pautaAprovada ? COLOR_SUCCESS : pautaRejeitada ? COLOR_ERROR : COLOR_GRAY }}>
          <Description>Total de votos: {resultado.totalVotos}</Description>
          <Description>Votos sim: {resultado.votosSim}</Description>
          <Description>Votos não: {resultado.votosNao}</Description>

          <Typography
            sx={{
              fontWeight: 'bold',
              color: pautaAprovada ? COLOR_SUCCESS : pautaRejeitada ? COLOR_ERROR : COLOR_GRAY,
              mt: '20px',
            }}
          >
            {resultado.resultado}
          </Typography>
        </CardContent>
      </StyledCard>
    </Grid>
  );
};

export default ResultadoCard;
