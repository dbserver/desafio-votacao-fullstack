<?php
namespace App\Http\Controllers;

use App\Models\Sessao;
use App\Models\Voto;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Validator;

class VotoController extends Controller
{
    /**
     * Listar todos os votos.
     */
    public function index()
    {
        $votos = Voto::all();
        return response()->json($votos);
    }

    /**
     * Criar um novo voto.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'sessao_id' => 'required|exists:sessoes,id',
            'user_id'   => 'required|exists:users,id',
            'voto'      => 'required|boolean',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'message' => 'Erro de validação',
                'errors'  => $validator->errors(),
            ], 422);
        }

        $sessao = Sessao::find($request->sessao_id);

        if (! $sessao) {
            return response()->json([
                'error' => 'Sessão não encontrada',
            ], 404);
        }

        $pautaId = $sessao->pauta_id;

        $sessoesPauta = Sessao::where('pauta_id', $pautaId)->get();

        if ($sessoesPauta->count() > 1) {
            Log::info('Existem outras sessões com a mesma pauta_id: ' . $pautaId);
        }

        $votoExiste = Voto::where('user_id', $request->user_id)
            ->whereIn('sessao_id', $sessoesPauta->pluck('id'))
            ->exists();

        if ($votoExiste) {
            return response()->json([
                'message' => 'Você já votou nesta pauta',
            ], 400);
        }

        $voto = Voto::create([
            'sessao_id' => $request->sessao_id,
            'user_id'   => $request->user_id,
            'voto'      => $request->voto,
        ]);

        return response()->json($voto, 201);
    }

    /**
     * Exibir um voto específico.
     */
    public function show($id)
    {
        $voto = Voto::find($id);

        if (! $voto) {
            return response()->json([
                'message' => 'Voto não encontrado',
            ], 404);
        }

        return response()->json($voto);
    }

    /**
     * Atualizar um voto.
     */
    public function update(Request $request, $id)
    {
        $voto = Voto::find($id);

        if (! $voto) {
            return response()->json([
                'message' => 'Voto não encontrado',
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'sessao_id' => 'sometimes|exists:sessoes,id',
            'user_id'   => 'sometimes|exists:users,id',
            'voto'      => 'sometimes|in:sim,nao',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'message' => 'Erro de validação',
                'errors'  => $validator->errors(),
            ], 422);
        }

        $voto->update($request->all());

        return response()->json($voto);
    }

    /**
     * Deletar um voto.
     */
    public function destroy($id)
    {
        $voto = Voto::find($id);

        if (! $voto) {
            return response()->json([
                'message' => 'Voto não encontrado',
            ], 404);
        }

        $voto->delete();

        return response()->json([
            'message' => 'Voto deletado com sucesso',
        ], 204);
    }

}
