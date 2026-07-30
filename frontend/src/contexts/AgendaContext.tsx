import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from 'react';
import { agendaService } from '../services/agendaService';
import type { Agenda, CreateAgendaDTO } from '../types/agenda';
import type { AlertState, RequestStatus } from '../types/general';

interface AgendaContextValue {
    agendas: Agenda[];
    alert: AlertState;
    status: RequestStatus;
    createAgenda: (createAgendaDTO: CreateAgendaDTO, callback: () => void) => Promise<void>;
    openSession: (agendaId: number, durationInMinutes: number) => void;
    vote: (agendaId: number, associateId: string, choice: 'YES' | 'NO') => void;
    refreshAgendas: () => Promise<void>;
}

const AgendaContext = createContext<AgendaContextValue | undefined>(undefined);

export function AgendaProvider({ children }: { children: ReactNode }) {
    const [agendas, setAgendas] = useState<Agenda[]>([]);
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

    const createAgenda = async ({ title, description }: CreateAgendaDTO, callback: () => void) => {
        try {
            await agendaService.create({ title, description });

            refreshAgendas();
            callback();

            showAlert('Pauta cadastrada com sucesso!', 'success');
        } catch (error: any) {
            if (error.response && error.response.status === 400 && error.response.data) {
                showAlert(Object.values(error.response.data).join("\n"), 'warning');
            } else {
                console.error('Failed to load agendas:', error);
                showAlert('Erro ao cadastrar pauta.', 'danger');
            }
        }
    };

    const openSession = (agendaId: number, durationInMinutes: number) => {
        showAlert(`Sessão aberta por ${durationInMinutes} minuto(s).`, 'info');
    };

    const vote = (agendaId: number, associateId: string, choice: 'YES' | 'NO') => {
        showAlert('Voto registrado com sucesso!', 'success');
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
            openSession,
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
