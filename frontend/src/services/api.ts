import axios, { type AxiosRequestConfig } from 'axios';
import type { ErrorResponse } from '../types/api';

const fallbackBaseUrl = 'http://localhost:8080/api/v1';

function normalizeBaseUrl(value: string | undefined): string {
  const trimmed = value?.trim();
  if (!trimmed) {
    return fallbackBaseUrl;
  }

  return trimmed.replace(/\/+$/, '');
}

export const API_BASE_URL = normalizeBaseUrl(import.meta.env.VITE_API_BASE_URL);

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export class ApiError extends Error {
  status?: number;
  timestamp?: string;
  details?: ErrorResponse;

  constructor(
    message: string,
    options: { status?: number; timestamp?: string; details?: ErrorResponse } = {},
  ) {
    super(message);
    this.name = 'ApiError';
    this.status = options.status;
    this.timestamp = options.timestamp;
    this.details = options.details;
    Object.setPrototypeOf(this, ApiError.prototype);
  }
}

function isErrorResponse(value: unknown): value is ErrorResponse {
  if (typeof value !== 'object' || value === null) {
    return false;
  }

  const record = value as Record<string, unknown>;
  return typeof record.status === 'number' && typeof record.mensagem === 'string';
}

export function toApiError(error: unknown, fallbackMessage: string): ApiError {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data;

    if (isErrorResponse(data)) {
      return new ApiError(data.mensagem || fallbackMessage, {
        status: data.status ?? error.response?.status,
        timestamp: data.timestamp,
        details: data,
      });
    }

    return new ApiError(fallbackMessage, {
      status: error.response?.status,
    });
  }

  if (error instanceof Error) {
    return new ApiError(error.message || fallbackMessage);
  }

  return new ApiError(fallbackMessage);
}

export async function request<T>(config: AxiosRequestConfig, fallbackMessage: string): Promise<T> {
  try {
    const response = await api.request<T>(config);
    return response.data;
  } catch (error) {
    throw toApiError(error, fallbackMessage);
  }
}
