<?php
namespace FeatureTestVoto;

use App\Models\Pauta;
use App\Models\Sessao;
use App\Models\User;
use App\Models\Voto;
use Illuminate\Testing\Fluent\AssertableJson;

test('should search voto', function () {
    $response = $this->get('/api/votos');

    $response->assertOk();
});

test('should create voto', function () {
    $pauta = Pauta::factory()->create();
    $sessao = Sessao::factory()->create(['pauta_id' => $pauta->id]);
    $user = User::factory()->create();

    $voto = [
        'sessao_id' => $sessao->id,
        'user_id'   => $user->id,
        'voto'      => true,
    ];

    $response = $this->postJson('/api/votos', $voto);
    $response
        ->assertStatus(201)
        ->assertJsonStructure([
            'sessao_id',
            'user_id',
            'voto',
        ]);
});

test('should be an error when create voto', function () {
    $response = $this->post('/api/votos');

    $response
        ->assertStatus(422)
        ->assertJson(fn(AssertableJson $json) =>
            $json
                ->has('message')
                ->has('errors'));
});

test('should be an error when create voto is not boolean', function () {
    $pauta = Pauta::factory()->create();
    $sessao = Sessao::factory()->create(['pauta_id' => $pauta->id]);
    $user = User::factory()->create();

    $voto = [
        'sessao_id' => $sessao->id,
        'user_id'   => $user->id,
        'voto'      => 'eu não quero votar',
    ];

    $response = $this->postJson('/api/votos', $voto);

    $response
        ->assertStatus(422)
        ->assertJson(fn(AssertableJson $json) =>
            $json
                ->has('message')
                ->has('errors'));
});

test('should show voto_id', function () {
    $pauta = Pauta::factory()->create();
    $sessao = Sessao::factory()->create(['pauta_id' => $pauta->id]);
    $user = User::factory()->create();

    $dataVoto = [
        'sessao_id' => $sessao->id,
        'user_id'   => $user->id,
        'voto'      => true,
    ];

    $voto = Voto::create($dataVoto);

    $response = $this->get('/api/votos/' . $voto->id);
    $response
        ->assertOk()
        ->assertJson(['id' => $voto->id]);
});
