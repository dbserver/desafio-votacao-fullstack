<?php
namespace FeatureTestResultado;

use App\Models\Voto;

test('should search resultado', function () {
    $voto = Voto::factory()->create();

    $response = $this->get('/api/resultados/' . $voto->sessao->pauta->id);
    $response
        ->assertStatus(200)
        ->assertJson([
            'pauta_id'  => $voto->sessao->pauta->id,
            'total_sim' => 1,
            'total_nao' => 0,
        ]);
});

test('should be an error when count resultado', function () {
    $pauta_id = 'Pauta não informada';

    $response = $this->get('/api/resultados/' . $pauta_id);
    $response
        ->assertStatus(404);
});
