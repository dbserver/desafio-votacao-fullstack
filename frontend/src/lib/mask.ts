import type { ChangeEvent } from "react";

export type Mask = (valor: string) => string;

export const onlyNumbers: Mask = (valor) => valor.replace(/\D/g, "");

export const withMask = (
  mask: Mask,
  onChange: (event: ChangeEvent<HTMLInputElement>) => void,
) => {
  return (event: ChangeEvent<HTMLInputElement>) => {
    event.target.value = mask(event.target.value);

    onChange(event);
  };
};
