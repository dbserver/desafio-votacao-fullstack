import { Box, Button, Paper, TextField, Typography } from '@mui/material';
import Grid from '@mui/material/Grid';
import React, { useState } from 'react';
import { useAssociate } from '../context/AssociateContext';
import AssociateService from '../api/AssociateService';
import { useNavigate } from 'react-router-dom';

function Login() {
  const [document, setDocument] = useState('');
  const navigate = useNavigate();
  const { setAssociate } = useAssociate(null);

  const handleLogin = (document, setAssociate) => {
    if (!document) {
      alert('Please enter your document number.');
    } else {
      AssociateService.getAssociateByDocument(document)
      .then((response) => {
        setAssociate(response.data);
        navigate('/menu');
      })
      .catch((error) => {
        console.error('Error fetching associate:', error);
        alert('Error while doing login: ' + (error.response?.data?.errorMessage || 'Unknown error'));
      });  
    }
      
  };
  
  return (

    <Grid
      container
      justifyContent="center"
      alignItems="center"
      sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}
    >
      <Grid item xs={12} sm={9} md={4}>
        <Paper elevation={3} sx={{ p: 3, borderRadius: 2 }}>
          <Box mb={3} textAlign="center">
            <Typography variant="h5" component="h2">
              Enter your credentials to access.
            </Typography>
          </Box>
          <form>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  label="Document"
                  fullWidth
                  variant="standard"
                  value={document}
                  onChange={(e) => setDocument(e.target.value)}
                  required
                />
              </Grid>
              <Grid item xs={12}>
                <Button
                  type="submit"
                  variant="contained"
                  color="primary"
                  fullWidth
                  onClick={(e) => {
                    e.preventDefault();
                    handleLogin(document, setAssociate);
                  }}
                >
                  Access Cooperative
                </Button>
              </Grid>
              <Grid item xs={12}>
                <Button
                  type="button"
                  variant="text"
                  color="primary"
                  fullWidth
                  onClick={() => {
                    navigate('/createAssociate');
                  }}
                >
                  Create Associate
                </Button>
              </Grid>
            </Grid>
          </form>
        </Paper>
      </Grid>
    </Grid>
  );
}

export default Login;