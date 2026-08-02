export interface AgendaDTO {
    id: number;
    title: string;
    description: string;
    status: AgendaStatus;
    limitDate: string;
    yesVotes: number;
    noVotes: number;
    totalVotes: number;
}

export interface CreateAgendaDTO {
    title: string;
    description: string;
}

export type AgendaStatus = 'PENDING' | 'STARTED' | 'FINISHED';

export type VoteOption = 'YES' | 'NO';
