import React, { useState } from 'react';
import { AppBar, Toolbar, IconButton, Typography, TextField, Button, Box, Container } from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { useNavigate } from 'react-router-dom';
import SessionService from '../api/SessionService';

export default function SessionForm() {
    const [duration, setDuration] = useState('');
    const [error, setError] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        const value = e.target.value;
        if (/^\d*$/.test(value)) {
            setDuration(value);
            setError(value !== '' && (parseInt(value, 10) <= 0));
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (duration === '' || parseInt(duration, 10) <= 0) {
            setError(true);
            return;
        }
        SessionService.createSession(duration)
            .then((response) => {
                alert(response.data.message);
                setDuration('');
            })
            .catch((error) => {
                console.error('Error creating session:', error);
                alert('Error creating session: ' + (error.response?.data?.errorMessage || 'Unknown error'));
            });
        
    };

    return (
        <>
            <AppBar position="static">
                <Toolbar>
                    <IconButton edge="start" color="inherit" onClick={() => navigate('/menu')}>
                        <ArrowBackIcon />
                    </IconButton>
                    <Typography variant="h6" component="div">
                        Create Session
                    </Typography>
                </Toolbar>
            </AppBar>
            <Container maxWidth="sm">
                <Box mt={5} component="form" onSubmit={handleSubmit} display="flex" flexDirection="column" gap={2}>
                    <TextField
                        label="Duration (minutes)"
                        type="text"
                        value={duration}
                        onChange={handleChange}
                        error={error}
                        helperText={error ? "Type a positive and valid number!" : ""}
                        inputProps={{ inputMode: 'numeric', pattern: '[0-9]*', min: 1 }}
                        required
                        fullWidth
                    />
                    <Button type="submit" variant="contained" color="primary">
                        Submit
                    </Button>
                </Box>
            </Container>
        </>
    );
}