import axios from "axios";
import { useEffect, useState } from "react";
import { Button, Form, Table, Modal } from "react-bootstrap";
import { useNavigate } from 'react-router-dom';
interface Agenda {
    id: number;
    description: string;
}

function Agenda(){

    const navigate = useNavigate();
    const [agendas, setAgendas] = useState<Agenda[]>([]);
    const [description, setDescription] = useState<string>('');
    const [editItem, setEditItem] = useState<Agenda | null>(null);
    const [show, setShow] = useState<boolean>(false);

    useEffect(() => {
        findAllAgendas();
    }, []);

    const createAgenda = async  (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        event.preventDefault();
        try {
            if (editItem) {
                await axios.put(`http://localhost:8080/api/agendas/${editItem.id}`, { description: description }); 
                setEditItem(null);
            } else {
                await axios.post('http://localhost:8080/api/agendas', { description: description }); 
            }
            handleClose();
            findAllAgendas(); 
            cleanState();
        } catch (error) {
            console.error('Erro ao salvar item:', error);
        }
    }

    const cleanState = () => {
        setDescription('');
    }

    const findAllAgendas = async () => {
        try {
            const response = await axios.get<Agenda[]>('http://localhost:8080/api/agendas'); 
            setAgendas(response.data);
        } catch (error) {
            console.error('Erro ao buscar itens:', error);
        }
    };

    const handleRedirec = (idAgenda: number) =>{
        navigate('/sessoes', {state: {idAgendaRedirec: idAgenda}});
    }

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <>
            <h3>Pautas</h3>

            <Button variant="warning" onClick={() => handleShow()}>
                Novo
            </Button>
            
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Cadastrar Pauta</Modal.Title>
                    </Modal.Header>

                    <Modal.Body>
                        <Form>
                            <Form.Group className="mb-3" controlId="formBasicDescription">
                                <Form.Label>Descrição</Form.Label>
                                <Form.Control type="text" placeholder="Informe a pauta da votação. Ex: Você já trabalhou para o exterior?" value={description} onChange={(e) => setDescription(e.target.value)}/>
                            </Form.Group>
                        </Form>
                    </Modal.Body>

                    <Modal.Footer>
                        <Button variant="success" type="submit" onClick={createAgenda}>
                            Cadastrar Pauta
                        </Button>
                    </Modal.Footer>
                </Modal>

            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Descrição</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        agendas.map((agenda) => (
                            <tr key={agenda.id}>
                                <td>{agenda.id}</td>
                                <td>{agenda.description}</td>
                                <td>
                                    <Button variant="primary" onClick={() => handleRedirec(agenda.id)}>
                                        Sessões
                                    </Button>
                                </td>
                            </tr>
                        ))
                    }
                </tbody>
            </Table>
        </>

    )
}

export default Agenda;