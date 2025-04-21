import { BASE_URL } from '../config/baseURL';
import { IAgenda, TAgendaBody } from '../types/agenda';

export const agendaService = {
  getAllAvaiableAgendas: async (sortByName = false): Promise<IAgenda[]> => {
    try {
      const res = await fetch(`${BASE_URL}/section/availiable-agendas`);
      if (!res.ok) throw new Error('Erro ao carregar a listagem');

      const data: IAgenda[] = await res.json();

      return sortByName
        ? data.sort((a, b) => a.name.localeCompare(b.name))
        : data;
    } catch (error) {
      console.error('Erro no serviço de listagem:', error);
      throw error;
    }
  },

  create: async (data: TAgendaBody): Promise<IAgenda> => {
    const res = await fetch(`${BASE_URL}/agenda`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!res.ok)
      throw new Error(
        'Não foi possível criar sua pauta, por favor, tente mais tarde!'
      );
    return res.json();
  },
};
