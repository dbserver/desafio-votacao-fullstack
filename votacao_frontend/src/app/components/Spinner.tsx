import React from 'react';
import { PiSpinnerGapThin } from 'react-icons/pi';

type TSpinnerProps = {
  size: number;
};

export default function Spinner({ size }: TSpinnerProps) {
  return (
    <PiSpinnerGapThin
      size={size}
      className="animate-spin"
      aria-label="Carregando conteúdo, por favor aguarde"
    />
  );
}
