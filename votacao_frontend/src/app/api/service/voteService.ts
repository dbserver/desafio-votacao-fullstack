import { BASE_URL } from '../config/baseURL';
import { IVote, TVoteBody } from '../types/vote';

export const voteService = {
  create: async (data: TVoteBody): Promise<IVote> => {
    const res = await fetch(`${BASE_URL}/vote`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (res.status === 409) {
      throw new Error('Você já votou nessa sessão');
    }
    if (!res.ok) throw new Error('Não foi possível realizar o voto!');
    return res.json();
  },
};
