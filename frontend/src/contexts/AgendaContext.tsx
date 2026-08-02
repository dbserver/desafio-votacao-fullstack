import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from 'react';
import { agendaService } from '../services/agendaService';
import type { AgendaDTO, CreateAgendaDTO, VoteOption } from '../types/agenda';
import type { AlertState, RequestStatus } from '../types/general';

interface AgendaContextValue {
    agendas: AgendaDTO[];
    alert: AlertState;
    status: RequestStatus;
    createAgenda: (createAgendaDTO: CreateAgendaDTO, afterCreateAgenda: () => void) => Promise<void>;
    openAgenda: (agendaId: number, durationInMinutes: number, afterOpenAgenda: (error?: string) => void) => Promise<void>;
    vote: (agendaId: number, associateId: string, voteOption: VoteOption, afterVote: (error?: string) => void) => Promise<void>;
    refreshAgendas: () => Promise<void>;
}

const AgendaContext = createContext<AgendaContextValue | undefined>(undefined);

export function AgendaProvider({ children }: { children: ReactNode }) {
    const [agendas, setAgendas] = useState<AgendaDTO[]>([]);
    const [alert, setAlert] = useState<AlertState>({ message: '', type: '' });
    const [status, setStatus] = useState<RequestStatus>('LOADING');

    const showAlert = (message: string, type: AlertState['type']) => {
        setAlert({ message, type });
        window.setTimeout(() => setAlert({ message: '', type: '' }), 10000);
    };

    const refreshAgendas = async () => {
        try {
            setStatus('LOADING');
            const data = await agendaService.getAll();
            setAgendas(data);
            setStatus('SUCCESS');
        } catch (error) {
            console.error('Failed to load agendas:', error);
            setStatus('ERROR');
        }
    };

    const createAgenda = async ({ title, description }: CreateAgendaDTO, afterCreateAgenda: () => void) => {
        try {
            await agendaService.create({ title, description });

            refreshAgendas();
            afterCreateAgenda();

            showAlert('Pauta cadastrada com sucesso!', 'success');
        } catch (error: any) {
            if (error.type === 'VALIDATION_ERROR' && error.errors) {
                showAlert(Object.values(error.errors).join("\n"), 'warning');
            } else {
                showAlert('Erro ao cadastrar pauta.', 'danger');
            }
        }
    };

    const openAgenda = async (agendaId: number, durationInMinutes: number, afterOpenAgenda: (error?: string) => void) => {
        try {
            await agendaService.openSession(agendaId, durationInMinutes);

            afterOpenAgenda();
        } catch (error: any) {
            if (error.type === 'VALIDATION_ERROR' && error.errors) {
                afterOpenAgenda(Object.values(error.errors).join("\n"));
            } else {
                showAlert('Erro ao abrir sessão de pauta.', 'danger');
            }
        }
    };

    const vote = async (agendaId: number, associateId: string, voteOption: VoteOption, afterVote: (error?: string) => void) => {
        try {
            await agendaService.vote(agendaId, associateId, voteOption);
            afterVote();
        } catch (error: any) {
            if (error.type === 'VALIDATION_ERROR' && error.errors) {
                afterVote(Object.values(error.errors).join("\n"));
            } else {
                showAlert('Erro ao votar em pauta.', 'danger');
            }
        }
    };


    useEffect(() => {
        refreshAgendas();
    }, []);

    const value = useMemo<AgendaContextValue>(
        () => ({
            agendas,
            alert,
            status,
            createAgenda,
            openAgenda,
            vote,
            refreshAgendas
        }),
        [agendas, alert, status]
    );

    return <AgendaContext.Provider value={value}>{children}</AgendaContext.Provider>;
}

export function useAgenda() {
    const context = useContext(AgendaContext);

    if (!context) {
        throw new Error('useAgenda must be used within an AgendaProvider');
    }

    return context;
}
