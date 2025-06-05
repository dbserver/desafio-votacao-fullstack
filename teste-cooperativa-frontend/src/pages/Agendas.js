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
import { useLocation, useNavigate } from 'react-router-dom';
import AgendaService from '../api/AgendaService';
import DialogSession from '../component/DialogSession';

export default function Agendas({state}) {
    const [openDialog, setOpenDialog] = useState(false);
    const [agendas, setAgendas] = useState([]);
    const [agendaId, setAgendaId] = useState(null);
    const location = useLocation();
    const session = location.state?.sessionId; 
    const navigate = useNavigate();

    const fetchAgendas = () => {
        if (session) {
            AgendaService.getAgendasBySessionId(session).then((response) => {
                setAgendas(response.data);
            }).catch((error) => {
                console.error('Error fetching agendas by session ID:', error);
                alert('Error fetching agendas: ' + (error.response?.data?.errorMessage || 'Unknown error'));
            });
        } else {
            AgendaService.getAgendas().then((response) => {
                setAgendas(response.data);
            }).catch((error) => {
                console.error('Error fetching agendas:', error);
                alert('Error fetching agendas: ' + (error.response?.data?.errorMessage || 'Unknown error'));
            });
        }
        
    }

    useEffect(() => {
        fetchAgendas();
    }, [openDialog]);

    const addAgendaToSession = (agendaId) => {
        setAgendaId(agendaId);
        setOpenDialog(true);
    }

    const handleDialogClose = () => {
        setOpenDialog(false);
        setAgendaId(null);
        fetchAgendas();
    };

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
                        Registred Agendas
                    </Typography>
                </Toolbar>
            </AppBar>
            <Container sx={{ mt: 4 }}>
                <Paper>
                    <TableContainer>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Id Agenda</TableCell>
                                    <TableCell>Description</TableCell>
                                    <TableCell>Status</TableCell>
                                    <TableCell>Registration Date</TableCell>
                                    <TableCell>Result Date</TableCell>
                                    <TableCell>Session ID</TableCell>
                                    <TableCell></TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {agendas.length === 0 ? (
                                    <TableRow>
                                        <TableCell colSpan={6} align="center">
                                            No agenda found.
                                        </TableCell>
                                    </TableRow>
                                ) : (
                                    agendas.map((agenda) => (
                                        <TableRow key={agenda.id}>
                                            <TableCell>{agenda.id}</TableCell>
                                            <TableCell title={agenda.description}>
                                                {agenda.description.length > 50
                                                    ? agenda.description.slice(0, 50) + '...'
                                                    : agenda.description}
                                            </TableCell>
                                            <TableCell>{agenda.status}</TableCell>
                                            <TableCell>{agenda.registrationDate}</TableCell>
                                            <TableCell>{agenda.resultDate}</TableCell>
                                            <TableCell>{agenda.session != null ? agenda.session.id : ''}</TableCell>
                                            <TableCell>
                                                <Button
                                                    variant='outlined'
                                                    color="primary"
                                                    onClick={() => addAgendaToSession(agenda.id)}
                                                    title="Select a Session for this Agenda"
                                                    enable={agenda.session === null} 
                                                    sx={{ maxWidth: '80px' }}
                                                >
                                                    Set Session
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

            <DialogSession
                open={openDialog}
                agendaId={agendaId}
                handleDialogClose={handleDialogClose}
            />
        </>
    );
}