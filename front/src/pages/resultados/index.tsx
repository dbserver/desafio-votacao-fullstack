import type { IResultado } from '@customTypes/IResultado';
import { Divider, Typography, Grid2 as Grid } from '@mui/material';
import ResultadoCard from './components/Card';
import { useEffect, useState } from 'react';
import api from '@api/index';
import Loading from '@components/Loading';

const Resultados = () => {
  const [resultados, setResultados] = useState<IResultado[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const getResultados = async () => {
    try {
      const response = await api.get('/votos/resultado');
      if (response.status === 200 && Array.isArray(response.data)) {
        setResultados(response.data);
      }
    } catch (err) {
      console.log(err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    getResultados();
  }, []);

  return (
    <>
      <Typography
        variant="h4"
        sx={{
          fontWeight: 700,
        }}
      >
        📊 Resultados
      </Typography>
      {isLoading ? (
        <Loading />
      ) : (
        <>
          <Grid container spacing={2}>
            {resultados
              .filter((resultado) => resultado.votosSim > resultado.votosNao)
              .map((resultado) => (
                <ResultadoCard key={resultado.idPauta} resultado={resultado} />
              ))}
          </Grid>
          <Divider sx={{ margin: '2rem 0' }} />
          <Grid container spacing={2}>
            {resultados
              .filter((resultado) => resultado.votosNao > resultado.votosSim)
              .map((resultado) => (
                <ResultadoCard key={resultado.idPauta} resultado={resultado} />
              ))}
          </Grid>
          <Divider sx={{ margin: '2rem 0' }} />
          <Grid container spacing={2}>
            {resultados
              .filter((resultado) => resultado.votosNao === resultado.votosSim)
              .map((resultado) => (
                <ResultadoCard key={resultado.idPauta} resultado={resultado} />
              ))}
          </Grid>
        </>
      )}
    </>
  );
};

export default Resultados;
