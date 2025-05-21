import { Button, styled } from '@mui/material';

export const StyledButton = styled(Button)(() => ({
  padding: '8px 16px',
  textTransform: 'none',
  letterSpacing: '1px',
  fontWeight: '500',
  fontSize: '1rem',
  gap: '8px',
  borderRadius: '8px',
  minWidth: '200px',
  '&:focus': {
    outline: 'none',
  },
}));
