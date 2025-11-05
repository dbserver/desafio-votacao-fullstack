import React from 'react';
import Button from './Button';

type TModalProps = {
  children: React.ReactNode;
  onConfirm?: () => void;
  onCancel: () => void;
};

export default function Modal({ children, onConfirm, onCancel }: TModalProps) {
  return (
    <div className="fixed inset-0 z-[1000] flex h-screen w-screen items-center justify-center bg-black/50">
      <div className="flex w-[400px] max-w-[90%] flex-col gap-4 rounded-lg bg-white p-6 px-8 text-center text-[var(--color-dark)] shadow-[0_10px_25px_rgba(0,0,0,0.2)]">
        {children}
        <div className="flex items-center justify-center gap-2">
          {onConfirm && <Button onClick={onConfirm}>Confirmar</Button>}
          <Button onClick={onCancel}>Cancelar</Button>
        </div>
      </div>
    </div>
  );
}
