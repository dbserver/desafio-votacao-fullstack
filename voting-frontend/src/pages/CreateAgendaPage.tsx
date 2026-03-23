import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createAgenda } from "../services/agendaService";
import { MainLayout } from "../layouts/MainLayout";
import { Container } from "../components/Container";
import { Card } from "../components/Card";
import { Button } from "../components/Button";
import { Toast, useToast } from "../components/Toast";

export function CreateAgendaPage() {
  const navigate = useNavigate();
  const { toasts, showToast, removeToast } = useToast();

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [errors, setErrors] = useState<{ title?: string; description?: string }>({});

  const validateForm = () => {
    const newErrors: { title?: string; description?: string } = {};

    if (!title.trim()) {
      newErrors.title = "Título é obrigatório";
    } else if (title.length < 5) {
      newErrors.title = "Título deve ter pelo menos 5 caracteres";
    } else if (title.length > 100) {
      newErrors.title = "Título não pode exceder 100 caracteres";
    }

    if (!description.trim()) {
      newErrors.description = "Descrição é obrigatória";
    } else if (description.length < 10) {
      newErrors.description = "Descrição deve ter pelo menos 10 caracteres";
    } else if (description.length > 500) {
      newErrors.description = "Descrição não pode exceder 500 caracteres";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      showToast("Por favor, corrija os erros no formulário", "warning");
      return;
    }

    setIsLoading(true);
    try {
      const newAgenda = await createAgenda({
        title,
        description,
      });
      
      showToast("✅ Pauta criada com sucesso!", "success");
      setTimeout(() => {
        navigate(`/pautas/${newAgenda.id}`);
      }, 1500);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || "Erro ao criar pauta";
      showToast(`❌ ${errorMessage}`, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const handleReset = () => {
    setTitle("");
    setDescription("");
    setErrors({});
  };

  return (
    <MainLayout>
      <Toast toasts={toasts} removeToast={removeToast} />

      <Container>
        <div className="max-w-2xl mx-auto">
          <div className="mb-10 text-center">
            <p className="text-blue-600 font-semibold text-sm mb-2">
              CRIAR NOVA PAUTA
            </p>
            <h1 className="text-4xl font-bold text-gray-900 mb-4">
              📝 Nova Pauta de Votação
            </h1>
            <p className="text-gray-600 text-lg">
              Preencha os detalhes abaixo para criar uma nova pauta de votação
            </p>
          </div>

          <Card>
            <form onSubmit={handleSubmit} className="space-y-6">
              <div>
                <label
                  htmlFor="title"
                  className="block text-sm font-semibold text-gray-700 mb-3"
                >
                  Título da Pauta
                </label>
                <input
                  id="title"
                  type="text"
                  placeholder="Ex: Aprovação de novo estatuto"
                  value={title}
                  onChange={(e) => {
                    setTitle(e.target.value);
                    if (errors.title) setErrors({ ...errors, title: undefined });
                  }}
                  disabled={isLoading}
                  className={`
                    w-full px-4 py-3 border-2 rounded-lg focus:outline-none transition
                    ${errors.title
                      ? "border-red-500 focus:border-red-500"
                      : "border-gray-300 focus:border-blue-500"
                    }
                    disabled:bg-gray-100 disabled:cursor-not-allowed
                  `}
                />
                {errors.title && (
                  <p className="text-red-600 text-sm mt-2 flex items-center gap-1">
                    ⚠️ {errors.title}
                  </p>
                )}
                <div className="mt-2 flex justify-between items-center text-xs text-gray-500">
                  <span>Mínimo 5 caracteres</span>
                  <span>{title.length}/100</span>
                </div>
              </div>

              <div>
                <label
                  htmlFor="description"
                  className="block text-sm font-semibold text-gray-700 mb-3"
                >
                  Descrição da Pauta
                </label>
                <textarea
                  id="description"
                  placeholder="Descreva em detalhes o assunto desta votação..."
                  value={description}
                  onChange={(e) => {
                    setDescription(e.target.value);
                    if (errors.description)
                      setErrors({ ...errors, description: undefined });
                  }}
                  disabled={isLoading}
                  rows={6}
                  className={`
                    w-full px-4 py-3 border-2 rounded-lg focus:outline-none transition resize-none
                    ${errors.description
                      ? "border-red-500 focus:border-red-500"
                      : "border-gray-300 focus:border-blue-500"
                    }
                    disabled:bg-gray-100 disabled:cursor-not-allowed
                  `}
                />
                {errors.description && (
                  <p className="text-red-600 text-sm mt-2 flex items-center gap-1">
                    ⚠️ {errors.description}
                  </p>
                )}
                <div className="mt-2 flex justify-between items-center text-xs text-gray-500">
                  <span>Mínimo 10 caracteres</span>
                  <span>{description.length}/500</span>
                </div>
              </div>

              <div className="p-4 bg-blue-50 rounded-lg border border-blue-200">
                <p className="text-sm text-blue-800">
                  💡 Após criar a pauta, você poderá definir quando abrir a sessão de
                  votação e visualizar os resultados em tempo real.
                </p>
              </div>

              <div className="flex gap-4 pt-4 border-t border-gray-200">
                <Button
                  type="button"
                  variant="secondary"
                  size="md"
                  onClick={handleReset}
                  disabled={isLoading}
                  className="flex-1"
                >
                  Limpar
                </Button>
                <Button
                  type="submit"
                  variant="primary"
                  size="md"
                  loading={isLoading}
                  disabled={isLoading}
                  className="flex-1"
                >
                  {isLoading ? "Criando..." : "✓ Criar Pauta"}
                </Button>
              </div>
            </form>
          </Card>

          <div className="mt-12 grid grid-cols-1 md:grid-cols-3 gap-6">
            <Card className="text-center">
              <p className="text-3xl mb-3">📋</p>
              <h3 className="font-semibold text-gray-900 mb-2">
                Informações Claras
              </h3>
              <p className="text-sm text-gray-600">
                Forneça título e descrição clara para que os votantes entendam o assunto
              </p>
            </Card>

            <Card className="text-center">
              <p className="text-3xl mb-3">🔓</p>
              <h3 className="font-semibold text-gray-900 mb-2">
                Abra a Sessão
              </h3>
              <p className="text-sm text-gray-600">
                Controle quando a votação começa clicando em "Abrir Sessão"
              </p>
            </Card>

            <Card className="text-center">
              <p className="text-3xl mb-3">📊</p>
              <h3 className="font-semibold text-gray-900 mb-2">
                Acompanhe Resultados
              </h3>
              <p className="text-sm text-gray-600">
                Veja os resultados em tempo real na guia "Resultados"
              </p>
            </Card>
          </div>
        </div>
      </Container>
    </MainLayout>
  );
}