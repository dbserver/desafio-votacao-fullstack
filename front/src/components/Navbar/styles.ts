import { DRAWER_WIDTH } from '@constants/measurements';
import { Drawer, styled } from '@mui/material';

export const StyledDrawer = styled(Drawer)(() => ({
  '& .MuiDrawer-paper': {
    boxSizing: 'border-box',
    width: DRAWER_WIDTH,
    padding: '1rem',
    gap: '20px',
  },
}));
