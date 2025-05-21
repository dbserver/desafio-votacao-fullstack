import Button from '@components/Button';
import { Box, Grid2 as Grid, Typography, type AlertColor } from '@mui/material';
import PautaCard from './components/Card';
import type { IPauta } from '@customTypes/IPauta';
import { useEffect, useState } from 'react';
import AddPautaModal from './components/AddPautaModal';
import VotacaoModal from './components/VotacaoModal';
import OpenSessaoModal from './components/OpenSessaoModal';
import api from '@api/index';
import Loading from '@components/Loading';
import Snackbar from '@components/Snackbar';
import moment from 'moment';

const Pautas = () => {
  const [isOpenAddPautaModal, setIsOpenAddPautaModal] = useState(false);
  const [isOpenVotacaoModal, setIsOpenVotacaoModal] = useState(false);
  const [isOpenSessaoModal, setIsOpenSessaoModal] = useState(false);
  const [selectedPauta, setSelectedPauta] = useState<IPauta>({
    id: 0,
    titulo: '',
    descricao: '',
    inicioSessao: null,
    fimSessao: null,
  });
  const [pautas, setPautas] = useState<IPauta[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const [displaySnackbar, setDisplaySnackbar] = useState(false);
  const [message, setMessage] = useState('');
  const [severity, setSeverity] = useState<AlertColor>();

  const handleOpenPautaModal = (pauta: IPauta) => {
    setSelectedPauta(pauta);
    setIsOpenVotacaoModal(true);
  };

  const handleOpenSessaoModal = (pauta: IPauta) => {
    setSelectedPauta(pauta);
    setIsOpenSessaoModal(true);
  };

  const getPautas = async () => {
    try {
      const response = await api.get('/pautas');
      if (response.status === 200 && Array.isArray(response.data)) {
        setPautas(response.data);
      }
    } catch (err) {
      console.log(err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    getPautas();
  }, []);

  const displaySnackBar = (message?: string, severity?: AlertColor) => {
    setMessage(message || 'Um erro ocorreu, por favor tente mais tarde');
    setSeverity(severity || 'error');
    setDisplaySnackbar(true);
  };

  return (
    <>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          mb: 4,
          px: 2,
          py: 2,
          borderRadius: 2,
        }}
      >
        <Typography
          variant="h4"
          sx={{
            fontWeight: 700,
          }}
        >
          🗳️ Pautas Abertas
        </Typography>

        <Button
          variant="contained"
          color="primary"
          sx={{
            width: '160px',
            fontWeight: 600,
            boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
            textTransform: 'none',
            borderRadius: '8px',
          }}
          onClick={() => setIsOpenAddPautaModal(true)}
          disabled={isLoading}
        >
          + Adicionar
        </Button>
      </Box>

      {isLoading ? (
        <Loading />
      ) : (
        <Grid container spacing={2}>
          {pautas
            .filter((pauta) => pauta.fimSessao === null || moment.utc(pauta.fimSessao).isAfter(moment.utc()))
            .map((pauta) => (
              <PautaCard
                key={pauta.id}
                pauta={pauta}
                onClick={() => handleOpenPautaModal(pauta)}
                onClickIcon={() => handleOpenSessaoModal(pauta)}
              />
            ))}
        </Grid>
      )}

      <AddPautaModal
        open={isOpenAddPautaModal}
        onClose={() => setIsOpenAddPautaModal(false)}
        getPautas={getPautas}
        displaySnackBar={displaySnackBar}
      />
      <VotacaoModal
        open={isOpenVotacaoModal}
        onClose={() => setIsOpenVotacaoModal(false)}
        pauta={selectedPauta}
        displaySnackBar={displaySnackBar}
      />
      <OpenSessaoModal
        open={isOpenSessaoModal}
        onClose={() => setIsOpenSessaoModal(false)}
        getPautas={getPautas}
        pauta={selectedPauta}
        displaySnackBar={displaySnackBar}
      />
      <Snackbar
        open={displaySnackbar}
        message={message}
        onClose={() => setDisplaySnackbar(false)}
        severity={severity}
      />
    </>
  );
};

export default Pautas;
