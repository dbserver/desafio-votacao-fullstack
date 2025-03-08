<?php
namespace App\Http\Controllers;

use App\Models\Sessao;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Validator;

class SessaoController extends Controller
{
    /**
     * Listar todas as sessões.
     */
    public function index()
    {
        $sessoes = Sessao::all();
        return response()->json($sessoes);
    }

    /**
     * Criar uma nova sessão.
     */
    public function store(Request $request)
    {
        try {
            if (! $request->has('data_inicio')) {
                $request->merge([
                    'data_inicio' => now()->format('Y-m-d H:i:00'),
                ]);
            }

            $tempoMaximo     = '1 day';
            $dataInicio      = Carbon::parse($request->data_inicio)->startOfMinute();
            $dataFinalMaxima = $dataInicio->copy()->modify($tempoMaximo);

            $rules = [
                'pauta_id'    => 'required|exists:pautas,id',
                'data_inicio' => [
                    'required',
                    'date_format:Y-m-d H:i:00',
                    'after_or_equal:' . now()->format('Y-m-d H:i:00'),
                ],
                'data_final'  => [
                    'date_format:Y-m-d H:i:s',
                    'after:data_inicio',
                    'before_or_equal:' . $dataFinalMaxima->format('Y-m-d H:i:00'),
                    'after_or_equal:' . now()->format('Y-m-d H:i:00'),
                ],
            ];

            $messages = [
                'pauta_id.required'          => 'O ID da pauta é obrigatório.',
                'pauta_id.exists'            => 'A pauta informada não existe.',
                'data_inicio.required'       => 'A data de início é obrigatória.',
                'data_inicio.date'           => 'A data de início deve ser uma data válida.',
                'data_final.required'        => 'A data final é obrigatória.',
                'data_final.date'            => 'A data final deve ser uma data válida.',
                'data_final.after'           => 'A data final deve ser após a data de início.',
                'data_final.before_or_equal' => 'A data final deve ser no máximo ' . $tempoMaximo . ' após a data de início.',
                'data_final.after_or_equal'  => 'A data final não pode ser anterior ao momento atual.',
            ];

            $validator = Validator::make($request->all(), $rules, $messages);

            if ($validator->fails()) {
                return response()->json([
                    'message' => 'Erro de validação',
                    'errors'  => $validator->errors(),
                ], 422);
            }

            if (! $request->has('data_final')) {
                $request->merge([
                    'data_final' => $dataInicio->copy()->addMinutes(1)->format('Y-m-d H:i:s'),
                ]);
            }

            $conflict = Sessao::where('pauta_id', $request->pauta_id)
                ->where(function ($query) use ($dataInicio, $request) {
                    $query->whereBetween('data_inicio', [$dataInicio, $request->data_final])
                        ->orWhereBetween('data_final', [$dataInicio, $request->data_final])
                        ->orWhere(function ($query) use ($dataInicio, $request) {
                            $query->where('data_inicio', '<', $dataInicio)
                                ->where('data_final', '>', $request->data_final);
                        });
                })
                ->exists();

            if ($conflict) {
                return response()->json([
                    'message' => 'Conflito de horário',
                    'errors'  => [
                        'data_inicio' => ['Já existe uma sessão para a pauta informada neste horário.'],
                    ],
                ], 422);
            }

            $sessao = Sessao::create($request->all());

            Log::info('Sessão criada com sucesso', [
                'sessao_id'   => $sessao->id,
                'pauta_id'    => $sessao->pauta_id,
                'data_inicio' => $sessao->data_inicio,
                'data_final'  => $sessao->data_final,
            ]);

            return response()->json($sessao, 201);

        } catch (\Exception $e) {
            Log::error('Erro ao criar sessão', [
                'error' => $e->getMessage(),
                'trace' => $e->getTraceAsString(),
            ]);

            return response()->json([
                'message' => 'Ocorreu um erro inesperado.',
            ], 500);
        }
    }

    /**
     * Buscar uma sessão específica.
     */
    public function show($id)
    {
        $sessao = Sessao::findOrFail($id);
        return response()->json($sessao);
    }

    /**
     * Atualizar uma sessão.
     */
    public function update(Request $request, $id)
    {
        try {
            $sessao = Sessao::findOrFail($id);

            $tempoMaximo = '1 day';

            $dataInicio = $request->has('data_inicio')
            ? Carbon::parse($request->data_inicio)
            : Carbon::parse($sessao->data_inicio);

            $dataFinalMaxima = $dataInicio->copy()->modify($tempoMaximo);

            $rules = [
                'pauta_id'    => 'sometimes|exists:pautas,id',
                'data_inicio' => [
                    'sometimes',
                    'date_format:Y-m-d H:i:s',
                    'before_or_equal:' . $dataFinalMaxima,
                    'after_or_equal:' . now()->format('Y-m-d H:i:s'),
                ],
                'data_final'  => [
                    'sometimes',
                    'date_format:Y-m-d H:i:s',
                    'after:data_inicio',
                    'before_or_equal:' . $dataFinalMaxima,
                    'after_or_equal:' . now()->format('Y-m-d H:i:s'),
                ],
            ];

            $messages = [
                'pauta_id.exists'            => 'A pauta informada não existe.',
                'data_inicio.date_format'    => 'A data de início deve ser uma data válida.',
                'data_final.date_format'     => 'A data final deve ser uma data válida.',
                'data_final.after'           => 'A data final deve ser após a data de início.',
                'data_final.before_or_equal' => 'A data final não pode ser mais que ' . $tempoMaximo . ' após a data de início.',
                'data_final.after_or_equal'  => 'A data final não pode ser anterior ao momento atual.',
            ];

            $validator = Validator::make($request->all(), $rules, $messages);

            if ($validator->fails()) {
                return response()->json([
                    'message' => 'Erro de validação',
                    'errors'  => $validator->errors(),
                ], 422);
            }

            $conflito = Sessao::where('pauta_id', $request->pauta_id ?? $sessao->pauta_id)
                ->where('id', '!=', $sessao->id) // Ignora a própria sessão
                ->where(function ($query) use ($dataInicio, $request, $sessao) {
                    $dataFinal = $request->has('data_final') ? $request->data_final : $sessao->data_final;
                    $query->whereBetween('data_inicio', [$dataInicio, $dataFinal])
                        ->orWhereBetween('data_final', [$dataInicio, $dataFinal])
                        ->orWhere(function ($query) use ($dataInicio, $dataFinal) {
                            $query->where('data_inicio', '<', $dataInicio)
                                ->where('data_final', '>', $dataFinal);
                        });
                })
                ->exists();

            if ($conflito) {
                return response()->json([
                    'message' => 'Conflito de horário',
                    'errors'  => [
                        'data_inicio' => ['Já existe uma sessão para a pauta informada neste horário.'],
                    ],
                ], 422);
            }

            $sessao->update($request->all($request->only(['pauta_id', 'data_inicio', 'data_final'])));
            return response()->json($sessao);

        } catch (\Exception $e) {
            Log::error('Erro ao atualizar sessão', [
                'error' => $e->getMessage(),
                'trace' => $e->getTraceAsString(),
            ]);

            return response()->json([
                'message' => 'Ocorreu um erro inesperado.',
            ], 500);
        }
    }

    /**
     * Deletar uma sessão.
     */
    public function destroy($id)
    {
        $sessao = Sessao::findOrFail($id);
        $sessao->delete();
        return response()->json(null, 204);
    }
}
