import { create } from 'zustand';
import { http } from '../services/index';
import type { Pauta, ResultadoVotacao } from '@/types/domain';

interface PautaState {
  pautas: Pauta[];
  resultados: Record<number, ResultadoVotacao>; // Mapeia pautaId -> ResultadoVotacao
  loading: boolean;
  error: string | null;

  fetchPautas: () => Promise<void>;
  fetchResultadoPauta: (pautaId: number) => Promise<ResultadoVotacao>;
  fetchAllResultados: () => Promise<void>;
  criarPauta: (titulo: string, descricao: string) => Promise<Pauta>;
  clearError: () => void;
}

export const usePautaStore = create<PautaState>((set, get) => ({
  pautas: [],
  resultados: {},
  loading: false,
  error: null,

  clearError: () => set({ error: null }),

  fetchPautas: async () => {
    set({ loading: true, error: null });
    try {
      const response = await http.get<Pauta[]>('/pautas');
      set({ pautas: response.data });
    } catch (err: unknown) {
      const message =
        err instanceof Error ? err.message : 'Erro ao carregar pautas.';
      set({ error: message });
    } finally {
      set({ loading: false });
    }
  },

  // Busca o resultado de uma pauta específica (GET /v1/pautas/{pautaId}/resultado)
  fetchResultadoPauta: async (pautaId: number) => {
    try {
      const response = await http.get<ResultadoVotacao>(`/pautas/${pautaId}/resultado`);
      set((state) => ({
        resultados: {
          ...state.resultados,
          [pautaId]: response.data,
        },
      }));
      return response.data;
    } catch (err: unknown) {
      const message =
        err instanceof Error ? err.message : `Erro ao carregar resultado da pauta ${pautaId}.`;
      set({ error: message });
      throw err;
    }
  },

  // Busca os resultados de todas as pautas em paralelo
  fetchAllResultados: async () => {
    set({ loading: true, error: null });
    try {
      const pautas = get().pautas;
      if (pautas.length === 0) {
        const responsePautas = await http.get<Pauta[]>('/pautas');
        set({ pautas: responsePautas.data });
      }

      const pautasAtuais = get().pautas;
      const promessas = pautasAtuais.map((pauta) =>
        http
          .get<ResultadoVotacao>(`/pautas/${pauta.id}/resultado`)
          .then((res) => res.data)
          .catch(() => null)
      );

      const resultadosArray = await Promise.all(promessas);
      const novosResultados: Record<number, ResultadoVotacao> = {};

      resultadosArray.forEach((res) => {
        if (res) {
          novosResultados[res.pautaId] = res;
        }
      });

      set({ resultados: novosResultados });
    } catch (err: unknown) {
      const message =
        err instanceof Error ? err.message : 'Erro ao carregar resultados.';
      set({ error: message });
    } finally {
      set({ loading: false });
    }
  },

  criarPauta: async (titulo: string, descricao: string) => {
    set({ loading: true, error: null });
    try {
      const response = await http.post<Pauta>('/pautas', { titulo, descricao });
      await get().fetchPautas();
      return response.data;
    } catch (err: unknown) {
      const message =
        err instanceof Error ? err.message : 'Erro ao criar pauta.';
      set({ error: message });
      throw err;
    } finally {
      set({ loading: false });
    }
  },
}));