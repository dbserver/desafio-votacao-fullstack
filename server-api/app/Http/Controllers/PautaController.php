<?php
namespace App\Http\Controllers;

use App\Models\Pauta;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class PautaController extends Controller
{
    /**
     * Listar todas as pautas.
     */
    public function index()
    {
        $pautas = Pauta::all();
        return response()->json($pautas);
    }

    /**
     * Criar uma nova pauta.
     */
    public function store(Request $request)
    {
        $rules = [
            'nome'      => 'required|string|max:255',
            'descricao' => 'required|string',
        ];

        $messages = [
            'nome.required'      => 'O nome da Pauta é obrigatório',
            'descricao.required' => 'A descrição da Pauta é obrigatório',
        ];

        $validator = Validator::make($request->all(), $rules, $messages);

        if ($validator->fails()) {
            return response()->json([
                'message' => 'Erro de validação',
                'errors'  => $validator->errors(),
            ], 422);
        }

        $pauta = Pauta::create($request->all());
        return response()->json($pauta, 201);
    }

    /**
     * Buscar uma pauta específica.
     */
    public function show($id)
    {
        $pauta = Pauta::findOrFail($id);
        return response()->json($pauta);
    }

    /**
     * Atualizar uma pauta.
     */
    public function update(Request $request, $id)
    {
        $request->validate([
            'nome'      => 'sometimes|string|max:255',
            'descricao' => 'nullable|string',
        ]);

        $pauta = Pauta::findOrFail($id);
        $pauta->update($request->all());
        return response()->json($pauta);
    }

    /**
     * Deletar uma pauta.
     */
    public function destroy($id)
    {
        $pauta = Pauta::findOrFail($id);
        $pauta->delete();
        return response()->json(null, 204);
    }
}
