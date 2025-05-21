import { HEADER_HEIGHT } from '@constants/measurements';
import { AppBar, styled } from '@mui/material';

export const StyledAppBar = styled(AppBar)(({ theme }) => ({
  position: 'fixed',
  height: HEADER_HEIGHT,
  background: theme.palette.common.white,
  color: theme.palette.common.black,
  [theme.breakpoints.up('sm')]: {
    display: 'none',
  },
}));
