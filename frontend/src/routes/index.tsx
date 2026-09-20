import { useEffect } from 'react';
import { createBrowserRouter, Navigate } from 'react-router-dom';

import type { PautaFormValues } from '@/components/common/PautaForm';
import { AppLayout } from '@/components/layout/AppLayout';
import { ROUTES } from '@/config/routes';
import { usePautaStore } from '@/stores/usePautaStore';
import { useSessaoStore } from '@/stores/useSessaoStore';
import type {
  AbrirSessaoPayload,
  PautaComStatus,
  RegistrarVotoPayload,
  //RegistrarVotoPayload,
  ResultadoVotacao,
} from '@/types/domain';
import { useVotoStore } from '@/stores/useVotoStore';

export const router = createBrowserRouter([
  {
    path: ROUTES.home,
    element: <AppLayout />,
    children: [
      { index: true, element: <Navigate to={ROUTES.gestaoPautas} replace /> },
      {
        path: ROUTES.gestaoPautas,
        lazy: () =>
          import('@/pages/GestaoPautas').then((module) => ({
            Component: function GestaoPautasRoute() {
              const { pautas, fetchPautas, criarPauta } = usePautaStore();
              const { fetchSessoes, calcularStatusSessao, abrirSessao } = useSessaoStore();

              useEffect(() => {
                void fetchPautas();
                void fetchSessoes();
              }, [fetchPautas, fetchSessoes]);

              // Mapeia as pautas cruzando com as sessões ativas/encerradas na store
              const pautasComStatus: PautaComStatus[] = pautas.map((pauta) => ({
                pauta,
                status: calcularStatusSessao(pauta.id),
              }));

              return (
                <module.GestaoPautas
                  pautas={pautasComStatus}
                  onCreatePauta={async (values: PautaFormValues) => {
                    await criarPauta(values.titulo, values.descricao);
                  }}
                  onAbrirSessao={async (payload: AbrirSessaoPayload) => {
                    await abrirSessao(payload);
                  }}
                />
              );
            },
          })),
      },
      {
        path: ROUTES.votacao,
        lazy: () =>
          import('@/pages/VotacaoAssociado').then((module) => ({
            Component: function VotacaoAssociadoRoute() {
              const { pautas, fetchPautas } = usePautaStore();
              const { sessoes, fetchSessoes } = useSessaoStore();
              const { votar } = useVotoStore();

              useEffect(() => {
                void fetchPautas();
                void fetchSessoes();
              }, [fetchPautas, fetchSessoes]);

              // Filtra pautas que possuem sessão aberta e em andamento no momento
              const pautasAtivas = pautas.filter((pauta) => {
                const sessao = sessoes.find((s) => s.pautaId === pauta.id);
                if (!sessao) return false;

                const agora = Date.now();
                const inicio = new Date(sessao.dataAbertura).getTime();
                const fim = new Date(sessao.dataEncerramento).getTime();

                return agora >= inicio && agora <= fim && sessao.aberta;
              });

              return (
                <module.VotacaoAssociado
                  pautasAtivas={pautasAtivas}
                  onVotar={async (payload: RegistrarVotoPayload) => {
                    await votar(payload);
                  }}
                />
              );
            },
          })),
      },
      {
        path: ROUTES.resultados,
        lazy: () =>
          import('@/pages/PainelResultados').then((module) => ({
            Component: function PainelResultadosRoute() {
              const { pautas, resultados, fetchAllResultados, loading } = usePautaStore();

              useEffect(() => {
                void fetchAllResultados();
              }, [fetchAllResultados]);

              // Mapeia os dados usando o dicionário 'resultados' alimentado pelo endpoint
              const listaResultados: ResultadoVotacao[] = pautas.map((pauta) => {
                const resultadoApi = resultados[pauta.id];

                return {
                  pautaId: pauta.id,
                  tituloPauta: pauta.titulo,
                  descricaoPauta: pauta.descricao,
                  votosSim: resultadoApi?.votosSim ?? 0,
                  votosNao: resultadoApi?.votosNao ?? 0,
                  totalVotos: resultadoApi?.totalVotos ?? 0,
                  resultado: resultadoApi?.resultado ?? 'SEM_VOTOS',
                  sessaoEncerrada: resultadoApi?.sessaoEncerrada ?? false,
                };
              });

              return (
                <module.PainelResultados
                  resultados={listaResultados}
                  loading={loading}
                  onRefresh={() => {
                    void fetchAllResultados();
                  }}
                />
              );
            },
          })),
      }
    ],
  },
  {
    path: ROUTES.notFound,
    lazy: () => import('@/pages/NotFound').then((module) => ({ Component: module.default })),
  },
]);
