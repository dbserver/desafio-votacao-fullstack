import React, { useState } from 'react';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { useNavigate } from 'react-router-dom';
import AgendaService from '../api/AgendaService';
import {
    AppBar,
    Toolbar,
    IconButton,
    Typography,
    Container,
    TextField,
    Button,
    Box,
} from '@mui/material';

const AgendaForm = () => {
    const [description, setDescription] = useState('');
    const navigate = useNavigate();

    const handleBack = () => {
        navigate('/menu');
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        AgendaService.createAgenda(description)
            .then((response) => {
                alert(response.data.message);
                setDescription('');
            })
            .catch((error) => {
                console.error('Error creating agenda:', error);
                alert('Error while creating agenda: ' + (error.message || 'Unknown error'));
            });
        
    };

    return (
        <>
            <AppBar position="static">
                <Toolbar>
                    <IconButton edge="start" color="inherit" onClick={handleBack} aria-label="voltar">
                        <ArrowBackIcon />
                    </IconButton>
                    <Typography variant="h6" component="div">
                        New Agenda
                    </Typography>
                </Toolbar>
            </AppBar>
            <Container maxWidth="sm" sx={{ mt: 4 }}>
                <Box
                    component="form"
                    onSubmit={handleSubmit}
                    display="flex"
                    flexDirection="column"
                    gap={3}
                >
                    <Typography variant="h5" align="center">
                        Create a New Agenda
                    </Typography>
                    <TextField
                        label="Description of the Agenda"
                        multiline
                        minRows={6}
                        variant="outlined"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                        fullWidth
                    />
                    <Button type="submit" variant="contained" color="primary" size="large">
                        Submit
                    </Button>
                </Box>
            </Container>
        </>
    );
}


export default AgendaForm;