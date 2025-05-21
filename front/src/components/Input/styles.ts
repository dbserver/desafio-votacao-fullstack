import { FormLabel, InputBase, inputBaseClasses } from '@mui/material';
import { alpha, styled } from '@mui/system';

export const StyledLabel = styled(FormLabel)(({ theme: { palette } }) => ({
  color: palette.common.black,
  fontSize: '1.125rem',
}));

export const StyledInput = styled(InputBase)(({ theme: { palette } }) => ({
  border: `1px solid ${palette.secondary.main}`,
  borderRadius: '8px',
  padding: '4px 16px',
  gap: '8px',
  svg: {
    color: palette.text.secondary,
  },
  input: {
    '&::placeholder': {
      color: palette.text.secondary,
      opacity: 1,
    },
  },
  '.MuiInputBase-inputMultiline': {
    padding: '4px 0px',
    '&::placeholder': {
      color: palette.text.secondary,
      opacity: 1,
    },
  },
  '&:hover': {
    borderColor: palette.secondary.main,
  },
  [`&.${inputBaseClasses.disabled}`]: {
    backgroundColor: alpha(palette.secondary.light, 0.25),
    borderColor: alpha(palette.secondary.main, 0.75),
    input: {
      color: palette.secondary.main,
      fontWeight: 300,
    },
  },
  [`&.${inputBaseClasses.focused}`]: {
    borderColor: palette.primary.main,
  },
  [`&.${inputBaseClasses.error}`]: {
    borderColor: palette.error.main,
  },
}));
