import axios from "axios";
import { useEffect, useState } from "react";
import { Button, Form, Table, Modal, Card , ProgressBar, Container, Row, Col  } from "react-bootstrap";
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import dayjs, { Dayjs } from 'dayjs';
import { useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import VoteResponse from "../Vote/VoteResponse";
const apiUrl = import.meta.env.VITE_APP_API_URL;
interface Session {
    id: number;
    agendaResponseDto: Agenda;
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
    const [sessions, setSessions] = useState<Session | null>(null);
    const [idAgenda, setIdAgenda] = useState<number | null>(idAgendaRedirec);
    const [agendas, setAgendas] = useState<Agenda[]>([]);
    const [startTime, setStartTime] = useState<Dayjs | null>(null);
    const [endTime, setEndTime] = useState<Dayjs | null>(null);
    const [show, setShow] = useState<boolean>(false);
    const [showVote, setShowVote] = useState<boolean>(false);

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

    const createSession = async  (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        event.preventDefault();

        try {
            await axios.post(`${apiUrl}/api/sessions`, {idAgenda: idAgenda,startTime: formatDateTimeForAPI(startTime),endTime: formatDateTimeForAPI(endTime)}); 
            handleClose();
            findAllSession(); 
            cleanState();
        } catch (error) {
            console.error('Erro ao salvar item:', error);
        }
    }

    const findAllSession = async () => {
        try {
            const response = await axios.get<Session>(`${apiUrl}/api/sessions/agenda/${idAgenda}/`); 
            console.log(response.data)
            setSessions(response.data);
        } catch (error) {
            console.error('Erro ao buscar itens:', error);
        }
    };

    const findAllAgendas = async () => {
        try {
            const response = await axios.get<Agenda[]>(`${apiUrl}/api/agendas`); 
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

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleCloseVote = () => setShowVote(false);
    const handleShowVote = () => setShowVote(true);

    return (
        <div>
            <h1>Sessões</h1>

            <>

            <Button variant="secondary" onClick={() => handleGoBack()}>
                Voltar
            </Button>

            <Button disabled={sessions !== null ? true : false} variant="warning" onClick={() => handleShow()}>
                Novo
            </Button>

            <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Cadastrar Sessão</Modal.Title>
                    </Modal.Header>

                    <Modal.Body>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <Form>
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
                        <Button variant="success" type="submit" onClick={createSession}>
                            Cadastrar Sessão
                        </Button>
                    </Modal.Footer>
                </Modal>
            
            <Modal show={showVote} onHide={handleCloseVote}>
                <Modal.Header closeButton>
                    <Modal.Title>Votar</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <VoteResponse idSessionProps={idAgendaRedirec} />
                </Modal.Body>

            </Modal>

            {
                sessions ?

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

            
                    <tr key={sessions?.agendaResponseDto.id}>
                        <td>{sessions?.agendaResponseDto.id}</td>
                        <td>{sessions?.agendaResponseDto.description}</td>
                        <td>{formatDateTimeForLayout(sessions?.startTime ?? null)}</td>
                        <td>{formatDateTimeForLayout(sessions?.endTime ?? null)}</td>
                               
                        <td>
                            <Button variant="primary" onClick={() => handleShowVote()}>
                                Votar
                            </Button>
                        </td> 
                    </tr>
      
        
                </tbody>
            </Table> : 
            <p>Não há sessão cadastrada ainda para está pauta.</p>
            }
           
            
        </>
        </div>
    )
}

export default Session;