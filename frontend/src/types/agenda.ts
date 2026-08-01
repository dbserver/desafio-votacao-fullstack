export interface Agenda {
    id: number;
    title: string;
    description: string;
    status: AgendaStatus;
    duration: number;
    limitDate: string;
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
