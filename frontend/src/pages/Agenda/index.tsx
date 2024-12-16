"use client";
import {FormEvent, useState} from "react";
import {Box, Button, Grid2, Stack, TextField} from "@mui/material";
import MainContainer from "../../ui/Util/main-container.tsx";
import ReturnButton from "../../ui/Util/returnButton.tsx";
import apiFetch from "../../axios/config";


export default function Agenda() {

    const [id, setId] = useState("");
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");

    const createAgenda = async (e: FormEvent) => {
        e.preventDefault()
        console.log('Criando pauta...')
        console.log('ID: ', id)
        console.log('Título: ', title)
        console.log('Descrição: ', description)

        const response = {id, title, description};
        try {
            await apiFetch.post('/agenda',
                response
            );
            console.log('Pauta criada com sucesso!')
        } catch (e) {
            console.error(e)
        }
    }

    return (
        <MainContainer>
            <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                <h1>Criar uma Pauta</h1>
            </Grid2>
            <Box
                component="form"
                sx={{'& .MuiTextField-root': {m: 1, width: '45ch'}}}
                noValidate
                autoComplete="off"
                onSubmit={createAgenda}
            >
                <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                    <Stack spacing={2}>
                        <TextField
                            required
                            id="id"
                            label="ID da Pauta"
                            variant="filled"
                            color="success"
                            onChange={(e) => setId(e.target.value)}
                        />
                        <TextField
                            id="title"
                            label="Título"
                            variant="filled"
                            color="success"
                            onChange={(e) => setTitle(e.target.value)}
                        />
                        <TextField
                            required
                            id="description"
                            label="Descrição"
                            multiline
                            rows={4}
                            variant="filled"
                            color="success"
                            onChange={(e) => setDescription(e.target.value)}
                        />
                        <Button variant="contained" color="success" type="submit">
                            Criar
                        </Button>
                        <ReturnButton/>
                    </Stack>
                </Grid2>
            </Box>
        </MainContainer>
    );
}