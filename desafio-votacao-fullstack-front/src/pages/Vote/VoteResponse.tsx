import axios from "axios";
import { Button, ProgressBar, Container, Row, Col, Form  } from "react-bootstrap";
import { useState, useEffect } from "react";
import { Associate } from "../Associate/AssociateTable";
import { Agenda } from "../Session";
import ErrorModal from "../../Components/Modal/ErrorModal/ErrorModal";

interface OptionVotes {
    yes: number;
    no: number;
}

interface Votes {
    totalVotes: number;
    totalVotesSim: number;
    totalVotesNao: number;
    associate: Associate;
    descriptionAgenda: string;
    response: string;
}

const VoteResponse = (props: any) => {

    // --------

    const [optionVotes, setOptionVotes] = useState<OptionVotes>({ yes: 0, no: 0 });
    const [hasVoted, setHasVoted] = useState<boolean>(false);
    const [votes, setVotes] = useState<Votes | null>();
    const [idSession, setIdSession] = useState<number | null>(null);
    const [agendas, setAgendas] = useState<Agenda[]>([]);
    const [cpfAssociate, setCpfAssociate] = useState<string | ''>('');
    const [response, setResponse] = useState<string | null>(null);
    const [showErrorModal, setShowErrorModal] = useState<boolean>(false);
    const [errorMessage, setErrorMessage] = useState<string>('');
    var yesPercentage = 0;
    var noPercentage = 0;

    const handleError = (error: string) => {
        setErrorMessage(error);
        setShowErrorModal(true);
      };
    
      const handleCloseError = () => {
        setShowErrorModal(false);
      };

    /// --------

    
    useEffect(() => {
        findAllVotes();
    }, []);

    const createVote = async  (OpcaoDeVoto: String) => {
        try {
            await axios.post('http://localhost:8080/api/votes', { idSession: props.idSessionProps, cpfAssociate: cpfAssociate, response: OpcaoDeVoto }); 
            findAllVotes(); 
            cleanState();
        } catch (error) {
            if (axios.isAxiosError(error)) {
                handleError(error.response?.data?.title || 'Ocorreu um erro ao processar sua solicitação.');
            } else {
                handleError('Erro desconhecido.');
            }
        }
    }

    const findAllVotes = async () => {
        try {
            const response = await axios.get<Votes>(`http://localhost:8080/api/votes/session/${props.idSessionProps}`); 
            const totalVotes = response.data.totalVotes
            yesPercentage = totalVotes ? (response.data.totalVotesSim / totalVotes) * 100 : 0;
            noPercentage = totalVotes ? (response.data.totalVotesNao / totalVotes) * 100 : 0;
            setVotes(response.data);
        } catch (error) {
            if (axios.isAxiosError(error)) {
                handleError(error.response?.data?.title || 'Ocorreu um erro ao processar sua solicitação.');
            } else {
                handleError('Erro desconhecido.');
            }
        }
    };
    
    const cleanState = () => {
        setIdSession(null);
        setCpfAssociate('');
        setResponse('');
    }

    return(
        <>      
                <Form>
                    <Form.Group className="mb-3" controlId="formBasicDescription">
                        <Form.Label>CPF do Associado</Form.Label>
                        <Form.Control type="text" placeholder="Formato: 575.386.060-50" value={cpfAssociate} onChange={(e) => setCpfAssociate(e.target.value)}/>
                    </Form.Group>
                </Form>
                <Container>
                    <Row className="my-3">
                        <Col>
                        <h3>{votes?.descriptionAgenda}</h3>
                        </Col>
                    </Row>
                    <Row className="my-3">
                        <Col>
                        <Button
                            variant="success"
                            onClick={() => createVote('Sim')}
                            disabled={hasVoted}
                        >
                            Sim
                        </Button>
                        </Col>
                        <Col>
                        <Button
                            variant="danger"
                            onClick={() => createVote('Não')}
                            disabled={hasVoted}
                        >
                            Não
                        </Button>
                        </Col>
                    </Row>
                    <Row className="my-3">
                        <Col>
                        <ProgressBar now={votes?.totalVotesSim} label={`Sim: ${yesPercentage.toFixed(2)}%`} variant="success" />
                        </Col>
                    </Row>
                    <Row className="my-3">
                        <Col>
                        <ProgressBar now={votes?.totalVotesNao} label={`Não: ${noPercentage.toFixed(2)}%`} variant="danger" />
                        </Col>
                    </Row>
                    <Row className="my-3">
                        <Col>
                        <p>Total de votos: {votes?.totalVotes}</p>
                        </Col>
                    </Row>
                </Container>
                <ErrorModal show={showErrorModal} handleClose={handleCloseError} errorMessage={errorMessage} />
        </>
    )
}

export default VoteResponse;