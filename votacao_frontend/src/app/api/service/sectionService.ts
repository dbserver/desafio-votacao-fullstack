import { BASE_URL } from '../config/baseURL';
import { ISection, TSectionBody } from '../types/section';

export const sectionService = {
  getAllSection: async (isSectionOpen: boolean): Promise<ISection[]> => {
    const PATH_URL = isSectionOpen ? '/section/open' : '/section';
    try {
      const res = await fetch(`${BASE_URL}${PATH_URL}`);
      if (!res.ok) throw new Error('Erro ao carregar a listagem');

      const data: ISection[] = await res.json();

      data.sort(
        (a, b) => new Date(b.start).getTime() - new Date(a.start).getTime()
      );

      return data;
    } catch (error) {
      console.error('Erro no serviço de listagem:', error);
      throw error;
    }
  },

  create: async (data: TSectionBody): Promise<ISection> => {
    const res = await fetch(`${BASE_URL}/section`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!res.ok)
      throw new Error(
        'Não foi possível criar sua sessão, por favor, tente mais tarde!'
      );
    return res.json();
  },
};
