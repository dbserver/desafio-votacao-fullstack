import type { RegistrarVotoPayload, Voto } from './domain';

export interface VotoState {
    loading: boolean;
    error: string | null;

    // Actions
    votar: (payload: RegistrarVotoPayload) => Promise<Voto>;
    clearError: () => void;
}