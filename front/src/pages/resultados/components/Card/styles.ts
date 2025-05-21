import { COLOR_WHITE } from '@constants/colors';
import { styled, Card, Typography } from '@mui/material';

export const StyledCard = styled(Card)(() => ({
  borderRadius: '25px',
  minWidth: '200px',
  display: 'flex',
  flexDirection: 'column',
  flex: 1,
  height: '100%',
}));

export const CardHeader = styled('div')({
  padding: '16px',
  paddingBottom: '8px',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
});

export const CardContent = styled('div')(({ theme }) => ({
  backgroundColor: theme.palette.common.white,
  borderRadius: '0 0 25px 25px',
  border: '2px solid',
  padding: '16px',
  paddingTop: '8px',
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'space-between',
  alignItems: 'center',
  flex: 1,
}));

export const Title = styled(Typography)({
  fontSize: '1.125rem',
  fontWeight: 'bold',
  color: COLOR_WHITE,
});

export const Description = styled(Typography)({
  fontSize: '1rem',
});
