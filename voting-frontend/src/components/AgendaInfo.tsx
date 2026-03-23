import { Card } from "./Card";
import { StatusBadge } from "./StatusBadge";

interface AgendaInfoProps {
  titulo: string;
  descricao: string;
  status?: "open" | "closed" | "pending";
}

export function AgendaInfo({ titulo, descricao, status = "pending" }: AgendaInfoProps) {
  const statusLabels = {
    open: { label: "Sessão Aberta", type: "open" as const },
    closed: { label: "Sessão Encerrada", type: "closed" as const },
    pending: { label: "Aguardando Abertura", type: "pending" as const },
  };

  const statusInfo = statusLabels[status];

  return (
    <Card title="Informações da Pauta">
      <div className="space-y-6">
        <div>
          <h2 className="text-3xl font-bold text-gray-900 mb-4">{titulo}</h2>
          <div className="flex gap-3 items-center mb-6">
            <StatusBadge status={statusInfo.type} label={statusInfo.label} />
          </div>
        </div>

        <div className="bg-gray-50 rounded-lg p-6">
          <p className="text-gray-700 text-lg leading-relaxed">{descricao}</p>
        </div>

        <div className="grid grid-cols-3 gap-4 pt-4">
          <div className="bg-blue-50 rounded-lg p-4 text-center">
            <p className="text-sm text-gray-600 mb-1">Status</p>
            <p className="text-xl font-bold text-blue-600">{statusInfo.label}</p>
          </div>
          <div className="bg-purple-50 rounded-lg p-4 text-center">
            <p className="text-sm text-gray-600 mb-1">Tipo</p>
            <p className="text-xl font-bold text-purple-600">Votação</p>
          </div>
          <div className="bg-green-50 rounded-lg p-4 text-center">
            <p className="text-sm text-gray-600 mb-1">Ação</p>
            <p className="text-xl font-bold text-green-600">Necessária</p>
          </div>
        </div>
      </div>
    </Card>
  );
}
