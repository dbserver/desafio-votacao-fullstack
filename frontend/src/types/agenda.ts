export interface Agenda {
    id: number;
    title: string;
    description: string;
    status: AgendaStatus;
    duration: number;
    closesAt: number | null;
    votes: Vote[];
}

export interface CreateAgendaDTO {
    title: string;
    description: string;
}

export type AgendaStatus = 'PENDING' | 'STARTED' | 'FINISHED';

export interface Vote {
    associateId: string;
    value: 'YES' | 'NO';
}
