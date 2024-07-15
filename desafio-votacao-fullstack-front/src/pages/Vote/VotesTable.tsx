import axios from "axios";
import { useEffect, useState } from "react";
import { Button, Form, Table } from "react-bootstrap"
import { Associate } from "../Associate/AssociateTable";
import { Agenda } from "../Session";

interface Votes {
    totalVotes: number;
    totalVotesSim: number;
    totalVotesNao: number;
    associate: Associate;
    descriptionAgenda: string;
    response: string;
}

function VoteTable(){

    const [votes, setVotes] = useState<Votes[]>([]);
    const [idSession, setIdSession] = useState<number | null>(null);
    const [agendas, setAgendas] = useState<Agenda[]>([]);
    const [cpfAssociate, setCpfAssociate] = useState<string | ''>('');
    const [response, setResponse] = useState<string | null>(null);

    useEffect(() => {
        findAllVotes();
    }, []);

    const createVote = async  (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        try {
   
            await axios.post('http://localhost:8080/api/votes', { idSession: idSession, cpfAssociate: cpfAssociate, response: response }); 
            findAllVotes(); 
            cleanState();
        } catch (error) {
            console.error('Erro ao salvar item:', error);
        }
    }

    const findAllVotes = async () => {
        try {
            const response = await axios.get<Votes[]>('http://localhost:8080/api/votes'); 
            setVotes(response.data);
        } catch (error) {
            console.error('Erro ao buscar itens:', error);
        }
    };
    
    const cleanState = () => {
        setIdSession(null);
        setCpfAssociate('');
        setResponse('');
    }

    return (
        <div>
            <>
                <h3>Votos</h3>
                <Form onSubmit={createVote}>
                    <Form.Group className="mb-3" controlId="formBasicCpf">
                        <Form.Label>CPF</Form.Label>
                        <Form.Control type="text" placeholder="Informe o CPF do associado." value={cpfAssociate} onChange={(e) => setCpfAssociate(e.target.value)}/>
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Opção de Votação:</Form.Label>
                        <Form.Select id="optionVote">
                            <option>SIM</option>
                            <option>NÃO</option>
                        </Form.Select>
                    </Form.Group>

                    <Button variant="success" type="submit">
                        Cadastrar Pauta
                    </Button>
                </Form>

                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>Associado</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            votes.map((vote) => (
                                <tr key={vote.associate.id}>
                                    <td>{vote.associate.name}</td>
                                </tr>
                            ))
                        }
                    </tbody>
                </Table>
            </>
        </div>
    )
}

export default VoteTable;