import axios from "axios";
import { useEffect, useState } from "react";
import { Button, Form, Table, Modal } from "react-bootstrap";
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import dayjs, { Dayjs } from 'dayjs';
import { useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
interface Session {
    id: number;
    idAgenda: number;
    descriptionAgenda: string;
    startTime: Dayjs;
    endTime: Dayjs;
}

export interface Agenda{
    id: number;
    description: string;
}

function Session(){

    const navigate = useNavigate()
    const location = useLocation();
    const { idAgendaRedirec } = location.state;
    const [sessions, setSessions] = useState<Session[]>([]);
    const [idAgenda, setIdAgenda] = useState<number | null>(idAgendaRedirec);
    const [agendas, setAgendas] = useState<Agenda[]>([]);
    const [startTime, setStartTime] = useState<Dayjs | null>(null);
    const [endTime, setEndTime] = useState<Dayjs | null>(null);
    const [show, setShow] = useState<boolean>(false);

    const formatDateTimeForAPI = (date: Dayjs | null): string => {
        return date ? dayjs(date).format('YYYY-MM-DDTHH:mm:ss') : '';
    };

    const formatDateTimeForLayout = (date: Dayjs | null): string => {
        return date ? dayjs(date).format('DD/MM/YYYY HH:mm:ss') : '';
    };

    useEffect(() => {
        findAllSession();
        findAllAgendas();
    }, []);

    const createSession = async  (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        try {
            await axios.post('http://localhost:8080/api/sessions', {idAgenda: idAgenda,startTime: formatDateTimeForAPI(startTime),endTime: formatDateTimeForAPI(endTime)}); 
            handleClose();
            findAllSession(); 
            cleanState();
        } catch (error) {
            console.error('Erro ao salvar item:', error);
        }
    }

    const findAllSession = async () => {
        try {
            const response = await axios.get<Session[]>('http://localhost:8080/api/sessions'); 
            setSessions(response.data);
        } catch (error) {
            console.error('Erro ao buscar itens:', error);
        }
    };

    const findAllAgendas = async () => {
        try {
            const response = await axios.get<Agenda[]>('http://localhost:8080/api/agendas'); 
            setAgendas(response.data);
        } catch (error) {
            console.error('Erro ao buscar itens:', error);
        }
    };

    const cleanState = () => {
        setIdAgenda(null);
        setStartTime(null);
        setEndTime(null);
    }

    const handleEdit = (session: Session) => {
        setStartTime(session.startTime); 
        setEndTime(session.endTime);
        setIdAgenda(idAgendaRedirec);
    };

    const handleGoBack = () => {
        navigate(-1); 
    };

    const handleVote = (session: Session) => {

    }
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <div>
            <h1>Sessões</h1>

            <>
            <h3>Pautas</h3>

            <Button variant="secondary" onClick={() => handleGoBack()}>
                Voltar
            </Button>

            <Button variant="warning" onClick={() => handleShow()}>
                Novo
            </Button>
            
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Cadastrar Sessão</Modal.Title>
                    </Modal.Header>

                    <Modal.Body>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <Form onSubmit={createSession}>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Pautas:</Form.Label>
                                        <Form.Select id="optionAgenda" value={idAgenda || ''} onChange={(e) => setIdAgenda(Number(e.target.value))}>
                                            <option value="">Selecione uma pauta.</option>
                                            {
                                                agendas.map(agenda =>  <option value={agenda.id}>{agenda.description}</option>)
                                            }
                                        </Form.Select>
                                    </Form.Group>
                                
                                    <Form.Group controlId="formDate">
                                        <Form.Label>Selecione o Início e Fim da sessão:</Form.Label>
                                        <br />
                                        <DateTimePicker
                                            value={startTime}
                                            onChange={(newValue) => setStartTime(newValue)}
                                            label="Selecione a data e hora do inicio da sessão."
                                        />

                                        <DateTimePicker
                                            value={endTime}
                                            onChange={(newValue) => setEndTime(newValue)}
                                            label="Selecione a data e hora do fim da sessão."
                                        />
                        
                                    </Form.Group>
                                
                                
                            </Form>
                        </LocalizationProvider>
                    </Modal.Body>

                    <Modal.Footer>
                        <Button variant="success" type="submit">
                            Cadastrar Sessão
                        </Button>
                    </Modal.Footer>
                </Modal>
           
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Pauta</th>
                        <th>Início</th>
                        <th>Fim</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        sessions.map((session) => (
                            <tr key={session.id}>
                                <td>{session.id}</td>
                                <td>{session.descriptionAgenda}</td>
                                <td>{formatDateTimeForLayout(session.startTime)}</td>
                                <td>{formatDateTimeForLayout(session.endTime)}</td>
                                <td>
                                    <Button variant="primary" onClick={() => handleVote(session)}>
                                        Votar
                                    </Button>
                                </td>
                            </tr>
                        ))
                    }
                </tbody>
            </Table>
        </>
        </div>
    )
}

export default Session;