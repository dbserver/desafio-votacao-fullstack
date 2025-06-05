import { Button, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle, FormControl, InputLabel, MenuItem, Select } from "@mui/material";
import { useEffect, useState } from "react";
import SessionService from "../api/SessionService";
import AgendaService from "../api/AgendaService";

export default function DialogSession({ open, agendaId, handleDialogClose }) {
    const [sessions, setSessions] = useState([]);
    const [selectedSessionId, setSelectedSessionId] = useState('');
    const [loadingSessions, setLoadingSessions] = useState(true);
    const [linking, setLinking] = useState(false);

    useEffect(() => {
        if (open) {
            setLoadingSessions(true);
            SessionService.getAllSessionsNotInitialized().then((res) => {
                setSessions(res.data);
                setLoadingSessions(false);
            }).catch((error) => {
                console.error('Error fetching sessions:', error);
                alert('Error fetching sessions: ' + (error.response?.data?.errorMessagemessage || 'Unknown error'));
                handleDialogClose();
            })
        }
    }, [open]);

    const handleSessionSelected = (event) => {
        setSelectedSessionId(event.target.value);
        setLinking(true);

        if (event.target.value) {
            AgendaService.addAgendaToSession(agendaId, event.target.value).then((res) => {
                alert(res.data.message);           
                setLinking(false);
                handleDialogClose();
                setSelectedSessionId('');
            }).catch((error) => {
                setSelectedSessionId('');
                console.error('Error linking agenda to session:', error);
                alert('Error linking agenda to session: ' + (error.response?.data?.errorMessage || 'Unknown error'));
            });
        }
    };

    return (
        <>
            <Dialog open={open} onClose={handleDialogClose}>
                <DialogTitle>{`Vinculate Agenda ${agendaId} to Session`}</DialogTitle>
                <DialogContent>
                    {loadingSessions ? (
                        <CircularProgress />
                    ) : (

                        <FormControl fullWidth sx={{ mt: 2 }}>
                            <InputLabel id="session-select-label">Session</InputLabel>
                            <Select
                                labelId="session-select-label"
                                value={selectedSessionId}
                                label="Session"
                                onChange={handleSessionSelected}
                            >
                            {sessions.map((session) => (
                                <MenuItem key={session.id} value={session.id}>
                                    {`Session ${session.id}`}
                                </MenuItem>
                            ))}
                            </Select>
                        </FormControl>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDialogClose} disabled={linking}>Cancell</Button>
                </DialogActions>
            </Dialog>
        </>
    )

}