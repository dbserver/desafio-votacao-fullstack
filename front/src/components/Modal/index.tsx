import React, { type ReactNode } from 'react';
import CloseIcon from '@mui/icons-material/Close';
import { StyledDialog, CloseButton, StyledTitle } from '@components/Modal/styles';

interface ModalProps {
  open: boolean;
  onClose: () => void;
  title: string;
  children?: ReactNode;
}

const Modal: React.FC<ModalProps> = ({ open, onClose, title, children }) => {
  return (
    <StyledDialog open={open} onClose={onClose}>
      <CloseButton onClick={onClose}>
        <CloseIcon />
      </CloseButton>
      <StyledTitle>{title}</StyledTitle>
      {children}
    </StyledDialog>
  );
};

export default Modal;
