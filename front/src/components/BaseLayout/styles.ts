import { DRAWER_WIDTH, HEADER_HEIGHT } from '@constants/measurements';
import { Paper, styled } from '@mui/material';

export const MainContainer = styled('main')(({ theme }) => ({
  boxSizing: 'border-box',
  marginTop: HEADER_HEIGHT,
  marginLeft: 0,
  overflow: 'auto',

  [theme.breakpoints.up('sm')]: {
    marginLeft: DRAWER_WIDTH,
    marginTop: 0,
  },
}));

export const PageContainer = styled(Paper)(({ theme }) => ({
  background: theme.palette.common.white,
  margin: '5%',
  padding: '3.5%',
  borderRadius: '16px',
}));
