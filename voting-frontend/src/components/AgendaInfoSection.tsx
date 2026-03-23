import { Card } from "./Card";
import { StatusBadge } from "./StatusBadge";

interface Agenda {
  id: number;
  titulo: string;
  descricao: string;
  sessionOpen?: boolean;
  createdAt?: string;
}

interface AgendaInfoSectionProps {
  agenda: Agenda;
}

export function AgendaInfoSection({ agenda }: AgendaInfoSectionProps) {
  const formattedDate = agenda.createdAt 
    ? new Date(agenda.createdAt).toLocaleDateString("pt-BR")
    : "Data não especificada";

  return (
    <div className="space-y-6">
      <Card>
        <div className="space-y-4">
          <div className="flex items-start justify-between gap-4">
            <div className="flex-1">
              <h2 className="text-3xl font-bold text-gray-900 mb-2">
                {agenda.titulo}
              </h2>
              <p className="text-gray-600 text-lg leading-relaxed">
                {agenda.descricao}
              </p>
            </div>
          </div>

          <div className="pt-4 border-t border-gray-200 flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Data de criação</p>
              <p className="text-gray-900 font-semibold">{formattedDate}</p>
            </div>
            <StatusBadge 
              status={agenda.sessionOpen ? "open" : "closed"} 
              label={`Nº da Pauta: ${agenda.id}`} 
              className="text-base px-6 py-3"
            />
          </div>
        </div>
      </Card>
    </div>
  );
}
