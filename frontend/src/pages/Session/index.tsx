"use client";
import {Alert, Box, Button, Grid2, Stack, TextField, Typography} from "@mui/material";
import MainContainer from "../../ui/Util/main-container.tsx";
import ReturnButton from "../../ui/Util/returnButton.tsx";
import {FormEvent, useState} from "react";
import apiFetch from "../../axios/config.ts";

export default function Session() {

    const [alertSession, setAlertSession] = useState(false);
    const [alertAgenda, setAlertAgenda] = useState(false);
    const [alertAgendaNotFound, setAlertAgendaNotFound] = useState(false);
    const [id, setId] = useState("");
    const [agendaId, setAgendaId] = useState("");
    const [duration, setDuration] = useState("");

    const createSession = async (e: FormEvent) => {
        e.preventDefault()
        console.log('Criando sessão...')
        console.log('ID da Sessão: ', id)
        console.log('ID da Pauta: ', agendaId)
        console.log('Duração: ', duration)

        setAlertSession(false);
        setAlertAgenda(false);
        setAlertAgendaNotFound(false);

        if (!id) {
            console.log('session Id is required');
            setAlertSession(true);
        }

        if (!agendaId) {
            console.log('session Id is required');
            setAlertAgenda(true);
        }

        const agendaResponse = await apiFetch.get(`/agenda/id`, {
                params: {id: agendaId}
            }
        );
        if (!agendaResponse.data && agendaId) {
            console.log('Agenda not found');
            setAlertAgendaNotFound(true);
        }

        const response = {id, agendaId, duration};
        console.log(response);
        try {
            await apiFetch.post('/session/create',
                response
            );
            console.log('Sessão criada com sucesso!')
        } catch (e) {
            console.error(e)
        }
    }

    return (
        <MainContainer>
            <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                <h1>Abrir uma Sessão de Votação</h1>
            </Grid2>

            <Box
                component="form"
                sx={{'& .MuiTextField-root': {m: 1, width: '45ch'}}}
                noValidate
                autoComplete="off"
                onSubmit={createSession}
            >
                <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                    <Box sx={{'& .MuiTextField-root': {m: 1, width: '25ch'}}}>
                        <Typography variant="body2" gutterBottom>
                            Para abrir uma sessão de votação, é necessário informar o ID da sessão e o ID da pauta.
                        </Typography>

                    </Box>
                </Grid2>
                <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">

                    <Stack spacing={2}>
                        <TextField
                            required
                            id="sessionId"
                            label="ID da Sessão"
                            variant="filled"
                            color="success"
                            onChange={(e) => setId(e.target.value)}
                        />
                        {alertSession ?
                            <><Alert variant="outlined" severity="error" sx={{width: '43ch'}}>
                                Um ID de sessão válida é importante.
                            </Alert><br/></>
                            : ""
                        }
                        <TextField
                            required
                            id="agendaId"
                            label="ID da Pauta"
                            variant="filled"
                            color="success"
                            onChange={(e) => setAgendaId(e.target.value)}
                        />
                        {alertAgenda ?
                            <><Alert variant="outlined" severity="error" sx={{width: '43ch'}}>
                                Um ID de pauta válida é importante.
                            </Alert><br/></>
                            : ""
                        }
                        {alertAgendaNotFound ?
                            <><Alert variant="outlined" severity="error" sx={{width: '43ch'}}>
                                Pauta não encontrada.
                            </Alert><br/></>
                            : ""
                        }
                        <TextField
                            id="duration"
                            label="Duração da Sessão (em minutos)"
                            type="number"
                            variant="filled"
                            inputProps={{min: 1}}
                            color="success"
                            slotProps={{
                                inputLabel: {
                                    shrink: true,
                                },
                            }}
                            onChange={(e) => setDuration(e.target.value)}
                        />
                        <Alert variant="outlined" severity="info" sx={{width: '43ch'}}>
                            O valor mínimo para a duração da sessão é de 1 minuto.
                        </Alert>
                        <Button variant="contained" color="success" type="submit">
                            Abrir Sessão
                        </Button>
                        <ReturnButton/>
                    </Stack>
                </Grid2>
            </Box>
        </MainContainer>
    );
}