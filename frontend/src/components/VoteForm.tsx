import { useState, type SyntheticEvent } from 'react';
import type { VoteOption } from '../types/agenda';
import { useAgenda } from '../contexts/AgendaContext';

interface VoteFormProps {
    agendaId: number;
}

function VoteForm({ agendaId }: VoteFormProps) {
    const { vote } = useAgenda();

    const [associateId, setAssociateId] = useState('');
    const [value, setValue] = useState<VoteOption>();
    
    const [success, setSuccess] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = (event: SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault();

        vote(agendaId, associateId, value!, (error) => {
            if (error) {
                setError(error);
                setSuccess('');
            } else {
                setError('');
                setSuccess('Voto registrado com sucesso!');
                setAssociateId('');
                setValue(undefined);
            }
        });
    };

    return (
        <form onSubmit={handleSubmit} className="bg-light p-3 rounded border border-success">
            <h6 className="fw-semibold text-success mb-3">Registrar Voto</h6>

            <div className="mb-2">
                <input
                    type="text"
                    className="form-control form-control-sm"
                    placeholder="ID único do Associado"
                    value={associateId}
                    onChange={(e) => setAssociateId(e.target.value)}
                    required
                />
            </div>

            <div className="d-flex gap-2 mb-2">
                <button
                    type="button"
                    className={`btn btn-sm flex-fill ${value === 'YES' ? 'btn-success' : 'btn-outline-success'}`}
                    onClick={() => setValue('YES')}
                >
                    SIM
                </button>
                <button
                    type="button"
                    className={`btn btn-sm flex-fill ${value === 'NO' ? 'btn-danger' : 'btn-outline-danger'}`}
                    onClick={() => setValue('NO')}
                >
                    NÃO
                </button>
            </div>

            <button
                type="submit"
                className="btn btn-sm btn-dark w-100"
                disabled={!associateId.trim() || !value}
            >
                Enviar Voto
            </button>

            {error && (
                <div className="alert alert-danger alert-dismissible fade show" role="alert">
                    {error}
                </div>
            )}
            {success && (
                <div className="alert alert-success alert-dismissible fade show" role="alert">
                    {success}
                </div>
            )}
        </form>
    );
}

export default VoteForm;