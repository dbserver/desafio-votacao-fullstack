import { styled } from '@mui/system';
import { Dialog, DialogTitle, IconButton } from '@mui/material';

export const StyledDialog = styled(Dialog)(() => ({
  '& .MuiPaper-root': {
    borderRadius: '20px',
    width: '560px',
    display: 'flex',
    flexDirection: 'column',
    padding: '0 32px 16px 32px',
  },

  '@media (max-width: 600px)': {
    '& .MuiPaper-root': {
      width: '90%',
      height: 'auto',
      maxHeight: '80vh',
    },
  },
}));

export const CloseButton = styled(IconButton)(() => ({
  position: 'absolute',
  right: '16px',
  top: '10px',
}));

export const StyledTitle = styled(DialogTitle)(({ theme }) => ({
  color: theme.palette.secondary.main,
  textAlign: 'center',
  fontWeight: 'bold',
}));
