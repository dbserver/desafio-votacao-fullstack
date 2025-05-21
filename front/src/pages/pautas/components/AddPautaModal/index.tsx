import { Box, type AlertColor } from '@mui/material';
import Button from '@components/Button';
import Input from '@components/Input';
import Modal from '@components/Modal';
import api from '@api/index';
import { useState } from 'react';

interface AddPautaModalProps {
  open: boolean;
  onClose: () => void;
  getPautas: () => void;
  displaySnackBar: (message?: string, severity?: AlertColor) => void;
}

const AddPautaModal = ({ open, onClose, getPautas, displaySnackBar }: AddPautaModalProps) => {
  const [isLoading, setIsLoading] = useState(false);
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');

  const createPauta = async () => {
    if (!titulo || !descricao) {
      displaySnackBar('Preencha todos os campos');
      return;
    }

    setIsLoading(true);
    try {
      const response = await api.post('/pautas', {
        titulo: titulo,
        descricao: descricao,
      });
      if (response.status === 200 || response.status === 201) {
        displaySnackBar('Pauta criada com sucesso', 'success');
        setTitulo('');
        setDescricao('');
        onClose();
        getPautas();
      }
    } catch (err) {
      console.log(err);
      displaySnackBar('Erro ao criar pauta');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal open={open} onClose={onClose} title="Adicionar pauta">
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        <Input label="Título" value={titulo} onChange={(e) => setTitulo(e.target.value)} />
        <Input label="Descrição" value={descricao} onChange={(e) => setDescricao(e.target.value)} />
        <Button onClick={() => createPauta()} isLoading={isLoading} sx={{ width: '200px', alignSelf: 'center' }}>
          Confirmar
        </Button>
      </Box>
    </Modal>
  );
};

export default AddPautaModal;
