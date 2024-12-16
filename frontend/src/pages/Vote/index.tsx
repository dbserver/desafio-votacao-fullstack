"use client";
import {
    Alert,
    Box,
    Button,
    FormControl,
    FormControlLabel,
    FormLabel,
    Grid2,
    Radio, RadioGroup,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import MainContainer from "../../ui/Util/main-container.tsx";
import ReturnButton from "../../ui/Util/returnButton.tsx";
import {FormEvent, useState} from "react";
import {green, pink} from "@mui/material/colors";
import apiFetch from "../../axios/config.ts";

export default function Vote() {
    const [voteId, setId] = useState("");
    const [associatedId, setAssociatedId] = useState("");
    const [agendaId, setAgendaId] = useState("");
    const [sessionId, setSessionId] = useState("");
    const [vote, setVote] = useState("");
    const [alertSession, setAlertSession] = useState(false);
    const [alertAgenda, setAlertAgenda] = useState(false);
    const [alertAssociated, setAlertAssociated] = useState(false);
    const [alertVote, setAlertVote] = useState(false);
    const [alertVoteChoice, setAlertVoteChoice] = useState(false);
    const [alertAgendaNotFound, setAlertAgendaNotFound] = useState(false);
    const [showDescription, setShowDescription] = useState(false);
    const [descriptionState, setDescriptionState] = useState("");

    const registerVote = async (e: FormEvent) => {
        e.preventDefault()
        console.log('Votando ...')
        console.log('ID do Voto: ', voteId)
        console.log('ID do Associado: ', associatedId)
        console.log('ID da Pauta: ', agendaId)
        console.log('ID da Sessão: ', sessionId)
        console.log('Voto: ', vote)

        setAlertVote(false);
        setAlertVoteChoice(false);
        setAlertSession(false);
        setAlertAgenda(false);

        if (!voteId) {
            console.log('vote Id is required');
            setAlertVote(true);
            return;
        }
        if (!vote) {
            console.log('voting is the reason of this page!!!');
            setAlertVoteChoice(true);
            return;
        }

        if (!sessionId) {
            console.log('session Id is required');
            setAlertSession(true);
            return;
        } else {
            const sessionResponse = await apiFetch.get(`/session/id`, {
                    params: {id: sessionId}
                }
            );
            console.log("session response: ", sessionResponse);
            if (!sessionResponse.data) {
                console.log('Session not found');
                setAlertSession(true);
                return;
            }
        }
        if (!agendaId) {
            console.log('agenda Id is required');
            setAlertAgenda(true);
            return;
        } else {
            const agendaResponse = await apiFetch.get(`/agenda/id`, {
                    params: {id: agendaId}
                }
            );
            console.log("agenda response: ", agendaResponse);
            if (!agendaResponse.data) {
                console.log('Agenda not found');
                setAlertAgenda(true);
                return;
            }
        }
        if (!associatedId) {
            console.log('associated Id is required');
            setAlertAssociated(true);
            return;
        } else {
            const AssociatedResponse = await apiFetch.get(`/associated/id`, {
                    params: {id: associatedId}
                }
            );
            console.log("associated response: ", AssociatedResponse);
            if (!AssociatedResponse.data) {
                console.log('Associated not found');
                setAlertAssociated(true);
                return;
            }
        }

        const response = {voteId, agendaId, sessionId, associatedId, vote};
        console.log(response)
        try {
            await apiFetch.post('/agenda/session/associated/vote',
                response
            );
            console.log('Voto registrado com sucesso!')
        } catch (e) {
            console.error(e)
        }
    }
    const getDescriptions = async () => {

        const agendaId = (document.getElementById("agendaId") as HTMLInputElement)?.value;
        console.log("agendaId123: ", agendaId);

        if (!agendaId) {
            setDescriptionState("");
            setShowDescription(false);
            return;
        }

        setDescriptionState("");
        setShowDescription(false);

        const agendaResponse = await apiFetch.get(`/agenda/id`, {
                params: {id: agendaId}
            }
        );
        if (!agendaResponse.data) {
            console.log('Agenda not found');
            setAlertAgendaNotFound(true);
            setShowDescription(false);
        } else {
            setShowDescription(true);
            setDescriptionState(agendaResponse.data.description.toString());
        }
    }

    return (
        <MainContainer>
            <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                <h1>Votar</h1>
            </Grid2>
            <Box
                component="form"
                sx={{'& .MuiTextField-root': {m: 1, width: '45ch'}}}
                noValidate
                autoComplete="off"
                onSubmit={registerVote}
            >
                <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                    <Box sx={{'& .MuiTextField-root': {m: 1, width: '25ch'}}}>
                        <Typography variant="body2" gutterBottom>
                            Lembre-se que você só pode votar uma única vez,
                            precisando identificar o ID da pauta, da sessão e o seu
                            para que seja possível realizar seu voto.
                        </Typography>

                    </Box>
                </Grid2>
                <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                    <Stack spacing={2}>
                        <TextField
                            required
                            id="voteId"
                            label="ID do Voto"
                            variant="filled"
                            color="success"
                            onChange={(e) => setId(e.target.value)}
                        />
                        {alertVote ?
                            <><Alert variant="outlined" severity="error" sx={{width: '43ch'}}>
                                ID do Voto faltando.
                            </Alert><br/></>
                            : ""
                        }
                        <TextField
                            required
                            id="associatedId"
                            label="ID do Associado"
                            variant="filled"
                            color="success"
                            onChange={(e) => setAssociatedId(e.target.value)}
                        />
                        {alertAssociated ?
                            <><Alert variant="outlined" severity="error" sx={{width: '43ch'}}>
                                ID do associado é um requisito.
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
                        <Button
                            variant="outlined"
                            color="success"
                            onClick={getDescriptions}
                        >
                            Buscar Descrição da Pauta
                        </Button>
                        <br/>
                        {showDescription ?
                            <><TextField
                                id="filled-read-only-input"
                                label="Descrição da Pauta"
                                value={`${descriptionState}`}
                                variant="filled"
                                color="success"
                                slotProps={{
                                    input: {
                                        readOnly: true,
                                    },
                                }}/><br/></>
                            : ""
                        }
                        {alertAgenda || alertAgendaNotFound ?
                            <><Alert variant="outlined" severity="error" sx={{width: '43ch'}}>
                                Um ID de pauta válida é importante.
                            </Alert><br/></>
                            : ""
                        }
                        <TextField
                            required
                            id="sessionId"
                            label="ID da Sessão"
                            variant="filled"
                            color="success"
                            onChange={(e) => setSessionId(e.target.value)}
                        />
                        {alertSession ?
                            <><Alert variant="outlined" severity="error" sx={{width: '43ch'}}>
                                Um ID de sessão válida é necessário.
                            </Alert><br/></>
                            : ""
                        }
                        <FormControl>
                            <FormLabel id="demo-controlled-radio-buttons-group">Votar: </FormLabel>
                            <RadioGroup
                                aria-labelledby="demo-controlled-radio-buttons-group"
                                name="controlled-radio-buttons-group"
                                onChange={(e) => setVote(e.target.value)}
                            >
                                <FormControlLabel value="SIM" control={<Radio sx={{
                                    color: green[800],
                                    '&.Mui-checked': {
                                        color: green[600],
                                    },
                                }}/>} label="SIM"
                                                  onChange={() => setVote("SIM")}
                                />
                                <FormControlLabel color="danger" value="NÃO" control={<Radio sx={{
                                    color: pink[800],
                                    '&.Mui-checked': {
                                        color: pink[600],
                                    },
                                }}/>} label="NÃO"
                                                  onChange={() => setVote("NÃO")}
                                />
                            </RadioGroup>
                        </FormControl>
                        {alertVoteChoice ?
                            <><Alert variant="outlined" severity="error" sx={{width: '43ch'}}>
                                Você precisa escolher se vota SIM ou NÃO para registrar o voto.
                            </Alert><br/></>
                            : ""
                        }
                        <Button variant="contained" color="success" type="submit">
                            Votar
                        </Button>
                        <ReturnButton/>
                    </Stack>
                </Grid2>
            </Box>
        </MainContainer>
    );
}