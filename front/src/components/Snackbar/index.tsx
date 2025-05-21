import { Alert, type AlertColor, Snackbar as SnackbarMUI } from '@mui/material';

interface SnackbarProps {
  open: boolean;
  onClose: () => void;
  message: string;
  severity?: AlertColor;
}

const Snackbar = ({ open, onClose, message, severity = 'error' }: SnackbarProps) => {
  return (
    <SnackbarMUI
      anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      open={open}
      autoHideDuration={3000}
      message={message}
      onClose={onClose}
    >
      <Alert severity={severity}>{message}</Alert>
    </SnackbarMUI>
  );
};

export default Snackbar;
