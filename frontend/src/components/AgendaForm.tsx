import { useState, type FormEvent } from 'react';
import { useAgenda } from '../contexts/AgendaContext';

function AgendaForm() {
    const { createAgenda } = useAgenda();

    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        createAgenda({ title, description }, () => {
            setTitle('');
            setDescription('');
        });
    };

    return (
        <div className="card shadow-sm">
            <div className="card-header bg-primary text-white">
                <h5 className="card-title mb-0">Nova Pauta</h5>
            </div>
            <div className="card-body">
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="title" className="form-label">
                            <span className="text-danger">*</span> Título da Pauta
                        </label>
                        <input
                            type="text"
                            id="title"
                            className="form-control"
                            placeholder="Ex: Aquisição de maquinário"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="description" className="form-label">Descrição</label>
                        <textarea
                            id="description"
                            className="form-control"
                            rows={3}
                            placeholder="Descreva detalhes da pauta..."
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                        />
                    </div>
                    <button type="submit" className="btn btn-success w-100">
                        Criar Pauta
                    </button>
                </form>
            </div>
        </div>
    );
}

export default AgendaForm;