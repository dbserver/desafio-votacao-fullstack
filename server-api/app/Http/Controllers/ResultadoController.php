<?php
namespace App\Http\Controllers;

use Exception;  
use App\Models\Pauta;
use App\Models\Sessao;
use Illuminate\Support\Facades\Validator;
use Illuminate\Database\QueryException;
use Illuminate\Support\Facades\Log;  

class ResultadoController extends Controller
{
    public function calcularResultados($pauta_id)
    {
        try {
            $validator = Validator::make(['pauta_id' => $pauta_id], [
                'pauta_id' => 'required|integer|exists:pautas,id',
            ]);

            if ($validator->fails()) {
                return response()->json(['error' => 'Pauta não encontrada'], 404);
            }

            $pauta = Pauta::find($pauta_id);
            if (! $pauta) {
                return response()->json(['error' => 'Pauta não encontrada'], 404);
            }

            $sessoes = Sessao::where('pauta_id', $pauta_id)->get();

            $totalSim = 0;
            $totalNao = 0;

            foreach ($sessoes as $sessao) {
                $totalSim += $sessao->votos->where('voto', true)->count();
                $totalNao += $sessao->votos->where('voto', false)->count();
            }

            return response()->json([
                'pauta_id'  => $pauta_id,
                'total_sim' => $totalSim,
                'total_nao' => $totalNao,
            ]);

        } catch (QueryException $e) {
            return response()->json([
                'error'   => 'Erro no banco de dados',
                'message' => $e->getMessage(),
            ], 500);
        } catch (Exception $e) {
            Log::error('Erro ao calcular resultados: ' . $e->getMessage());
            return response()->json([
                'error'   => 'Erro interno no servidor',
                'message' => $e->getMessage(),
            ], 500);
        }
    }
}
