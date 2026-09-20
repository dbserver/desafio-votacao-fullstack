import type { ChangeEvent } from "react";

export type Mask = (valor: string) => string;

export const onlyNumbers: Mask = (valor) => valor.replace(/\D/g, "");

export const cpfMask: Mask = (valor) => {
  const digits = onlyNumbers(valor).slice(0, 11);

  if (digits.length > 9) {
    return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6, 9)}-${digits.slice(9)}`;
  }

  if (digits.length > 6) {
    return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6)}`;
  }

  if (digits.length > 3) {
    return `${digits.slice(0, 3)}.${digits.slice(3)}`;
  }

  return digits;
};

export const withMask = (
  mask: Mask,
  onChange: (event: ChangeEvent<HTMLInputElement>) => void,
) => {
  return (event: ChangeEvent<HTMLInputElement>) => {
    event.target.value = mask(event.target.value);

    onChange(event);
  };
};
