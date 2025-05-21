import { Box, Typography, type AlertColor } from '@mui/material';
import Button from '@components/Button';
import Input from '@components/Input';
import Modal from '@components/Modal';
import { COLOR_ERROR, COLOR_SUCCESS } from '@constants/colors';
import type { IPauta } from '@customTypes/IPauta';
import Loading from '@components/Loading';
import api from '@api/index';
import { useState } from 'react';

interface VotacaoModalProps {
  open: boolean;
  onClose: () => void;
  pauta: IPauta;
  displaySnackBar: (message?: string, severity?: AlertColor) => void;
}

const VotacaoModal = ({ open, onClose, pauta, displaySnackBar }: VotacaoModalProps) => {
  const [isLoading, setIsLoading] = useState(false);
  const [identificacao, setIdentificacao] = useState('');

  const sendVote = async (vote: boolean) => {
    if (!Number(identificacao)) {
      displaySnackBar('Preencha o campo de identificação corretamente');
      return;
    }

    setIsLoading(true);

    try {
      await api.post('/votos', {
        idPauta: pauta.id,
        associadoId: Number(identificacao),
        voto: vote,
      });

      displaySnackBar('Voto registrado com sucesso', 'success');
      setIdentificacao('');
      onClose();
    } catch (error: any) {
      if (error.response?.status === 400) {
        displaySnackBar(error.response.data?.mensagem || 'Erro de validação');
        setIdentificacao('');
      } else {
        displaySnackBar('Erro ao registrar voto');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal open={open} onClose={onClose} title={`Votação ${pauta.titulo}`}>
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
        <Typography>{pauta.descricao}</Typography>
        <Input
          label="Identificação"
          textHelper="Insira um valor numérico"
          value={identificacao}
          onChange={(e) => setIdentificacao(e.target.value)}
        />
        {isLoading ? (
          <Loading />
        ) : (
          <Box sx={{ display: 'flex', justifyContent: 'center', gap: '16px' }}>
            <Button onClick={() => sendVote(false)} isLoading={isLoading} sx={{ backgroundColor: COLOR_ERROR }}>
              Não
            </Button>
            <Button onClick={() => sendVote(true)} isLoading={isLoading} sx={{ backgroundColor: COLOR_SUCCESS }}>
              Sim
            </Button>
          </Box>
        )}
      </Box>
    </Modal>
  );
};

export default VotacaoModal;
