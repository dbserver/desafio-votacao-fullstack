"use client";
import {Box, Button, Grid2, Stack, TextField} from "@mui/material";
import MainContainer from "../../ui/Util/main-container.tsx";
import ReturnButton from "../../ui/Util/returnButton.tsx";
import apiFetch from "../../axios/config";
import {FormEvent, useState} from "react";

export default function Associated() {

    const [id, setId] = useState("");
    const [name, setName] = useState("");
    const [cpf, setCPF] = useState("");

    const createAssociated = async (e: FormEvent) => {
        e.preventDefault()
        console.log('Criando associado...')
        console.log('ID: ', id)
        console.log('Nome: ', name)
        console.log('Descrição: ', cpf)
        try {
            await apiFetch.post('/associated/create', {id, name, cpf})
            console.log('Associado criado com sucesso!')
        } catch (e) {
            console.error(e)
        }
    }

    return (
        <MainContainer>
            <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                <h1>Criar Associado</h1>
            </Grid2>
            <Box
                component="form"
                sx={{'& .MuiTextField-root': {m: 1, width: '45ch'}}}
                noValidate
                autoComplete="off"
                onSubmit={createAssociated}
            >
                <Grid2 display="flex" justifyContent="center" alignItems="center" size="grow">
                    <Stack spacing={2}>
                        <TextField
                            required
                            id="filled-required"
                            label="ID do Associado"
                            variant="filled"
                            color="success"
                            onChange={(e) => setId(e.target.value)}
                        />
                        <TextField
                            required
                            id="filled-required"
                            label="Nome"
                            variant="filled"
                            color="success"
                            onChange={(e) => setName(e.target.value)}
                        />
                        <TextField
                            required
                            id="filled-required"
                            label="CPF"
                            variant="filled"
                            color="success"
                            onChange={(e) => setCPF(e.target.value)}
                        />
                        <Button variant="contained" color="success" type="submit">
                            Cadastrar
                        </Button>
                        <ReturnButton/>
                    </Stack>
                </Grid2>
            </Box>
        </MainContainer>
    );
}