import { StyledCard, CardHeader, CardContent, ButtonContainer, StyledButton, Title, Description } from './styles';
import { Box, Grid2 as Grid, IconButton, Tooltip } from '@mui/material';
import { type IPauta } from '@customTypes/IPauta';
import { LockOpen } from '@mui/icons-material';
import { COLOR_WHITE } from '@constants/colors';
import { useMemo } from 'react';

const PautaCard = ({
  pauta,
  onClick,
  onClickIcon,
}: {
  pauta: IPauta;
  onClick: React.MouseEventHandler<HTMLButtonElement>;
  onClickIcon?: React.MouseEventHandler<HTMLButtonElement>;
}) => {
  const isClosed = useMemo(() => {
    return pauta.fimSessao !== null;
  }, [pauta]);

  return (
    <Grid size={{ xs: 12, md: 6, lg: 4 }}>
      <StyledCard>
        <CardHeader sx={{ justifyContent: isClosed ? 'center' : 'space-between' }}>
          <Title>{pauta.titulo}</Title>
          {isClosed || !onClickIcon ? (
            <Box height={40} />
          ) : (
            <Tooltip title="Clique para abrir sessão" placement="top">
              <IconButton onClick={onClickIcon}>
                <LockOpen sx={{ color: COLOR_WHITE }} />
              </IconButton>
            </Tooltip>
          )}
        </CardHeader>
        <CardContent>
          <Description>{pauta.descricao}</Description>
          <ButtonContainer>
            <StyledButton onClick={onClick} disabled={!isClosed}>
              Votar
            </StyledButton>
          </ButtonContainer>
        </CardContent>
      </StyledCard>
    </Grid>
  );
};

export default PautaCard;
