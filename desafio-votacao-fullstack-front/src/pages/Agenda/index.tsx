import axios from "axios";
import { useEffect, useState } from "react";
import { Button, Form, Table, Modal } from "react-bootstrap";
import { useNavigate } from 'react-router-dom';
import ErrorModal from "../../Components/Modal/ErrorModal/ErrorModal";
interface Agenda {
    id: number;
    description: string;
}

function Agenda(){

    const apiUrl = process.env.REACT_APP_API_URL;

    const navigate = useNavigate();
    const [agendas, setAgendas] = useState<Agenda[]>([]);
    const [description, setDescription] = useState<string>('');
    const [editItem, setEditItem] = useState<Agenda | null>(null);
    const [show, setShow] = useState<boolean>(false);
    const [showErrorModal, setShowErrorModal] = useState<boolean>(false);
    const [errorMessage, setErrorMessage] = useState<string>('');

    const handleError = (error: string) => {
        setErrorMessage(error);
        setShowErrorModal(true);
    };
    
    const handleCloseError = () => {
        setShowErrorModal(false);
    };

    useEffect(() => {
        findAllAgendas();
    }, []);

    const createAgenda = async  (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        event.preventDefault();
        try {
            if (editItem) {
                await axios.put(`${apiUrl}/api/agendas/${editItem.id}`, { description: description }); 
                setEditItem(null);
            } else {
                await axios.post(`${apiUrl}/api/agendas`, { description: description }); 
            }
            handleClose();
            findAllAgendas(); 
            cleanState();
        } catch (error) {
            if (axios.isAxiosError(error)) {
                handleError(error.response?.data?.title || 'Ocorreu um erro ao processar sua solicitação.');
            } else {
                handleError('Erro desconhecido.');
            }
        }
    }

    const cleanState = () => {
        setDescription('');
    }

    const findAllAgendas = async () => {
        try {
            const response = await axios.get<Agenda[]>(`${apiUrl}/api/agendas`); 
            setAgendas(response.data);
        } catch (error) {
            if (axios.isAxiosError(error)) {
                handleError(error.response?.data?.title || 'Ocorreu um erro ao processar sua solicitação.');
            } else {
                handleError('Erro desconhecido.');
            }
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
            <ErrorModal show={showErrorModal} handleClose={handleCloseError} errorMessage={errorMessage} />
        </>

    )
}

export default Agenda;