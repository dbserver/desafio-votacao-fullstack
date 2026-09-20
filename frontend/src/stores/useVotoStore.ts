import { create } from 'zustand';

import { http } from '../services/index';
import type { VotoState } from '@/types/votosStore';
import type { RegistrarVotoPayload, Voto } from '@/types/domain';
import { usePautaStore } from './usePautaStore';

export const useVotoStore = create<VotoState>((set) => ({
  loading: false,
  error: null,

  clearError: () => set({ error: null }),

  // Registrar voto do associado (POST /v1/votos ou /v1/pautas/{pautaId}/votos)
  votar: async (payload: RegistrarVotoPayload) => {
    set({ loading: true, error: null });
    try {
      const response = await http.post<Voto>('/votos', payload);

      // Recarrega as pautas para atualizar a contagem do painel/placar de votação
      await usePautaStore.getState().fetchPautas();

      return response.data;
    } catch (err: unknown) {
      const message =
        err instanceof Error ? err.message : 'Erro ao registrar voto. Tente novamente.';
      set({ error: message });
      throw err;
    } finally {
      set({ loading: false });
    }
  },
}));