import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';

type ModalProperties = {
  type: string;
  title?: string;
  message: string;
}

const ModalMessage = ({type, title, message}: ModalProperties) => {
  return (
    <Card className="text-center">
      <Card.Header>{type}</Card.Header>
      <Card.Body>
        <Card.Title>{title}</Card.Title>
        <Card.Text>
          {message}
        </Card.Text>
        <Button variant="primary">Ok</Button>
      </Card.Body>
    </Card>
  );
}

export default ModalMessage;