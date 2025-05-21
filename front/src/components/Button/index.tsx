import { CircularProgress } from '@mui/material';
import { type ButtonProps } from '@mui/material/Button';
import { StyledButton } from './styles';

interface ICustomButton extends ButtonProps {
  isLoading?: boolean;
  variant?: 'contained' | 'outlined' | 'text';
  color?: 'inherit' | 'primary' | 'secondary' | 'success' | 'error' | 'info' | 'warning';
}

const Button = ({ children, isLoading, variant = 'contained', disabled, ...props }: ICustomButton) => {
  return (
    <StyledButton variant={variant} disabled={isLoading || disabled} disableElevation {...props}>
      {isLoading && <CircularProgress size={20} />}
      {children}
    </StyledButton>
  );
};

export default Button;
