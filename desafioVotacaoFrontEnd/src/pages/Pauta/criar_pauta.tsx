import { useForm } from "react-hook-form";
import { FiAlertCircle } from "react-icons/fi";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { usePautaMutation } from "./hooks/data_post";
import { schema } from "./schema/schema";
import { useNavigate } from "react-router-dom";

type CriarPautaFormData = z.infer<typeof schema>;

export function CriarPauta() {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CriarPautaFormData>({
    resolver: zodResolver(schema),
  });

  const { mutate } = usePautaMutation();
  const navigate = useNavigate();

  const onSubmit = (data: CriarPautaFormData) => {
    mutate(data, {
      onSuccess: () => {
        reset({
          titulo: "",
          descricao: "",
        });
        navigate("/List-pautas");
      },
    });
  };

  return (
    <div className="max-w-2xl mx-auto p-6">
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <div className="bg-blue-600 px-6 py-4">
          <h2 className="text-xl font-semibold text-white">Criar Nova Pauta</h2>
        </div>

        <form onSubmit={handleSubmit(onSubmit)} className="p-6 space-y-6">
          <div>
            <label htmlFor="titulo" className="block text-sm font-medium text-gray-700 mb-1">
              Título *
            </label>
            <div className="relative">
              <input
                id="titulo"
                type="text"
                {...register("titulo")}
                className={`block w-full px-4 py-2 rounded-md border ${errors.titulo ? "border-red-500" : "border-gray-300"} focus:ring-blue-500 focus:border-blue-500`}
                placeholder="Digite o título da pauta"
              />
              {errors.titulo && (
                <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                  <FiAlertCircle className="h-5 w-5 text-red-500" />
                </div>
              )}
            </div>
            {errors.titulo && <p className="mt-1 text-sm text-red-600">{errors.titulo.message}</p>}
          </div>

          <div>
            <label htmlFor="descricao" className="block text-sm font-medium text-gray-700 mb-1">
              Descrição *
            </label>
            <div className="relative">
              <textarea
                id="descricao"
                rows={4}
                {...register("descricao")}
                className={`block w-full px-4 py-2 rounded-md border ${errors.descricao ? "border-red-500" : "border-gray-300"} focus:ring-blue-500 focus:border-blue-500`}
                placeholder="Descreva os detalhes da pauta"
              />
              {errors.descricao && (
                <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                  <FiAlertCircle className="h-5 w-5 text-red-500" />
                </div>
              )}
            </div>
            {errors.descricao && <p className="mt-1 text-sm text-red-600">{errors.descricao.message}</p>}
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              onClick={() => reset()}
              className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              Limpar
            </button>
            <button
              type="submit"
              className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50"
            >
              <span className="flex items-center">Criar</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
