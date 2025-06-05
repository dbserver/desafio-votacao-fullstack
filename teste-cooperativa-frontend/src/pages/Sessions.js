import React, { useEffect, useState } from 'react';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';

import {
    AppBar,
    Toolbar,
    IconButton,
    Typography,
    Container,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Button,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import SessionService from '../api/SessionService';

export default function Sessions() {
    const [sessions, setSessions] = useState([]);
    const navigate = useNavigate();

    const fetchSessions = () => {
        SessionService.getSessions().then((response) => {
            setSessions(response.data);
        }).catch((error) => {
            console.error('Error fetching sessions:', error);
            alert('Error fetching sessions: ' + (error.response?.data?.errorMessage || 'Unknown error'));
        });
    }

    useEffect(() => {
        fetchSessions();
    }, []);


    const handleTextEvent = (session) => { 
        if (session.startDate === null) {
            return 'Start Session';
        } else if (session.startDate != null && session.endDate === null) {
            return 'Vote in Agendas';
        } else if (session.endDate != null) {
            return 'View Agendas';
        }
        return '';
    }

    const handleSessionEvent = (session) => {
        if (session.startDate === null) {
            //start session
            SessionService.startSession(session.id)
                .then((response) => {
                    alert(response.data.message);
                    fetchSessions();
                })
                .catch((error) => {
                    console.error('Error starting session:', error);
                    alert('Error starting session: ' + (error.response?.data?.errorMessage || 'Unknown error'));
                });
        } else if (session.startDate != null && session.endDate === null) {
            //vote in agendas
            navigate('/vote', { state: { sessionId: session.id } });
        } else if (session.endDate != null) {
            //View agendas of this session
            navigate('/agendas', { state: { sessionId: session.id } });
        }
    }

    const handleBack = () => {
        navigate('/menu');
    };

    return (
        <>
            <AppBar position="static">
                <Toolbar>
                    <IconButton edge="start" color="inherit" onClick={handleBack} aria-label="voltar">
                        <ArrowBackIcon />
                    </IconButton>
                    <Typography variant="h6" sx={{ flexGrow: 1 }}>
                        Registred Sessions
                    </Typography>
                </Toolbar>
            </AppBar>
            <Container sx={{ mt: 4 }}>
                <Paper>
                    <TableContainer>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Id Session</TableCell>
                                    <TableCell>Duration (minutes)</TableCell>
                                    <TableCell>Start Date</TableCell>
                                    <TableCell>End Date</TableCell>
                                    <TableCell></TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {sessions.length === 0 ? (
                                    <TableRow>
                                        <TableCell colSpan={6} align="center">
                                            No session found.
                                        </TableCell>
                                    </TableRow>
                                ) : (
                                    sessions.map((session) => (
                                        <TableRow key={session.id}>
                                            <TableCell>{session.id}</TableCell>
                                            <TableCell>{session.durationTime}</TableCell>
                                            <TableCell>{session.startDate}</TableCell>
                                            <TableCell>{session.endDate}</TableCell>
                                            <TableCell>
                                                <Button
                                                    variant='outlined'
                                                    color="primary"
                                                    onClick={() => handleSessionEvent(session)}
                                                    title="Select a Session for this Agenda "
                                                    sx={{ maxWidth: '80px' }}
                                                >
                                                    {handleTextEvent(session)}
                                                </Button>
                                            </TableCell>
                                        </TableRow>
                                    ))
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Paper>
            </Container>
        </>
    );
}