import { Box, type AlertColor } from '@mui/material';
import Button from '@components/Button';
import Input from '@components/Input';
import Modal from '@components/Modal';
import type { IPauta } from '@customTypes/IPauta';
import api from '@api/index';
import { useState } from 'react';

interface OpenSessaoModalProps {
  open: boolean;
  onClose: () => void;
  getPautas: () => void;
  pauta: IPauta;
  displaySnackBar: (message?: string, severity?: AlertColor) => void;
}

const OpenSessaoModal = ({ open, onClose, getPautas, pauta, displaySnackBar }: OpenSessaoModalProps) => {
  const [isLoading, setIsLoading] = useState(false);
  const [minutos, setMinutos] = useState(0);

  const openSessao = async () => {
    setIsLoading(true);
    try {
      const response = await api.post('/sessoes', {
        idPauta: pauta.id,
        minutos: minutos === 0 ? 1 : minutos,
      });
      if (response.status === 200 || response.status === 201) {
        displaySnackBar('Sessão aberta criada com sucesso', 'success');
        setMinutos(0);
        onClose();
        getPautas();
      } else {
        displaySnackBar(response.data.mensagem);
        setMinutos(0);
        onClose();
      }
    } catch (err) {
      console.log(err);
      displaySnackBar('Erro ao abrir sessão');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal open={open} onClose={onClose} title={`Abrir sessão ${pauta.titulo}`}>
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
        <Input
          label="Tempo de duração"
          type="number"
          textHelper="Tempo padrão é 1 minuto"
          value={minutos}
          onChange={(e) => setMinutos(Number(e.target.value))}
        />
        <Button onClick={() => openSessao()} isLoading={isLoading}>
          Abrir
        </Button>
      </Box>
    </Modal>
  );
};

export default OpenSessaoModal;
