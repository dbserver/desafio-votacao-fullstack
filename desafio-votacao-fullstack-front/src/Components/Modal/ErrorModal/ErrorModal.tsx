import { Modal, Button } from 'react-bootstrap';

interface ErrorModalProps {
    show: boolean;
    handleClose: () => void;
    errorMessage: string;
}

const ErrorModal: React.FC<ErrorModalProps> = ({ show, handleClose, errorMessage }) => {
    return (
        <Modal show={show} onHide={handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Erro</Modal.Title>
          </Modal.Header>
          <Modal.Body>{errorMessage}</Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
              Fechar
            </Button>
          </Modal.Footer>
        </Modal>
      );
}

export default ErrorModal;