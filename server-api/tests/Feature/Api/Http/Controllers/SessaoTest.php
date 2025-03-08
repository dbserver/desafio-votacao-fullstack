<?php
namespace FeatureTestSessao;

use App\Models\Pauta;
use App\Models\Sessao;
use Carbon\Carbon;
use Illuminate\Support\Carbon\now;
use Illuminate\Testing\Fluent\AssertableJson;

test('should search sessao', function () {
    $response = $this->get('/api/sessoes');

    $response->assertOk();
});

test('should create sessao', function () {
    $pauta = Pauta::factory()->create();

    $releaseDate = Carbon::now();

    $dataSessao = [
        'pauta_id'    => $pauta->id,
        'data_inicio' => $releaseDate->format('Y-m-d H:i:00'),
        'data_final'  => $releaseDate->addMinutes(1)->format('Y-m-d H:i:00'),
    ];

    $response = $this->postJson('/api/sessoes', $dataSessao);
    $response
        ->assertStatus(201)
        ->assertJsonStructure([
            'pauta_id',
            'data_inicio',
            'data_final',
            'id',
        ]);
});

test('should be an error when create sessão', function () {
    $pauta = Pauta::factory()->create();

    $releaseDate = Carbon::now();

    $dataSessao = [
        'pauta_id'    => $pauta->id,
        'data_inicio' => $releaseDate->format('Y-m-d H:i:00'),
        'data_final'  => $releaseDate->addMinutes(1)->format('Y-m-d H:i:00'),
    ];

    Sessao::create($dataSessao);

    $response = $this->post('/api/sessoes');
    $response
        ->assertStatus(422)
        ->assertJson(fn(AssertableJson $json) =>
            $json
                ->has('message')
                ->has('errors'));
});

test('should show sessao-id', function () {
    $pauta = Pauta::factory()->create();

    $releaseDate = Carbon::now();

    $dataSessao = [
        'pauta_id'    => $pauta->id,
        'data_inicio' => $releaseDate->format('Y-m-d H:i:00'),
        'data_final'  => $releaseDate->addMinutes(1)->format('Y-m-d H:i:00'),
    ];

    $sessao = Sessao::create($dataSessao);

    $response = $this->get('/api/sessoes/' . $sessao->id);
    $response
        ->assertOk()
        ->assertJson(['id' => $sessao->id]);
});

test('should update sessao', function () {
    $pauta = Pauta::factory()->create();

    $releaseDate = Carbon::now();

    $dataSessao = [
        'pauta_id'    => $pauta->id,
        'data_inicio' => $releaseDate->format('Y-m-d H:i:00'),
        'data_final'  => $releaseDate->addMinutes(1)->format('Y-m-d H:i:00'),
    ];

    $sessao = Sessao::create($dataSessao);

    $newDataSessao = [
        'pauta_id'    => $pauta->id,
        'data_inicio' => $releaseDate->format('Y-m-d H:i:00'),
        'data_final'  => $releaseDate->addMinutes(5)->format('Y-m-d H:i:00'),
    ];

    $response = $this
        ->putJson('/api/sessoes/' . $sessao->id, $newDataSessao);

    $response
        ->assertOk()
        ->assertJsonStructure([
            'pauta_id',
            'data_inicio',
            'data_final',
            'id',
        ]);
});

test('should delete sessao', function () {
    $pauta = Pauta::factory()->create();

    $releaseDate = Carbon::now();

    $dataSessao = [
        'pauta_id'    => $pauta->id,
        'data_inicio' => $releaseDate->format('Y-m-d H:i:00'),
        'data_final'  => $releaseDate->addMinutes(1)->format('Y-m-d H:i:00'),
    ];

    $sessao = Sessao::create($dataSessao);

    $response = $this
        ->delete('/api/sessoes/' . $sessao->id);

    $response
        ->assertStatus(204);
});

test('should be an error when delete sessao', function () {
    $response = $this
        ->delete('/api/sessoes/');

    $response
        ->assertStatus(405);
});
