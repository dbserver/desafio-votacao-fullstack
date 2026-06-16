const BRAZIL_TIME_ZONE = 'America/Sao_Paulo';

function normalizeBrazilTimestamp(value: string): string {
  if (/[zZ]$/.test(value) || /[+-]\d{2}:\d{2}$/.test(value)) {
    return value;
  }

  return `${value}-03:00`;
}

export function formatDateTime(value: string | null | undefined): string {
  if (!value) {
    return 'Não informado';
  }

  const date = new Date(normalizeBrazilTimestamp(value));
  if (Number.isNaN(date.getTime())) {
    return value;
  }

  return new Intl.DateTimeFormat('pt-BR', {
    dateStyle: 'medium',
    timeStyle: 'short',
    timeZone: BRAZIL_TIME_ZONE,
  }).format(date);
}

export function formatPercent(value: number): string {
  return `${value.toFixed(1)}%`;
}
