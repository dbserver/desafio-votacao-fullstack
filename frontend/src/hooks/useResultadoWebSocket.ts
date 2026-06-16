import { useEffect, useRef } from 'react';
import { Client, type IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import type { Resultado } from '../types';

function normalizeBaseUrl(value: string): string {
  return value.replace(/\/+$/, '');
}

function isObject(value: unknown): value is Record<string, unknown> {
  return typeof value === 'object' && value !== null;
}

function isResultado(value: unknown): value is Resultado {
  if (!isObject(value)) {
    return false;
  }

  return (
    typeof value.pautaId === 'string' &&
    typeof value.sessaoId === 'string' &&
    typeof value.sim === 'number' &&
    typeof value.nao === 'number' &&
    typeof value.total === 'number' &&
    typeof value.encerrada === 'boolean'
  );
}

export function useResultadoWebSocket(
  sessaoId: string | null,
  onResultado: (resultado: Resultado) => void,
): void {
  const onResultadoRef = useRef(onResultado);

  useEffect(() => {
    onResultadoRef.current = onResultado;
  }, [onResultado]);

  useEffect(() => {
    if (sessaoId === null) {
      return;
    }

    const baseUrl = normalizeBaseUrl(import.meta.env.VITE_WS_URL?.trim() || 'http://localhost:8080');
    const client = new Client({
      webSocketFactory: () => new SockJS(`${baseUrl}/ws`),
      reconnectDelay: 0,
    });

    client.debug = () => undefined;

    client.onConnect = () => {
      client.subscribe(`/topic/resultado/${sessaoId}`, (message: IMessage) => {
        try {
          const parsed: unknown = JSON.parse(message.body) as unknown;
          if (isResultado(parsed)) {
            onResultadoRef.current(parsed);
          }
        } catch {
          // Mensagem inválida é ignorada.
        }
      });
    };

    client.activate();

    return () => {
      void client.deactivate();
    };
  }, [sessaoId]);
}
