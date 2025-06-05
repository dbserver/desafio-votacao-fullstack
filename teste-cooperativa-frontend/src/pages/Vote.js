import React, { useEffect, useState } from 'react';
import { AppBar, Toolbar, IconButton, Typography, Button, Card, CardContent, Stack, CircularProgress, Alert } from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { useNavigate, useLocation } from 'react-router-dom';
import AgendaService from '../api/AgendaService';
import VoteService from '../api/VoteService';
import { useAssociate } from '../context/AssociateContext';

const Vote = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const session = location.state?.sessionId; 
    const [agendas, setAgendas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [voteStatus, setVoteStatus] = useState({});
    const [error, setError] = useState(null);
    const { associate } = useAssociate();

    useEffect(() => {
        if (!session) {
            alert('Session not provided. Try again.');
            return;
        }

        AgendaService.getAgendasBySessionId(session).then((response) => {
            setAgendas(response.data);
            verifyVotes(response.data);
            setLoading(false);
        }).catch((error) => {
            console.error('Error trying to get agendas: ', error.response);
            setError('Error trying to get agendas: ' + (error.response?.data?.errorMessage || 'Unknown error'));
            setLoading(false);
        })
    }, [session, navigate]);

    const verifyVotes = (agendas) => {
        agendas.forEach(agenda => fetchVoteStatus(agenda.id));
    };

    const fetchVoteStatus = (agendaId) => {
        const vote = {
            'associateId': associate.id,
            'agendaId': agendaId,
            'approve': true
        }
        VoteService.getVoteStatus(vote).then((response) => {
            const approved = response.data;
            if (!(approved === '')) {
                setVoteStatus(prev => ({
                    ...prev,
                    [agendaId]: approved ? 'approved' : 'rejected'
                }));
            }
            
        }).catch((error) => {
            console.error('Error fetching votes:', error);
            setError('Error fetching votes: ' + (error.response?.data?.errorMessage || 'Unknown error'));
        });
    }

    const handleVote = (agendaId, approved) => {
        setError(null);
        const voteData = {
            'associateId': associate.id,
            'agendaId': agendaId,
            'approve': approved
        }
        VoteService.applyVote(voteData).then((response) => {
            alert(response.data.message);
            setVoteStatus(prev => ({
                ...prev,
                [agendaId]: approved ? 'approved' : 'rejected'
            }));
        }).catch((error) => {
            console.error('Error registering vote:', error);
            setError('Error registering vote: ' + (error.response?.data?.errorMessage || 'Unknown error'));
        });
    };

    if (loading) {
        return (
            <Stack alignItems="center" mt={4}>
                <CircularProgress />
            </Stack>
        );
    }

    return (
        <div>
            <AppBar position="static">
                <Toolbar>
                    <IconButton edge="start" color="inherit" onClick={() => navigate('/sessions')}>
                        <ArrowBackIcon />
                    </IconButton>
                    <Typography variant="h6" sx={{ flexGrow: 1 }}>
                        Agenda Voting - Session {session}
                    </Typography>
                </Toolbar>
            </AppBar>
            <Stack spacing={3} mt={4} mx="auto" maxWidth={600}>
                {error && <Alert severity="error">{error}</Alert>}
                {agendas.length === 0 && (
                    <Typography variant="body1" align="center">
                        No agenda linked to this session.
                    </Typography>
                )}
                {agendas.map((agenda) => (
                    <Card key={agenda.id} variant="outlined">
                        <CardContent>
                            <Typography variant="body1" gutterBottom>
                                {agenda.description}
                            </Typography>
                            <Stack direction="row" spacing={2} mt={2}>
                                <Button
                                    variant={voteStatus[agenda.id] === 'approved' ? 'contained' : 'outlined'}
                                    color="success"
                                    disabled={!!voteStatus[agenda.id]}
                                    onClick={() => handleVote(agenda.id, true)}
                                >
                                    Approve
                                </Button>
                                <Button
                                    variant={voteStatus[agenda.id] === 'rejected' ? 'contained' : 'outlined'}
                                    color="error"
                                    disabled={!!voteStatus[agenda.id]}
                                    onClick={() => handleVote(agenda.id, false)}
                                >
                                    Denie
                                </Button>
                            </Stack>
                            {voteStatus[agenda.id] === 'approved' && (
                                <Alert severity="success" sx={{ mt: 2 }}>
                                    You approved this Agenda.
                                </Alert>
                            )}
                            {voteStatus[agenda.id] === 'rejected' && (
                                <Alert severity="warning" sx={{ mt: 2 }}>
                                    You denied this Agenda.
                                </Alert>
                            )}
                        </CardContent>
                    </Card>
                ))}
            </Stack>
        </div>
    );
};

export default Vote;