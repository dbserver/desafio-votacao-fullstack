
import { useEffect, useState, ChangeEvent, MouseEvent } from "react";
import { Button, Form, Table, Modal } from "react-bootstrap"
import { findAllAssociates, createAssociate, updateAssociate } from "../../services/associateService";
import DataTable from "../../Components/DateTable/DataTable";
import { Column } from "../../Components/DateTable/DataTable";
export interface Associate {
    id: number;
    name: string;
    cpf: string;
}

const AssociateTable = () => {

    const [associates, setAssociates] = useState<Associate[]>([]);
    const [associate, setAssociate] = useState<Associate>({id: 0, name: '', cpf: ''});
    const [editItem, setEditItem] = useState<Associate | null>(null);
    const [show, setShow] = useState<boolean>(false);

    useEffect(() => {
        loadAssociates();
      }, []);
    
      const loadAssociates = async () => {
        try {
          const data = await findAllAssociates();
          setAssociates(data);
        } catch (error) {
          console.error("Erro ao buscar itens:", error);
        }
      };
    
      const handleCreateOrUpdate = async (event: MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        try {
          if (editItem) {
            await updateAssociate(editItem.id, { name: associate.name, cpf: associate.cpf });
            setEditItem(null);
          } else {
            await createAssociate({ name: associate.name, cpf: associate.cpf });
          }
          loadAssociates();
          cleanState();
          handleClose();
        } catch (error) {
          console.error("Erro ao salvar item:", error);
        }
      };
    
      const cleanState = () => {
        setAssociate({ id: 0, name: "", cpf: "" });
      };
    
      const handleEdit = (associate: Associate) => {
        setEditItem(associate);
        setAssociate({ ...associate, name: associate.name, cpf: associate.cpf });
      };
    
      const handleOnChangeSetAssociate = (event: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setAssociate((prevState) => ({ ...prevState, [name]: value }));
      };
    
      const handleClose = () => setShow(false);
      const handleShow = () => setShow(true);

      const columns: Column<Associate>[] = [
        { header: "ID", accessor: "id" },
        { header: "Nome", accessor: "name" },
        { header: "CPF", accessor: "cpf" },
      ];
    
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
                                <Form.Control name="name" type="text" placeholder="Informe o nome do associado." value={associate.name} onChange={handleOnChangeSetAssociate}/>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicCpf">
                                <Form.Label>CPF</Form.Label>
                                <Form.Control name="cpf" type="text" placeholder="Informe o CPF do associado." value={associate.cpf} onChange={handleOnChangeSetAssociate}/>
                            </Form.Group>

                        </Form>
                    </Modal.Body>

                    <Modal.Footer>
                        <Button variant="success" type="submit" onClick={handleCreateOrUpdate}>
                            Cadastrar Associado
                        </Button>
                    </Modal.Footer>
                </Modal>

                <DataTable data={associates} columns={columns} />
        </>
        
    )
} 

export default AssociateTable;