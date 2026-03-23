import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { listAgendas } from "../services/agendaService";
import { MainLayout } from "../layouts/MainLayout";
import { Container } from "../components/Container";
import { Card } from "../components/Card";
import { LoadingSpinner } from "../components/LoadingSpinner";
import { StatusBadge } from "../components/StatusBadge";
import { Button } from "../components/Button";
import { Toast, useToast } from "../components/Toast";
import type { Agenda } from "../types/agenda";

export function ListAgendasPage() {
  const [agendas, setAgendas] = useState<Agenda[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const { toasts, showToast, removeToast } = useToast();

  useEffect(() => {
    loadAgendas();
  }, []);

  const loadAgendas = async () => {
    try {
      setIsLoading(true);
      const data = await listAgendas();
      setAgendas(data);
    } catch (error) {
      console.error("Erro ao carregar pautas:", error);
      showToast("Erro ao carregar pautas", "error");
    } finally {
      setIsLoading(false);
    }
  };

  const filteredAgendas = agendas.filter((pauta) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      pauta.title?.toLowerCase().includes(searchLower) ||
      pauta.description?.toLowerCase().includes(searchLower)
    );
  });

  return (
    <MainLayout>
      <Toast toasts={toasts} removeToast={removeToast} />

      <Container>
        <div className="mb-10">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h1 className="text-4xl font-bold text-gray-900 mb-2">
                📋 Pautas de Votação
              </h1>
              <p className="text-gray-600">
                Escolha uma pauta para visualizar os detalhes e participar da votação
              </p>
            </div>
            <div className="text-3xl font-bold text-blue-600 bg-blue-50 px-6 py-4 rounded-lg">
              {agendas.length}
            </div>
          </div>
          <div className="h-1 w-20 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-full" />
        </div>

        <div className="mb-8">
          <div className="relative">
            <input
              type="text"
              placeholder="🔍 Buscar pauta por título ou descrição..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full px-6 py-4 text-lg border-2 border-gray-300 rounded-xl focus:border-blue-500 focus:outline-none transition"
            />
            {searchTerm && (
              <button
                onClick={() => setSearchTerm("")}
                className="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600 text-xl"
              >
                ✕
              </button>
            )}
          </div>
        </div>

        {isLoading && (
          <LoadingSpinner size="lg" message="Carregando pautas..." />
        )}

        {!isLoading && agendas.length === 0 && (
          <Card>
            <div className="text-center py-16">
              <p className="text-5xl mb-4">📭</p>
              <p className="text-2xl font-bold text-gray-900 mb-2">
                Nenhuma pauta cadastrada
              </p>
              <p className="text-gray-600">
                As pautas aparecerão aqui quando forem criadas
              </p>
            </div>
          </Card>
        )}

        {!isLoading && agendas.length > 0 && filteredAgendas.length === 0 && (
          <Card>
            <div className="text-center py-16">
              <p className="text-5xl mb-4">🔍</p>
              <p className="text-2xl font-bold text-gray-900 mb-2">
                Nenhum resultado encontrado
              </p>
              <p className="text-gray-600 mb-6">
                Tente buscar com outros termos
              </p>
              <Button
                variant="secondary"
                onClick={() => setSearchTerm("")}
              >
                Limpar busca
              </Button>
            </div>
          </Card>
        )}

        {!isLoading && filteredAgendas.length > 0 && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredAgendas.map((pauta) => (
              <div key={pauta.id} className="group cursor-pointer">
                <Card className="h-full hover:shadow-lg transition-shadow duration-300 transform hover:-translate-y-1">
                  <Link to={`/pautas/${pauta.id}`} className="block h-full">
                    <div className="flex flex-col h-full">
                      {/* Card Header */}
                      <div className="mb-4">
                        <div className="flex items-start justify-between gap-2 mb-3">
                          <StatusBadge
                            status="pending"
                            label={`Pauta #${pauta.id}`}
                          />
                          <span className="text-2xl">📋</span>
                        </div>
                        <h3 className="text-xl font-bold text-gray-900 line-clamp-2 group-hover:text-blue-600 transition">
                          {pauta.title}
                        </h3>
                      </div>

                      <p className="text-gray-600 text-sm line-clamp-3 flex-grow mb-4">
                        {pauta.description}
                      </p>

                      <div className="border-t border-gray-200 pt-4">

                        <Button
                          variant="primary"
                          size="md"
                          className="w-full group-hover:bg-blue-700 transition"
                        >
                          Acessar Pauta →
                        </Button>
                      </div>
                    </div>
                  </Link>
                </Card>
              </div>
            ))}
          </div>
        )}

        {!isLoading && filteredAgendas.length > 0 && (
          <div className="text-center mt-12 text-gray-600">
            <p>
              Mostrando <span className="font-semibold">{filteredAgendas.length}</span> de{" "}
              <span className="font-semibold">{agendas.length}</span> pautas
            </p>
          </div>
        )}
      </Container>
    </MainLayout>
  );
}