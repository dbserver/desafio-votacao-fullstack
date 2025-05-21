import { Box, CircularProgress, Typography } from '@mui/material';

interface LoadingProps {
  message?: string;
}

const Loading = ({ message = 'Carregando...' }: LoadingProps) => {
  return (
    <Box sx={{ display: 'flex', alignItems: 'center', gap: '8px', marginTop: '16px' }}>
      <CircularProgress size={16} />
      <Typography>{message}</Typography>
    </Box>
  );
};

export default Loading;
