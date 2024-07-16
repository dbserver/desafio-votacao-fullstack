import axios from "axios";
import { useEffect, useState } from "react";
import { Button, Form, Table, Modal } from "react-bootstrap"

export interface Associate {
    id: number;
    name: string;
    cpf: string;
}

const AssociateTable = () => {

    const [associates, setAssociates] = useState<Associate[]>([]);
    const [name, setName] = useState<string>('');
    const [cpf, setCpf] = useState<string>('');
    const [editItem, setEditItem] = useState<Associate | null>(null);
    const [show, setShow] = useState<boolean>(false);

    useEffect(() => {
        findAllAssociates();
    }, []);

    const createAssociate = async  (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        event.preventDefault();
        try {
            if (editItem) {
                await axios.put(`http://localhost:8080/api/associate/${editItem.id}`, { name: name, cpf: cpf }); 
                setEditItem(null);
            } else {
                await axios.post('http://localhost:8080/api/associate', { name: name, cpf: cpf  }); 
            }
            findAllAssociates(); 
            cleanState();
            handleClose();
        } catch (error) {
            console.error('Erro ao salvar item:', error);
        }
    }

    const findAllAssociates = async () => {
        try {
            const response = await axios.get<Associate[]>('http://localhost:8080/api/associate'); 
            setAssociates(response.data);
        } catch (error) {
            console.error('Erro ao buscar itens:', error);
        }
    };

    const cleanState = () => {
        setName('');
        setCpf('');
    }

    const handleEdit = (associate: Associate) => {
        setEditItem(associate);
        setName(associate.name); 
        setCpf(associate.cpf)
    };

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    
    return (
        <>
                 <Button variant="warning" onClick={() => handleShow()}>
                    Novo
                </Button>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Cadastrar Pauta</Modal.Title>
                    </Modal.Header>

                    <Modal.Body>
                        <Form>
                            <Form.Group className="mb-3" controlId="formBasicName">
                                <Form.Label>Nome</Form.Label>
                                <Form.Control type="text" placeholder="Informe o nome do associado." value={name} onChange={(e) => setName(e.target.value)}/>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicCpf">
                                <Form.Label>CPF</Form.Label>
                                <Form.Control type="text" placeholder="Informe o CPF do associado." value={cpf} onChange={(e) => setCpf(e.target.value)}/>
                            </Form.Group>

                            
                        </Form>
                    </Modal.Body>

                    <Modal.Footer>
                        <Button variant="success" type="submit" onClick={createAssociate}>
                            Cadastrar Associado
                        </Button>
                    </Modal.Footer>
                </Modal>
            

            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>CPF</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        associates.map((associate) => (
                            <tr key={associate.id}>
                                <td>{associate.id}</td>
                                <td>{associate.name}</td>
                                <td>{associate.cpf}</td>
                            </tr>
                        ))
                    }
                </tbody>
            </Table>
        </>
        
    )
} 

export default AssociateTable;