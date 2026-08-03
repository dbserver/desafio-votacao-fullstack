import type { CreateAgendaDTO, VoteOption } from '../types/agenda';
import apiClient from './api';

export const agendaService = {
    // List all agendas
    getAll: async () => {
        const response = await apiClient.get('/agendas');
        return response.data;
    },

    // Create a new agenda
    create: async (agenda: CreateAgendaDTO) => {
        const response = await apiClient.post('/agendas', agenda);
        return response.data;
    },

    // open a new agenda session
    openSession: async (agendaId: number, duration: number) => {
        const response = await apiClient.post(`/agendas/${agendaId}/open`, { duration });
        return response.data;
    },

    // vote on an agenda
    vote: async (agendaId: number, associateId: string, voteOption: VoteOption) => {
        const response = await apiClient.post(`/agendas/${agendaId}/vote`, { associateId, option: voteOption });
        return response.data;
    },
};