"use client";
import apiFetch from "../../axios/config";
import {
    Alert,
    Box, Button,
    Grid2,
    Stack, TextField,
    Typography
} from "@mui/material";
import MainContainer from "../../ui/Util/main-container.tsx";
import ReturnButton from "../../ui/Util/returnButton.tsx";
import {useState} from "react";

export default function VoteResult() {
    const [sessionId, setSessionId] = useState("");
    const [voteResult, setVoteResult] = useState<VoteResult | null>(null);

    interface VoteResult {
        voteYes: number;
        voteNo: number;
        totalVotes: number;
    }

    const getVoteResult = async () => {
        try {
            const response = await apiFetch.get(`/agenda/session/associated/result`, {
                params: {sessionId}
            }).then((response) => response);
            const data = response.data.body;
            setVoteResult(data);

            console.log(data);

            return data;
        } catch (e) {

            console.error(e);
        }
    };

    return (
        <MainContainer>
            <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                <h1>Resultado das Votações</h1>
            </Grid2>
            <Box
                component="form"
                sx={{'& .MuiTextField-root': {m: 1, width: '45ch'}}}
                noValidate
                autoComplete="off"
            >
                <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                    <Stack spacing={2}>
                        <TextField
                            required
                            id="sessionID"
                            label="Indique o ID da Sessão"
                            variant="filled"
                            color="success"
                            value={sessionId}
                            onChange={(e) => setSessionId(e.target.value)}
                        />
                        <Button
                            variant="outlined"
                            color="success"
                            onClick={getVoteResult}
                        >
                            Pesquisar Resultado
                        </Button>
                        <Box sx={{'& .MuiTextField-root': {m: 1, width: '35ch'}}}>
                            {voteResult?.totalVotes == 0 || voteResult === null ?
                                (<Typography variant="body2" gutterBottom>
                                    <Alert variant="outlined" severity="warning">
                                        Um ID de sessão válida é importante.
                                    </Alert>
                                    <br/><br/><br/>
                                    Nenhuma votação foi realizada.
                                    <br/><br/><br/>
                                </Typography>)
                                :
                                (<Box>
                                    <Typography variant="body2" gutterBottom>
                                        Resultado da votação:
                                    </Typography>
                                    <Typography variant="body2" gutterBottom>
                                        Votos Sim: {voteResult.voteYes}
                                    </Typography>
                                    <Typography variant="body2" gutterBottom>
                                        Votos Não: {voteResult.voteNo}
                                    </Typography>
                                    <Typography variant="body2" gutterBottom>
                                        Total de Votos: {voteResult.totalVotes}
                                    </Typography>
                                    <Typography variant="body2" gutterBottom>
                                        Resultado da
                                        votação: {voteResult?.voteYes > voteResult?.voteNo ? "Aprovado" : "Reprovado"}
                                    </Typography>
                                    <br/>
                                    <br/>
                                </Box>)
                            }
                        </Box>
                    </Stack>
                </Grid2>
                <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                    <Stack spacing={2}>
                        <ReturnButton/>
                    </Stack>
                </Grid2>
            </Box>
        </MainContainer>
    );
}