import { FormControl, FormHelperText, type InputProps as IMUIProps, type SxProps } from '@mui/material';
import { StyledInput, StyledLabel } from './styles';

export interface InputProps extends IMUIProps {
  formSx?: SxProps;
  label?: string;
  textHelper?: string;
  error?: boolean;
  disabled?: boolean;
}

const Input = ({ label, textHelper, error, disabled, formSx, ...props }: InputProps) => {
  return (
    <FormControl error={error} sx={formSx}>
      <StyledLabel htmlFor={label}>{label}</StyledLabel>
      <StyledInput id={label} disabled={disabled} {...props} />
      <FormHelperText sx={{ fontSize: '14px' }}>{textHelper}</FormHelperText>
    </FormControl>
  );
};

export default Input;
