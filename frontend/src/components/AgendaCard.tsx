import { useEffect, useState } from 'react';
import VoteForm from './VoteForm';
import AgendaResult from './AgendaResult';
import type { Agenda } from '../types/agenda';
import { useAgenda } from '../contexts/AgendaContext';

interface AgendaCardProps {
    agenda: Agenda;
}

function AgendaCard({ agenda }: AgendaCardProps) {
    const { openSession, vote } = useAgenda();
    const [duration, setDuration] = useState('');
    const [remainingTime, setRemainingTime] = useState('');

    useEffect(() => {
        if (agenda.status !== 'STARTED' || !agenda.limitDate) return;

        const updateTimer = () => {
            const now = new Date().getTime();
            
            const limitDate = Date.parse(agenda.limitDate);

            if (limitDate === null) {
                setRemainingTime('Expirado');
                return;
            }

            const difference = limitDate - now;
            if (difference <= 0) {
                setRemainingTime('Expirado');
            } else {
                const seconds = Math.floor((difference / 1000) % 60);
                const minutes = Math.floor((difference / 1000 / 60) % 60);
                setRemainingTime(`${minutes}m ${seconds}s`);
            }
        };

        updateTimer();
        const interval = setInterval(updateTimer, 1000);

        return () => clearInterval(interval);
    }, [agenda.status, agenda.limitDate]);

    const handleStartSession = () => {
        const parsedDuration = Number(duration) || 1;
        openSession(agenda.id, parsedDuration);
    };

    return (
        <div className="card shadow-sm border mb-3">
            <div className="card-body">
                <div className="d-flex justify-content-between align-items-start">
                    <h5 className="card-title text-primary">{agenda.title}</h5>

                    {agenda.status === 'PENDING' && (
                        <span className="badge bg-secondary">Aguardando Abertura</span>
                    )}
                    {agenda.status === 'STARTED' && (
                        <span className="badge bg-success">Votação Aberta</span>
                    )}
                    {agenda.status === 'FINISHED' && (
                        <span className="badge bg-danger">Sessão Encerrada</span>
                    )}
                </div>

                <p className="card-text text-muted small">{agenda.description}</p>

                <hr />

                {/* 1. Aguardando Abertura */}
                {agenda.status === 'PENDING' && (
                    <div className="bg-light p-3 rounded">
                        <label className="form-label fw-semibold">Abrir Sessão de Votação</label>
                        <div className="input-group">
                            <span className="input-group-text">Minutos:</span>
                            <input
                                type="number"
                                className="form-control"
                                min="1"
                                placeholder="Padrão: 1 minuto"
                                value={duration}
                                onChange={(e) => setDuration(e.target.value)}
                            />
                            <button className="btn btn-primary" onClick={handleStartSession}>
                                Iniciar Votação
                            </button>
                        </div>
                    </div>
                )}

                {/* 2. Sessão Aberta */}
                {agenda.status === 'STARTED' && (
                    <div className="row">
                        <div className="col-md-6 mb-3 mb-md-0">
                            <VoteForm agendaId={agenda.id} />
                        </div>
                        <div className="col-md-6 d-flex flex-column justify-content-center">
                            <div className="p-3 bg-light rounded text-center">
                                <span className="d-block text-muted small">Tempo Restante:</span>
                                <span className="fs-4 fw-bold text-danger">{remainingTime}</span>
                            </div>
                        </div>
                    </div>
                )}

                {/* 3. Sessão Encerrada */}
                {/* {agenda.status === 'FINISHED' && (
                    <AgendaResult votes={agenda.votes} />
                )} */}
            </div>
        </div>
    );
}

export default AgendaCard;