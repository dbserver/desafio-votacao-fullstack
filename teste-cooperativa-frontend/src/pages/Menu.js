import React from 'react';
import { AppBar, Toolbar, Typography, Container, Grid, Button, Box, Paper, IconButton } from '@mui/material';
import LogoutIcon from '@mui/icons-material/Logout';
import { useNavigate } from 'react-router-dom';
import { useAssociate } from '../context/AssociateContext';

function Menu() {
    const { associate } = useAssociate();
    const navigate = useNavigate();
    
    const menuOptions = [
        { label: 'View available Agendas', path: '/agendas' },
        { label: 'Create Agenda', path: '/createAgenda' },
        { label: 'Create Session', path: '/createSession' },
        { label: 'Start Session', path: '/sessions' },
    ];

    if (!associate) {
        return (
            <Container maxWidth="sm" sx={{ mt: 4 }}>
                <Paper elevation={3} sx={{ p: 3 }}>
                    <Typography variant="h5" align="center" gutterBottom>
                        Please log in to access the menu.
                    </Typography>
                </Paper>
            </Container>
        );
    }

    const handleBack = () => {
        navigate('/');
    }

    return (
        <>
            <AppBar position="static">
                <Toolbar>
                    <IconButton edge="start" color="inherit" onClick={handleBack} aria-label="voltar">
                        <LogoutIcon />
                    </IconButton>
                    <Typography variant="h6" sx={{ flexGrow: 1 }}>
                        Cooperative - Main Menu
                    </Typography>
                </Toolbar>
            </AppBar>
            <Container maxWidth="sm" sx={{ mt: 4 }}>
                <Paper elevation={3} sx={{ p: 3 }}>
                    <Typography variant="h5" align="center" gutterBottom>
                        Welcome, {associate.name}!
                    </Typography>
                    <Box mt={4}>
                        <Grid container spacing={3} direction="column">
                            {menuOptions.map((option) => (
                                <Grid item key={option.label}>
                                    <Button
                                        fullWidth
                                        variant="contained"
                                        color="primary"
                                        size="large"
                                        onClick={() => navigate(option.path)}
                                    >
                                        {option.label}
                                    </Button>
                                </Grid>
                            ))}
                        </Grid>
                    </Box>
                </Paper>
            </Container>
        </>
    )
}

export default Menu;